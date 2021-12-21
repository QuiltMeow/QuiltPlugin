package ew.quilt.FastRespawn;

import ew.quilt.plugin.Main;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class FastRespawnListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        World world = player.getWorld();
        Location location = player.getLocation();
        world.playEffect(location, Effect.PARTICLE_SMOKE, 100, 100);
        Main.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
            @Override
            public void run() {
                player.spigot().respawn();
                player.setFoodLevel(20);
                player.setHealth(20);
                player.setExhaustion(4);
                player.setSaturation(20);
                player.setFireTicks(0);
            }
        }, 4);
    }
}
