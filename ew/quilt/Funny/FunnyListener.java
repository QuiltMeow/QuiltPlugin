package ew.quilt.Funny;

import ew.quilt.Config.ConfigManager;
import ew.quilt.plugin.Main;
import ew.quilt.util.ItemUtil;
import ew.quilt.util.Randomizer;
import ew.quilt.util.TimerUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class FunnyListener implements Listener {

    private static final int HUNGER_LEVEL = 2;
    private static final int FLASH_DISTANCE = 10;
    private static final int EXP_SPLIT = 100;

    private static final Map<String, Long> FIRE_COOL_TIME = new HashMap<>();
    private static final Map<String, Long> HUGE_DAMAGE_COOL_TIME = new HashMap<>();
    private static final Map<String, Long> FLASH_COOL_TIME = new HashMap<>();
    private static final Map<String, Long> HEAL_COOL_TIME = new HashMap<>();
    private static final Map<String, Long> GHOST_WALK_COOL_TIME = new HashMap<>();

    private static final Map<String, Long> FREEZE_COOL_TIME = new HashMap<>();
    private static final List<String> FREEZE_PLAYER = new ArrayList<>();

    @EventHandler
    public void onPlayerConsumeFood(PlayerItemConsumeEvent event) {
        ItemStack item = event.getItem();
        Player player = event.getPlayer();

        if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
            List<String> loreList = item.getItemMeta().getLore();
            for (String lore : loreList) {
                if (lore.equalsIgnoreCase("§C越吃越餓的食物")) {
                    int foodLevel = player.getFoodLevel() - HUNGER_LEVEL;
                    player.setFoodLevel(foodLevel < 0 ? 0 : foodLevel);

                    float saturation = player.getSaturation() - HUNGER_LEVEL;
                    player.setSaturation(saturation < 0 ? 0 : saturation);
                    ItemUtil.removeInventoryItem(player.getInventory(), item.getType(), 1);
                    event.setCancelled(true);
                    return;
                } else if (lore.equalsIgnoreCase("§C被下毒的")) {
                    ItemUtil.removeInventoryItem(player.getInventory(), item.getType(), 1);
                    player.setHealth(0);
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onDropExpBottle(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            ItemStack item = event.getItem();
            ItemStack itemCheck = player.getInventory().getItemInOffHand();
            if (item == null || item.isSimilar(itemCheck)) {
                return;
            }
            if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
                if (item.getType() == Material.BOOK) {
                    List<String> loreList = item.getItemMeta().getLore();
                    for (String lore : loreList) {
                        String[] splitted = lore.split(" : ");
                        if (splitted.length >= 2) {
                            if (splitted[0].contains("經驗值")) {
                                final int exp;
                                try {
                                    exp = Integer.parseInt(splitted[1]);
                                    if (exp < 0) {
                                        throw new NumberFormatException("經驗數值不得小於 0");
                                    }
                                } catch (NumberFormatException ex) {
                                    System.err.println("經驗值解析錯誤 : " + ex);
                                    continue;
                                }
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
                                        int currentExp = exp;
                                        while (currentExp > EXP_SPLIT) {
                                            ExperienceOrb orb = drop.getWorld().spawn(drop.getLocation(), ExperienceOrb.class);
                                            orb.setExperience(EXP_SPLIT);
                                            currentExp -= EXP_SPLIT;
                                        }
                                        ExperienceOrb orb = drop.getWorld().spawn(drop.getLocation(), ExperienceOrb.class);
                                        orb.setExperience(currentExp);
                                        drop.remove();
                                    }
                                }, TimerUtil.secondToTick(3));
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onFlashSkill(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            ItemStack item = event.getItem();
            ItemStack itemCheck = player.getInventory().getItemInOffHand();
            if (item == null || item.isSimilar(itemCheck)) {
                return;
            }
            if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
                if (item.getType() == Material.GOLD_INGOT) {
                    List<String> loreList = item.getItemMeta().getLore();
                    for (String lore : loreList) {
                        if (lore.equalsIgnoreCase("§B朝著面向的位置瞬間傳送一小段距離")) {
                            World world = player.getWorld();
                            Location location = player.getLocation();
                            Vector vector = location.getDirection().normalize().multiply(FLASH_DISTANCE);
                            Location flash = new Location(world, location.getX() + vector.getX(), location.getY(), location.getZ() + vector.getZ());
                            if (world.getBlockAt(flash).getType() == Material.AIR) {
                                String name = player.getName();
                                Long coolTime = FLASH_COOL_TIME.get(name);
                                long current = System.currentTimeMillis();
                                if (coolTime == null || current > coolTime + 300 * 1000) { // 300 秒
                                    player.teleport(flash);
                                    world.spawnParticle(Particle.LAVA, flash, 3);
                                    FLASH_COOL_TIME.put(name, current);
                                } else {
                                    long remaining = (coolTime + 300 * 1000 - current) / 1000;
                                    player.sendMessage(ChatColor.RED + "冷卻時間尚未結束 無法使用該技能 ! 剩餘冷卻時間 : " + remaining + " 秒");
                                }
                            }
                            return;
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onHealSkill(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            ItemStack item = event.getItem();
            ItemStack itemCheck = player.getInventory().getItemInOffHand();
            if (item == null || item.isSimilar(itemCheck)) {
                return;
            }
            if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
                if (item.getType() == Material.EMERALD) {
                    List<String> loreList = item.getItemMeta().getLore();
                    for (String lore : loreList) {
                        if (lore.equalsIgnoreCase("§B回復玩家生命")) {
                            String name = player.getName();
                            Long coolTime = HEAL_COOL_TIME.get(name);
                            long current = System.currentTimeMillis();
                            if (coolTime == null || current > coolTime + 240 * 1000) { // 240 秒
                                int randHealth = Randomizer.rand(3, 10);
                                if (player.getHealthScale() <= player.getHealth() + randHealth) {
                                    player.setHealth(player.getHealthScale());
                                } else {
                                    player.setHealth(player.getHealth() + randHealth);
                                }
                                player.getWorld().spawnParticle(Particle.TOTEM, player.getLocation(), 20);

                                final float speed = player.getWalkSpeed();
                                player.setWalkSpeed(speed + (float) 0.2);

                                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
                                    @Override
                                    public void run() {
                                        player.setWalkSpeed(speed);
                                    }
                                }, TimerUtil.secondToTick(3));
                                HEAL_COOL_TIME.put(name, current);
                            } else {
                                long remaining = (coolTime + 240 * 1000 - current) / 1000;
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
    public void onGhostWalkSkill(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            ItemStack item = event.getItem();
            ItemStack itemCheck = player.getInventory().getItemInOffHand();
            if (item == null || item.isSimilar(itemCheck)) {
                return;
            }
            if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
                if (item.getType() == Material.BLAZE_POWDER) {
                    List<String> loreList = item.getItemMeta().getLore();
                    for (String lore : loreList) {
                        if (lore.equalsIgnoreCase("§B提高玩家跑速")) {
                            String name = player.getName();
                            Long coolTime = GHOST_WALK_COOL_TIME.get(name);
                            long current = System.currentTimeMillis();
                            if (coolTime == null || current > coolTime + 210 * 1000) { // 210 秒
                                final float speed = player.getWalkSpeed();
                                player.setWalkSpeed(speed + (float) 0.15);

                                int effectTask = Main.getPlugin().getServer().getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
                                    @Override
                                    public void run() {
                                        player.getWorld().spawnParticle(Particle.FLAME, player.getLocation(), 10);
                                    }
                                }, 0, TimerUtil.secondToTick(1));

                                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
                                    @Override
                                    public void run() {
                                        player.setWalkSpeed(speed);
                                        Main.getPlugin().getServer().getScheduler().cancelTask(effectTask);
                                    }
                                }, TimerUtil.secondToTick(10));
                                GHOST_WALK_COOL_TIME.put(name, current);
                            } else {
                                long remaining = (coolTime + 210 * 1000 - current) / 1000;
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
    public void onLightningBow(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getDamager();
            if (arrow.getShooter() instanceof Player && event.getEntity() instanceof Player) {
                Player damager = (Player) arrow.getShooter();
                ItemStack item = damager.getInventory().getItemInMainHand();
                if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
                    if (item.getType() == Material.BOW) {
                        List<String> loreList = item.getItemMeta().getLore();
                        for (String lore : loreList) {
                            if (lore.equalsIgnoreCase("§E雷擊 I")) {
                                int chance = Randomizer.nextInt(100);
                                if (chance < 10) { // 10 %
                                    Player target = (Player) event.getEntity();
                                    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
                                        @Override
                                        public void run() {
                                            target.getWorld().strikeLightning(target.getLocation());
                                        }
                                    }, TimerUtil.secondToTick(1));
                                }
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onFreezeSword(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player damager = (Player) event.getDamager();
            ItemStack item = damager.getInventory().getItemInMainHand();
            if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
                if (ItemUtil.isSword(item.getType())) {
                    List<String> loreList = item.getItemMeta().getLore();
                    for (String lore : loreList) {
                        if (lore.equalsIgnoreCase("§E定身 I")) {
                            String name = damager.getName();
                            Long coolTime = FREEZE_COOL_TIME.get(name);
                            long current = System.currentTimeMillis();
                            if (coolTime == null || current > coolTime + 600 * 1000) { // 600 秒
                                Player target = (Player) event.getEntity();
                                FREEZE_PLAYER.add(target.getName());
                                FREEZE_COOL_TIME.put(name, current);
                                target.sendMessage(ChatColor.RED + "你被定身了 10 秒內無法移動 !");
                                damager.sendMessage(ChatColor.GREEN + "定身效果成功發動 !");

                                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
                                    @Override
                                    public void run() {
                                        FREEZE_PLAYER.remove(target.getName());
                                    }
                                }, TimerUtil.secondToTick(10));
                            }
                            return;
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (FREEZE_PLAYER.indexOf(player.getName()) != -1) {
            player.sendMessage(ChatColor.RED + "定身狀態下無法移動 !");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDamageByPlayer(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player damager = (Player) event.getDamager();
            ItemStack item = damager.getInventory().getItemInMainHand();
            if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
                List<String> loreList = item.getItemMeta().getLore();
                if (item.getType() == Material.FLINT_AND_STEEL) { // 點燃
                    for (String lore : loreList) {
                        if (lore.equalsIgnoreCase("§B點燃其他玩家")) {
                            String name = damager.getName();
                            Long coolTime = FIRE_COOL_TIME.get(name);
                            long current = System.currentTimeMillis();
                            if (coolTime == null || current > coolTime + 180 * 1000) { // 180 秒
                                Player target = (Player) event.getEntity();
                                target.setFireTicks(TimerUtil.secondToTick(Randomizer.rand(5, 20)));
                                target.sendMessage(ChatColor.RED + "你著火了 請盡速進行滅火 !");
                                FIRE_COOL_TIME.put(name, current);
                            } else {
                                long remaining = (coolTime + 180 * 1000 - current) / 1000;
                                damager.sendMessage(ChatColor.RED + "冷卻時間尚未結束 無法使用該技能 ! 剩餘冷卻時間 : " + remaining + " 秒");
                            }
                            return;
                        }
                    }
                } else if (item.getType() == Material.BLAZE_ROD) { // 重擊
                    for (String lore : loreList) {
                        if (lore.equalsIgnoreCase("§B對目標造成大量重擊傷害")) {
                            String name = damager.getName();
                            Long coolTime = HUGE_DAMAGE_COOL_TIME.get(name);
                            long current = System.currentTimeMillis();
                            if (coolTime == null || current > coolTime + 70 * 1000) { // 70 秒
                                event.setDamage(Randomizer.rand(10, 30));
                                Player target = (Player) event.getEntity();
                                target.getWorld().createExplosion(target.getLocation(), 0);
                                HUGE_DAMAGE_COOL_TIME.put(name, current);
                            } else {
                                long remaining = (coolTime + 180 * 1000 - current) / 1000;
                                damager.sendMessage(ChatColor.RED + "冷卻時間尚未結束 無法使用該技能 ! 剩餘冷卻時間 : " + remaining + " 秒");
                            }
                            return;
                        }
                    }
                }
            }
        }
    }
}
