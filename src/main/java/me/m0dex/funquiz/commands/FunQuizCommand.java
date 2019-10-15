package me.m0dex.funquiz.commands;

import me.m0dex.funquiz.FunQuiz;
import me.m0dex.funquiz.inventories.MainMenuInventory;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FunQuizCommand extends CommandModule {

    public FunQuizCommand(FunQuiz _instance) {
        super(_instance, "funquiz", "funquiz.help", 0, 0, false);
    }

    @Override
    public void run(CommandSender sender, CommandContext args) {
        MainMenuInventory.INVENTORY.open((Player)sender);
    }
}
