package me.m0dex.funquiz.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.file.Paths;

public class Configuration {

    private File configFile;
    private FileConfiguration config;
    private JavaPlugin instance;

    public Configuration(JavaPlugin _instance, String folder, String filename) {

        instance = _instance;

        configFile = new File(Paths.get(instance.getDataFolder().getAbsolutePath(), folder).toFile(), filename);

        reloadConfig();
    }


    /**
     * Reloads the config and tries to load it into FileConfiguration variable
     */
    public void reloadConfig() {

        loadConfig();

        config = new YamlConfiguration();

        try {
            config.load(configFile);

        } catch(Exception ex) {
            instance.getLogger().severe("Couldn't read config file " + configFile + "\n" + ex.getLocalizedMessage());
        }
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
            instance.getLogger().severe("Couldn't save config file" + configFile + "\n" + ex.getLocalizedMessage());
        }

        return false;
    }

    /**
     *          Tries to load the config from a file.
     *          If it does not exist, it then creates it and if there exists a resource in .jar of the plugin, copies its contents from there
     */
    private void loadConfig() {

        if (!configFile.exists()) {

            try (InputStream in = instance.getResource(configFile.getName());
                 OutputStream out = new FileOutputStream(configFile)) {

                configFile.getParentFile().mkdirs();
                configFile.createNewFile();

                if (in != null) {
                    int b;
                    while ((b = in.read()) != -1)
                        out.write(b);
                }

            } catch (IOException ex) {
                instance.getLogger().severe("Couldn't write to config file " + configFile + "\n" + ex.getLocalizedMessage());
            }
        }
    }

    /**
     *
     * @return <code>FileConfiguration</code> or <code>null</code>
     */
    public FileConfiguration getConfig() {
        return config;
    }
}