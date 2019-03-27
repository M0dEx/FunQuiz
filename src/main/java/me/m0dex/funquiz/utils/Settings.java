package me.m0dex.funquiz.utils;

import me.m0dex.funquiz.FunQuiz;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class Settings {

    private FunQuiz instance;

    private FileConfiguration config;

    private Map<String, Object> settings = new HashMap<>();

    public Settings(FunQuiz _instance, FileConfiguration _config) {

        instance = _instance;

        config = _config;

        for(String key : config.getKeys(true)) {
            settings.put(key, config.get(key));
        }
    }

    /**
     * Gets the setting with the specified key.
     *
     * @param key   <code>String</code> setting key
     * @return      <code>Object</code> setting or <code>null</code> if no setting with the key is found
     */
    public Object getSetting(String key) {
        return settings.get(key);
    }
}
