package me.m0dex.funquiz.commands;

import me.m0dex.funquiz.FunQuiz;
import org.bukkit.command.CommandSender;

public abstract class CommandModule {

    public String cmdName;
    public String perm;
    public int minArgs;
    public int maxArgs;
    public boolean allowConsole;

    private FunQuiz instance;

    public CommandModule(FunQuiz _instance, String _cmdName, String _perm, int _minArgs, int _maxArgs, boolean _allowConsole) {
        this.instance = _instance;

        this.cmdName = _cmdName;
        this.perm = _perm;
        this.minArgs = _minArgs;
        this.allowConsole = _allowConsole;

        if(_maxArgs == -1)
            this.maxArgs = 99;
        else
            this.maxArgs = _maxArgs;

        instance.addCommand(cmdName, this);
    }

    public abstract void run(CommandSender sender, String[] args);
}
