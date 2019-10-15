package me.m0dex.funquiz.questions;

import me.m0dex.funquiz.FunQuiz;
import me.m0dex.funquiz.utils.Common;
import me.m0dex.funquiz.utils.Messages;
import org.bukkit.entity.Player;
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
    int timerTaskID;

    FunQuiz instance;

    /**
     * Initializes the question object
     * @param _name A <b>unique</b> name which the question can be identified with
     * @param _question The question itself
     * @param _answers List of answers (<code>String</code>)
     * @param _rewards List of rewards (<code>String</code>)
     * @param _instance Instance of the plugin
     */
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

    /**
     * Checks the answer
     * @param player Player answering the question
     * @param answ Answer
     */
    public void checkAnswer(Player player, String answ) {

        if(!answers.contains(answ)) {

            Common.tell(player, Messages.WRONG_ANSWER);
            return;

        } else if(playersAnswered.size() >= instance.getSettings().answersAccepted) {

            Common.tell(player, Messages.ANSWERED_TOO_LATE);
            return;

        } else if(playersAnswered.contains(player.getUniqueId())) {

            Common.tell(player, Messages.ALREADY_ANSWERED);
            return;

        }

        playersAnswered.add(player.getUniqueId());
        Common.tell(player, Messages.ANSWERED_CORRECTLY);

        if(playersAnswered.size() >= instance.getSettings().answersAccepted)
            this.end();
    }

    /**
     * Sends the question into the chat and starts the timer task to end the question
     */
    public void run() {
        Common.broadcast(Messages.QUESTION.getMessage("%question%-" + question ));
        instance.getQuestionManager().setActiveQuestion(this);
        timerTaskID = instance.getTaskManager().addTask(new BukkitRunnable() {
            @Override
            public void run() {
                end();
            }
        }.runTaskLater(instance, 20*instance.getSettings().timeout));
    }

    /**
     * Ends the question and gives the player(s) their reward
     */
    public void end() {
        instance.getTaskManager().stopTask(timerTaskID);
        instance.getQuestionManager().setActiveQuestion(null);
        Common.broadcast(Messages.QUESTION_END.getMessage( "%answer%-" + answers.get(0)));
        sendRewards();
        playersAnswered.clear();
    }

    /**
     * Sends the rewards to the players
     */
    private void sendRewards() {
        for(UUID uuid : playersAnswered) {
            Player player = instance.getServer().getPlayer(uuid);

            if(player == null)
                continue;

            for(String reward : rewards)
                Common.executeReward(player, reward);
        }
    }

    public String getName() { return name; }
    public String getQuestion() { return question; }
    public List<String> getAnswers() { return answers; }
    public List<String> getRewards() { return rewards; }

    public int getPlayersAnswered() { return playersAnswered.size(); }
}
