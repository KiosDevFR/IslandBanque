package org.cubitech.islandbanque.guiobjects;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.island.IslandPrivilege;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import me.clip.placeholderapi.PlaceholderAPI;
import me.mraxetv.beasttokens.api.BeastTokensAPI;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.cubitech.islandbanque.IslandBanque;
import org.cubitech.islandbanque.managers.CommandsManager;
import org.cubitech.islandbanque.managers.FilesManager;
import org.cubitech.islandbanque.managers.IslandManager;
import org.cubitech.islandbanque.managers.MessagesManager;
import org.cubitech.islandbanque.objects.Bank;
import org.cubitech.islandbanque.objects.Menu;
import org.cubitech.islandbanque.prompts.CustomAmountInput;
import org.cubitech.islandbanque.utils.UtilsBanque;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;
import xyz.xenondevs.invui.window.Window;

public class AddCurrency extends AbstractItem {

    private ItemStack material;
    private ItemStack naMaterial;
    private String name;
    private String naName;
    private String[] lore;
    private String[] naLore;
    private int percentage;
    private int currencyType;
    private int customModelData;
    private int naCustomModelData;
    private Menu menuInstance;

    public AddCurrency(ItemStack material,ItemStack naMaterial, String name, String naName, String[] lore, String[] naLore, int percentage, String currencyType, int customModelData, int naCustomModelData, Menu menuInstance) {
        this.material = material;
        this.naMaterial = naMaterial;
        this.name = name;
        this.naName = naName;
        this.lore = lore;
        this.naLore = naLore;
        this.percentage = percentage;
        this.currencyType = UtilsBanque.getTypeIntFromText(currencyType);
        this.customModelData = customModelData;
        this.naCustomModelData = naCustomModelData;
        this.menuInstance = menuInstance;
    }

    @Override
    public ItemProvider getItemProvider() {

        Player executePlayer = menuInstance.getExecutePlayer();

        MessagesManager.getInstance().restartPlaceholders();
        SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(executePlayer);
        Island island = superiorPlayer.getIsland();
        Bank islandBank = IslandManager.getInstance().getIslandBank(island);


        if(currencyType == 0) {
            MessagesManager.getInstance().setPlaceholder("%quantite%", String.valueOf((long) IslandBanque.getInstance().getEconomy().getBalance(executePlayer) * percentage / 100));
            MessagesManager.getInstance().setPlaceholder("%formatte_quantite%", UtilsBanque.formatNumber((long) IslandBanque.getInstance().getEconomy().getBalance(executePlayer) * percentage / 100));
            MessagesManager.getInstance().setPlaceholder("%espace_quantite%", UtilsBanque.formatLongToSpaces((long) IslandBanque.getInstance().getEconomy().getBalance(executePlayer) * percentage / 100));

            MessagesManager.getInstance().setPlaceholder("%banque_argent%", String.valueOf(islandBank.getMoney()));
            MessagesManager.getInstance().setPlaceholder("%formatte_banque_argent%", UtilsBanque.formatNumber(islandBank.getMoney()));
            MessagesManager.getInstance().setPlaceholder("%espace_banque_argent%", UtilsBanque.formatLongToSpaces(islandBank.getMoney()));

            MessagesManager.getInstance().setPlaceholder("%joueur_argent%", String.valueOf((long) IslandBanque.getInstance().getEconomy().getBalance(executePlayer)));
            MessagesManager.getInstance().setPlaceholder("%formatte_joueur_argent%", UtilsBanque.formatNumber((long) IslandBanque.getInstance().getEconomy().getBalance(executePlayer)));
            MessagesManager.getInstance().setPlaceholder("%espace_joueur_argent%", UtilsBanque.formatLongToSpaces((long) IslandBanque.getInstance().getEconomy().getBalance(executePlayer)));
        } else if(currencyType == 1) {
            MessagesManager.getInstance().setPlaceholder("%quantite%", String.valueOf((long) UtilsBanque.getTotalExperience(executePlayer) * percentage / 100));
            MessagesManager.getInstance().setPlaceholder("%formatte_quantite%", UtilsBanque.formatNumber((long) UtilsBanque.getTotalExperience(executePlayer) * percentage / 100));
            MessagesManager.getInstance().setPlaceholder("%espace_quantite%", UtilsBanque.formatLongToSpaces((long) UtilsBanque.getTotalExperience(executePlayer) * percentage / 100));

            MessagesManager.getInstance().setPlaceholder("%banque_exp%", String.valueOf(islandBank.getExp()));
            MessagesManager.getInstance().setPlaceholder("%formatte_banque_exp%", UtilsBanque.formatNumber(islandBank.getExp()));
            MessagesManager.getInstance().setPlaceholder("%espace_banque_exp%", UtilsBanque.formatLongToSpaces(islandBank.getExp()));

            MessagesManager.getInstance().setPlaceholder("%joueur_exp%", String.valueOf((long) UtilsBanque.getTotalExperience(executePlayer)));
            MessagesManager.getInstance().setPlaceholder("%formatte_joueur_exp%", UtilsBanque.formatNumber(UtilsBanque.getTotalExperience(executePlayer)));
            MessagesManager.getInstance().setPlaceholder("%espace_joueur_exp%", UtilsBanque.formatLongToSpaces(UtilsBanque.getTotalExperience(executePlayer)));
        } else {
            MessagesManager.getInstance().setPlaceholder("%quantite%", String.valueOf((long) (Math.floor(BeastTokensAPI.getTokensManager().getTokens(executePlayer)) * percentage / 100)));
            MessagesManager.getInstance().setPlaceholder("%formatte_quantite%", UtilsBanque.formatNumber((long) (Math.floor(BeastTokensAPI.getTokensManager().getTokens(executePlayer)) * percentage / 100)));
            MessagesManager.getInstance().setPlaceholder("%espace_quantite%", UtilsBanque.formatLongToSpaces((long) (Math.floor(BeastTokensAPI.getTokensManager().getTokens(executePlayer)) * percentage / 100)));

            MessagesManager.getInstance().setPlaceholder("%banque_farmpoints%", String.valueOf(islandBank.getFarmPoints()));
            MessagesManager.getInstance().setPlaceholder("%formatte_banque_farmpoints%", UtilsBanque.formatNumber(islandBank.getFarmPoints()));
            MessagesManager.getInstance().setPlaceholder("%espace_banque_farmpoints%", UtilsBanque.formatLongToSpaces(islandBank.getFarmPoints()));

            MessagesManager.getInstance().setPlaceholder("%joueur_farmpoints%", String.valueOf((long)BeastTokensAPI.getTokensManager().getTokens(executePlayer)));
            MessagesManager.getInstance().setPlaceholder("%formatte_joueur_farmpoints%", UtilsBanque.formatNumber((long) BeastTokensAPI.getTokensManager().getTokens(executePlayer)));
            MessagesManager.getInstance().setPlaceholder("%espace_joueur_farmpoints%", UtilsBanque.formatLongToSpaces((long) BeastTokensAPI.getTokensManager().getTokens(executePlayer)));
        }



        if((!superiorPlayer.getIsland().hasPermission(superiorPlayer, IslandPrivilege.getByName("DEPOSIT_MONEY")) && !superiorPlayer.getIsland().hasPermission(superiorPlayer, IslandPrivilege.getByName("ALL")))){
            String processedName = PlaceholderAPI.setPlaceholders(executePlayer, MessagesManager.getInstance().processText(naName));
            String[] processedLore = new String[naLore.length];
            for(int i = 0; i < naLore.length; i++){
                processedLore[i] = PlaceholderAPI.setPlaceholders(executePlayer, MessagesManager.getInstance().processText(naLore[i]));
            }
            return new ItemBuilder(naMaterial)
                    .setDisplayName(processedName)
                    .addLoreLines(processedLore)
                    .setCustomModelData(naCustomModelData);
        }else{
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
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {

        Player executePlayer = menuInstance.getExecutePlayer();

        if (clickType.isLeftClick()) {
            final SuperiorPlayer[] superiorPlayer = {SuperiorSkyblockAPI.getPlayer(executePlayer)};
            if (!superiorPlayer[0].getIsland().hasPermission(superiorPlayer[0], IslandPrivilege.getByName("DEPOSIT_MONEY")) && !superiorPlayer[0].getIsland().hasPermission(superiorPlayer[0], IslandPrivilege.getByName("ALL"))) {
                player.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.no_permission_ajouter")));
                return;
            }

            if(percentage <= 0 && percentage != -1) {
                player.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.failure.too_low")));
                return;
            }

            long quantityCalculated = 0;
            MessagesManager.getInstance().restartPlaceholders();

            if (percentage == -1){
                ConversationFactory convFactory = new ConversationFactory(IslandBanque.getInstance());
                getWindows().forEach(Window::close);
                CustomAmountInput cai = new CustomAmountInput(currencyType, "add", getWindows());
                Conversation conv = convFactory.withFirstPrompt(cai).withEscapeSequence(FilesManager.getLanguageFileConfig().getString("languages.custom_inputs.escape-sequence")).withLocalEcho(false).buildConversation(player);
                conv.begin();
                return;
            }

            switch (currencyType) {
                case 0 -> {
                    if(percentage > 0) {
                        quantityCalculated = (long) (IslandBanque.getInstance().getEconomy().getBalance(executePlayer) * percentage / 100);
                    }

                    if (IslandBanque.getInstance().getEconomy().getBalance(executePlayer) < percentage && quantityCalculated <= 0) {
                        player.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.failure.not_enough")));
                        return;
                    }
                }
                case 1 -> {
                    if(percentage > 0) {
                        quantityCalculated = (long) ((double)UtilsBanque.getTotalExperience(executePlayer) * percentage / 100);
                    }

                    if (UtilsBanque.getTotalExperience(executePlayer) < percentage && quantityCalculated <= 0) {
                        player.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.failure.not_enough")));
                        return;
                    }
                }
                case 2 -> {
                    if(percentage > 0) {
                        quantityCalculated = (long) (BeastTokensAPI.getTokensManager().getTokens(executePlayer) * percentage / 100);
                    }

                    if (BeastTokensAPI.getTokensManager().getTokens(executePlayer) < percentage && quantityCalculated <= 0) {
                        player.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.failure.not_enough")));
                        return;
                    }
                }
            }

            CommandsManager.addBalanceCommand(currencyType, quantityCalculated, executePlayer);
        }

        menuInstance.updateItems();
    }

}
