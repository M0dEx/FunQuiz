package me.m0dex.funquiz.questions;

import me.m0dex.funquiz.FunQuiz;
import me.m0dex.funquiz.utils.Configuration;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class QuestionManager {

    private static FunQuiz instance;

    private Configuration questionsConf;

    private List<Question> questions;

    private Question activeQuestion;

    public QuestionManager(FunQuiz _instance, Configuration _conf) {
        instance = _instance;
        questionsConf = _conf;

        activeQuestion = null;

        loadQuestions();
    }

    public Question getActiveQuestion() {
        return activeQuestion;
    }

    public void setActiveQuestion(Question question) { activeQuestion = question; }

    private void loadQuestions() {
        questions = new ArrayList<>();

        ConfigurationSection section = questionsConf.getConfig().getConfigurationSection("questions");

        for(String key : section.getKeys(false)) {

            String name = key.toUpperCase().replace('-', '_');
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

    public static String parseReward(String rew) {

        rew = rew.toLowerCase();
        String[] args = rew.split(" ");

        if(instance.getCommand(args[0]) != null)
            return rew;

        return "";
    }
}
