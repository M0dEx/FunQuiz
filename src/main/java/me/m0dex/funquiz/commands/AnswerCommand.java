package me.m0dex.funquiz.commands;

import me.m0dex.funquiz.FunQuiz;
import me.m0dex.funquiz.questions.Question;
import me.m0dex.funquiz.questions.QuestionManager;
import me.m0dex.funquiz.utils.Common;
import me.m0dex.funquiz.utils.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class AnswerCommand extends CommandModule {

    QuestionManager questionManager;

    public AnswerCommand(FunQuiz _instance, QuestionManager _questionManager) {
        super(_instance, "answer", "", 1, 1, false);
        questionManager = _questionManager;
    }

    @Override
    public void run(CommandSender sender, String[] args) {

        if(!(sender instanceof Player)) {
            Common.tell(sender, Messages.ONLY_PLAYERS_CAN_ANSWER);
            return;
        }

        Player player = (Player) sender;

        Question question = questionManager.getActiveQuestion();

        if(question == null) {
            Common.tell(sender, Messages.NO_QUESTION);
            return;
        }

        String answer = args[0].toLowerCase();

        switch(question.checkAnswer(player.getUniqueId(), answer)) {
            case 1:
                Common.tell(player, Messages.ANSWERED_TOO_LATE);
                break;
            case 2:
                Common.tell(player, Messages.ALREADY_ANSWERED);
                break;
            case 3:
                Common.tell(player, Messages.ANSWERED_CORRECTLY);
                break;
            default:
                Common.tell(player, Messages.WRONG_ANSWER);
                break;
        }
    }
}
