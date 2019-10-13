package me.m0dex.funquiz.utils;

import me.m0dex.funquiz.FunQuiz;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class Settings {

    private FunQuiz instance;

    private FileConfiguration config;

    public int answersAccepted;
    public int timeout;
    public int interval;

    public boolean otdbEnabled;
    public List<String> defaultRewards;

    public Settings(FunQuiz _instance, FileConfiguration _config) {

        instance = _instance;

        config = _config;

        instance.saveDefaultConfig();

        answersAccepted = config.getInt("questions.answers-accepted", 1);
        timeout = config.getInt("questions.timeout", 10);
        interval = config.getInt("questions.interval", 30);

        otdbEnabled = config.getBoolean("open-trivia-db.enabled", false);
        defaultRewards = config.getStringList("open-trivia-db.default-rewards");
    }
}
