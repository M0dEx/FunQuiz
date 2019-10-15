package me.m0dex.funquiz.utils;

import org.bukkit.configuration.file.FileConfiguration;

public enum Messages {

    // Basic messages
    NO_PERMISSION("&3&lFunQuiz &e&l» &c&lYou do not have the permission to do this!"),
    NOT_A_CONSOLE_COMMAND("&3&lFunQuiz &e&l» &c&lThis is not a console command."),

    // Inventory menus
    MAIN_MENU_TITLE("&3&lMain Menu"),

    //Question command
    QUESTIONS_LIST("&3&lList of available questions&e&l:\n%questions%"),
    QUESTIONS_INVALID("&3&lFunQuiz &e&l» Unknown question:&c&l %name%"),
    QUESTIONS_INFO("&3&lQuestion %name%&e&l:" +
            "\n&2&lQuestion: &e%question%" +
            "\n&2&lAnswers: &e%answers%" +
            "\n&2&lRewards: &e%rewards%"),

    // Question related messages
    QUESTION("&3&lQuestion &e&l» &a&l%question%"),
    QUESTION_END("&3&lFunQuiz &e&l» The time to answer the question ran out! The correct answer was: &b&l%answer%&e&l."),
    WRONG_ANSWER("&3&lFunQuiz &e&l» &c&lWrong answer!"),
    ANSWERED_TOO_LATE("&3&lFunQuiz &e&l» &c&lYou answered too late!"),
    ALREADY_ANSWERED("&3&lFunQuiz &e&l» &c&lYou've already answered!"),
    ANSWERED_CORRECTLY("&3&lFunQuiz &e&l» &a&lYou answered correctly!"),
    REWARD_GIVE("&3&lFunQuiz &e&l» You received &c&l%amount% &e&lof &c&l%item%&e&l!");


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
     * @param args  Message variables to replace in this format "%repl1%"-"Replacement";"%repl2%"-"Replacement2"
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
