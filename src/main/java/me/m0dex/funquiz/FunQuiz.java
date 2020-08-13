package me.m0dex.funquiz;

import co.aikar.taskchain.BukkitTaskChainFactory;
import co.aikar.taskchain.TaskChain;
import co.aikar.taskchain.TaskChainFactory;
import fr.minuskube.inv.InventoryManager;
import me.m0dex.funquiz.bstats.Metrics;
import me.m0dex.funquiz.cache.PlayerCache;
import me.m0dex.funquiz.commands.CommandExecutor;
import me.m0dex.funquiz.commands.CommandModule;
import me.m0dex.funquiz.commands.FunQuizCommand;
import me.m0dex.funquiz.commands.QuestionsCommand;
import me.m0dex.funquiz.database.Database;
import me.m0dex.funquiz.listeners.ChatListener;
import me.m0dex.funquiz.listeners.PlayerListener;
import me.m0dex.funquiz.questions.QuestionManager;
import me.m0dex.funquiz.updater.Updater;
import me.m0dex.funquiz.utils.*;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class FunQuiz extends JavaPlugin {

    private Configuration mainCfg;
    private Configuration messagesCfg;
    private Configuration questionsCfg;

    private CommandExecutor cmdExec;
    private Logger log;
    private Settings settings;
    private QuestionManager questionManager;
    private InventoryManager inventoryManager;
    private TaskManager taskManager;

    private Map<String, CommandModule> commandMap = new HashMap<>();

    private Database database;
    private boolean databaseReady;
    private PlayerCache playerCache;

    private Metrics metrics;
    private TaskChainFactory taskFactory;
    private Updater updater;

    private Economy econ;

    private static FunQuiz instance;

    /**
     * Initializes the plugin
     */
    @Override
    public void onEnable() {
        instance = this;

        taskFactory = BukkitTaskChainFactory.create(this);

        log = this.getLogger();
        cmdExec = new CommandExecutor(this);

        if(!hookEconomy()) {
            log.warning("Couldn't hook into Vault. Rewards with the keyword 'money' in them will not work!");
        }

        if(!loadConfigs()) {
            log.severe("Something went wrong while loading the config files!");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        settings = new Settings(this, mainCfg);

        playerCache = new PlayerCache(instance);

        if (settings.useSQLite)
            database = new Database(this);
        else {
            database = new Database(this,
                    settings.hostname,
                    settings.port,
                    settings.database,
                    settings.username,
                    settings.password);
        }

        initDatabase();

        taskManager = new TaskManager(this);
        questionManager = new QuestionManager(this, questionsCfg);
        inventoryManager = new InventoryManager(this);
        inventoryManager.init();

        registerCommands();
        registerListeners();
        initializeMetrics();

        initUpdater();
    }

    /**
     * Cleans up variables, tasks and event handlers
     */
    @Override
    public void onDisable() {

        taskManager.stopTasks();
        saveDatabase();
        HandlerList.unregisterAll(this);
    }

    /**
     * Reloads everything that should be reloaded
     */
    public void reload() {

        taskManager.stopTasks();

        mainCfg.loadConfig();
        questionsCfg.loadConfig();
        messagesCfg.loadConfig();

        settings = new Settings(this, mainCfg);

        loadMessages();

        questionManager.reload();

        registerListeners();
    }

    /**
     * Hooks into Vault for economy
     * @return  <code>Economy econ</code> if Vault is a plugin on the server
     *          <code>null</code> if there is not.
     */
    private boolean hookEconomy() {

        if (getServer().getPluginManager().getPlugin("Vault") == null)
            return false;

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null)
            return false;

        econ = rsp.getProvider();
        return econ != null;
    }

    /**
     * Loads the configuration files.
     * @return  <code>true</code> if configs loaded correctly;
     *          <code>false</code> if some exception happened.
     */
    private boolean loadConfigs() {

        mainCfg =       new Configuration(this, "", "config.yml");
        messagesCfg =   new Configuration(this, "", "messages.yml");
        questionsCfg =  new Configuration(this, "", "questions.yml");

        boolean success = mainCfg.loadConfig() && messagesCfg.loadConfig() && questionsCfg.loadConfig();

        loadMessages();

        return success;
    }

    /**
     * Loads the configuration with messages
     */
    private void loadMessages() {

        Messages.setFile(this.messagesCfg.getConfig());
        Messages[] arrayOfMessages;
        int j = (arrayOfMessages = Messages.values()).length;
        for (int i = 0; i < j; i++) {
            Messages value = arrayOfMessages[i];

            this.messagesCfg.getConfig().addDefault(value.getPath(), value.getDefault());
        }
        this.messagesCfg.getConfig().options().copyDefaults(true);
        this.messagesCfg.saveConfig();
    }

    /**
     * Registers all commands.
     */
    private void registerCommands() {

        new FunQuizCommand(this);
        new QuestionsCommand(this);
    }

    private void registerListeners() {

        PluginManager pm = this.getServer().getPluginManager();

        pm.registerEvents(new ChatListener(this), this);
        pm.registerEvents(new PlayerListener(this), this);
    }

    /**
     * Initializes metrics and custom graphs
     */
    private void initializeMetrics() {

        metrics = new Metrics(this);

        metrics.addCustomChart(new Metrics.SimplePie("open_trivia_db", () -> (instance.getSettings().otdbEnabled ? "Enabled" : "Disabled")));
    }

    private void initDatabase() {

        TaskChain<?> dbInitChain = taskFactory.newChain();
        dbInitChain
                .async(() -> {
                    database.openConnection(settings.useSQLite);
                })
                .sync(() -> {
                    databaseReady = true;
                })
                .execute();
    }

    private void saveDatabase() {

        playerCache.savePlayerData();

        TaskChain<?> dbSaveChain = taskFactory.newChain();
        dbSaveChain
                .sync(() -> {
                    databaseReady = false;
                    instance.getLogger().info("Saving database...");
                })
                .async(() -> {
                    instance.getPlayerCache().savePlayerData();
                    database.closeConnection();
                })
                .execute();
    }

    private void initUpdater() {

        updater = new Updater(this);

        TaskChain<?> chain = taskFactory.newChain();
        chain
                .async(() -> {
                    updater.checkForUpdate();
                })
                .execute();
    }

    public boolean update() {

        if (!updater.isUpdateAvailable())
            return false;

        return updater.update();
    }

    /**
     * Adds a command into the commandMap.
     *
     * @param command   <code>String</code> command name to be added
     * @param module    <code>CommandModule</code> reference to the CommandModule of the command
     */
    public void addCommand(String command, CommandModule module) {

        commandMap.put(command.toLowerCase(), module);
        this.getCommand(command).setExecutor(this.cmdExec);
    }

    public boolean isDatabaseReady() { return databaseReady; }

    /**
     * Gets the command specified.
     *
     * @param command   <code>String</code> command
     * @return          <code>CommandModule</code> if the command exists;
     *                  <code>null</code> if it doesn't.
     */
    public CommandModule getCommandModule(String command) {

        return commandMap.get(command.toLowerCase());
    }

    public Settings getSettings() { return settings; }

    public TaskManager getTaskManager() { return taskManager; }

    public QuestionManager getQuestionManager() { return questionManager; }
    public InventoryManager getInventoryManager() { return inventoryManager; }
    public TaskChainFactory getTaskFactory() { return taskFactory; }
    public Database getDB() { return database; }
    public PlayerCache getPlayerCache() { return playerCache; }
    public Economy getEconomy() { return econ; }

    /**
     * Gets the reference of the current instance of the plugin.
     * Important for processes that use servers classes, methods and resources.
     * @return  <code>FunQuiz</code> instance.
     */
    public static FunQuiz getInstance() { return instance; }
}
