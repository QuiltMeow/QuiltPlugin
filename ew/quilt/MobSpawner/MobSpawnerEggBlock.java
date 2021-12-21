package ew.quilt.MobSpawner;

import ew.quilt.plugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class MobSpawnerEggBlock implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getPlayer().hasPermission("quilt.mob.spawner.egg")) {
            return;
        }
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }

        Block block = event.getClickedBlock();
        if (block == null) {
            return;
        }
        if (block.getType() != Material.MOB_SPAWNER) {
            return;
        }

        ItemStack item = event.getItem();
        if (item == null) {
            return;
        }
        if (item.getType() != Material.MONSTER_EGG && item.getType() != Material.MONSTER_EGGS) {
            return;
        }

        CreatureSpawner cs = (CreatureSpawner) block.getState();
        final Location location = cs.getLocation();
        final EntityType type = cs.getSpawnedType();
        event.setCancelled(true);

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
            @Override
            public void run() {
                Block block = location.getBlock();
                if (block == null || block.getType() != Material.MOB_SPAWNER) {
                    return;
                }
                CreatureSpawner cs = (CreatureSpawner) block.getState();
                cs.setSpawnedType(type);
            }
        });
    }
}
