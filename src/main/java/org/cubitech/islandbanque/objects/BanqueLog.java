package org.cubitech.islandbanque.objects;

import com.bgsoftware.superiorskyblock.api.island.Island;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BanqueLog {
    private long amount;
    private Island island;
    private long totalAmountAfter;
    private LocalDateTime date;
    private String playerName;

    public BanqueLog(long amount, Island island,long totalAmountAfter, LocalDateTime date, String playerName) {
        this.amount = amount;
        this.island = island;
        this.totalAmountAfter = totalAmountAfter;
        this.date = date;
        this.playerName = playerName;
    }

    public long getAmount() {
        return amount;
    }

    public Island getIsland() {
        return island;
    }

    public long getTotalAmountAfter(){return totalAmountAfter;}

    public LocalDateTime getDate() {
        return date;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getFormatedDate() {
        DateTimeFormatter dTF = DateTimeFormatter.ofPattern("dd MMM HH'h'mm");
        return date.format(dTF);
    }
}
