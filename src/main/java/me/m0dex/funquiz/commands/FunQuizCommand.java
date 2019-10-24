package me.m0dex.funquiz.commands;

import me.m0dex.funquiz.FunQuiz;
import org.bukkit.command.CommandSender;

public class FunQuizCommand extends CommandModule {

    public FunQuizCommand(FunQuiz _instance) {
        super(_instance, "funquiz", "funquiz.help", 0, 0, true);
    }

    @Override
    public void run(CommandSender sender, CommandContext args) {
        //TODO: Main command
    }
}
