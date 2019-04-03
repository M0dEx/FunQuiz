package me.m0dex.funquiz.questions;

import me.m0dex.funquiz.FunQuiz;
import me.m0dex.funquiz.utils.Common;
import me.m0dex.funquiz.utils.Messages;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Question {

    String name;
    String question;
    List<String> answers;
    List<String> rewards;

    List<UUID> playersAnswered;

    FunQuiz instance;

    public Question(String _name, String _question, List<String> _answers, List<String> _rewards, FunQuiz _instance) {

        instance = _instance;

        name = _name.toUpperCase();
        question = Common.applyColours(_question);

        for(String ans : _answers)
            answers.add(ans.toLowerCase());

        for(String rew : _rewards) {
            rew = QuestionManager.parseReward(rew);
            if(rew != "")
                rewards.add(rew);
            else
                instance.getLogger().severe("Couldn't load reward \"" + rew + "\"");
        }

        playersAnswered = new ArrayList<>();
    }

    public void run() {
        Common.broadcast(Messages.QUESTION.getMessage(question + "-%question%"));
    }

    public void end() {
        playersAnswered.clear();
        Common.broadcast(Messages.QUESTION_END.getMessage(answers.get(0) + "-%answer%"));
    }
}
