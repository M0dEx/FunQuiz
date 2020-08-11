package me.m0dex.funquiz.listeners;

import me.m0dex.funquiz.FunQuiz;
import me.m0dex.funquiz.questions.Question;
import me.m0dex.funquiz.questions.QuestionManager;
import me.m0dex.funquiz.utils.Common;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private FunQuiz instance;
    private QuestionManager qm;

    public ChatListener(FunQuiz _instance) {
        super();

        instance = _instance;
        qm = instance.getQuestionManager();
    }

    @EventHandler(ignoreCancelled = true)
    public void onAsyncChatEvent(AsyncPlayerChatEvent e) {

        Player player = e.getPlayer();
        String message = Common.stripColours(e.getMessage()).toLowerCase();
        Question question = qm.getActiveQuestion();

        if(instance.getSettings().disabledWorlds.contains(player.getWorld().getName().toLowerCase()))
            return;

        if(question == null)
            return;

        if(!message.startsWith(instance.getSettings().answerPrefix))
            return;

        message = message.replace(instance.getSettings().answerPrefix, "");

        e.setCancelled(question.checkAnswer(player, message));
    }
}
