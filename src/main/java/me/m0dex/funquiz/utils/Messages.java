package me.m0dex.funquiz.utils;

import org.bukkit.configuration.file.FileConfiguration;

public enum Messages {

    // Basic messages
    NO_PERMISSION("&3&lFunQuiz &e&l» &c&lYou do not have the permission to do this!"),
    NOT_A_CONSOLE_COMMAND("&3&lFunQuiz &e&l» &c&lThis is not a console command."),
    RELOADED("&3&lFunQuiz &e&l» &a&lThe plugin was reloaded!"),
    UNKNOWN_PLAYER("&3&lFunQuiz &e&l» &c&lUnknown player!"),

    // Inventories
    INVENTORY_MAIN_MENU_TITLE("&3&lMain Menu"),
    INVENTORY_QUESTION_LIST_TITLE("&3&lQuestion List"),
    INVENTORY_BACK("&c&lBack"),
    INVENTORY_EXIT("&c&lExit"),
    INVENTORY_QUESTION_LIST("&2&lQuestion List"),
    INVENTORY_RELOAD("&2&lReload plugin"),
    INVENTORY_ASK_RANDOM("&2&lAsk a random question"),
    INVENTORY_QUESTION_NAME("&6&l%name%"),
    INVENTORY_QUESTION_QUESTION("&2&lQuestion: &e%question%"),
    INVENTORY_QUESTION_ANSWERS("&2&lAnswers: &e%answers%"),
    INVENTORY_QUESTION_REWARDS("&2&lRewards: &e%rewards%"),
    INVENTORY_PREVIOUS("&e&lPrevious page"),
    INVENTORY_NEXT("&e&lNext page"),

    // FunQuiz command
    FUNQUIZ_STATS("&3&lStatistics for player %player%&e&l:" +
            "\n&e&lAnswered &a&lcorrectly&e&l: %correct%" +
            "\n&e&lAnswered &c&lincorrectly&e&l: %incorrect%" +
            "\n&e&lRatio&e&l: %ratio%"),
    UPDATE_SUCCESSFUL("&3&lFunQuiz &e&l» &a&lThe plugin was successfully updated!"),
    UPDATE_UNSUCCESSFUL("&3&lFunQuiz &e&l» &c&lCouldn't update the plugin. See the console for more information!"),

    // Question command
    QUESTIONS_LIST("&3&lList of available questions&e&l:\n&r&a%questions%"),
    QUESTIONS_INVALID("&3&lFunQuiz &e&l» &c&lUnknown question:&c&l %name%"),
    QUESTIONS_ALREADY_ACTIVE("&3&lFunQuiz &e&l» &c&lThere's already an active question."),
    QUESTIONS_ASKED("&3&lFunQuiz &e&l» Question asked!"),
    QUESTIONS_INFO("&3&lQuestion %name%&e&l:" +
            "\n&2&lQuestion: &e%question%" +
            "\n&2&lAnswers: &e%answers%" +
            "\n&2&lRewards: &e%rewards%"),
    QUESTIONS_HELP("&e&l--------------- &3&lQuestions Help&e&l ---------------" +
            "\n&2/questions list &e» &bLists the names of all questions" +
            "\n " +
            "\n&2/questions info <name> &e» &bDisplays information about the given question (question, answers, rewards)" +
            "\n " +
            "\n&2/questions ask [name] &e» &bAsks a random question if the name is not given, otherwise asks the given question" +
            "\n " +
            "\n&2/questions reload &e» &bReloads all questions and refreshes OTDB questions (if OTDB is enabled)" +
            "\n "),

    // Question related messages
    QUESTION("&3&lQuestion &e&l» &a&l%question%"),
    NOT_ENOUGH_PLAYERS("&3&lFunQuiz &e&l» &c&lThere is not enough players for FunQuiz to ask questions!"),
    QUESTION_END("&3&lFunQuiz &e&l» The time to answer the question ran out! The correct answer was: &b&l%answer%&e&l."),
    WRONG_ANSWER("&3&lFunQuiz &e&l» &c&lWrong answer!"),
    ANSWERED_TOO_LATE("&3&lFunQuiz &e&l» &c&lYou answered too late!"),
    ALREADY_ANSWERED("&3&lFunQuiz &e&l» &c&lYou've already answered!"),
    ANSWERED_CORRECTLY("&3&lFunQuiz &e&l» &a&lYou answered correctly!"),
    REWARD_GIVE("&3&lFunQuiz &e&l» You received &c&l%amount% &e&lof &c&l%item%&e&l!"),
    REWARD_MONEY("&3&lFunQuiz &e&l» You received &c&l%amount% &e&las a reward!");


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
            output = output.replace(arg.split("-")[0], arg.split("-")[1]);

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
