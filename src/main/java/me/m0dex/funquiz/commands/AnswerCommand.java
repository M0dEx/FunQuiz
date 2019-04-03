package me.m0dex.funquiz.commands;

import me.m0dex.funquiz.FunQuiz;
import me.m0dex.funquiz.questions.Question;
import me.m0dex.funquiz.questions.QuestionManager;
import me.m0dex.funquiz.utils.Common;
import me.m0dex.funquiz.utils.Messages;
import org.bukkit.command.CommandSender;

public class AnswerCommand extends CommandModule {

    QuestionManager questionManager;

    public AnswerCommand(FunQuiz _instance, QuestionManager _questionManager) {
        super(_instance, "answer", "", 1, 1, false);
        questionManager = _questionManager;
    }

    @Override
    public void run(CommandSender sender, String[] args) {

        Question question = questionManager.getActiveQuestion();

        if(question == null) {
            Common.tell(sender, Messages.NO_QUESTION);
            return;
        }

        //TODO: Handle answers
    }
}
