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

    public void reloadConfig() {

        /*
        Stupid TRY-CATCH block. Nothing I can do about that.
         */

        try {
            loadConfig();
        } catch(IOException ex) {
            instance.getLogger().severe("Couldn't close input/output streams for config file " + configFile + "\n" + ex.getLocalizedMessage());
        }

        config = new YamlConfiguration();

        try {
            config.load(configFile);

        } catch(Exception ex) {
            instance.getLogger().severe("Couldn't read config file " + configFile + "\n" + ex.getLocalizedMessage());
        }
    }

    public boolean saveConfig() {

        try {
            config.save(configFile);
            return true;

        } catch(Exception ex) {
            instance.getLogger().severe("Couldn't save config file" + configFile + "\n" + ex.getLocalizedMessage());
        }

        return false;
    }

    private void loadConfig() throws IOException {

        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            configFile.createNewFile();
        }

        InputStream in = instance.getResource(configFile.getName());
        OutputStream out = new FileOutputStream(configFile);

        try {
            if (in != null) {
                int b;
                while((b = in.read()) != -1)
                    out.write(b);
            }

        } catch(IOException ex) {
            instance.getLogger().severe("Couldn't write to config file " + configFile + "\n" + ex.getLocalizedMessage());
        } finally {
            if(in != null)
                in.close();

            if(out != null)
                out.close();
        }
    }

    public FileConfiguration getConfig() {
        return config;
    }
}