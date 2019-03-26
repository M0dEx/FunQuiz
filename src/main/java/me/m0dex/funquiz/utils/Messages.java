package me.m0dex.funquiz.utils;

import org.bukkit.configuration.file.FileConfiguration;

public enum Messages {

    NO_PERMISSION("&e&lYou do not have the permission to do this!");

    private String path;
    private String value;

    private static FileConfiguration conf;

    Messages(String _value) {
        path = this.name().toLowerCase().replace("_","-");
        value = _value;
    }

    public static void setFile(FileConfiguration config) {
        conf = config;
    }

    public String getDefault() {
        return this.value;
    }

    public String getPath() {
        return this.path;
    }

    public String getMessage(String args) {

        String output = conf.getString(this.path, this.value);

        for(String arg : args.split(";"))
            output = output.replaceAll(arg.split("-")[0], arg.split("-")[1]);

        return output;
    }

    public String getMessage() {
        return conf.getString(this.path, this.value);
    }
}
