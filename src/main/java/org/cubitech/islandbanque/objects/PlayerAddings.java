package org.cubitech.islandbanque.objects;

import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;

public class PlayerAddings {
    private final SuperiorPlayer player;
    private final long money;
    private final long xp;
    private final long farmpoints;

    public PlayerAddings(SuperiorPlayer player, long money, long xp, long farmpoints) {
        this.player = player;
        this.money = money;
        this.xp = xp;
        this.farmpoints = farmpoints;
    }

    public SuperiorPlayer getPlayer() {
        return player;
    }

    public long getMoney() {
        return money;
    }

    public long getXp() {
        return xp;
    }

    public long getFarmpoints() {
        return farmpoints;
    }
}
