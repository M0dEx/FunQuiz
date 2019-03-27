package me.m0dex.funquiz.commands;

import me.m0dex.funquiz.FunQuiz;
import org.bukkit.command.CommandSender;

public class FunQuizCommand extends CommandModule {

    public FunQuizCommand(FunQuiz _instance) {
        super(_instance, "funquiz", "", 0, 0, true);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        //TODO: Main command
    }
}
