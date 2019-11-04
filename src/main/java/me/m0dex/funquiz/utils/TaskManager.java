package me.m0dex.funquiz.utils;

import me.m0dex.funquiz.FunQuiz;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class TaskManager {

    private FunQuiz instance;

    private Map<Integer, BukkitTask> tasks;

    public TaskManager(FunQuiz _instance) {

        instance = _instance;

        tasks = new HashMap<>();
    }

    /**
     * Adds the task to the monitored Map
     * @param task Task to add
     * @return Integer - task ID
     */
    public int addTask(BukkitTask task) {

        int tID = task.getTaskId();

        if(tasks.containsKey(tID))
            return -1;

        tasks.put(tID, task);

        return tID;
    }

    public BukkitTask getTask(int tID) {

        if(tasks.containsKey(tID))
            return tasks.get(tID);
        else
            instance.getLogger().severe("Couldn't find task with ID: " + tID);

        return null;
    }

    /**
     * Stops the task identified by task ID
     * @param tID Task ID
     */
    public void stopTask(int tID) {

        if(tasks.containsKey(tID)) {
            tasks.get(tID).cancel();
            tasks.remove(tID);
        } else
            instance.getLogger().severe("Couldn't find task with ID: " + tID);
    }

    /**
     * Stops all tasks
     */
    public void stopTasks() {
        for(int id : tasks.keySet())
            tasks.get(id).cancel();
    }
}
