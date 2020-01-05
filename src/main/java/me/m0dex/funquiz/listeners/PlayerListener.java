package me.m0dex.funquiz.listeners;

import me.m0dex.funquiz.FunQuiz;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    FunQuiz instance;

    public PlayerListener(FunQuiz _instance) {
        instance = _instance;
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        instance.getPlayerCache().addPlayer(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        instance.getPlayerCache().removePlayer(player.getUniqueId());
    }
}
