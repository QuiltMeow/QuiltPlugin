package ew.quilt.Funny;

import ew.quilt.Config.ConfigManager;
import ew.quilt.plugin.Main;
import ew.quilt.util.TimerUtil;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class BombListener implements Listener {

    private static final boolean FIRE_BLOCK = false;
    private static final boolean BREAK_BLOCK = true;

    @EventHandler
    public void onDropBomb(PlayerInteractEvent event) {
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
                    Float radius = null;
                    Integer time = 4; // 4 秒 TNT 爆炸時間
                    for (String lore : loreList) {
                        String[] splitted = lore.split(" : ");
                        if (splitted.length >= 2) {
                            if (splitted[0].contains("爆炸半徑")) {
                                try {
                                    radius = Float.parseFloat(splitted[1]);
                                    if (radius < 0) {
                                        throw new NumberFormatException("爆炸半徑不得小於 0");
                                    }
                                } catch (NumberFormatException ex) {
                                    System.err.println("爆炸半徑解析錯誤 : " + ex);
                                }
                            } else if (splitted[0].contains("爆炸時間")) {
                                try {
                                    time = Integer.parseInt(splitted[1]);
                                    if (time < 0) {
                                        throw new NumberFormatException("爆炸時間不得小於 0");
                                    }
                                } catch (NumberFormatException ex) {
                                    System.err.println("爆炸時間解析錯誤 : " + ex);
                                }
                            }
                        }
                    }
                    if (radius != null) {
                        Location location = player.getLocation();

                        ItemStack toDrop = new ItemStack(item);
                        toDrop.setAmount(1);
                        Item drop = player.getWorld().dropItem(location, toDrop);
                        drop.setPickupDelay(Integer.MAX_VALUE);
                        drop.setVelocity(location.getDirection().normalize().multiply(ConfigManager.DIRECTION_SPEED));

                        item.setAmount(item.getAmount() - 1);
                        final float radiusFinal = radius;
                        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
                            @Override
                            public void run() {
                                Location dropLocation = drop.getLocation();
                                drop.getWorld().createExplosion(dropLocation.getX(), dropLocation.getY(), dropLocation.getZ(), radiusFinal, FIRE_BLOCK, BREAK_BLOCK);
                                drop.remove();
                            }
                        }, TimerUtil.secondToTick(time));
                    }
                }
            }
        }
    }
}
