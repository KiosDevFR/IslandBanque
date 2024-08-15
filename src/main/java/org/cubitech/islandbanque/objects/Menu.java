package org.cubitech.islandbanque.objects;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.cubitech.islandbanque.guiobjects.*;
import org.cubitech.islandbanque.managers.FilesManager;
import org.cubitech.islandbanque.managers.IslandManager;
import org.cubitech.islandbanque.utils.UtilsBanque;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.gui.structure.Structure;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.window.Window;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Menu {

    private Gui argent;
    private Gui exp;
    private Gui farmPoints;
    private Gui principal;
    private final Player executePlayer;
    private List<Item> updateItems = new ArrayList<>();

    public Menu(Player executePlayer) {
        this.executePlayer = executePlayer;
        this.createGuis();
    }

    private void createGuis() {
        argent = createGui(0);
        principal = createGui(6);
        exp = createGui(1);
        farmPoints = createGui(2);
    }

    /**
     * Shows a menu gui to a player.
     * @param displayPlayer Player to display the gui
     * @param guiId Gui id to display (0- moneyBank, 1- expBank, 2- farmPointsBank, 3- logsArgents, 4- logsExp, 5- logsFarmpoints, 6- home)
     */
    public void showGui(Player displayPlayer, int guiId) {

        Gui gui;
        String guiName = FilesManager.getGuiFileConfig(guiId).getString("title");
        switch (guiId) {
            case 0 -> {gui = argent;}
            case 1 -> {gui = exp;}
            case 2 -> {gui = farmPoints;}
            case 3,4,5 -> {
                ConfigurationSection items = FilesManager.getGuiFileConfig(guiId).getConfigurationSection("items");
                char structIteration = '1';
                char[][] structureChar = new char[FilesManager.getGuiFileConfig(guiId).getInt("row")][9];
                String[] structureStr = new String[FilesManager.getGuiFileConfig(guiId).getInt("row")];
                List<Item> logsFill = new ArrayList<>();
                ItemStack logsFillMaterial = new ItemStack(Material.GRASS_BLOCK);
                String logsFillAmountPositive = "";
                String logsFillAmountNegative = "";
                String[] logsFillDateAdd = new String[0];
                String[] logsFillDateRemove = new String[0];

                int logsFillCustomModelData = 0;

                for(int i = 0; i < structureChar.length; i++) {
                    Arrays.fill(structureChar[i], '.');
                }
                for(String propertie : items.getKeys(false)) {

                    String function = FilesManager.getGuiFileConfig(guiId).getString("items." + propertie + ".function");
                    int slot = FilesManager.getGuiFileConfig(guiId).getInt("items." + propertie + ".slot");

                    if(function == null) {
                        function = " ";
                    }
                    if (function.equals("log")) {
                        int startSlot = FilesManager.getGuiFileConfig(guiId).getInt("items." + propertie + ".start_slot");
                        int endSlot = FilesManager.getGuiFileConfig(guiId).getInt("items." + propertie + ".end_slot");
                        logsFillMaterial = UtilsBanque.getMaterial(FilesManager.getGuiFileConfig(guiId).getString("items." + propertie + ".material"));
                        logsFillCustomModelData = FilesManager.getGuiFileConfig(guiId).getInt("items." + propertie + ".custom_model_data");
                        logsFillAmountPositive = FilesManager.getGuiFileConfig(guiId).getString("items." + propertie + ".log_amount_positive");
                        logsFillAmountNegative = FilesManager.getGuiFileConfig(guiId).getString("items." + propertie + ".log_amount_negative");
                        logsFillDateAdd = FilesManager.getGuiFileConfig(guiId).getList("items." + propertie + ".log_date_add").toArray(new String[0]);
                        logsFillDateRemove = FilesManager.getGuiFileConfig(guiId).getList("items." + propertie + ".log_date_remove").toArray(new String[0]);

                        for (int i = startSlot / structureChar[0].length; i <= endSlot / structureChar[0].length; i++) {
                            for (int j = startSlot % structureChar[0].length; j <= endSlot % structureChar[0].length; j++) {
                                structureChar[i][j] = structIteration;
                            }
                        }

                    } else {
                        structureChar[slot / structureChar[0].length][slot % structureChar[0].length] = structIteration;
                    }
                    structIteration++;
                }

                for (int i = 0; i < structureChar.length; i++) {
                    structureStr[i] = "";
                    for (int j = 0; j < structureChar[0].length; j++) {
                        structureStr[i] += structureChar[i][j];
                    }
                }
                switch (guiId) {
                    case 3 -> {
                        for (BanqueLogMoney log : IslandManager.getInstance().getIslandMoneyLogs(SuperiorSkyblockAPI.getPlayer(executePlayer).getIsland())) {
                            if(FilesManager.getGuiFileConfig(guiId).getString("items.logs_slots.material").equalsIgnoreCase("PLAYER")){
                                logsFill.add(new LogDisplayer(UtilsBanque.getHead(log.getPlayerName()), logsFillCustomModelData, executePlayer, log, logsFillAmountPositive, logsFillAmountNegative, logsFillDateAdd, logsFillDateRemove));
                            }else
                                logsFill.add(new LogDisplayer(logsFillMaterial, logsFillCustomModelData, executePlayer, log, logsFillAmountPositive, logsFillAmountNegative, logsFillDateAdd, logsFillDateRemove));
                        }
                    }
                    case 4 -> {
                        for (BanqueLogXp log : IslandManager.getInstance().getIslandXpLogs(SuperiorSkyblockAPI.getPlayer(executePlayer).getIsland())) {
                            if(FilesManager.getGuiFileConfig(guiId).getString("items.logs_slots.material").equalsIgnoreCase("PLAYER")){
                                logsFill.add(new LogDisplayer(UtilsBanque.getHead(log.getPlayerName()), logsFillCustomModelData, executePlayer, log, logsFillAmountPositive, logsFillAmountNegative, logsFillDateAdd, logsFillDateRemove));
                            }else
                                logsFill.add(new LogDisplayer(logsFillMaterial, logsFillCustomModelData, executePlayer, log, logsFillAmountPositive, logsFillAmountNegative, logsFillDateAdd, logsFillDateRemove));
                        }
                    }
                    case 5 -> {
                        for (BanqueLogFarmpoints log : IslandManager.getInstance().getIslandFarmpointsLogs(SuperiorSkyblockAPI.getPlayer(executePlayer).getIsland())) {
                            if(FilesManager.getGuiFileConfig(guiId).getString("items.logs_slots.material").equalsIgnoreCase("PLAYER")){
                                logsFill.add(new LogDisplayer(UtilsBanque.getHead(log.getPlayerName()), logsFillCustomModelData, executePlayer, log, logsFillAmountPositive, logsFillAmountNegative, logsFillDateAdd, logsFillDateRemove));
                            }else
                                logsFill.add(new LogDisplayer(logsFillMaterial, logsFillCustomModelData, executePlayer, log, logsFillAmountPositive, logsFillAmountNegative, logsFillDateAdd, logsFillDateRemove));
                        }
                    }
                }
                structIteration = '1';
                Structure structure = new Structure(structureStr);
                for(String propertie : items.getKeys(false)) {

                    String function = FilesManager.getGuiFileConfig(guiId).getString("items." + propertie + ".function");
                    ItemStack material = UtilsBanque.getMaterial(FilesManager.getGuiFileConfig(guiId).getString("items." + propertie + ".material"));
                    int customModelData = FilesManager.getGuiFileConfig(guiId).getInt("items." + propertie + ".custom_model_data");
                    String name = FilesManager.getGuiFileConfig(guiId).getString("items." + propertie + ".name");
                    String[] lore = FilesManager.getGuiFileConfig(guiId).getStringList("items." + propertie + ".lore").toArray(new String[0]);

                    if(function == null) {
                        function = " ";
                    }
                    switch (function) {
                        case "log" -> {structure.addIngredient(structIteration, Markers.CONTENT_LIST_SLOT_HORIZONTAL);}
                        case "banque-farmpoints" -> {structure.addIngredient(structIteration, new FarmPointsBank(material, name, lore, customModelData, this));}
                        case "banque-exp" -> {structure.addIngredient(structIteration, new ExpBank(material, name, lore, customModelData, this));}
                        case "banque-argent" -> {structure.addIngredient(structIteration, new MoneyBank(material, name, lore, customModelData, this));}
                        case "back" -> {structure.addIngredient(structIteration, new BackToPrincipal(material, name, lore, customModelData, this));}
                        case "page-suivante" -> {structure.addIngredient(structIteration, new ForwardPage(material, name, lore, customModelData, executePlayer));}
                        case "page-precedente" -> {structure.addIngredient(structIteration, new BackPage(material, name, lore, customModelData, executePlayer));}
                        case "logs-argent", "logs-exp", "logs-farmpoints" -> {structure.addIngredient(structIteration, new LogButton(material, name, lore, function, customModelData, this));}
                        default -> {structure.addIngredient(structIteration, new SimpItem(material, name, lore, customModelData, executePlayer));}
                    }
                    structIteration++;
                }

                gui = PagedGui.items().setStructure(structure).setContent(logsFill).build();

            }
            default -> {gui = principal;}
        }

        updateItems.clear();
        for(int i = 0; i < gui.getWidth(); i++) {
            for(int j = 0; j < gui.getHeight(); j++) {
                if(gui.hasSlotElement(i, j)) {
                    updateItems.add(gui.getItem(i, j));
                }
            }
        }

        Window window = Window.single()
                .setViewer(displayPlayer)
                .setTitle(guiName)
                .setGui(gui)
                .build();
        window.open();
    }

    private Gui createGui(int fileId) {

        ConfigurationSection items = FilesManager.getGuiFileConfig(fileId).getConfigurationSection("items");
        if(items == null) {
            return null;
        }

        String[] structure = new String[FilesManager.getGuiFileConfig(fileId).getInt("row")];
        Arrays.fill(structure, ". . . . . . . . .");

        Gui gui = Gui.normal()
                .setStructure(structure)
                .build();

        for(String propertie : items.getKeys(false)) {
            String function = FilesManager.getGuiFileConfig(fileId).getString("items." + propertie + ".function");
            ItemStack material = UtilsBanque.getMaterial(FilesManager.getGuiFileConfig(fileId).getString("items." + propertie + ".material"));
            int customModelData = FilesManager.getGuiFileConfig(fileId).getInt("items." + propertie + ".custom_model_data");
            int slot = FilesManager.getGuiFileConfig(fileId).getInt("items." + propertie + ".slot");
            String name = FilesManager.getGuiFileConfig(fileId).getString("items." + propertie + ".name");
            String[] lore = FilesManager.getGuiFileConfig(fileId).getStringList("items." + propertie + ".lore").toArray(new String[0]);
            if(function == null) {
                function = " ";
            }

            switch (function) {
                case "add-argent", "add-exp", "add-farmpoints" -> {
                    int quantity = FilesManager.getGuiFileConfig(fileId).getInt("items." + propertie + ".percentage");
                    ItemStack naMaterial = UtilsBanque.getMaterial(FilesManager.getGuiFileConfig(fileId).getString("items." + propertie + ".no_permission.material"));
                    int naCustomModelData = FilesManager.getGuiFileConfig(fileId).getInt("items." + propertie + ".no_permission.custom_model_data");
                    String naName = ChatColor.translateAlternateColorCodes('&', FilesManager.getGuiFileConfig(fileId).getString("items." + propertie + ".no_permission.name"));
                    String[] naLore = FilesManager.getGuiFileConfig(fileId).getStringList("items." + propertie + ".no_permission.lore").toArray(new String[0]);
                    gui.setItem(slot, new AddCurrency(material,naMaterial, name,naName, lore,naLore, quantity, function.substring(4), customModelData,naCustomModelData, this));
                }
                case "remove-argent", "remove-exp", "remove-farmpoints" -> {
                    int quantity = FilesManager.getGuiFileConfig(fileId).getInt("items." + propertie + ".percentage");
                    ItemStack naMaterial = UtilsBanque.getMaterial(FilesManager.getGuiFileConfig(fileId).getString("items." + propertie + ".no_permission.material"));
                    int naCustomModelData = FilesManager.getGuiFileConfig(fileId).getInt("items." + propertie + ".no_permission.custom_model_data");
                    String naName = FilesManager.getGuiFileConfig(fileId).getString("items." + propertie + ".no_permission.name");
                    if(naName != null)
                        naName = ChatColor.translateAlternateColorCodes('&', naName);
                    String[] naLore = FilesManager.getGuiFileConfig(fileId).getStringList("items." + propertie + ".no_permission.lore").toArray(new String[0]);
                    gui.setItem(slot, new WithdrawCurrency(material,naMaterial, name,naName, lore,naLore, quantity, function.substring(7), customModelData,naCustomModelData, this));
                }
                case "banque-farmpoints" -> {gui.setItem(slot, new FarmPointsBank(material, name, lore, customModelData, this));}
                case "banque-exp" -> {gui.setItem(slot, new ExpBank(material, name, lore, customModelData, this));}
                case "banque-argent" -> {gui.setItem(slot, new MoneyBank(material, name, lore, customModelData, this));}
                case "back" -> {
                    gui.setItem(slot, new BackToPrincipal(material, name, lore, customModelData, this));}
                case "command" -> {
                    String[] leftCommand = FilesManager.getGuiFileConfig(fileId).getList("items." + propertie + ".left_click_commands").toArray(new String[0]);
                    String[] rightCommand = FilesManager.getGuiFileConfig(fileId).getList("items." + propertie + ".right_click_commands").toArray(new String[0]);
                    gui.setItem(slot, new ExecuteCommand(material, name, lore, leftCommand, rightCommand, customModelData, executePlayer));
                }
                case "logs-argent", "logs-exp", "logs-farmpoints" -> {
                    gui.setItem(slot, new LogButton(material, name, lore, function, customModelData, this));
                }
                case "interets" -> {
                    ItemStack naMaterial = UtilsBanque.getMaterial(FilesManager.getGuiFileConfig(fileId).getString("items." + propertie + ".non-available.material"));
                    int naCustomModelData = FilesManager.getGuiFileConfig(fileId).getInt("items." + propertie + ".non-available.custom_model_data");
                    String naName = ChatColor.translateAlternateColorCodes('&', FilesManager.getGuiFileConfig(fileId).getString("items." + propertie + ".non-available.name"));
                    String[] naLore = FilesManager.getGuiFileConfig(fileId).getStringList("items." + propertie + ".non-available.lore").toArray(new String[0]);
                    for(int i = 0; i < naLore.length; i++) {
                        naLore[i] = ChatColor.translateAlternateColorCodes('&', naLore[i]);
                    }

                    gui.setItem(slot, new Interests(material, name, lore, customModelData, naMaterial, naName, naLore, naCustomModelData, this));
                }
                default -> {
                    gui.setItem(slot, new SimpItem(material, name, lore, customModelData, executePlayer));
                }
            }
        }
        return gui;
    }
    
    public void updateItems() {
        for (Item item : updateItems) {
            item.notifyWindows();
        }
    }

    public Player getExecutePlayer() {
        return executePlayer;
    }
}