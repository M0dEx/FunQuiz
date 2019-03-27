package me.m0dex.funquiz;

import me.m0dex.funquiz.utils.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class FunQuiz extends JavaPlugin {

    private Configuration messagesCfg;
    private Configuration questionsCfg;

    private Logger log;

    private static FunQuiz instance;

    @Override
    public void onEnable() {
        this.instance = this;

        log = this.getLogger();

        messagesCfg =   new Configuration(this, "", "messages.yml");
        questionsCfg =  new Configuration(this, "", "questions.yml");

        if(!loadConfigs()) {
            log.severe("Something went wrong while loading the config files...");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
    }

    /**
     * Loads the configuration files.
     * @return  <code>true</code> if configs loaded correctly;
     *          <code>false</code> if some exception happened.
     */
    private boolean loadConfigs() {



        return true;
    }

    /**
     * Gets the reference of the current instance of the plugin.
     * Important for processes that use servers classes, methods and resources.
     * @return  <code>FunQuiz</code> instance.
     */
    public static FunQuiz getInstance() { return instance; }
}
