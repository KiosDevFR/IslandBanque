package org.cubitech.islandbanque.guiobjects;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import me.clip.placeholderapi.PlaceholderAPI;
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

public class ExpBank extends AbstractItem {
    private ItemStack material;
    private String name;
    private String[] lore;
    private int customModelData;
    private Menu menuInstance;

    public ExpBank(ItemStack material, String name, String[] lore, int customModelData, Menu menuInstance) {
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
        MessagesManager.getInstance().setPlaceholder("%banque_exp%", String.valueOf(IslandManager.getInstance().getIslandBank(superiorPlayer.getIsland()).getExp()));
        MessagesManager.getInstance().setPlaceholder("%formatte_banque_exp%", UtilsBanque.formatNumber(IslandManager.getInstance().getIslandBank(superiorPlayer.getIsland()).getExp()));
        MessagesManager.getInstance().setPlaceholder("%espace_banque_exp%", UtilsBanque.formatLongToSpaces(IslandManager.getInstance().getIslandBank(superiorPlayer.getIsland()).getExp()));
        MessagesManager.getInstance().setPlaceholder("%joueur_exp%", String.valueOf(UtilsBanque.getTotalExperience(executePlayer)));
        MessagesManager.getInstance().setPlaceholder("%formatte_joueur_exp%", UtilsBanque.formatNumber(UtilsBanque.getTotalExperience(executePlayer)));
        MessagesManager.getInstance().setPlaceholder("%espace_joueur_exp%", UtilsBanque.formatLongToSpaces(UtilsBanque.getTotalExperience(executePlayer)));

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
            menuInstance.showGui(player, 1);
        }
    }
}
