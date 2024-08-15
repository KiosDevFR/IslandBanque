package org.cubitech.islandbanque.managers;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import me.mraxetv.beasttokens.api.BeastTokensAPI;
import org.bukkit.entity.Player;
import org.cubitech.islandbanque.IslandBanque;
import org.cubitech.islandbanque.objects.*;
import org.cubitech.islandbanque.utils.UtilsBanque;

import java.util.ArrayList;

public class IslandManager {
    private static IslandManager instance;

    private IslandManager(){

    }

    /**
     * This method access to the Bank related by the UUID of the island on the database and
     * return an object copy
     * @param island
     * @return
     */
    public Bank getIslandBank(Island island){
        return DatabaseManager.getInstance().getIslandBank(island);
    }

    /**
     * Adds balance to an island.
     * @param player
     * @param type (0 - money, 1 - xp, 2 - farmpoints)
     * @param amount
     */
    public void addBalanceToIsland(Player player, int type,long amount){
        SuperiorPlayer splayer = SuperiorSkyblockAPI.getPlayer(player);
        addBalanceToIsland(splayer.getIsland(),type,amount);
        switch (type) {
            case 0 -> {
                if(amount > 0)
                IslandBanque.getInstance().getEconomy().withdrawPlayer(player,amount);
                else if(amount < 0){
                    IslandBanque.getInstance().getEconomy().depositPlayer(player,-amount);
                }
            }
            case 1 -> {
                UtilsBanque.setTotalExperience(player,(int)(UtilsBanque.getTotalExperience(player) - amount));
            }
            case 2 -> {
                if(amount > 0)
                BeastTokensAPI.getTokensManager().removeTokens(player,amount);
                else if(amount < 0){
                    BeastTokensAPI.getTokensManager().addTokens(player,-amount);
                }
            }
            default -> {
                throw new RuntimeException("An unexpected value has been introduced.");
            }
        }
        DatabaseManager.getInstance().generateTransaction(splayer,type,amount);
    }

    public boolean hasClaimedInterest(SuperiorPlayer player){
        return DatabaseManager.getInstance().hasClaimedInterest(player.getIsland());
    }

    public boolean hasClaimedInterest(Island island){
        return DatabaseManager.getInstance().hasClaimedInterest(island);
    }

    public void restartClaimedInterests(){
        DatabaseManager.getInstance().restartClaimedInterests();
    }

    public void claimInterest(SuperiorPlayer player){
        DatabaseManager.getInstance().claimInterest(player);
    }
    public void claimInterest(Island island){
        DatabaseManager.getInstance().claimInterest(island);
    }

    /**
     * Sets interest of island.
     * @param island
     * @param amount
     */
    public void setInterest(Island island,double amount){
        DatabaseManager.getInstance().setInterest(island,amount);
    }

    /**
     * Returns interest of island.
     * @param island
     * @return
     */
    public double getInterest(Island island){
        return DatabaseManager.getInstance().getInterestMoney(island);
    }

    private void addBalanceToIsland(Island island, int type,long amount){
        DatabaseManager.getInstance().addBalanceToIsland(island, type, amount);
    }

    public PlayerAddings getPlayerAddings(SuperiorPlayer player, Island island){
        return DatabaseManager.getInstance().getPlayerAddings(player,island);
    }

    public static IslandManager getInstance(){
        if(instance == null){
            instance = new IslandManager();
        }
        return instance;
    }

    public void setInterestLimit(Island island, int type, long amount) {
        DatabaseManager.getInstance().setInterestLimit(island, type, amount);
    }

    public long getInterestLimit(Island island, int type) {
        return switch (type){
            case 0-> DatabaseManager.getInstance().getInterestLimitMoney(island);
            case 1-> DatabaseManager.getInstance().getInterestLimitXp(island);
            case 2-> DatabaseManager.getInstance().getInterestLimitFarmpoints(island);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }

    public ArrayList<BanqueLogMoney> getIslandMoneyLogs(Island island){
        return DatabaseManager.getInstance().getIslandMoneyLogs(island);
    }
    public ArrayList<BanqueLogXp> getIslandXpLogs(Island island){
        return DatabaseManager.getInstance().getIslandXpLogs(island);
    }
    public ArrayList<BanqueLogFarmpoints> getIslandFarmpointsLogs(Island island){
        return DatabaseManager.getInstance().getIslandFarmpointsLogs(island);
    }

    public void adminAddIslandBalance(Island island, int type, long amount) {
        Bank previousBank = IslandManager.getInstance().getIslandBank(island);
        long bankAmount = switch (type){
            case 0 -> previousBank.getMoney();

            case 1 -> previousBank.getExp();

            case 2 -> previousBank.getFarmPoints();

            default -> throw new RuntimeException("UNEXPECTED VALUE");
        };
        bankAmount += amount;
        if(bankAmount < 0){
            bankAmount = 0;
        }
        DatabaseManager.getInstance().setIslandBalance(island,type,bankAmount);
    }
}
