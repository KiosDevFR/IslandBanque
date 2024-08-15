package org.cubitech.islandbanque.commands;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.IslandPrivilege;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import me.mraxetv.beasttokens.api.BeastTokensAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.cubitech.islandbanque.IslandBanque;
import org.cubitech.islandbanque.managers.*;
import org.cubitech.islandbanque.objects.*;
import org.cubitech.islandbanque.utils.UtilsBanque;

import java.util.List;

public class IslandBanqueCommandExecutor implements CommandExecutor, Listener {

    public IslandBanqueCommandExecutor() {
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        Player player = null;
        SuperiorPlayer superiorPlayer = null;
        if (commandSender instanceof Player) {
            player = (Player) commandSender;
            superiorPlayer = SuperiorSkyblockAPI.getPlayer(player);
        }

        if (args.length > 0)
            switch (args[0]) {
                case "monnaies" -> {
                    if (player == null) {
                        commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.main.must_be_player")));
                        return false;
                    }
                    if (!player.hasPermission("islandbanque.player")) {
                        commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.main.no_permission")));
                        return false;
                    }
                    MessagesManager.getInstance().restartPlaceholders();
                    MessagesManager.getInstance().setPlaceholder("%joueur_farmpoints%",String.valueOf(BeastTokensAPI.getTokensManager().getTokens(player)));
                    MessagesManager.getInstance().setPlaceholder("%joueur_exp%",String.valueOf(UtilsBanque.getTotalExperience(player)));
                    MessagesManager.getInstance().setPlaceholder("%joueur_argent%",String.valueOf(IslandBanque.getInstance().getEconomy().getBalance(player)));

                    MessagesManager.getInstance().setPlaceholder("%banque_farmpoints%",String.valueOf(0));
                    MessagesManager.getInstance().setPlaceholder("%banque_exp%",String.valueOf(0));
                    MessagesManager.getInstance().setPlaceholder("%banque_argent%",String.valueOf(0));
                    if (superiorPlayer.hasIsland()) {
                        Bank bank = IslandManager.getInstance().getIslandBank(superiorPlayer.getIsland());
                        MessagesManager.getInstance().setPlaceholder("%banque_argent%", String.valueOf(bank.getMoney()));
                        MessagesManager.getInstance().setPlaceholder("%banque_exp%", String.valueOf(bank.getExp()));
                        MessagesManager.getInstance().setPlaceholder("%banque_farmpoints%", String.valueOf(bank.getFarmPoints()));
                    }


                    List<String> list = (List<String>) FilesManager.getLanguageFileConfig().getList("languages.monnaies");
                    for(String s : list){
                        player.sendMessage(MessagesManager.getInstance().processText(FilesManager.getPrefix() + s));
                    }
                    return true;
                }
                case "ajouter" -> {
                    MessagesManager.getInstance().restartPlaceholders();
                    if (player == null) {
                        commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.main.must_be_player")));
                        return false;
                    }
                    if (!player.hasPermission("islandbanque.command")) {
                        commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.main.no_permission_command")));
                        return false;
                    }
                    if (args.length < 3) {
                        commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.failure.bad_syntax")));
                        return false;
                    }
                    if (args[1].equals("argent") || args[1].equals("exp") || args[1].equals("farmpoints")) {
                        try {
                            long amount = 0;
                            
                            if(args[2].equalsIgnoreCase("ALL")){
                                amount = switch (args[1]) {
                                    case "argent" -> (long) IslandBanque.getInstance().getEconomy().getBalance(player);
                                    case "exp" -> (long) UtilsBanque.getTotalExperience(player);
                                    case "farmpoints" -> (long) BeastTokensAPI.getTokensManager().getTokens(player);
                                    default -> throw new RuntimeException("UNEXPECTED VALUE");
                                };
                            }else{
                                amount = Long.parseLong(args[2]);
                            }


                            if (!superiorPlayer.getIsland().hasPermission(superiorPlayer, IslandPrivilege.getByName("DEPOSIT_MONEY")) && !superiorPlayer.getIsland().hasPermission(superiorPlayer, IslandPrivilege.getByName("ALL"))) {
                                commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.no_permission_ajouter")));
                                return false;
                            }
                            if (amount <= 0) {
                                commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.failure.too_low")));
                                return false;
                            }
                            int typeOperation = UtilsBanque.getTypeIntFromText(args[1]);
                            switch (typeOperation) {
                                case 0 -> {
                                    if (IslandBanque.getInstance().getEconomy().getBalance(player) < amount) {
                                        commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.failure.not_enough")));
                                        return false;
                                    }
                                }
                                case 1 -> {
                                    if (UtilsBanque.getTotalExperience(player) < amount) {
                                        commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.failure.not_enough")));
                                        return false;
                                    }
                                }
                                case 2 -> {
                                    if (BeastTokensAPI.getTokensManager().getTokens(player) < amount) {
                                        commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.failure.not_enough")));
                                        return false;
                                    }
                                }
                            }
                            CommandsManager.addBalanceCommand(typeOperation, amount, player);
                        } catch (NumberFormatException e) {
                            commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.failure.virgule")));
                            return false;
                        }
                    } else {
                        commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.failure.bad_syntax")));
                        return false;
                    }
                    return true;
                }
                case "retirer" -> {
                    if (player == null) {
                        commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.main_must_be_player")));
                        return false;
                    }
                    if (!player.hasPermission("islandbanque.command")) {
                        commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.main.no_permission_command")));
                        return false;
                    }
                    if (args.length < 3) {
                        commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.failure.bad_syntax")));
                        return false;
                    }
                    if (args[1].equals("argent") || args[1].equals("exp") || args[1].equals("farmpoints")) {
                        try {
                            long amount = 0;

                            Bank islandPlayerBank = IslandManager.getInstance().getIslandBank(superiorPlayer.getIsland());
                            if(args[2].equalsIgnoreCase("ALL")){
                                amount = switch (args[1]) {
                                    case "argent" -> islandPlayerBank.getMoney();
                                    case "exp" -> islandPlayerBank.getExp();
                                    case "farmpoints" -> islandPlayerBank.getFarmPoints();
                                    default -> throw new RuntimeException("UNEXPECTED VALUE");
                                };
                            }else{
                                amount = Long.parseLong(args[2]);
                            }

                            PlayerAddings playerAddings = IslandManager.getInstance().getPlayerAddings(superiorPlayer, superiorPlayer.getIsland());
                            long playerAddingAmount = switch (args[1]) {
                                case "argent" -> playerAddings.getMoney();
                                case "exp" -> playerAddings.getXp();
                                case "farmpoints" -> playerAddings.getFarmpoints();
                                default -> throw new RuntimeException("UNEXPECTED VALUE");
                            };
                            if (amount > playerAddingAmount && !superiorPlayer.getIsland().hasPermission(superiorPlayer, IslandPrivilege.getByName("WITHDRAW_MONEY")) && !superiorPlayer.getIsland().hasPermission(superiorPlayer, IslandPrivilege.getByName("ALL"))) {
                                MessagesManager.getInstance().setPlaceholder("%quantite_peut_retirer%", String.valueOf(playerAddingAmount));
                                commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.failure.no_permission_retirer")));
                                return false;
                            }
                            if (amount <= 0) {
                                commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.failure.too_low")));
                                return false;
                            }
                            int typeOperation = UtilsBanque.getTypeIntFromText(args[1]);
                            Bank previousBank = IslandManager.getInstance().getIslandBank(superiorPlayer.getIsland());

                            switch (typeOperation) {
                                case 0 -> {
                                    if (previousBank.getMoney() < amount) {
                                        commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.failure.not_enough")));
                                        return false;
                                    }
                                }
                                case 1 -> {
                                    if (previousBank.getExp() < amount) {
                                        commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.failure.not_enough")));
                                        return false;
                                    }
                                }
                                case 2 -> {
                                    if (previousBank.getFarmPoints() < amount) {
                                        commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.failure.not_enough")));
                                        return false;
                                    }
                                }
                            }
                            CommandsManager.withdrawBalanceCommand(UtilsBanque.getTypeIntFromText(args[1]), amount, player);
                        } catch (NumberFormatException e) {
                            commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.failure.virgule")));
                            return false;
                        }
                    } else {
                        commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.failure.bad_syntax")));
                        return false;
                    }
                    return true;
                }

                case "give" -> {

                    if (player != null && !player.hasPermission("islandbanque.admin")) {
                        commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.main.no_permission_command")));
                        return false;
                    }
                    if (args.length < 4) {
                        commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.failure.bad_syntax")));
                        return false;
                    }
                    if (args[2].equals("argent") || args[2].equals("exp") || args[2].equals("farmpoints")) {
                        try {
                            long amount = Long.parseLong(args[3]);
                            Player targetPlayer = Bukkit.getPlayer(args[1]);
                            if (targetPlayer == null) {
                                commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.admin.no_player_found")));
                                return false;
                            }
                            if (amount <= 0) {
                                commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.failure.too_low")));
                                return false;
                            }
                            int typeOperation = UtilsBanque.getTypeIntFromText(args[2]);
                            CommandsManager.adminAddPlayerIslandBalance(targetPlayer, typeOperation, amount);
                            commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.admin.quantity_add_success")));
                        } catch (NumberFormatException e) {
                            commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.failure.virgule")));
                            return false;
                        }
                    } else {
                        commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.failure.bad_syntax")));
                        return false;
                    }
                    return true;
                }
                case "take" -> {
                    if (player != null && !player.hasPermission("islandbanque.admin")) {
                        commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.main.no_permission_command")));
                        return false;
                    }
                    if (args.length < 4) {
                        commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.failure.bad_syntax")));
                        return false;
                    }
                    if (args[2].equals("argent") || args[2].equals("exp") || args[2].equals("farmpoints")) {
                        try {
                            long amount = Long.parseLong(args[3]);
                            Player targetPlayer = Bukkit.getPlayer(args[1]);
                            if (targetPlayer == null) {
                                commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.admin.no_player_found")));
                                return false;
                            }
                            if (amount <= 0) {
                                commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.failure.too_low")));
                                return false;
                            }
                            int typeOperation = UtilsBanque.getTypeIntFromText(args[2]);
                            CommandsManager.adminAddPlayerIslandBalance(targetPlayer, typeOperation, -amount);
                            commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.admin.quantity_remove_success")));
                        } catch (NumberFormatException e) {
                            commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.failure.virgule")));
                            return false;
                        }
                    } else {
                        commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.failure.bad_syntax")));
                        return false;
                    }
                    return true;
                }
                case "limite" -> {

                    if (player != null && !player.hasPermission("islandbanque.admin")) {
                        commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.main.no_permission_command")));
                        return false;
                    }
                    if (args.length < 4) {
                        commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.failure.bad_syntax")));
                        return false;
                    }
                    Player userPlayer = Bukkit.getPlayer(args[1]);
                    if (userPlayer == null) {
                        commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.admin.no_player_found")));
                        return false;
                    }
                    SuperiorPlayer userSuperiorPlayer = SuperiorSkyblockAPI.getPlayer(userPlayer);
                    try {
                        long amount = Long.parseLong(args[3]);
                        if (amount < 0) {
                            commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.failure.too_low")));
                            return false;
                        }
                        if (!userSuperiorPlayer.hasIsland()) {
                            commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.admin.no_player_island")));
                            return false;
                        }
                        if (args[2].equals("argent") || args[2].equals("exp") || args[2].equals("farmpoints")) {
                            int type = UtilsBanque.getTypeIntFromText(args[2]);
                            CommandsManager.assignInterestLimitToPlayer(userSuperiorPlayer, type, amount);
                        } else {
                            commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.failure.bad_syntax")));
                            return false;
                        }

                    } catch (NumberFormatException e) {
                        commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.failure.virgule")));
                        return false;
                    }

                    return true;
                }
                case "interet" -> {
                    if (player != null && !player.hasPermission("islandbanque.admin")) {
                        commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.main.no_permission_command")));
                        return false;
                    }
                    if (args.length < 3) {
                        commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.failure.bad_syntax")));
                        return false;
                    }
                    Player userPlayer = Bukkit.getPlayer(args[1]);
                    if (userPlayer == null) {
                        commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.admin.no_player_found")));
                        return false;
                    }
                    SuperiorPlayer userSuperiorPlayer = SuperiorSkyblockAPI.getPlayer(userPlayer);
                    try {
                        double amount = Double.parseDouble(args[2]);
                        if (amount < 0) {
                            commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.failure.too_low")));
                            return false;
                        }
                        if (!userSuperiorPlayer.hasIsland()) {
                            commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.admin.no_player_island")));
                            return false;
                        }
                        CommandsManager.assignInterestToPlayer(userSuperiorPlayer, amount);

                    } catch (NumberFormatException e) {
                        commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.failure.virgule")));
                        return false;
                    }

                    return true;
                }
                case "ouvrir" -> {
                    if (player == null) {
                        commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.main.must_be_player")));
                        return false;
                    }
                    if (!player.hasPermission("islandbanque.menu")) {
                        commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.main.no_permission_command")));
                        return false;
                    }
                    if (!superiorPlayer.hasIsland()) {
                        commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.admin.no_player_island")));
                        return false;
                    }
                    if (args.length == 1) {
                        Menu menu = new Menu(player);
                        menu.showGui(player, 6);
                    }
                    return true;
                }
                case "reload" -> {
                    if (player != null && !player.hasPermission("islandbanque.admin")) {
                        commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.main.no_permission_command")));
                        return false;
                    }
                    FilesManager.reloadLanguages();
                    FilesManager.setPrefix(FilesManager.getLanguageFileConfig().getString("languages.main.prefix"));
                    FilesManager.reloadGuiFile();
                    TimersManager.getInstance().startTimer();
                    commandSender.sendMessage(FilesManager.getPrefix() + ChatColor.translateAlternateColorCodes('&', FilesManager.getLanguageFileConfig().getString("languages.main.reload")));
                    return true;
                }
                case "view" -> {
                    if (player != null && !player.hasPermission("islandbanque.admin")) {
                        commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.main.no_permission_command")));
                        return false;
                    }
                    if (args.length == 2) {
                        Player executePlayer = Bukkit.getPlayer(args[1]);
                        if (executePlayer == null || !SuperiorSkyblockAPI.getPlayer(executePlayer).hasIsland()) {
                            commandSender.sendMessage(FilesManager.getPrefix() + MessagesManager.getInstance().processText(FilesManager.getLanguageFileConfig().getString("languages.admin.no_player_island")));
                            return false;
                        }
                        Menu menu = new Menu(player);
                        menu.showGui(executePlayer, 6);
                    }
                    return true;
                }
                default -> {
                    return true;
                }
            }
        return false;
    }
}
