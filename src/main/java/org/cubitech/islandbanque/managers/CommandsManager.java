package org.cubitech.islandbanque.managers;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import org.bukkit.entity.Player;

public class CommandsManager {
    public static void addBalanceCommand(int typeOperation, long amount, Player player){
        SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player);
        if (!superiorPlayer.hasIsland()) {
            player.sendRawMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.admin.no_player_island")));
            return;
        }
        IslandManager.getInstance().addBalanceToIsland(player, typeOperation,amount);
        MessagesManager.getInstance().restartPlaceholders();
        MessagesManager.getInstance().setPlaceholder("%quantite%",String.valueOf(amount));
        switch (typeOperation){
            case 0 -> {
                player.sendRawMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.banque.added_money")));
            }
            case 1 -> {
                player.sendRawMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.banque.added_exp")));
            }
            case 2 -> {
                player.sendRawMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.banque.added_farmpoints")));
            }
        }
    }
    public static void withdrawBalanceCommand(int typeOperation, long amount, Player player) {
        SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player);
        if (!superiorPlayer.hasIsland()) {
            player.sendRawMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.admin.no_player_island")));
            return;
        }

        IslandManager.getInstance().addBalanceToIsland(player, typeOperation, -amount);
        MessagesManager.getInstance().restartPlaceholders();
        MessagesManager.getInstance().setPlaceholder("%quantite%",String.valueOf(amount));
        switch (typeOperation){
            case 0 -> {
                player.sendRawMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.banque.removed_money")));
            }
            case 1 -> {
                player.sendRawMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.banque.removed_exp")));
            }
            case 2 -> {
                player.sendRawMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.banque.removed_farmpoints")));
            }
        }
    }
    public static void assignInterestLimitToPlayer(SuperiorPlayer userSuperiorPlayer, int type, long amount) {
        IslandManager.getInstance().setInterestLimit(userSuperiorPlayer.getIsland(),type,amount);
    }

    public static void assignInterestToPlayer(SuperiorPlayer userSuperiorPlayer, double amount) {
        IslandManager.getInstance().setInterest(userSuperiorPlayer.getIsland(), amount);
    }

    /**
     * Adds or removes balance to a player's island.and.
     * @param player
     * @param type
     * @param amount Can be negative.
     */
    public static void adminAddPlayerIslandBalance(Player player, int type, long amount){
        IslandManager.getInstance().adminAddIslandBalance(SuperiorSkyblockAPI.getPlayer(player).getIsland(),type,amount);
    }
}