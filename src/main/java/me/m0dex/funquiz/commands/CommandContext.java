package me.m0dex.funquiz.commands;

import me.m0dex.funquiz.utils.Common;

public class CommandContext {

    private String[] args;

    public CommandContext(String[] _args) {
        args = _args;
    }

    public String getString(int index) {

        if(index >= 0 && index < args.length)
            return args[index].toLowerCase();
        else
            return "";
    }

    public int getInt(int index) {

        return Common.tryParseInt(getString(index));
    }
}
