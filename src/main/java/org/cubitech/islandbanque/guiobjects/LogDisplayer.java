package org.cubitech.islandbanque.guiobjects;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.cubitech.islandbanque.managers.IslandManager;
import org.cubitech.islandbanque.managers.MessagesManager;
import org.cubitech.islandbanque.objects.*;
import org.cubitech.islandbanque.utils.UtilsBanque;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

public class LogDisplayer extends AbstractItem {

    private ItemStack material;
    private String name;
    private String[] lore;
    private int customModelData;
    private Player executePlayer;
    private BanqueLog log;

    public LogDisplayer(ItemStack material, int customModelData, Player executePlayer, BanqueLog log, String nameAmountPositive, String nameAmountNegative, String[] loreDateAdd, String[] loreDateRemove) {
        this.material = material;
        this.customModelData = customModelData;
        this.executePlayer = executePlayer;
        this.log = log;
        if(this.log.getAmount() < 0) {
            this.name = nameAmountNegative;
            this.lore = loreDateRemove;
        } else {
            this.name = nameAmountPositive;
            this.lore = loreDateAdd;
        }
    }

    @Override
    public ItemProvider getItemProvider() {
        String[] processedLore;
        MessagesManager.getInstance().restartPlaceholders();
        MessagesManager.getInstance().setPlaceholder("%quantite%", String.valueOf(Math.abs(log.getAmount())));
        MessagesManager.getInstance().setPlaceholder("%formatte_quantite%", UtilsBanque.formatNumber(Math.abs(log.getAmount())));
        MessagesManager.getInstance().setPlaceholder("%espace_quantite%", UtilsBanque.formatLongToSpaces(Math.abs(log.getAmount())));
        MessagesManager.getInstance().setPlaceholder("%banque_joueur%", log.getPlayerName());
        Island targetIsland = SuperiorSkyblockAPI.getPlayer(executePlayer).getIsland();
        MessagesManager.getInstance().setPlaceholder("%banque_log_montant%", String.valueOf(log.getTotalAmountAfter()));
        MessagesManager.getInstance().setPlaceholder("%formatte_banque_log_montant%", UtilsBanque.formatNumber(log.getTotalAmountAfter()));
        MessagesManager.getInstance().setPlaceholder("%espace_banque_log_montant%", UtilsBanque.formatLongToSpaces(log.getTotalAmountAfter()));
        MessagesManager.getInstance().setPlaceholder("%banque_log_date%", log.getFormatedDate());

        long playerAddingsAmount = -1;
        SuperiorPlayer superiorLogPlayer = SuperiorSkyblockAPI.getPlayer(log.getPlayerName());
        if(log instanceof BanqueLogMoney){
            playerAddingsAmount = IslandManager.getInstance().getPlayerAddings(superiorLogPlayer,targetIsland).getMoney();
        }else if(log instanceof BanqueLogXp){
            playerAddingsAmount = IslandManager.getInstance().getPlayerAddings(superiorLogPlayer,targetIsland).getXp();
        }else {
            playerAddingsAmount = IslandManager.getInstance().getPlayerAddings(superiorLogPlayer,targetIsland).getFarmpoints();
        }
        String prefix = playerAddingsAmount >= 0 ? "&a+" : "&c";
        MessagesManager.getInstance().setPlaceholder("%banque_log_solde_total%", prefix + playerAddingsAmount);
        MessagesManager.getInstance().setPlaceholder("%formatte_banque_log_solde_total%", prefix + UtilsBanque.formatNumber(playerAddingsAmount));
        MessagesManager.getInstance().setPlaceholder("%espace_banque_log_solde_total%", prefix + UtilsBanque.formatLongToSpaces(playerAddingsAmount));

        processedLore = new String[lore.length];
        for(int i = 0; i < lore.length; i++){
            processedLore[i] = PlaceholderAPI.setPlaceholders(executePlayer, MessagesManager.getInstance().processText(lore[i]));
        }

        String processedName = PlaceholderAPI.setPlaceholders(executePlayer, MessagesManager.getInstance().processText(name));

        return new ItemBuilder(material)
                .setDisplayName(processedName)
                .addLoreLines(processedLore)
                .setCustomModelData(customModelData);
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
    }
}
