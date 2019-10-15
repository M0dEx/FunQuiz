package me.m0dex.funquiz.commands;

import me.m0dex.funquiz.FunQuiz;
import me.m0dex.funquiz.questions.Question;
import me.m0dex.funquiz.utils.Common;
import me.m0dex.funquiz.utils.Messages;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class QuestionsCommand extends CommandModule {

    public QuestionsCommand(FunQuiz _instance) {
        super(_instance, "questions", "funquiz.questions.help", 1, 2, true);
    }

    @Override
    public void run(CommandSender sender, CommandContext args) {

        instance.getLogger().info(args.getString(0));

        switch(args.getString(0).toLowerCase()) {
            case "list":
                list(sender);
                break;
            case "info":
                info(sender, args);
        }
    }

    private void list(CommandSender sender) {

        List<String> questionNames = instance.getQuestionManager().getQuestionNames();

        StringBuilder questions = new StringBuilder();
        questions.append("&r&a");
        questions.append(questionNames.get(0));

        for(int i = 1; i < questionNames.size(); i++) {
            questions.append("&r, &a");
            questions.append(questionNames.get(i));
        }

        Common.tell(sender, Messages.QUESTIONS_LIST.getMessage("%questions%-" + questions.toString()));
    }

    private void info(CommandSender sender, CommandContext args) {

        List<String> questionNames = instance.getQuestionManager().getQuestionNames();

        if(questionNames.contains(args.getString(1).toUpperCase())) {

            Question question = instance.getQuestionManager().getQuestion(args.getString(1));

            String answers = String.join(", " + question.getAnswers());
            String rewards = String.join(", " + question.getRewards());

            Common.tell(sender, Messages.QUESTIONS_INFO.getMessage("%name%-" + question.getName() +
                    ";%question%-" + question.getQuestion() +
                    ";%answers%-" + question.getAnswers() +
                    ";%rewards%-" + question.getRewards()));

        } else
            Common.tell(sender, Messages.QUESTIONS_INVALID.getMessage("%name%-" + args.getString(1)));
    }
}
