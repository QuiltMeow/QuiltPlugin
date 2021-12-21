package ew.quilt.PigZombie;

import ew.quilt.util.Randomizer;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class PigZombieSpawnListener implements Listener {

    private static final boolean NO_SPAWN = false;
    private static final double SPAWN_CHANCE = 0.01;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMobSpawn(CreatureSpawnEvent event) {
        if (event.getEntity().getType() == EntityType.PIG_ZOMBIE && event.getEntity().getWorld().getEnvironment() != World.Environment.NETHER) {
            if (NO_SPAWN) {
                event.setCancelled(true);
            } else {
                setFalseByPercent(event, SPAWN_CHANCE);
            }
        }
    }

    public void setFalseByPercent(CreatureSpawnEvent event, double chance) {
        if (Randomizer.nextDouble(100) >= chance) {
            event.setCancelled(true);
        }
    }
}
