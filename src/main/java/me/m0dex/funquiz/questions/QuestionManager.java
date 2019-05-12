package me.m0dex.funquiz.questions;

import me.m0dex.funquiz.FunQuiz;
import me.m0dex.funquiz.utils.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuestionManager {

    private static FunQuiz instance;

    private Configuration questionsConf;

    private List<Question> questions;

    private Question activeQuestion;

    private Random random;

    public QuestionManager(FunQuiz _instance, Configuration _conf) {
        instance = _instance;
        questionsConf = _conf;

        activeQuestion = null;

        random = new Random();

        loadQuestions();
    }

    public Question getActiveQuestion() {
        return activeQuestion;
    }

    public void askQuestion() {
        int index = random.nextInt(questions.size());

        Question selected = questions.get(index);
        selected.run();
        setActiveQuestion(selected);
    }

    public void askQuestion(String name) {
        //TODO: Selecting a question
    }

    public void setActiveQuestion(Question question) { activeQuestion = question; }

    private void loadQuestions() {
        questions = new ArrayList<>();

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
    }
}
