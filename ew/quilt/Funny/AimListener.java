package ew.quilt.Funny;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class AimListener implements Listener {

    private static final Map<String, Long> FIRE_BALL_COOL_TIME = new HashMap<>();
    private static final Map<String, Long> WITHER_SKULL_COOL_TIME = new HashMap<>();

    @EventHandler
    public void onWitherAim(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            ItemStack item = event.getItem();
            ItemStack itemCheck = player.getInventory().getItemInOffHand();
            if (item == null || item.isSimilar(itemCheck)) {
                return;
            }
            if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
                if (item.getType() == Material.SULPHUR) {
                    List<String> loreList = item.getItemMeta().getLore();
                    for (String lore : loreList) {
                        if (lore.equalsIgnoreCase("§B朝著面向的位置發射凋零頭顱")) {
                            String name = player.getName();
                            Long coolTime = WITHER_SKULL_COOL_TIME.get(name);
                            long current = System.currentTimeMillis();
                            if (coolTime == null || current > coolTime + 120 * 1000) { // 120 秒
                                WitherSkull wither = (WitherSkull) player.getWorld().spawnEntity(player.getEyeLocation(), EntityType.WITHER_SKULL);
                                wither.setShooter(player);
                                WITHER_SKULL_COOL_TIME.put(name, current);
                            } else {
                                long remaining = (coolTime + 120 * 1000 - current) / 1000;
                                player.sendMessage(ChatColor.RED + "冷卻時間尚未結束 無法使用該技能 ! 剩餘冷卻時間 : " + remaining + " 秒");
                            }
                            return;
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onFireBallAim(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            ItemStack item = event.getItem();
            ItemStack itemCheck = player.getInventory().getItemInOffHand();
            if (item == null || item.isSimilar(itemCheck)) {
                return;
            }
            if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
                if (item.getType() == Material.SLIME_BALL) {
                    List<String> loreList = item.getItemMeta().getLore();
                    for (String lore : loreList) {
                        if (lore.equalsIgnoreCase("§B朝著面向的位置發射火球")) {
                            String name = player.getName();
                            Long coolTime = FIRE_BALL_COOL_TIME.get(name);
                            long current = System.currentTimeMillis();
                            if (coolTime == null || current > coolTime + 60 * 1000) { // 60 秒
                                Fireball fireBall = (Fireball) player.getWorld().spawnEntity(player.getEyeLocation(), EntityType.FIREBALL);
                                fireBall.setShooter(player);
                                FIRE_BALL_COOL_TIME.put(name, current);
                            } else {
                                long remaining = (coolTime + 60 * 1000 - current) / 1000;
                                player.sendMessage(ChatColor.RED + "冷卻時間尚未結束 無法使用該技能 ! 剩餘冷卻時間 : " + remaining + " 秒");
                            }
                            return;
                        }
                    }
                }
            }
        }
    }
}
