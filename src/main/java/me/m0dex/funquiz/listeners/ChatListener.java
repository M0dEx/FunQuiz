package me.m0dex.funquiz.listeners;

import me.m0dex.funquiz.FunQuiz;
import me.m0dex.funquiz.questions.QuestionManager;
import me.m0dex.funquiz.utils.Common;
import me.m0dex.funquiz.utils.Messages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    FunQuiz instance;
    QuestionManager qm;

    public ChatListener(FunQuiz _instance) {
        super();

        instance = _instance;
        qm = instance.getQuestionManager();
    }

    @EventHandler(ignoreCancelled = true)
    public void onAsyncChatEvent(AsyncPlayerChatEvent e) {

        Player player = e.getPlayer();
        String message = Common.stripColours(e.getMessage());

        if(qm.getActiveQuestion() == null)
            return;

        switch (qm.getActiveQuestion().checkAnswer(player.getUniqueId(), message.toLowerCase())) {
            case 0:
                Common.tell(player, Messages.WRONG_ANSWER);
                break;
            case 1:
                Common.tell(player, Messages.ANSWERED_TOO_LATE);
                break;
            case 2:
                Common.tell(player, Messages.ALREADY_ANSWERED);
                break;
            case 3:
                Common.tell(player, Messages.ANSWERED_CORRECTLY);
                break;
        }

        e.setCancelled(true);
    }
}
