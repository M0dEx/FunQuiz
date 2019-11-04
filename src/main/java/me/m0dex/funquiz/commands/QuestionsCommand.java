package me.m0dex.funquiz.commands;

import me.m0dex.funquiz.FunQuiz;
import me.m0dex.funquiz.questions.Question;
import me.m0dex.funquiz.utils.Common;
import me.m0dex.funquiz.utils.Messages;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class QuestionsCommand extends CommandModule {

    public QuestionsCommand(FunQuiz _instance) {
        super(_instance, "questions", "", 0, 2, true);
    }

    @Override
    public void run(CommandSender sender, CommandContext args) {

        switch(args.getString(0).toLowerCase()) {
            case "list":
                list(sender);
                break;
            case "info":
                info(sender, args);
                break;
            case "ask":
                ask(sender, args);
                break;
            case "reload":
                reload(sender);
                break;
            default:
                help(sender);
                break;
        }
    }

    private void list(CommandSender sender) {

        if(!Common.hasPermission(sender, "funquiz.questions.list"))
            return;

        List<Question> questions = instance.getQuestionManager().getQuestions();

        String questionList = questions.stream().map(Question::getName).collect(Collectors.joining("&r, &a"));

        Common.tell(sender, Messages.QUESTIONS_LIST.getMessage("%questions%-" + questionList));
    }

    private void info(CommandSender sender, CommandContext args) {

        if(!Common.hasPermission(sender, "funquiz.questions.info"))
            return;

        Question question = instance.getQuestionManager().getQuestion(args.getString(1));

        if(question != null) {

            Common.tell(sender, Messages.QUESTIONS_INFO.getMessage("%name%-" + question.getName() +
                    ";%question%-" + question.getQuestion() +
                    ";%answers%-" + question.getAnswers() +
                    ";%rewards%-" + question.getRewards()));

        } else
            Common.tell(sender, Messages.QUESTIONS_INVALID.getMessage("%name%-" + args.getString(1)));
    }

    private void ask(CommandSender sender, CommandContext args) {

        if(!Common.hasPermission(sender, "funquiz.questions.ask"))
            return;

        if(args.getString(1).equals("")) {

            if(instance.getQuestionManager().askQuestion() == 0) {
                instance.getQuestionManager().restartQuestionTask();
                Common.tell(sender, Messages.QUESTIONS_ASKED);
            } else {
                Common.tell(sender, Messages.QUESTIONS_ALREADY_ACTIVE);
            }

        } else {

            int status = instance.getQuestionManager().askQuestion(args.getString(1));
            if(status == 0) {
                instance.getQuestionManager().restartQuestionTask();
                Common.tell(sender, Messages.QUESTIONS_ASKED);
            } else if(status == 1) {
                Common.tell(sender, Messages.QUESTIONS_INVALID.getMessage("%name%-" + args.getString(1)));
            } else {
                Common.tell(sender, Messages.QUESTIONS_ALREADY_ACTIVE);
            }
        }
    }

    private void reload(CommandSender sender) {

        if(!Common.hasPermission(sender, "funquiz.questions.reload"))
            return;

        instance.getQuestionManager().reload();
    }

    private void help(CommandSender sender) {
        if(!Common.hasPermission(sender, "funquiz.questions.help"))
            return;

        Common.tell(sender, Messages.QUESTIONS_HELP);
    }
}
