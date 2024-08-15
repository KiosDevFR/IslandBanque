package org.cubitech.islandbanque.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class IslandBanqueTabCompleter implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        Player player = null;
        if(sender instanceof Player){
            player = (Player) sender;
        }
        if(player != null && !player.hasPermission("islandbanque.player") && args[0].equalsIgnoreCase("monnaies")){
            return null;
        }
        if(player != null && !player.hasPermission("islandbanque.menu") && args[0].equalsIgnoreCase("ouvrir")){
            return null;
        }
        if(player != null && !player.hasPermission("islandbanque.command") && (args[0].equalsIgnoreCase("ajouter") || args[0].equalsIgnoreCase("retirer"))){
            return null;
        }
        if(player != null && !player.hasPermission("islandbanque.admin") && (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("take") || args[0].equalsIgnoreCase("view") || args[0].equalsIgnoreCase("interet") || args[0].equalsIgnoreCase("limite"))){
            return null;
        }
        switch (args.length) {
            case 1 -> {
                ArrayList<String> commands = new ArrayList<>();
                if(player == null || player.hasPermission("islandbanque.player")){
                    commands.add("monnaies");
                }
                if(player == null || player.hasPermission("islandbanque.menu")){
                    commands.add("ouvrir");
                }
                if(player == null || player.hasPermission("islandbanque.command")){
                    commands.add("ajouter");
                    commands.add("retirer");
                }
                if(player == null || player.hasPermission("islandbanque.admin")){
                    commands.add("reload");
                    commands.add("give");
                    commands.add("take");
                    commands.add("view");
                    commands.add("interet");
                    commands.add("limite");
                }
                return commands;
            }
            case 2 -> {
                if(args[0].equalsIgnoreCase("ajouter") || args[0].equalsIgnoreCase("retirer")){
                    return new ArrayList<>(List.of("argent","exp","farmpoints"));
                }else if(args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("take") || args[0].equalsIgnoreCase("view") || args[0].equalsIgnoreCase("interet") || args[0].equalsIgnoreCase("limite")){
                    return new ArrayList<>(List.of("<Joueur>"));
                }
            }
            case 3 -> {
                if(args[0].equalsIgnoreCase("ajouter") || args[0].equalsIgnoreCase("retirer")){
                    return new ArrayList<>(List.of("<Montant>","ALL"));
                }else if(args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("take") || args[0].equalsIgnoreCase("interet")|| args[0].equalsIgnoreCase("limite")){
                    return new ArrayList<>(List.of("argent","exp","farmpoints"));
                }
            }
            case 4 -> {
                if(args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("take") || args[0].equalsIgnoreCase("interet")|| args[0].equalsIgnoreCase("limite")){
                    return new ArrayList<>(List.of("<Montant>"));
                }
            }

        }
        return null;
    }
}
