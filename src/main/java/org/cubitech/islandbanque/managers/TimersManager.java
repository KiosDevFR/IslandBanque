package org.cubitech.islandbanque.managers;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import org.cubitech.islandbanque.IslandBanque;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class TimersManager {
    private static TimersManager instance;
    public BukkitTask task;

    private TimersManager(){
    }
    public void startTimer() {
        cancelTask();
        task = Bukkit.getServer().getScheduler().runTaskLater(IslandBanque.getInstance(), new Runnable() {
            @Override
            public void run() {
                Bukkit.getConsoleSender().sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.main.interest_can_claim_again")));
                IslandManager.getInstance().restartClaimedInterests();
                task = Bukkit.getServer().getScheduler().runTaskLater(IslandBanque.getInstance(), this, calculateTicksBeforeNextTime(IslandBanque.getInstance().getConfig().getString("Config.time-of-day-to-claim-interest")));
            }
        }, calculateTicksBeforeNextTime(IslandBanque.getInstance().getConfig().getString("Config.time-of-day-to-claim-interest")));
    }

    private void cancelTask() {
        if (task != null)
            try {
                task.cancel();
            } catch (Throwable e) {
                //Ignore
            }
    }


    public static TimersManager getInstance(){
        if(instance == null) {
            instance = new TimersManager();
        }
        return instance;
    }

    /**
     * Gets reimaining time in ticks until the next "time" on "world".
     * @param world World where the time will be got.
     * @param time value amount 0 and 24000
     * @return
     */
    public static long calculateTicksBeforeNextTime(String time){
        LocalTime now = LocalTime.now();
        String[] times = time.split(":");
        int timesHours = Integer.valueOf(times[0]);
        int timesMinutes = Integer.valueOf(times[1]);
        LocalTime targetTime = LocalTime.of(timesHours,timesMinutes);
        LocalTime resultTime = LocalTime.of(0,0);
        long seconds = now.until(targetTime, ChronoUnit.SECONDS);

        if(seconds < 0){
            seconds+= 86400;
        }

        if(seconds >= 0 && seconds <= 61){ // if the remaining time is less than a minute and a second, it skips to the next day to avoid message repetition.
            return 86400;
        }

        return seconds*20;
    }
}
