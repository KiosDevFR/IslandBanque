package org.cubitech.islandbanque.objects;

import com.bgsoftware.superiorskyblock.api.island.Island;

public class Bank {
    private Island island;
    private long money;
    private long xp;
    private long farmPoints;

    public Bank(Island island, long money, long xp, long farmPoints) {
        this.island = island;
        this.money = money;
        this.xp = xp;
        this.farmPoints = farmPoints;
    }

    public Island getIsland() {
        return this.island;
    }

    public long getMoney() {
        return money;
    }

    public long getExp() {
        return xp;
    }

    public long getFarmPoints() {
        return farmPoints;
    }
}
