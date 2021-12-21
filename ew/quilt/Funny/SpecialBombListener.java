package ew.quilt.Funny;

import ew.quilt.Config.ConfigManager;
import ew.quilt.plugin.Main;
import ew.quilt.util.TimerUtil;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class SpecialBombListener implements Listener {

    private static final int EACH_NUCLEAR_EXPLODE_POWER = 30;

    @EventHandler
    public void onDropNuclearBomb(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            ItemStack item = event.getItem();
            ItemStack itemCheck = player.getInventory().getItemInOffHand();
            if (item == null || item.isSimilar(itemCheck)) {
                return;
            }
            if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
                if (item.getType() == Material.APPLE) {
                    List<String> loreList = item.getItemMeta().getLore();
                    for (String lore : loreList) {
                        if (lore.equalsIgnoreCase("§C核彈")) {
                            Location location = player.getLocation();

                            ItemStack toDrop = new ItemStack(item);
                            toDrop.setAmount(1);
                            Item drop = player.getWorld().dropItem(location, toDrop);
                            drop.setPickupDelay(Integer.MAX_VALUE);
                            drop.setVelocity(location.getDirection().normalize().multiply(ConfigManager.DIRECTION_SPEED));

                            item.setAmount(item.getAmount() - 1);
                            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
                                @Override
                                public void run() {
                                    World world = drop.getWorld();
                                    Location dropLocation = drop.getLocation();
                                    List<Location> explode = new ArrayList<>();
                                    for (int i = -14; i <= 14; ++i) {
                                        double x = dropLocation.getX() + (i * 5);
                                        for (int j = -14; j <= 14; ++j) {
                                            double z = dropLocation.getZ() + (j * 5);
                                            for (int k = -6; k <= 6; ++k) {
                                                double y = dropLocation.getY() + (k * 5);
                                                Location toAdd = new Location(world, x, y, z);
                                                explode.add(toAdd);
                                            }
                                        }
                                    }
                                    drop.remove();

                                    final int[] info = {-1, 0};
                                    info[0] = Main.getPlugin().getServer().getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
                                        @Override
                                        public void run() {
                                            Location toExplode = explode.get(info[1]++);
                                            if (toExplode == null) {
                                                Bukkit.getScheduler().cancelTask(info[0]);
                                                return;
                                            }
                                            world.createExplosion(toExplode, EACH_NUCLEAR_EXPLODE_POWER);
                                        }
                                    }, 0, 3);
                                }
                            }, TimerUtil.secondToTick(8));
                            return;
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDropDeathBomb(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            ItemStack item = event.getItem();
            ItemStack itemCheck = player.getInventory().getItemInOffHand();
            if (item == null || item.isSimilar(itemCheck)) {
                return;
            }
            if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
                if (item.getType() == Material.APPLE) {
                    List<String> loreList = item.getItemMeta().getLore();
                    for (String lore : loreList) {
                        if (lore.equalsIgnoreCase("§C死亡炸彈")) {
                            Location location = player.getLocation();

                            ItemStack toDrop = new ItemStack(item);
                            toDrop.setAmount(1);
                            Item drop = player.getWorld().dropItem(location, toDrop);
                            drop.setPickupDelay(Integer.MAX_VALUE);
                            drop.setVelocity(location.getDirection().normalize().multiply(ConfigManager.DIRECTION_SPEED));

                            item.setAmount(item.getAmount() - 1);
                            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
                                @Override
                                public void run() {
                                    World world = drop.getWorld();
                                    List<Entity> entityList = drop.getNearbyEntities(40, 25, 40);
                                    for (Entity entity : entityList) {
                                        if (entity instanceof Damageable) {
                                            world.createExplosion(entity.getLocation(), 0);
                                            ((Damageable) entity).setHealth(0);
                                        }
                                    }
                                }
                            }, TimerUtil.secondToTick(6));
                            return;
                        }
                    }
                }
            }
        }
    }
}
