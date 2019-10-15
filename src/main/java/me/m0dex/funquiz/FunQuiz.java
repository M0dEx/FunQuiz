package me.m0dex.funquiz;

import fr.minuskube.inv.InventoryManager;
import me.m0dex.funquiz.commands.CommandExecutor;
import me.m0dex.funquiz.commands.CommandModule;
import me.m0dex.funquiz.commands.FunQuizCommand;
import me.m0dex.funquiz.commands.QuestionsCommand;
import me.m0dex.funquiz.listeners.ChatListener;
import me.m0dex.funquiz.questions.QuestionManager;
import me.m0dex.funquiz.utils.*;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class FunQuiz extends JavaPlugin {

    private Configuration messagesCfg;
    private Configuration questionsCfg;

    private CommandExecutor cmdExec;
    private Logger log;
    private Settings settings;
    private QuestionManager questionManager;
    private TaskManager taskManager;
    private InventoryManager invManager;

    private int questionTaskID;

    private Map<String, CommandModule> commandMap = new HashMap<>();

    private static FunQuiz instance;

    /**
     * Initializes the plugin
     */
    @Override
    public void onEnable() {
        instance = this;

        log = this.getLogger();
        cmdExec = new CommandExecutor(this);

        if(!loadConfigs()) {
            log.severe("Something went wrong while loading the config files!");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        settings = new Settings(this, getConfig());

        taskManager = new TaskManager(this);
        questionManager = new QuestionManager(this, questionsCfg);
        invManager = new InventoryManager(this);
        invManager.init();

        registerCommands();
        registerListeners();
        registerTasks();
    }

    /**
     * Cleans up variables, tasks and event handlers
     */
    @Override
    public void onDisable() {
        taskManager.stopTasks();
        HandlerList.unregisterAll(this);
    }

    /**
     * Loads the configuration files.
     * @return  <code>true</code> if configs loaded correctly;
     *          <code>false</code> if some exception happened.
     */
    private boolean loadConfigs() {

        messagesCfg =   new Configuration(this, "", "messages.yml");
        questionsCfg =  new Configuration(this, "", "questions.yml");

        boolean success = messagesCfg.reloadConfig() && questionsCfg.reloadConfig();

        settings = new Settings(this, this.getConfig());

        Messages.setFile(this.messagesCfg.getConfig());
        Messages[] arrayOfMessages;
        int j = (arrayOfMessages = Messages.values()).length;
        for (int i = 0; i < j; i++) {
            Messages value = arrayOfMessages[i];

            this.messagesCfg.getConfig().addDefault(value.getPath(), value.getDefault());
        }
        this.messagesCfg.getConfig().options().copyDefaults(true);
        this.messagesCfg.saveConfig();

        return success;
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
    }

    /**
     * Registers the main tasks
     */
    private void registerTasks() {

        questionTaskID = taskManager.addTask(new BukkitRunnable() {
            @Override
            public void run() {
                questionManager.askQuestion();
            }
        }.runTaskTimer(this, 20*60*settings.interval, 20*60*settings.interval));
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

    public InventoryManager getInventoryManager() { return invManager; }

    /**
     * Gets the reference of the current instance of the plugin.
     * Important for processes that use servers classes, methods and resources.
     * @return  <code>FunQuiz</code> instance.
     */
    public static FunQuiz getInstance() { return instance; }
}
