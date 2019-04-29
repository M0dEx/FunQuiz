package me.m0dex.funquiz.utils;

import org.bukkit.configuration.file.FileConfiguration;

public enum Messages {

    // Basic messages
    NO_PERMISSION("&e&lYou do not have the permission to do this!"),
    NOT_A_CONSOLE_COMMAND("&e&lThis is not a console command."),

    // Question related messages
    QUESTION("&a&l%question%"),
    NO_QUESTION("&e&lThere is no active question!"),
    QUESTION_END("&e&lThe time to answer the question ran out! The correct answer was: &b&l%answer%&e&l."),
    ONLY_PLAYERS_CAN_ANSWER("&e&lOnly players can answer questions!"),
    WRONG_ANSWER("&c&lWrong answer!"),
    ANSWERED_TOO_LATE("&c&lYou answered too late!"),
    ALREADY_ANSWERED("&c&lYou've already answered!"),
    ANSWERED_CORRECTLY("&a&lYou answered correctly!");


    private String path;
    private String value;

    private static FileConfiguration conf;

    Messages(String _value) {
        path = this.name().toLowerCase().replace("_","-");
        value = _value;
    }

    /**
     * Sets the configuration file for messages
     * @param config    <code>FileConfiguration</code> config file
     */
    public static void setFile(FileConfiguration config) {
        conf = config;
    }

    public String getDefault() {
        return this.value;
    }

    public String getPath() {
        return this.path;
    }

    /**
     *
     * @param args  Message variables to replace in this format "Replacement"-"%repl1%";"Replacement2"-"%repl2%"
     * @return      <code>String</code> message specified in the config with variables replaced by specified replacements
     */
    public String getMessage(String args) {

        String output = conf.getString(this.path, this.value);

        for(String arg : args.split(";"))
            output = output.replaceAll(arg.split("-")[0], arg.split("-")[1]);

        return output;
    }

    /**
     * Gets message from config
     * @return      <code>String</code> message specified in the config
     */
    public String getMessage() {
        return conf.getString(this.path, this.value);
    }
}
