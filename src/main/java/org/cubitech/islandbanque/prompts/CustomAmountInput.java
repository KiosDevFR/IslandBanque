package org.cubitech.islandbanque.prompts;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.IslandPrivilege;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import me.mraxetv.beasttokens.api.BeastTokensAPI;
import org.bukkit.Bukkit;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.cubitech.islandbanque.IslandBanque;
import org.cubitech.islandbanque.managers.CommandsManager;
import org.cubitech.islandbanque.managers.FilesManager;
import org.cubitech.islandbanque.managers.IslandManager;
import org.cubitech.islandbanque.managers.MessagesManager;
import org.cubitech.islandbanque.objects.Bank;
import org.cubitech.islandbanque.objects.PlayerAddings;
import org.cubitech.islandbanque.utils.UtilsBanque;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.xenondevs.invui.window.Window;

import java.util.List;
import java.util.Set;

public class CustomAmountInput extends RegexPrompt {

    private final int currencyType;
    private final String operationType;
    private final Set<Window> windows;

    public CustomAmountInput(int currencyType, String operationType, Set<Window> windows) {
        super("\\b(?:\\d+|all|" + FilesManager.getLanguageFileConfig().getString("languages.custom_inputs.escape_sequence") + ")\\b");
        this.currencyType = currencyType;
        this.operationType = operationType;
        this.windows = windows;
    }

    @NotNull
    @Override
    public String getPromptText(@NotNull ConversationContext context) {

        Player player = (Player) context.getForWhom();
        SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player);
        String currencyTypeTxt;

        MessagesManager.getInstance().restartPlaceholders();

        switch (currencyType){
            case 0 -> {
                currencyTypeTxt = "argent";
                MessagesManager.getInstance().setPlaceholder("%joueur_argent%", String.valueOf((long) IslandBanque.getInstance().getEconomy().getBalance(player)));
                MessagesManager.getInstance().setPlaceholder("%espace_joueur_argent%", UtilsBanque.formatLongToSpaces((long) IslandBanque.getInstance().getEconomy().getBalance(player)));
                MessagesManager.getInstance().setPlaceholder("%formatte_joueur_argent%", UtilsBanque.formatNumber((long) IslandBanque.getInstance().getEconomy().getBalance(player)));
                MessagesManager.getInstance().setPlaceholder("%banque_argent%", String.valueOf(IslandManager.getInstance().getIslandBank(superiorPlayer.getIsland()).getMoney()));
                MessagesManager.getInstance().setPlaceholder("%espace_banque_argent%", UtilsBanque.formatLongToSpaces(IslandManager.getInstance().getIslandBank(superiorPlayer.getIsland()).getMoney()));
                MessagesManager.getInstance().setPlaceholder("%formatte_banque_argent%", UtilsBanque.formatNumber(IslandManager.getInstance().getIslandBank(superiorPlayer.getIsland()).getMoney()));
            }
            case 1 -> {
                currencyTypeTxt = "exp";
                MessagesManager.getInstance().setPlaceholder("%joueur_exp%", String.valueOf(UtilsBanque.getTotalExperience(player)));
                MessagesManager.getInstance().setPlaceholder("%espace_joueur_exp%", UtilsBanque.formatLongToSpaces(UtilsBanque.getTotalExperience(player)));
                MessagesManager.getInstance().setPlaceholder("%formatte_joueur_exp%", UtilsBanque.formatNumber(UtilsBanque.getTotalExperience(player)));
                MessagesManager.getInstance().setPlaceholder("%banque_exp%", String.valueOf(IslandManager.getInstance().getIslandBank(superiorPlayer.getIsland()).getExp()));
                MessagesManager.getInstance().setPlaceholder("%espace_banque_exp%", UtilsBanque.formatLongToSpaces(IslandManager.getInstance().getIslandBank(superiorPlayer.getIsland()).getExp()));
                MessagesManager.getInstance().setPlaceholder("%formatte_banque_exp%", UtilsBanque.formatNumber(IslandManager.getInstance().getIslandBank(superiorPlayer.getIsland()).getExp()));
            }
            case 2 -> {
                currencyTypeTxt = "farmpoints";
                MessagesManager.getInstance().setPlaceholder("%joueur_farmpoints%", String.valueOf((long) BeastTokensAPI.getTokensManager().getTokens(player)));
                MessagesManager.getInstance().setPlaceholder("%espace_joueur_farmpoints%", UtilsBanque.formatLongToSpaces((long) BeastTokensAPI.getTokensManager().getTokens(player)));
                MessagesManager.getInstance().setPlaceholder("%formatte_joueur_farmpoints%", UtilsBanque.formatNumber((long) BeastTokensAPI.getTokensManager().getTokens(player)));
                MessagesManager.getInstance().setPlaceholder("%banque_farmpoints%", String.valueOf(IslandManager.getInstance().getIslandBank(superiorPlayer.getIsland()).getFarmPoints()));
                MessagesManager.getInstance().setPlaceholder("%espace_banque_farmpoints%", UtilsBanque.formatLongToSpaces(IslandManager.getInstance().getIslandBank(superiorPlayer.getIsland()).getFarmPoints()));
                MessagesManager.getInstance().setPlaceholder("%formatte_banque_farmpoints%", UtilsBanque.formatNumber(IslandManager.getInstance().getIslandBank(superiorPlayer.getIsland()).getFarmPoints()));
            }
            default -> throw new IllegalStateException("Unexpected value: " + currencyType);
        };

        List<String> promtList = FilesManager.getLanguageFileConfig().getStringList("languages.custom_inputs." + currencyTypeTxt + "." + operationType);
        String promptText = "";
        for(String line : promtList) {
            promptText += line + "\n";
        }

        return MessagesManager.getInstance().processText(promptText);
    }

    @Nullable
    @Override
    protected Prompt acceptValidatedInput(@NotNull ConversationContext context, @NotNull String input) {
        Conversable conversable = context.getForWhom();
        long quantity = 0;

        if(input.equalsIgnoreCase(FilesManager.getLanguageFileConfig().getString("languages.custom_inputs.escape_sequence"))) {
            conversable.sendRawMessage(FilesManager.getPrefix() + FilesManager.getLanguageFileConfig().getString("languages.custom_inputs.escape_message"));
            return END_OF_CONVERSATION;
        }

        switch (operationType) {
            case "add" -> {
                switch (currencyType) {
                    case 0 -> {
                        if(input.equals("all")) {
                            quantity = (long) IslandBanque.getInstance().getEconomy().getBalance((Player) conversable);
                        } else {
                            quantity = Long.parseLong(input);
                        }

                        if (IslandBanque.getInstance().getEconomy().getBalance((Player) conversable) < quantity) {
                            conversable.sendRawMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.failure.not_enough")));
                            return END_OF_CONVERSATION;
                        }
                    }
                    case 1 -> {
                        if(input.equals("all")) {
                            quantity = UtilsBanque.getTotalExperience((Player) conversable);
                        } else {
                            quantity = Long.parseLong(input);
                        }

                        if (UtilsBanque.getTotalExperience((Player) conversable) < quantity) {
                            conversable.sendRawMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.failure.not_enough")));
                            return END_OF_CONVERSATION;
                        }
                    }
                    case 2 -> {
                        if(input.equals("all")) {
                            quantity = (long) BeastTokensAPI.getTokensManager().getTokens((Player) conversable);
                        } else {
                            quantity = Long.parseLong(input);
                        }

                        if (BeastTokensAPI.getTokensManager().getTokens((Player) conversable) < quantity) {
                            conversable.sendRawMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.failure.not_enough")));
                            return END_OF_CONVERSATION;
                        }
                    }
                }

                if (quantity <= 0) {
                    conversable.sendRawMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.failure.too_low")));
                    return END_OF_CONVERSATION;
                }

                CommandsManager.addBalanceCommand(currencyType, quantity, (Player) conversable);
            }

            case "remove" -> {
                SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer((Player) conversable);
                PlayerAddings playerAddings = IslandManager.getInstance().getPlayerAddings(superiorPlayer, superiorPlayer.getIsland());
                long playerAddingAmount = switch (currencyType) {
                    case 0 -> playerAddings.getMoney();
                    case 1 -> playerAddings.getXp();
                    case 2 -> playerAddings.getFarmpoints();
                    default -> throw new RuntimeException("UNEXPECTED VALUE");
                };

                Bank islandBank = IslandManager.getInstance().getIslandBank(superiorPlayer.getIsland());

                switch (currencyType) {
                    case 0 -> {
                        if(input.equals("all")) {
                            quantity = islandBank.getMoney();
                        } else {
                            quantity = Long.parseLong(input);
                        }

                        if (islandBank.getMoney() < quantity) {
                            conversable.sendRawMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.failure.not_enough")));
                            return END_OF_CONVERSATION;
                        }
                    }
                    case 1 -> {
                        if(input.equals("all")) {
                            quantity = islandBank.getExp();
                        } else {
                            quantity = Long.parseLong(input);
                        }

                        if (islandBank.getExp() < quantity) {
                            conversable.sendRawMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.failure.not_enough")));
                            return END_OF_CONVERSATION;
                        }
                    }
                    case 2 -> {
                        if(input.equals("all")) {
                            quantity = islandBank.getFarmPoints();
                        } else {
                            quantity = Long.parseLong(input);
                        }

                        if (islandBank.getFarmPoints() < quantity) {
                            conversable.sendRawMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.failure.not_enough")));
                            return END_OF_CONVERSATION;
                        }
                    }
                }

                if (quantity > playerAddingAmount && !superiorPlayer.getIsland().hasPermission(superiorPlayer, IslandPrivilege.getByName("WITHDRAW_MONEY")) && !superiorPlayer.getIsland().hasPermission(superiorPlayer, IslandPrivilege.getByName("ALL"))) {
                    MessagesManager.getInstance().setPlaceholder("%quantite_peut_retirer%", String.valueOf(playerAddingAmount));
                    conversable.sendRawMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.failure.no_permission_retirer")));
                    return END_OF_CONVERSATION;
                }
                if (quantity <= 0) {
                    conversable.sendRawMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.failure.too_low")));
                    return END_OF_CONVERSATION;
                }

                CommandsManager.withdrawBalanceCommand(currencyType, quantity, (Player) conversable);
            }
        }

        windows.forEach(Window::open);

        return END_OF_CONVERSATION;
    }
}
