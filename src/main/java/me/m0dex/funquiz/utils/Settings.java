package me.m0dex.funquiz.utils;

import me.m0dex.funquiz.FunQuiz;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class Settings {

    private FunQuiz instance;

    private FileConfiguration config;

    /*
        QUESTIONS
     */
    public int minPlayers;
    public int answersAccepted;
    public int timeout;
    public int interval;

    public Sound soundAsked;
    public Sound soundAnsweredRight;
    public Sound soundAnsweredWrong;
    public Sound soundEnded;

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

        minPlayers = config.getInt("questions.min-players", 3);
        answersAccepted = config.getInt("questions.answers-accepted", 1);
        timeout = config.getInt("questions.timeout", 10);
        interval = config.getInt("questions.interval", 30);

        try {
            String soundAskedString = config.getString("questions.sounds.asked", "NONE").toUpperCase().trim();
            if (soundAskedString.equals("NONE"))
                soundAsked = null;
            else
                soundAsked = Sound.valueOf(soundAskedString);

            String soundAnsweredRightString = config.getString("questions.sounds.answered-right", "NONE").toUpperCase().trim();
            if (soundAnsweredRightString.equals("NONE"))
                soundAnsweredRight = null;
            else
                soundAnsweredRight = Sound.valueOf(soundAnsweredRightString);

            String soundAnsweredWrongString = config.getString("questions.sounds.answered-wrong", "NONE").toUpperCase().trim();
            if (soundAnsweredWrongString.equals("NONE"))
                soundAnsweredWrong = null;
            else
                soundAnsweredWrong = Sound.valueOf(soundAnsweredWrongString);

            String soundEndedString = config.getString("questions.sounds.ended", "NONE").toUpperCase().trim();
            if (soundEndedString.equals("NONE"))
                soundEnded = null;
            else
                soundEnded = Sound.valueOf(soundEndedString);

        } catch (Exception ex) {
            instance.getLogger().severe("Couldn't parse one of the sound values. Please check!");
        }

        otdbEnabled = config.getBoolean("open-trivia-db.enabled", false);
        defaultRewards = config.getStringList("open-trivia-db.default-rewards");

        answerPrefix = config.getString("misc.answer-prefix", "?");
    }
}
