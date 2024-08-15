package org.cubitech.islandbanque.guiobjects;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.island.IslandPrivilege;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import me.clip.placeholderapi.PlaceholderAPI;
import me.mraxetv.beasttokens.api.BeastTokensAPI;
import org.bukkit.Material;
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
import org.cubitech.islandbanque.objects.PlayerAddings;
import org.cubitech.islandbanque.prompts.CustomAmountInput;
import org.cubitech.islandbanque.utils.UtilsBanque;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;
import xyz.xenondevs.invui.window.Window;

public class WithdrawCurrency extends AbstractItem {

    private ItemStack material;
    private ItemStack naMaterial;
    private String name;
    private String naName;
    private String[] lore;
    private String[] naLore;
    private long percentage;
    private int currencyType;
    private int customModelData;
    private int naCustomModelData;
    private Menu menuInstance;

    public WithdrawCurrency(ItemStack material,ItemStack naMaterial, String name, String naName, String[] lore, String[] naLore, long percentage, String currencyType, int customModelData, int naCustomModelData, Menu menuInstance) {
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
            MessagesManager.getInstance().setPlaceholder("%quantite%", String.valueOf(islandBank.getMoney() * percentage / 100));
            MessagesManager.getInstance().setPlaceholder("%formatte_quantite%", UtilsBanque.formatNumber(islandBank.getMoney() * percentage / 100));
            MessagesManager.getInstance().setPlaceholder("%espace_quantite%", UtilsBanque.formatLongToSpaces(islandBank.getMoney() * percentage / 100));

            MessagesManager.getInstance().setPlaceholder("%banque_argent%", String.valueOf(islandBank.getMoney()));
            MessagesManager.getInstance().setPlaceholder("%formatte_banque_argent%", UtilsBanque.formatNumber(islandBank.getMoney()));
            MessagesManager.getInstance().setPlaceholder("%espace_banque_argent%", UtilsBanque.formatLongToSpaces(islandBank.getMoney()));

            MessagesManager.getInstance().setPlaceholder("%joueur_argent%", String.valueOf((long) IslandBanque.getInstance().getEconomy().getBalance(executePlayer)));
            MessagesManager.getInstance().setPlaceholder("%formatte_joueur_argent%", UtilsBanque.formatNumber((long) IslandBanque.getInstance().getEconomy().getBalance(executePlayer)));
            MessagesManager.getInstance().setPlaceholder("%espace_joueur_argent%", UtilsBanque.formatLongToSpaces((long) IslandBanque.getInstance().getEconomy().getBalance(executePlayer)));

        } else if(currencyType == 1) {
            MessagesManager.getInstance().setPlaceholder("%quantite%", String.valueOf(islandBank.getExp() * percentage / 100));
            MessagesManager.getInstance().setPlaceholder("%formatte_quantite%", UtilsBanque.formatNumber(islandBank.getExp() * percentage / 100));
            MessagesManager.getInstance().setPlaceholder("%espace_quantite%", UtilsBanque.formatLongToSpaces(islandBank.getExp() * percentage / 100));

            MessagesManager.getInstance().setPlaceholder("%banque_exp%", String.valueOf(islandBank.getExp()));
            MessagesManager.getInstance().setPlaceholder("%formatte_banque_exp%", UtilsBanque.formatNumber(islandBank.getExp()));
            MessagesManager.getInstance().setPlaceholder("%espace_banque_exp%", UtilsBanque.formatLongToSpaces(islandBank.getExp()));

            MessagesManager.getInstance().setPlaceholder("%joueur_exp%", String.valueOf((long) UtilsBanque.getTotalExperience(executePlayer)));
            MessagesManager.getInstance().setPlaceholder("%formatte_joueur_exp%", UtilsBanque.formatNumber(UtilsBanque.getTotalExperience(executePlayer)));
            MessagesManager.getInstance().setPlaceholder("%espace_joueur_exp%", UtilsBanque.formatLongToSpaces(UtilsBanque.getTotalExperience(executePlayer)));
        } else {
            MessagesManager.getInstance().setPlaceholder("%quantite%", String.valueOf(islandBank.getFarmPoints() * percentage / 100));
            MessagesManager.getInstance().setPlaceholder("%formatte_quantite%", UtilsBanque.formatNumber( islandBank.getFarmPoints() * percentage / 100));
            MessagesManager.getInstance().setPlaceholder("%espace_quantite%", UtilsBanque.formatLongToSpaces(islandBank.getFarmPoints() * percentage / 100));

            MessagesManager.getInstance().setPlaceholder("%banque_farmpoints%", String.valueOf(islandBank.getFarmPoints()));
            MessagesManager.getInstance().setPlaceholder("%formatte_banque_farmpoints%", UtilsBanque.formatNumber(islandBank.getFarmPoints()));
            MessagesManager.getInstance().setPlaceholder("%espace_banque_farmpoints%", UtilsBanque.formatLongToSpaces(islandBank.getFarmPoints()));

            MessagesManager.getInstance().setPlaceholder("%joueur_farmpoints%", String.valueOf((long) BeastTokensAPI.getTokensManager().getTokens(executePlayer)));
            MessagesManager.getInstance().setPlaceholder("%formatte_joueur_farmpoints%", UtilsBanque.formatNumber((long) BeastTokensAPI.getTokensManager().getTokens(executePlayer)));
            MessagesManager.getInstance().setPlaceholder("%espace_joueur_farmpoints%", UtilsBanque.formatLongToSpaces((long) BeastTokensAPI.getTokensManager().getTokens(executePlayer)));
        }
        PlayerAddings playerAddings = IslandManager.getInstance().getPlayerAddings(superiorPlayer,island);
        long playerAddingsFinal = switch (currencyType){
            case 0 -> playerAddings.getMoney();
            case 1 -> playerAddings.getXp();
            default -> playerAddings.getFarmpoints();
        };
        MessagesManager.getInstance().setPlaceholder("%quantite_peut_retirer%",String.valueOf(playerAddingsFinal));
        MessagesManager.getInstance().setPlaceholder("%formatte_quantite_peut_retirer%",UtilsBanque.formatNumber(playerAddingsFinal));
        MessagesManager.getInstance().setPlaceholder("%espace_quantite_peut_retirer%",UtilsBanque.formatLongToSpaces(playerAddingsFinal));

        if (!superiorPlayer.getIsland().hasPermission(superiorPlayer, IslandPrivilege.getByName("WITHDRAW_MONEY")) && !superiorPlayer.getIsland().hasPermission(superiorPlayer, IslandPrivilege.getByName("ALL"))) {
            String processedName = PlaceholderAPI.setPlaceholders(executePlayer, MessagesManager.getInstance().processText(naName));
            String[] processedLore = new String[naLore.length];
            for (int i = 0; i < naLore.length; i++) {
                processedLore[i] = PlaceholderAPI.setPlaceholders(executePlayer, MessagesManager.getInstance().processText(naLore[i]));
            }

            return new ItemBuilder(naMaterial)
                    .setDisplayName(processedName)
                    .addLoreLines(processedLore)
                    .setCustomModelData(naCustomModelData);
        }else {
            String processedName = PlaceholderAPI.setPlaceholders(executePlayer, MessagesManager.getInstance().processText(name));
            String[] processedLore = new String[lore.length];
            for (int i = 0; i < lore.length; i++) {
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
            SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(executePlayer);
            PlayerAddings playerAddings = IslandManager.getInstance().getPlayerAddings(superiorPlayer, superiorPlayer.getIsland());
            long playerAddingAmount = switch (currencyType) {
                case 0 -> playerAddings.getMoney();
                case 1 -> playerAddings.getXp();
                case 2 -> playerAddings.getFarmpoints();
                default -> throw new RuntimeException("UNEXPECTED VALUE");
            };

            Bank previousBank = IslandManager.getInstance().getIslandBank(superiorPlayer.getIsland());

            if(percentage <= 0 && percentage != -1) {
                player.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.failure.too_low")));
                return;
            }

            long quantityCalculated = 0;
            MessagesManager.getInstance().restartPlaceholders();

            if (percentage == -1){
                ConversationFactory convFactory = new ConversationFactory(IslandBanque.getInstance());
                getWindows().forEach(Window::close);
                Conversation conv = convFactory.withFirstPrompt(new CustomAmountInput(currencyType, "remove", getWindows())).withLocalEcho(false).withTimeout(10).buildConversation(player);
                conv.begin();
            }

            switch (currencyType) {
                case 0 -> {
                    if(percentage > 0) {
                        quantityCalculated = IslandManager.getInstance().getIslandBank(superiorPlayer.getIsland()).getMoney() * percentage / 100;
                    }

                    if (previousBank.getMoney() < quantityCalculated && quantityCalculated <= 0) {
                        player.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.failure.not_enough")));
                        return;
                    }
                }
                case 1 -> {
                    if(percentage > 0) {
                        quantityCalculated = IslandManager.getInstance().getIslandBank(SuperiorSkyblockAPI.getPlayer(executePlayer).getIsland()).getExp() * percentage / 100;
                    }

                    if (previousBank.getExp() < quantityCalculated && quantityCalculated <= 0) {
                        player.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.failure.not_enough")));
                        return;
                    }
                }
                case 2 -> {
                    if(percentage > 0) {
                        quantityCalculated = IslandManager.getInstance().getIslandBank(SuperiorSkyblockAPI.getPlayer(executePlayer).getIsland()).getFarmPoints() * percentage / 100;
                    }

                    if (previousBank.getFarmPoints() < quantityCalculated && quantityCalculated <= 0) {
                        player.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.failure.not_enough")));
                        return;
                    }
                }
            }

            if (quantityCalculated > playerAddingAmount && !superiorPlayer.getIsland().hasPermission(superiorPlayer, IslandPrivilege.getByName("WITHDRAW_MONEY")) && !superiorPlayer.getIsland().hasPermission(superiorPlayer, IslandPrivilege.getByName("ALL"))) {
                MessagesManager.getInstance().setPlaceholder("%quantite_peut_retirer%", String.valueOf(playerAddingAmount));
                player.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.failure.no_permission_retirer")));
                return;
            }

            if(quantityCalculated <= 0){
                player.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.failure.not_enough")));
                return;
            }

            CommandsManager.withdrawBalanceCommand(currencyType, quantityCalculated, executePlayer);
        }

        menuInstance.updateItems();
    }
}
