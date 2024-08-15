package org.cubitech.islandbanque.managers;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.cubitech.islandbanque.IslandBanque;
import org.cubitech.islandbanque.enums.GuiFiles;

import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

public class FilesManager {
    private static String prefix;
    private static File languageFile;
    private static FileConfiguration languageFileConfig;
    private static File[] guiFiles;
    private static FileConfiguration[] guiFileConfigs;

    public static void registerConfig() {
        File config = new File(IslandBanque.getInstance().getDataFolder(), "config.yml");
        if (!config.exists()) {
            IslandBanque.getInstance().saveDefaultConfig();
        }
    }

    //// LANGUAGES.YML ////
    public static FileConfiguration getLanguageFileConfig() {
        if (languageFileConfig == null) {
            reloadLanguages();
        }
        return languageFileConfig;
    }

    public static void reloadLanguages() {
        if (languageFileConfig == null) {
            languageFile = new File(IslandBanque.getInstance().getDataFolder(), "languages.yml");
        }
        languageFileConfig = YamlConfiguration.loadConfiguration(languageFile);
        Reader defConfigStream;
        try {
            defConfigStream = new InputStreamReader(IslandBanque.getInstance().getResource("languages.yml"), "UTF8");
            if (defConfigStream != null) {
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                languageFileConfig.setDefaults(defConfig);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void registerLanguages() {
        languageFile = new File(IslandBanque.getInstance().getDataFolder(), "languages.yml");
        if (!languageFile.exists()) {
            IslandBanque.getInstance().saveResource("languages.yml", false);
        }
    }

    public static FileConfiguration getGuiFileConfig(int fileId) {
        if (guiFileConfigs[fileId] == null) {
            reloadGuiFile();
        }
        return guiFileConfigs[fileId];
    }

    public static void reloadGuiFile() {

        File guiFolder = new File(IslandBanque.getInstance().getDataFolder(), "guis");
        if (!guiFolder.exists()) {
            try {
                guiFolder.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for(int i = 0; i < guiFiles.length; i++) {
            if (guiFileConfigs[i] == null) {
                guiFiles[i] = new File(IslandBanque.getInstance().getDataFolder() + "/guis", GuiFiles.values()[i].getFileName());
            }
            guiFileConfigs[i] = YamlConfiguration.loadConfiguration(guiFiles[i]);
            Reader defConfigStream;
            try {
                defConfigStream = new InputStreamReader(IslandBanque.getInstance().getResource("guis/" + GuiFiles.values()[i].getFileName()), "UTF8");
                if (defConfigStream != null) {
                    YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                    guiFileConfigs[i].setDefaults(defConfig);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getPrefix() {
        return prefix;
    }

    public static void setPrefix(String prefix) {
        FilesManager.prefix = ChatColor.translateAlternateColorCodes('&', prefix);
    }

    public static void registerGuiFiles() {
        File guiFolder = new File(IslandBanque.getInstance().getDataFolder(), "guis");
        if (!guiFolder.exists()) {
            try {
                guiFolder.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        guiFiles = new File[GuiFiles.values().length];
        guiFileConfigs = new FileConfiguration[GuiFiles.values().length];

        for(int i = 0; i < GuiFiles.values().length; i++) {
            guiFiles[i] = new File(IslandBanque.getInstance().getDataFolder() + "/guis", GuiFiles.values()[i].getFileName());
            if (!guiFiles[i].exists()) {
                IslandBanque.getInstance().saveResource("guis/" + GuiFiles.values()[i].getFileName(), false);
            }
        }
    }
}
