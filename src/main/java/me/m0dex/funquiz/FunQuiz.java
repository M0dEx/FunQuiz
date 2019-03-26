package me.m0dex.funquiz;

import me.m0dex.funquiz.utils.Messages;
import org.bukkit.plugin.java.JavaPlugin;

public class FunQuiz extends JavaPlugin {

    private static FunQuiz instance;

    @Override
    public void onEnable() {
        this.instance = this;
    }

    public static FunQuiz getInstance() { return instance; }
}
