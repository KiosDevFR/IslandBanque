package org.cubitech.islandbanque.guiobjects;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

public class SimpItem extends AbstractItem {

    private ItemStack material;
    private String name;
    private String[] lore;
    private int customModelData;
    private Player executePlayer;

    public SimpItem(ItemStack material, String name, String[] lore, int customModelData, Player executePlayer) {
        this.material = material;
        this.name = name;
        this.lore = lore;
        this.customModelData = customModelData;
        this.executePlayer = executePlayer;
    }

    @Override
    public ItemProvider getItemProvider() {
        String processedName = PlaceholderAPI.setPlaceholders(executePlayer, ChatColor.translateAlternateColorCodes('&',name));
        String[] processedLore = new String[lore.length];
        for(int i = 0; i < lore.length; i++){
            processedLore[i] = PlaceholderAPI.setPlaceholders(executePlayer, ChatColor.translateAlternateColorCodes('&',lore[i]));
        }

        return new ItemBuilder(material)
                .setDisplayName(processedName)
                .addLoreLines(processedLore)
                .setCustomModelData(customModelData);
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
    }
}
