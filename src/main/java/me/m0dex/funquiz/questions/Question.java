package me.m0dex.funquiz.questions;

import me.m0dex.funquiz.FunQuiz;
import me.m0dex.funquiz.utils.Common;
import me.m0dex.funquiz.utils.Messages;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Question {

    String name;
    String question;
    List<String> answers;
    List<String> rewards;

    List<UUID> playersAnswered;
    BukkitTask timerTask;

    private long timeout;

    FunQuiz instance;

    public Question(String _name, String _question, List<String> _answers, List<String> _rewards, FunQuiz _instance) {

        instance = _instance;

        name = _name.toUpperCase();
        question = Common.applyColours(_question);
        answers = new ArrayList<>();
        rewards = new ArrayList<>();

        for(String ans : _answers)
            answers.add(ans.toLowerCase());

        for(String rew : _rewards) {
            rewards.add(rew.toLowerCase());
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
        playersAnswered.clear();
        Common.broadcast(Messages.QUESTION.getMessage("%question%-" + question ));
        timerTask = new BukkitRunnable() {
            @Override
            public void run() {
                end();
            }
        }.runTaskLater(instance, 20*instance.getSettings().timeout);
        timeout = System.currentTimeMillis()+1000*instance.getSettings().timeout;
    }

    public void end() {
        timerTask.cancel();
        instance.getQuestionManager().setActiveQuestion(null);
        Common.broadcast(Messages.QUESTION_END.getMessage( "%answer%-" + answers.get(0)));
        sendRewards();
    }

    private void sendRewards() {
        for(UUID uuid : playersAnswered) {
            Player player = instance.getServer().getPlayer(uuid);

            if(player == null)
                continue;

            for(String reward : rewards)
                Common.executeReward(player, reward);
        }
    }

    public int getPlayersAnswered() { return playersAnswered.size(); }
}
