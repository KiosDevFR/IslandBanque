package org.cubitech.islandbanque.objects;

import com.bgsoftware.superiorskyblock.api.island.Island;

import java.time.LocalDateTime;

public class BanqueLogFarmpoints extends BanqueLog{
    public BanqueLogFarmpoints(long amount, Island island,long totalAmountAfter, LocalDateTime date, String playerName) {
        super(amount, island,totalAmountAfter, date,playerName);
    }
}
