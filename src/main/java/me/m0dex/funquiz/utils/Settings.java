package me.m0dex.funquiz.utils;

import me.m0dex.funquiz.FunQuiz;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class Settings {

    private FunQuiz instance;

    private FileConfiguration config;

    /*
        QUESTIONS
     */
    public int answersAccepted;
    public int timeout;
    public int interval;

    /*
        OPEN TRIVIA DB
     */
    public boolean otdbEnabled;
    public List<String> defaultRewards;

    /*
        MISCELLANEOUS
     */
    public String answerPrefix;

    public Settings(FunQuiz _instance, FileConfiguration _config) {

        instance = _instance;

        config = _config;

        instance.saveDefaultConfig();
        instance.getConfig().options().copyDefaults(true);
        instance.saveConfig();

        answersAccepted = config.getInt("questions.answers-accepted", 1);
        timeout = config.getInt("questions.timeout", 10);
        interval = config.getInt("questions.interval", 30);

        otdbEnabled = config.getBoolean("open-trivia-db.enabled", false);
        defaultRewards = config.getStringList("open-trivia-db.default-rewards");

        answerPrefix = config.getString("misc.answer-prefix", "?");
    }
}
