package me.m0dex.funquiz.commands;

import me.m0dex.funquiz.FunQuiz;
import me.m0dex.funquiz.inventories.Inventories;
import me.m0dex.funquiz.utils.Common;
import me.m0dex.funquiz.utils.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FunQuizCommand extends CommandModule {

    public FunQuizCommand(FunQuiz _instance) {
        super(_instance, "funquiz", "", 0, 1, true);
    }

    @Override
    public void run(CommandSender sender, CommandContext args) {

        switch(args.getString(0)) {
            case "menu":
                menu(sender);
                break;
            case "reload":
                reload(sender);
                break;
            default:
                help(sender);
                break;
        }
    }

    private void menu(CommandSender sender) {

        if(!Common.hasPermission(sender, "funquiz.menu"))
            return;

        if(sender instanceof Player)
            Inventories.MainMenu.open((Player)sender);
        else
            Common.tell(sender, Messages.NOT_A_CONSOLE_COMMAND);
    }

    private void reload(CommandSender sender) {

        if(!Common.hasPermission(sender, "funquiz.reload"))
            return;

        Common.tell(sender, Messages.RELOADED);
        instance.reload();
    }

    @Override
    public void help(CommandSender sender) {
        if(!Common.hasPermission(sender, "funquiz.help"))
            return;
    }
}
