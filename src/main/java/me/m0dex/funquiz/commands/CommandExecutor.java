package me.m0dex.funquiz.commands;

import me.m0dex.funquiz.FunQuiz;
import me.m0dex.funquiz.utils.Common;
import me.m0dex.funquiz.utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandExecutor implements org.bukkit.command.CommandExecutor {

    FunQuiz instance;

    public CommandExecutor(FunQuiz _instance) {
        super();
        this.instance = _instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        CommandModule module = instance.getCommandModule(cmd.getName());
        if(module != null) {

            if(Common.hasPermission(sender, module.perm) || module.perm == "") {

                if(module.allowConsole) {

                    if(module.minArgs <= args.length && args.length <= module.maxArgs)
                        module.run(sender, new CommandContext(args));
                    else
                        return false;

                } else {

                    if(sender instanceof Player) {

                        if (module.minArgs <= args.length && args.length <= module.maxArgs)
                            module.run(sender, new CommandContext(args));
                        else
                            return false;

                    } else
                        Common.tell(sender, Messages.NOT_A_CONSOLE_COMMAND);
                }

            }
        }

        return true;
    }
}
