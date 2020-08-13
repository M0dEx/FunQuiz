package me.m0dex.funquiz.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

public class Configuration {

    private final File configFile;
    private FileConfiguration config;
    private final JavaPlugin instance;

    public Configuration(JavaPlugin _instance, String folder, String filename) {

        instance = _instance;

        configFile = new File(Paths.get(instance.getDataFolder().getAbsolutePath(), folder).toFile(), filename);
    }

    /**
     *          Tries to save the config to the specified file
     * @return  <code>true</code> if saving the config was successful;
     *          <code>false</code> if it wasn't.
     */
    public boolean saveConfig() {

        try {
            config.save(configFile);
            return true;

        } catch(Exception ex) {
            instance.getLogger().severe("Couldn't save config file " + configFile + ":");
            ex.printStackTrace();
        }

        return false;
    }

    /**
     *          Tries to load the config from a file.
     *          If it does not exist, it then creates it and if there exists a resource in .jar of the plugin, copies its contents from there
     */
    public boolean loadConfig() {

        try {

            if (!configFile.exists()) {

                if (instance.getResource(configFile.getName()) != null)
                    instance.saveResource(configFile.getName(), false);
                else {
                    configFile.getParentFile().mkdirs();
                    configFile.createNewFile();
                }

                config = YamlConfiguration.loadConfiguration(configFile);

            } else {

                config = YamlConfiguration.loadConfiguration(configFile);

                InputStream in = instance.getResource(configFile.getName());

                if (in == null)
                    return true;

                YamlConfiguration newConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(in, StandardCharsets.UTF_8));

                config.setDefaults(newConfig);
            }

            config.save(configFile);

        } catch(Exception ex) {
            instance.getLogger().severe("Couldn't load config file " + configFile + ":");
            ex.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     *
     * @return <code>FileConfiguration</code> or <code>null</code>
     */
    public FileConfiguration getConfig() {
        return config;
    }
}