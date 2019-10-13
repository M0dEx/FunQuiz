package me.m0dex.funquiz.questions;

import me.m0dex.funquiz.FunQuiz;
import me.m0dex.funquiz.questions.opentriviadb.OpenTriviaDB;
import me.m0dex.funquiz.utils.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;

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

        enabled = true;

        loadQuestions();
    }

    public Question getActiveQuestion() {
        return activeQuestion;
    }

    public void askQuestion() {

        List<Question> allQuestions = new ArrayList<>();
        allQuestions.addAll(questions);
        allQuestions.addAll(otdbQuestions);

        int index = random.nextInt(allQuestions.size());

        Question selected = allQuestions.get(index);
        selected.run();
        setActiveQuestion(selected);
    }

    public void askQuestion(String name) {
        //TODO: Selecting a question
    }

    public void setActiveQuestion(Question question) { activeQuestion = question; }

    private void loadQuestions() {

        ConfigurationSection section = questionsConf.getConfig().getConfigurationSection("questions");

        for(String key : section.getKeys(false)) {

            String name = key.toUpperCase().replace('-', ' ');
            String question = section.getString(key + ".question");
            List<String> answers = section.getStringList(key + ".answers");
            List<String> rewards = section.getStringList(key + ".rewards");

            if(question == null || answers == null || rewards == null) {
                instance.getLogger().severe("Couldn't load question " + name);
                continue;
            }

            questions.add(new Question(name, question, answers, rewards, instance));
        }

        if(instance.getSettings().otdbEnabled) {
            triviaTaskID = instance.getTaskManager().addTask(new BukkitRunnable() {
                @Override
                public void run() {
                    enabled = false;

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {}

                    instance.getLogger().info("Getting Open Trivia DB questions...");
                    otdbQuestions.clear();
                    otdbQuestions.addAll(otdb.getQuestions());

                    enabled = true;
                }
            }.runTaskTimerAsynchronously(instance, 0, instance.getSettings().interval * 60 * 25 * 20));
        }
    }
}
