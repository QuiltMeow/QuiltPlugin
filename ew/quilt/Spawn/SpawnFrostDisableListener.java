package ew.quilt.Spawn;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.EntityBlockFormEvent;

public class SpawnFrostDisableListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFrost(EntityBlockFormEvent event) {
        if (event.getEntity() instanceof Player) {
            World world = event.getEntity().getWorld();
            if (world.getName().equalsIgnoreCase("world")) {
                event.setCancelled(true);
            }
        }
    }
}
