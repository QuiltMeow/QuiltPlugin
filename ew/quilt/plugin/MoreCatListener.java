package ew.quilt.plugin;

import ew.quilt.util.Randomizer;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public class MoreCatListener implements Listener {

    @EventHandler
    public void onEntitySpawn(CreatureSpawnEvent event) {
        if (event.getEntityType() == EntityType.WOLF && event.getSpawnReason() == SpawnReason.NATURAL) {
            if (((Wolf) event.getEntity()).isAdult()) {
                Location location = event.getLocation();
                location.getWorld().spawnEntity(location.add(Randomizer.isSuccess(50) ? 1 : -1, 0, Randomizer.isSuccess(50) ? 1 : -1), EntityType.OCELOT);
            }
        }
    }
}
