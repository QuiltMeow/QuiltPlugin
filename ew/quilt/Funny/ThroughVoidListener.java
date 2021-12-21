package ew.quilt.Funny;

import ew.quilt.Config.ConfigManager;
import ew.quilt.plugin.Main;
import ew.quilt.util.TimerUtil;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ThroughVoidListener implements Listener {

    @EventHandler
    public void onThroughVoid(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            ItemStack item = event.getItem();
            ItemStack itemCheck = player.getInventory().getItemInOffHand();
            if (item == null || item.isSimilar(itemCheck)) {
                return;
            }
            if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
                if (item.getType() == Material.DIAMOND) {
                    List<String> loreList = item.getItemMeta().getLore();
                    for (String lore : loreList) {
                        if (lore.equalsIgnoreCase("§C直達地心")) {
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
                                    Location dropLocation = drop.getLocation();
                                    World world = drop.getWorld();
                                    drop.remove();

                                    boolean first = true;
                                    for (double y = dropLocation.getY(); y > 5; y -= 5) {
                                        if (first) {
                                            world.createExplosion(dropLocation.getX(), y, dropLocation.getZ(), 10, false, true);
                                            first = false;
                                        } else {
                                            world.createExplosion(dropLocation.getX(), y, dropLocation.getZ(), 30, false, true);
                                        }
                                    }
                                    world.createExplosion(dropLocation.getX(), 5, dropLocation.getZ(), 30, false, true);
                                }
                            }, TimerUtil.secondToTick(4));
                            return;
                        }
                    }
                }
            }
        }
    }
}
