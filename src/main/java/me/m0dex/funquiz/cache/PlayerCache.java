package me.m0dex.funquiz.cache;

import co.aikar.taskchain.TaskChain;
import me.m0dex.funquiz.FunQuiz;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerCache {

    private Map<UUID, PlayerData> playerData;

    private FunQuiz instance;

    public PlayerCache(FunQuiz _instance) {
        instance = _instance;
        playerData = new HashMap<>();
    }

    public void addPlayer(UUID uuid) {

        if (!playerData.containsKey(uuid)) {
            playerData.put(uuid, new PlayerData());
            TaskChain<?> dbChain = instance.getTaskFactory().newChain();
            dbChain
                    .async(() -> {
                        int timeout = 0;
                        while(!instance.isDatabaseReady() && timeout < 10) {
                            timeout++;
                            try {
                                Thread.sleep(100);
                            } catch (Exception e) {
                                if(instance.getSettings().debug) {
                                    instance.getLogger().severe("An error occured while database thread was asleep: ");
                                    e.printStackTrace();
                                }
                            }
                        }
                        dbChain.setTaskData("playerData", instance.getDB().getPlayerData(uuid));
                    })
                    .sync(() -> {
                        playerData.put(uuid, dbChain.getTaskData("playerData"));
                    })
                    .execute();
        } else
            if(instance.getSettings().debug)
                instance.getLogger().warning("Player already exists in cache: " + instance.getServer().getPlayer(uuid).getName());
    }

    public void removePlayer(UUID uuid) {

        if(playerData.containsKey(uuid)) {
            TaskChain<?> dbChain = instance.getTaskFactory().newChain();
            dbChain
                    .async(() -> {
                        int timeout = 0;
                        while(!instance.isDatabaseReady() && timeout < 10) {
                            timeout++;
                            try {
                                Thread.sleep(100);
                            } catch (Exception e) {
                                if(instance.getSettings().debug) {
                                    instance.getLogger().severe("An error occured while database thread was asleep: ");
                                    e.printStackTrace();
                                }
                            }
                        }
                        instance.getDB().savePlayerData(uuid, playerData.get(uuid));
                    })
                    .sync(() -> {
                        playerData.remove(uuid);
                    })
                    .execute();

        } else
            if(instance.getSettings().debug)
                instance.getLogger().warning("Player doesn't exist in cache: " + instance.getServer().getPlayer(uuid));
    }

    public void savePlayerData() {

        instance.getDB().savePlayerCache(playerData);
    }

    public PlayerData getPlayerData(UUID uuid) {

        if(playerData.containsKey(uuid))
            return playerData.get(uuid);
        else
            return new PlayerData();
    }
}
