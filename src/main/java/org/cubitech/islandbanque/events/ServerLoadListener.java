package org.cubitech.islandbanque.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;
import org.cubitech.islandbanque.IslandBanque;

public class ServerLoadListener implements Listener {
    @EventHandler
    public void onServerLoad(ServerLoadEvent e) {
        IslandBanque.getInstance().setUpAll();
    }
}
