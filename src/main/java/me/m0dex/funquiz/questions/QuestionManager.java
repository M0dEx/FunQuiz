package me.m0dex.funquiz.questions;

import me.m0dex.funquiz.FunQuiz;
import me.m0dex.funquiz.questions.opentriviadb.OpenTriviaDB;
import me.m0dex.funquiz.utils.Configuration;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuestionManager {

    private FunQuiz instance;
    private OpenTriviaDB otdb;
    private Configuration questionsConf;

    private List<Question> questions;
    private List<Question> otdbQuestions;
    private Question activeQuestion;
    private boolean enabled;

    private int triviaTaskID;
    private int questionTaskID;

    private Random random;

    public QuestionManager(FunQuiz _instance, Configuration _conf) {
        instance = _instance;
        questionsConf = _conf;

        otdb = new OpenTriviaDB(instance);

        questions = new ArrayList<>();
        otdbQuestions = new ArrayList<>();

        activeQuestion = null;

        random = new Random();

        triviaTaskID = -1;

        loadQuestions();

        enabled = true;

        registerQuestionTask();
    }

    public Question getActiveQuestion() {
        return activeQuestion;
    }

    public void reload() {

        enabled = false;

        if (instance.getSettings().otdbEnabled)
            instance.getTaskManager().stopTask(triviaTaskID);

        questions = new ArrayList<>();
        otdbQuestions = new ArrayList<>();

        activeQuestion = null;

        triviaTaskID = -1;

        //TODO: Handle saving and reloading
        questionsConf.loadConfig();

        loadQuestions();

        enabled = true;
    }

    /**
     * Selects a random question from a list of all questions (questions.yml AND Open Trivia DB, if enabled)
     */
    public int askQuestion() {

        List<Question> allQuestions = getQuestions();

        int index = random.nextInt(allQuestions.size());

        return askQuestion(allQuestions.get(index).name);
    }

    public int askQuestion(String name) {

        if(activeQuestion != null)
            return 3;

        if(instance.getServer().getOnlinePlayers().size() < instance.getSettings().minPlayers)
            return 2;

        Question selected = getQuestion(name);

        if(selected == null) {
            instance.getLogger().warning("Question \"" + name + "\" doesn't exist");
            return 1;
        }

        selected.run();
        return 0;
    }

    public Question getQuestion(String name) {

        for(Question question : getQuestions()) {
            if(question.name.equalsIgnoreCase(name))
                return question;
        }

        return null;
    }

    public List<Question> getQuestions() {

        List<Question> allQuestions = new ArrayList<>();
        allQuestions.addAll(questions);
        allQuestions.addAll(otdbQuestions);

        return allQuestions;
    }

    void setActiveQuestion(Question question) { activeQuestion = question; }

    private void registerQuestionTask() {

        if(instance.getSettings().interval.isZero()) {

            if(instance.getSettings().debug)
                instance.getLogger().info("Question task not initialized due to interval being set to 0!");

            return;
        }

        questionTaskID = instance.getTaskManager().addTask(new BukkitRunnable() {
            @Override
            public void run() {
                askQuestion();
            }
        }.runTaskTimer(instance, instance.getSettings().interval.toTicks(), instance.getSettings().interval.toTicks()));
    }

    public void restartQuestionTask() {

        if(questionTaskID != -1)
            instance.getTaskManager().stopTask(questionTaskID);

        registerQuestionTask();
    }

    /**
     * Loads questions from questions.yml and Open Trivia DB if it's enabled
     */
    private void loadQuestions() {

        ConfigurationSection section = questionsConf.getConfig().getConfigurationSection("questions");

        for (String key : section.getKeys(false)) {

            String name = key.toUpperCase().replace('-', '_');
            String question = StringEscapeUtils.unescapeJava(section.getString(key + ".question"));
            boolean hideAnswer = section.getBoolean(key + ".hide-answer", false);
            List<String> answers = section.getStringList(key + ".answers");
            List<String> rewards = section.getStringList(key + ".rewards");

            if (question.equals("") || answers == null || rewards == null) {
                instance.getLogger().severe("Couldn't load question " + name);
                continue;
            }

            questions.add(new Question(name, question, hideAnswer, answers, rewards, instance));
        }

        if (instance.getSettings().otdbEnabled) {
            triviaTaskID = instance.getTaskManager().addTask(new BukkitRunnable() {
                @Override
                public void run() {
                    enabled = false;

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                    }

                    otdbQuestions.clear();
                    otdbQuestions.addAll(otdb.getQuestions());

                    enabled = true;
                }
            }.runTaskTimerAsynchronously(instance, 0, instance.getSettings().interval.toTicks() * 25));
        }
    }
}
