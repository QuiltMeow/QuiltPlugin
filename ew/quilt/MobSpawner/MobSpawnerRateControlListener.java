package ew.quilt.MobSpawner;

import ew.quilt.util.Randomizer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class MobSpawnerRateControlListener implements Listener {

    private static final double SPAWN_CHANCE = 35;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMobSpawn(CreatureSpawnEvent event) {
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER) {
            setFalseByPercent(event, SPAWN_CHANCE);
        }
    }

    public void setFalseByPercent(CreatureSpawnEvent event, double chance) {
        if (Randomizer.nextDouble(100) >= chance) {
            event.setCancelled(true);
        }
    }
}
