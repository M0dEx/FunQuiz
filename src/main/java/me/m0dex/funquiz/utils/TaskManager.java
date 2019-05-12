package me.m0dex.funquiz.utils;

import me.m0dex.funquiz.FunQuiz;
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

    public int addTask(BukkitTask task) {

        int tID = task.getTaskId();

        if(tasks.containsKey(tID))
            return -1;

        tasks.put(tID, task);

        return tID;
    }

    public BukkitTask getTask(int tID) {
        return tasks.get(tID);
    }

    public void stopTasks() {
        for(int id : tasks.keySet())
            tasks.get(id).cancel();
    }
}
