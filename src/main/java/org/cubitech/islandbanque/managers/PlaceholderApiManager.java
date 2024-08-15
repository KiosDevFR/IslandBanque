package org.cubitech.islandbanque.managers;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.cubitech.islandbanque.utils.UtilsBanque;
import org.jetbrains.annotations.NotNull;

public class PlaceholderApiManager extends PlaceholderExpansion {

    @Override
    public @NotNull String getAuthor() {
        return "keyset";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "islandbanque";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        Island playerIsland = SuperiorSkyblockAPI.getPlayer((Player) player).getIsland();
        if(playerIsland == null) {
            return "0";
        }
        switch(params) {
            case "interets" -> {
                return String.valueOf(IslandManager.getInstance().getInterest(playerIsland));
            }
            case "argent" -> {
                return String.valueOf(IslandManager.getInstance().getIslandBank(playerIsland).getMoney());
            }
            case "formatte_argent" -> {
                return UtilsBanque.formatNumber(IslandManager.getInstance().getIslandBank(playerIsland).getMoney());
            }
            case "espace_argent" -> {
                return UtilsBanque.formatLongToSpaces(IslandManager.getInstance().getIslandBank(playerIsland).getMoney());
            }
            case "exp" -> {
                return String.valueOf(IslandManager.getInstance().getIslandBank(playerIsland).getExp());
            }
            case "formatte_exp" -> {
                return UtilsBanque.formatNumber(IslandManager.getInstance().getIslandBank(playerIsland).getExp());
            }
            case "espace_exp" -> {
                return UtilsBanque.formatLongToSpaces(IslandManager.getInstance().getIslandBank(playerIsland).getExp());
            }
            case "farmpoints" -> {
                return String.valueOf(IslandManager.getInstance().getIslandBank(playerIsland).getFarmPoints());
            }
            case "formatte_farmpoints" -> {
                return UtilsBanque.formatNumber(IslandManager.getInstance().getIslandBank(playerIsland).getFarmPoints());
            }
            case "espace_farmpoints" -> {
                return UtilsBanque.formatLongToSpaces(IslandManager.getInstance().getIslandBank(playerIsland).getFarmPoints());
            }
            case "limite_argent" -> {
                return String.valueOf(IslandManager.getInstance().getInterestLimit(playerIsland, 0));
            }
            case "formatte_limite_argent" -> {
                return UtilsBanque.formatNumber(IslandManager.getInstance().getInterestLimit(playerIsland, 0));
            }
            case "espace_limite_argent" -> {
                return UtilsBanque.formatLongToSpaces(IslandManager.getInstance().getInterestLimit(playerIsland, 0));
            }
            case "limite_exp" -> {
                return String.valueOf(IslandManager.getInstance().getInterestLimit(playerIsland, 1));
            }
            case "formatte_limite_exp" -> {
                return UtilsBanque.formatNumber(IslandManager.getInstance().getInterestLimit(playerIsland, 1));
            }
            case "espace_limite_exp" -> {
                return UtilsBanque.formatLongToSpaces(IslandManager.getInstance().getInterestLimit(playerIsland, 1));
            }
            case "limite_farmpoints" -> {
                return String.valueOf(IslandManager.getInstance().getInterestLimit(playerIsland, 2));
            }
            case "formatte_limite_farmpoints" -> {
                return UtilsBanque.formatNumber(IslandManager.getInstance().getInterestLimit(playerIsland, 2));
            }
            case "espace_limite_farmpoints" -> {
                return UtilsBanque.formatLongToSpaces(IslandManager.getInstance().getInterestLimit(playerIsland, 2));
            }
            default -> {return null;}
        }
    }

}
