package org.cubitech.islandbanque.guiobjects;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.cubitech.islandbanque.objects.Menu;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

public class LogButton extends AbstractItem {

    private ItemStack material;
    private String name;
    private String[] lore;
    private String currencyType;
    private int customModelData;
    private Menu menuInstance;

    public LogButton(ItemStack material, String name, String[] lore, String currencyType, int customModelData, Menu menuInstance) {
        this.material = material;
        this.name = name;
        this.lore = lore;
        this.currencyType = currencyType;
        this.customModelData = customModelData;
        this.menuInstance = menuInstance;
    }

    @Override
    public ItemProvider getItemProvider() {
        Player executePlayer = menuInstance.getExecutePlayer();

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
        if (clickType.isLeftClick()) {
            switch (currencyType) {
                case "logs-argent" -> {
                    menuInstance.showGui(player, 3);}
                case "logs-exp" -> {
                    menuInstance.showGui(player, 4);}
                case "logs-farmpoints" -> {
                    menuInstance.showGui(player, 5);}
            }
        }
    }
}
