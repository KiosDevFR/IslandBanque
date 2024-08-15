package org.cubitech.islandbanque;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.cubitech.islandbanque.commands.IslandBanqueCommandExecutor;
import org.cubitech.islandbanque.commands.IslandBanqueTabCompleter;
import org.cubitech.islandbanque.events.ServerLoadListener;
import org.cubitech.islandbanque.events.HeadDatabaseAPIListener;
import org.cubitech.islandbanque.managers.*;

public class IslandBanque extends JavaPlugin {
    private static IslandBanque instance;
    private static final String PREFIX = ChatColor.YELLOW + "[" + ChatColor.GOLD + "IslandBanque" + ChatColor.YELLOW  + "]";
    private Economy economy = null;

    @Override
    public void onEnable(){
        instance = this;
        this.registerEvents();
        this.getServer().getPluginManager().registerEvents(new ServerLoadListener(), this);
        this.getServer().getPluginManager().registerEvents(new HeadDatabaseAPIListener(), this);
        this.getCommand("islandbanque").setExecutor(new IslandBanqueCommandExecutor());
        this.getCommand("islandbanque").setTabCompleter(new IslandBanqueTabCompleter());
    }

    @Override
    public void onDisable(){

    }

    private void registerEvents() {
        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new ServerLoadListener(), this);
    }

    public void setUpAll() {
        if (!setupEconomy()) {
            Bukkit.getConsoleSender().sendMessage(PREFIX + ChatColor.RED + " Vault or compatible economy plugin is missing, disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if(!Bukkit.getPluginManager().isPluginEnabled("BeastTokens")) {
            Bukkit.getConsoleSender().sendMessage(PREFIX + ChatColor.RED + " BeastTokens is missing, disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if(!Bukkit.getPluginManager().isPluginEnabled("SuperiorSkyblock2")) {
            Bukkit.getConsoleSender().sendMessage(PREFIX + ChatColor.RED + " SuperiorSkyblock2 is missing, disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PlaceholderApiManager().register();
        }else{
            Bukkit.getConsoleSender().sendMessage(PREFIX + ChatColor.RED + " PlaceholderAPI2 is missing, disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        FilesManager.registerConfig();
        FilesManager.registerLanguages();
        FilesManager.registerGuiFiles();
        FilesManager.setPrefix(FilesManager.getLanguageFileConfig().getString("languages.main.prefix"));
        DatabaseManager.setUpDatabase();
        TimersManager.getInstance().startTimer();
        MessagesManager.getInstance();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    public Economy getEconomy(){
        return economy;
    }

    public static IslandBanque getInstance(){
        return instance;
    }


}
