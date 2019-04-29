package me.m0dex.funquiz.questions;

import me.m0dex.funquiz.FunQuiz;
import me.m0dex.funquiz.utils.Common;
import me.m0dex.funquiz.utils.Messages;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Question {

    String name;
    String question;
    List<String> answers;
    List<String> rewards;

    List<UUID> playersAnswered;
    BukkitRunnable timerTask;

    private long timeout;

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

    public int checkAnswer(UUID playerUUID, String answ) {

        /*
            0 == Wrong answer
            1 == Right answer but too many people answered already
            2 == Already answered
            3 == Right answer
         */

        if(!answers.contains(answ))
            return 0;

        if(playersAnswered.size() >= instance.getSettings().answersAccepted)
            return 1;

        if(playersAnswered.contains(playerUUID))
            return 2;

        playersAnswered.add(playerUUID);

        return 3;
    }

    public void run() {
        instance.getQuestionManager().setActiveQuestion(this);
        Common.broadcast(Messages.QUESTION.getMessage(question + "-%question%"));
        timerTask = (BukkitRunnable) new BukkitRunnable() {
            @Override
            public void run() {
                end();
                this.cancel();
            }
        }.runTaskLater(instance, System.currentTimeMillis()+20*instance.getSettings().timeout);
        timeout = System.currentTimeMillis()+20*instance.getSettings().timeout;
    }

    public void end() {
        instance.getQuestionManager().setActiveQuestion(null);
        playersAnswered.clear();
        Common.broadcast(Messages.QUESTION_END.getMessage(answers.get(0) + "-%answer%"));
    }
}
