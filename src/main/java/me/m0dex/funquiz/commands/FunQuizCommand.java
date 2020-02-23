package me.m0dex.funquiz.commands;

import co.aikar.taskchain.TaskChain;
import me.m0dex.funquiz.FunQuiz;
import me.m0dex.funquiz.cache.PlayerData;
import me.m0dex.funquiz.inventories.Inventories;
import me.m0dex.funquiz.utils.Common;
import me.m0dex.funquiz.utils.Messages;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FunQuizCommand extends CommandModule {

    public FunQuizCommand(FunQuiz _instance) {
        super(_instance, "funquiz", "", 0, 2, true);
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
            case "stats":
                stats(sender, args);
                break;
            case "update":
                update(sender, args);
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

    @SuppressWarnings("deprecation")
    private void stats(CommandSender sender, CommandContext args) {

        if(!Common.hasPermission(sender, "funquiz.stats"))
            return;

        if(args.getString(1).equals("")) {

            if (!(sender instanceof Player)) {
                Common.tell(sender, Messages.NOT_A_CONSOLE_COMMAND);
                return;
            }

            PlayerData data = instance.getPlayerCache().getPlayerData(((Player) sender).getUniqueId());
            int correct = data.getAnsRight();
            int incorrect = data.getAnsWrong();
            Common.tell(sender, Messages.FUNQUIZ_STATS.getMessage("%player%-" + ((Player) sender).getDisplayName() + ";%correct%-" + correct + ";%incorrect%-" + incorrect + ";%ratio%-" + data.getRatio()));
        } else {

            Player player = instance.getServer().getPlayer(args.getString(1));

            if (player == null) {

                TaskChain<?> chain = instance.getTaskFactory().newChain();
                chain
                        .async(() -> {
                            chain.setTaskData("player", instance.getServer().getOfflinePlayer(args.getString(1)));
                        })
                        .async(() -> {
                            OfflinePlayer offPlayer = chain.getTaskData("player");

                            if (offPlayer != null)
                                chain.setTaskData("playerData", instance.getDB().getPlayerData(offPlayer.getUniqueId()));
                        })
                        .sync(() -> {

                            OfflinePlayer offPlayer = chain.getTaskData("player");

                            if (offPlayer == null) {
                                Common.tell(sender, Messages.UNKNOWN_PLAYER);
                                return;
                            }

                            PlayerData data = chain.getTaskData("playerData");

                            if (data.isEmpty()) {
                                Common.tell(sender, Messages.UNKNOWN_PLAYER);
                                return;
                            }

                            int correct = data.getAnsRight();
                            int incorrect = data.getAnsWrong();
                            Common.tell(sender, Messages.FUNQUIZ_STATS.getMessage("%player%-" + offPlayer.getName() + ";%correct%-" + correct + ";%incorrect%-" + incorrect + ";%ratio%-" + data.getRatio()));
                        })
                        .execute();

            } else {

                PlayerData data = instance.getPlayerCache().getPlayerData(player.getUniqueId());

                int correct = data.getAnsRight();
                int incorrect = data.getAnsWrong();
                Common.tell(sender, Messages.FUNQUIZ_STATS.getMessage("%player%-" + player.getDisplayName() + ";%correct%-" + correct + ";%incorrect%-" + incorrect + ";%ratio%-" + data.getRatio()));
            }
        }
    }

    public void update(CommandSender sender, CommandContext args) {

        if (!Common.hasPermission(sender, "funquiz.update"))
            return;

        TaskChain<?> chain = instance.getTaskFactory().newChain();
        chain
                .async(() -> {
                    chain.setTaskData("success", instance.update());
                })
                .sync(() -> {
                    if(chain.getTaskData("success"))
                        Common.tell(sender, Messages.UPDATE_SUCCESSFUL);
                    else
                        Common.tell(sender, Messages.UPDATE_UNSUCCESSFUL);
                })
                .execute();
    }

    @Override
    public void help(CommandSender sender) {
        if(!Common.hasPermission(sender, "funquiz.help"))
            return;

        Common.tell(sender, Messages.FUNQUIZ_HELP);
    }
}
