package me.m0dex.funquiz.questions;

import me.m0dex.funquiz.FunQuiz;
import me.m0dex.funquiz.utils.Common;
import me.m0dex.funquiz.utils.Messages;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

public class Question {

    String name;
    String question;
    boolean hideAnswer;
    List<String> answers;
    List<String> rewards;

    List<UUID> playersAnswered;
    int timerTaskID;

    int answeredRight;
    int answeredWrong;

    FunQuiz instance;

    /**
     * Initializes the question object
     * @param _name A <b>unique</b> name which the question can be identified with
     * @param _question The question itself
     * @param _hideAnswer Boolean specifying whether the answer should be revealed at the end of the question
     * @param _answers List of answers (<code>String</code>)
     * @param _rewards List of rewards (<code>String</code>)
     * @param _instance Instance of the plugin
     */
    public Question(String _name, String _question, boolean _hideAnswer, List<String> _answers, List<String> _rewards, FunQuiz _instance) {

        instance = _instance;

        name = _name.toUpperCase();
        question = Common.applyColours(_question);
        hideAnswer = _hideAnswer;
        answers = new ArrayList<>();
        rewards = new ArrayList<>();

        for(String ans : _answers)
            answers.add(ans.toLowerCase());

        for(String rew : _rewards) {
            rewards.add(rew.toLowerCase());
        }

        playersAnswered = new ArrayList<>();

        answeredRight = 0;
        answeredWrong = 0;
    }

    /**
     * Initializes the question object
     * @param _name A <b>unique</b> name which the question can be identified with
     * @param _question The question itself
     * @param _answers List of answers (<code>String</code>)
     * @param _rewards List of rewards (<code>String</code>)
     * @param _instance Instance of the plugin
     */
    public Question(String _name, String _question, List<String> _answers, List<String> _rewards, FunQuiz _instance) {

        this(_name, _question, _instance.getSettings().hideAnswer, _answers, _rewards, _instance);
    }

    /**
     * Checks the answer
     * @param player Player answering the question
     * @param answ Answer
     */
    public void checkAnswer(Player player, String answ) {

        if(!answers.contains(answ)) {

            Common.tell(player, Messages.WRONG_ANSWER);
            Common.playSound(player, instance.getSettings().soundAnsweredWrong);
            instance.getPlayerCache().getPlayerData(player.getUniqueId()).addAnsWrong();
            answeredWrong++;
            return;

        } else if(playersAnswered.size() >= instance.getSettings().answersAccepted) {

            Common.tell(player, Messages.ANSWERED_TOO_LATE);
            Common.playSound(player, instance.getSettings().soundAnsweredWrong);
            return;

        } else if(playersAnswered.contains(player.getUniqueId())) {

            Common.tell(player, Messages.ALREADY_ANSWERED);
            Common.playSound(player, instance.getSettings().soundAnsweredWrong);
            return;

        }

        playersAnswered.add(player.getUniqueId());
        Common.tell(player, Messages.ANSWERED_CORRECTLY);
        Common.playSound(player, instance.getSettings().soundAnsweredRight);
        instance.getPlayerCache().getPlayerData(player.getUniqueId()).addAnsRight();
        answeredRight++;

        if(playersAnswered.size() >= instance.getSettings().answersAccepted)
            this.end();
    }

    /**
     * Sends the question into the chat and starts the timer task to end the question
     */
    public void run() {
        Common.broadcast(Messages.QUESTION.getMessage("%question%-" + question ), instance.getSettings().disabledWorlds);
        Common.broadcastSound(instance.getSettings().soundAsked, instance.getSettings().disabledWorlds);
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

        if(this.hideAnswer)
            Common.broadcast(Messages.QUESTION_END_NO_ASNWER.getMessage(), instance.getSettings().disabledWorlds);
        else
            Common.broadcast(Messages.QUESTION_END.getMessage( "%answer%-" + answers.get(0)), instance.getSettings().disabledWorlds);

        Common.broadcast(Messages.QUESTION_END_STATS.getMessage("%correct%-" + answeredRight + ";%incorrect%-" + answeredWrong), instance.getSettings().disabledWorlds);

        Common.broadcastSound(instance.getSettings().soundEnded, instance.getSettings().disabledWorlds);

        sendRewards();
        playersAnswered.clear();
        answeredRight = 0;
        answeredWrong = 0;
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
