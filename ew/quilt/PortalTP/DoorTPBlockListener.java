package ew.quilt.PortalTP;

import ew.quilt.Config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class DoorTPBlockListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPortal(PlayerPortalEvent event) {
        World from = event.getFrom().getWorld();
        if (event.getCause() == TeleportCause.NETHER_PORTAL) {
            if (from.getName().equalsIgnoreCase("world_nether")) {
                World survival = Bukkit.getWorld(ConfigManager.SURVIVAL_EWG_WORLD);
                event.getPlayer().teleport(survival.getSpawnLocation());
            } else {
                World nether = Bukkit.getWorld("world_nether");
                event.getPlayer().teleport(nether.getSpawnLocation());
            }
            event.setCancelled(true);
        } else if (event.getCause() == TeleportCause.END_PORTAL) {
            if (!from.getName().equalsIgnoreCase("world_the_end")) {
                World end = Bukkit.getWorld("world_the_end");
                event.getPlayer().teleport(end.getSpawnLocation());
                event.setCancelled(true);
            }
        }
    }
}
