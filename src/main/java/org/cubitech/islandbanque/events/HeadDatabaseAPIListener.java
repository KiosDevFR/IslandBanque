package org.cubitech.islandbanque.events;

import me.arcaniax.hdb.api.DatabaseLoadEvent;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class HeadDatabaseAPIListener implements Listener {
    private static HeadDatabaseAPI api;

    @EventHandler
    public void onDatabaseLoad(DatabaseLoadEvent e) {
        api = new HeadDatabaseAPI();
    }

    public static HeadDatabaseAPI getApi(){
        return api;
    }
}