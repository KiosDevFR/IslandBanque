package org.cubitech.islandbanque.guiobjects;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.cubitech.islandbanque.IslandBanque;
import org.cubitech.islandbanque.managers.FilesManager;
import org.cubitech.islandbanque.managers.IslandManager;
import org.cubitech.islandbanque.managers.MessagesManager;
import org.cubitech.islandbanque.managers.TimersManager;
import org.cubitech.islandbanque.objects.Bank;
import org.cubitech.islandbanque.objects.Menu;
import org.cubitech.islandbanque.utils.UtilsBanque;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

public class Interests extends AbstractItem {

    private ItemStack material, naMaterial, aMaterial;
    private String name, naName, aName;
    private String[] lore, naLore, aLore;
    private int customModelData, naCustomModelData, aCustomModelData;
    private Menu menuInstance;

    public Interests(ItemStack naMaterial, String naName, String[] naLore, int naCustomModelData, ItemStack aMaterial, String aName, String[] aLore, int aCustomModelData, Menu menuInstance) {
        this.menuInstance = menuInstance;
        this.naMaterial = naMaterial;
        this.naName = naName;
        this.naLore = naLore;
        this.naCustomModelData = naCustomModelData;
        this.aMaterial = aMaterial;
        this.aName = aName;
        this.aLore = aLore;
        this.aCustomModelData = aCustomModelData;
    }

    @Override
    public ItemProvider getItemProvider() {

        Player executePlayer = menuInstance.getExecutePlayer();

        MessagesManager.getInstance().restartPlaceholders();

        SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(executePlayer);
        Bank islandBanque = IslandManager.getInstance().getIslandBank(superiorPlayer.getIsland());
        double islandInterest = IslandManager.getInstance().getInterest(superiorPlayer.getIsland());

        if (!IslandManager.getInstance().hasClaimedInterest(superiorPlayer)) {
            lore = naLore;
            name = naName;
            material = naMaterial;
            customModelData = naCustomModelData;

        } else {
            lore = aLore;
            name = aName;
            material = aMaterial;
            customModelData = aCustomModelData;
        }

        MessagesManager.getInstance().setPlaceholder("%interets%", String.valueOf(islandInterest));
        MessagesManager.getInstance().setPlaceholder("%interet_argent%", String.valueOf((long)((double)islandBanque.getMoney() * islandInterest / 100d)));
        MessagesManager.getInstance().setPlaceholder("%formatte_interet_argent%", UtilsBanque.formatNumber((long)((double)islandBanque.getMoney() * islandInterest / 100d)));
        MessagesManager.getInstance().setPlaceholder("%espace_interet_argent%", UtilsBanque.formatLongToSpaces((long)((double)islandBanque.getMoney() * islandInterest / 100d)));
        MessagesManager.getInstance().setPlaceholder("%interet_exp%", String.valueOf((long)((double)islandBanque.getExp() * islandInterest / 100d)));
        MessagesManager.getInstance().setPlaceholder("%formatte_interet_exp%", UtilsBanque.formatNumber((long)((double)islandBanque.getExp() * islandInterest / 100d)));
        MessagesManager.getInstance().setPlaceholder("%espace_interet_exp%", UtilsBanque.formatLongToSpaces((long)((double)islandBanque.getExp() * islandInterest / 100d)));
        MessagesManager.getInstance().setPlaceholder("%interet_farmpoints%", String.valueOf((long)((double)islandBanque.getFarmPoints() * islandInterest / 100d)));
        MessagesManager.getInstance().setPlaceholder("%formatte_interet_farmpoints%", UtilsBanque.formatNumber((long)((double)islandBanque.getFarmPoints() * islandInterest / 100d)));
        MessagesManager.getInstance().setPlaceholder("%espace_interet_farmpoints%", UtilsBanque.formatLongToSpaces((long)((double)islandBanque.getFarmPoints() * islandInterest / 100d)));

        MessagesManager.getInstance().setPlaceholder("%interet_max_argent%", String.valueOf(IslandManager.getInstance().getInterestLimit(superiorPlayer.getIsland(), 0)));
        MessagesManager.getInstance().setPlaceholder("%formatte_interet_max_argent%", UtilsBanque.formatNumber(IslandManager.getInstance().getInterestLimit(superiorPlayer.getIsland(), 0)));
        MessagesManager.getInstance().setPlaceholder("%espace_interet_max_argent%", UtilsBanque.formatLongToSpaces(IslandManager.getInstance().getInterestLimit(superiorPlayer.getIsland(), 0)));
        MessagesManager.getInstance().setPlaceholder("%interet_max_exp%", String.valueOf(IslandManager.getInstance().getInterestLimit(superiorPlayer.getIsland(), 1)));
        MessagesManager.getInstance().setPlaceholder("%formatte_interet_max_exp%", UtilsBanque.formatNumber(IslandManager.getInstance().getInterestLimit(superiorPlayer.getIsland(), 1)));
        MessagesManager.getInstance().setPlaceholder("%espace_interet_max_exp%", UtilsBanque.formatLongToSpaces(IslandManager.getInstance().getInterestLimit(superiorPlayer.getIsland(), 1)));
        MessagesManager.getInstance().setPlaceholder("%interet_max_farmpoints%", String.valueOf(IslandManager.getInstance().getInterestLimit(superiorPlayer.getIsland(), 2)));
        MessagesManager.getInstance().setPlaceholder("%formatte_interet_max_farmpoints%", UtilsBanque.formatNumber(IslandManager.getInstance().getInterestLimit(superiorPlayer.getIsland(), 2)));
        MessagesManager.getInstance().setPlaceholder("%espace_interet_max_farmpoints%", UtilsBanque.formatLongToSpaces(IslandManager.getInstance().getInterestLimit(superiorPlayer.getIsland(), 2)));
        MessagesManager.getInstance().setPlaceholder("%temps_interet%", UtilsBanque.ticksToHoursMinutes(TimersManager.calculateTicksBeforeNextTime(IslandBanque.getInstance().getConfig().getString("Config.time-of-day-to-claim-interest"))));

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

        Player executePlayer = menuInstance.getExecutePlayer();
        SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(executePlayer);
        Bank previousBank = IslandManager.getInstance().getIslandBank(superiorPlayer.getIsland());

        if (clickType.isLeftClick()) {
            if (!IslandManager.getInstance().hasClaimedInterest(SuperiorSkyblockAPI.getPlayer(executePlayer))) {
                IslandManager.getInstance().claimInterest(SuperiorSkyblockAPI.getPlayer(executePlayer));
                Bank nowBank = IslandManager.getInstance().getIslandBank(superiorPlayer.getIsland());
                MessagesManager.getInstance().setPlaceholder("%interet_argent%",String.valueOf(nowBank.getMoney() - previousBank.getMoney()));
                MessagesManager.getInstance().setPlaceholder("%formatte_interet_argent%",UtilsBanque.formatNumber(nowBank.getMoney() - previousBank.getMoney()));
                MessagesManager.getInstance().setPlaceholder("%espace_interet_argent%",UtilsBanque.formatLongToSpaces(nowBank.getMoney() - previousBank.getMoney()));

                MessagesManager.getInstance().setPlaceholder("%interet_exp%",String.valueOf(nowBank.getExp() - previousBank.getExp()));
                MessagesManager.getInstance().setPlaceholder("%formatte_interet_exp%",UtilsBanque.formatNumber(nowBank.getExp() - previousBank.getExp()));
                MessagesManager.getInstance().setPlaceholder("%espace_interet_exp%",UtilsBanque.formatLongToSpaces(nowBank.getExp() - previousBank.getExp()));

                MessagesManager.getInstance().setPlaceholder("%interet_farmpoints%",String.valueOf(nowBank.getFarmPoints() - previousBank.getFarmPoints()));
                MessagesManager.getInstance().setPlaceholder("%formatte_interet_farmpoints%",UtilsBanque.formatNumber(nowBank.getFarmPoints() - previousBank.getFarmPoints()));
                MessagesManager.getInstance().setPlaceholder("%espace_interet_farmpoints%",UtilsBanque.formatLongToSpaces(nowBank.getFarmPoints() - previousBank.getFarmPoints()));


                player.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.main.interets")));
            }else{
                player.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.main.cannot_claim_interets")));
            }
        }
        menuInstance.updateItems();
    }
}
