package org.cubitech.islandbanque.guiobjects;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import me.clip.placeholderapi.PlaceholderAPI;
import me.mraxetv.beasttokens.api.BeastTokensAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.cubitech.islandbanque.managers.IslandManager;
import org.cubitech.islandbanque.managers.MessagesManager;
import org.cubitech.islandbanque.objects.Menu;
import org.cubitech.islandbanque.utils.UtilsBanque;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

public class FarmPointsBank extends AbstractItem {

    private ItemStack material;
    private String name;
    private String[] lore;
    private int customModelData;
    private Menu menuInstance;

    public FarmPointsBank(ItemStack material, String name, String[] lore, int customModelData, Menu menuInstance) {
        this.material = material;
        this.name = name;
        this.lore = lore;
        this.customModelData = customModelData;
        this.menuInstance = menuInstance;
    }

    @Override
    public ItemProvider getItemProvider() {

        Player executePlayer = menuInstance.getExecutePlayer();

        SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(executePlayer);

        MessagesManager.getInstance().restartPlaceholders();
        MessagesManager.getInstance().setPlaceholder("%banque_farmpoints%", String.valueOf(IslandManager.getInstance().getIslandBank(superiorPlayer.getIsland()).getFarmPoints()));
        MessagesManager.getInstance().setPlaceholder("%formatte_banque_farmpoints%", UtilsBanque.formatNumber(IslandManager.getInstance().getIslandBank(superiorPlayer.getIsland()).getFarmPoints()));
        MessagesManager.getInstance().setPlaceholder("%espace_banque_farmpoints%", UtilsBanque.formatLongToSpaces(IslandManager.getInstance().getIslandBank(superiorPlayer.getIsland()).getFarmPoints()));
        MessagesManager.getInstance().setPlaceholder("%joueur_farmpoints%", String.valueOf(BeastTokensAPI.getTokensManager().getTokens(executePlayer)));
        MessagesManager.getInstance().setPlaceholder("%formatte_joueur_farmpoints%", UtilsBanque.formatNumber((long) BeastTokensAPI.getTokensManager().getTokens(executePlayer)));
        MessagesManager.getInstance().setPlaceholder("%espace_joueur_farmpoints%", UtilsBanque.formatLongToSpaces((long) BeastTokensAPI.getTokensManager().getTokens(executePlayer)));

        String processedName = PlaceholderAPI.setPlaceholders(executePlayer, MessagesManager.getInstance().processText(name));
        String[] processedLore = new String[lore.length];
        for(int i = 0; i < lore.length; i++){
            processedLore[i] = PlaceholderAPI.setPlaceholders(executePlayer, MessagesManager.getInstance().processText(lore[i]));
        }

        return new ItemBuilder(material)
                .setDisplayName(processedName)
                .addLoreLines(processedLore)
                .setCustomModelData(customModelData);
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        if (clickType.isLeftClick()) {
            menuInstance.showGui(player, 2);
        }
        menuInstance.updateItems();
    }
}
