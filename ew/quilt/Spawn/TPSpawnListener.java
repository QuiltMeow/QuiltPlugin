package ew.quilt.Spawn;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class TPSpawnListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        if (world.getName().equalsIgnoreCase("world_nether") || world.getName().equalsIgnoreCase("world_the_end")) {
            World spawn = Bukkit.getWorld("world");
            player.teleport(spawn.getSpawnLocation());
        }
    }
}
