package me.m0dex.funquiz;

import me.m0dex.funquiz.commands.AnswerCommand;
import me.m0dex.funquiz.commands.CommandExecutor;
import me.m0dex.funquiz.commands.CommandModule;
import me.m0dex.funquiz.commands.FunQuizCommand;
import me.m0dex.funquiz.questions.QuestionManager;
import me.m0dex.funquiz.utils.Configuration;
import me.m0dex.funquiz.utils.Settings;
import org.bukkit.plugin.java.JavaPlugin;

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

    private Map<String, CommandModule> commandMap = new HashMap<>();

    private static FunQuiz instance;

    @Override
    public void onEnable() {
        this.instance = this;

        log = this.getLogger();
        cmdExec = new CommandExecutor(this);

        if(!loadConfigs()) {
            log.severe("Something went wrong while loading the config files...");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        settings = new Settings(this, getConfig());

        questionManager = new QuestionManager(this, questionsCfg);

        registerCommands();
    }

    /**
     * Loads the configuration files.
     * @return  <code>true</code> if configs loaded correctly;
     *          <code>false</code> if some exception happened.
     */
    private boolean loadConfigs() {

        messagesCfg =   new Configuration(this, "", "messages.yml");
        questionsCfg =  new Configuration(this, "", "questions.yml");

        settings = new Settings(this, this.getConfig());

        return true;
    }

    /**
     * Registers all commands.
     */
    private void registerCommands() {

        new FunQuizCommand(this);
        new AnswerCommand(this, questionManager);
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

    /**
     * Gets the reference of the current instance of the plugin.
     * Important for processes that use servers classes, methods and resources.
     * @return  <code>FunQuiz</code> instance.
     */
    public static FunQuiz getInstance() { return instance; }
}
