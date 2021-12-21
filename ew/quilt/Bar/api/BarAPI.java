package ew.quilt.Bar.api;

import ew.quilt.Bar.api.nms.*;
import ew.quilt.plugin.Main;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import java.util.HashMap;
import java.util.UUID;

public class BarAPI implements Listener {

    private static HashMap<UUID, FakeDragon> players = new HashMap<UUID, FakeDragon>();
    private static HashMap<UUID, Integer> timers = new HashMap<UUID, Integer>();

    private static boolean useSpigotHack = false;

    public static boolean useSpigotHack() {
        return useSpigotHack;
    }

    public BarAPI() {
        if (!useSpigotHack) {
            if (v1_8Fake.isUsable()) {
                useSpigotHack = true;
                BarUtil.detectVersion();
                Main.getPlugin().getLogger().info("偵測可使用 Spigot Hack 並已啟用 1.8 Fake");
            }
        }

        if (useSpigotHack) {
            Main.getPlugin().getServer().getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
                @Override
                public void run() {
                    for (UUID uuid : players.keySet()) {
                        Player player = Bukkit.getPlayer(uuid);
                        BarUtil.sendPacket(player, players.get(uuid).getTeleportPacket(getDragonLocation(player.getLocation())));
                    }
                }
            }, 0, 5);
        }
    }

    @Deprecated
    public static void setMessage(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            setMessage(player, message);
        }
    }

    @Deprecated
    public static void setMessage(Player player, String message) {
        if (hasBar(player)) {
            removeBar(player);
        }
        FakeDragon dragon = getDragon(player, message);
        dragon.name = cleanMessage(message);
        dragon.health = dragon.getMaxHealth();

        cancelTimer(player);
        sendDragon(dragon, player);
    }

    @Deprecated
    public static void setMessage(String message, float percent) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            setMessage(player, message, percent);
        }
    }

    @Deprecated
    public static void setMessage(Player player, String message, float percent) {
        Validate.isTrue(0F <= percent && percent <= 100F, "百分比數值必須在 0 ~ 100 之間 目前數值 : ", percent);

        if (hasBar(player)) {
            removeBar(player);
        }

        FakeDragon dragon = getDragon(player, message);
        dragon.name = cleanMessage(message);
        dragon.health = (percent / 100f) * dragon.getMaxHealth();

        cancelTimer(player);
        sendDragon(dragon, player);
    }

    @Deprecated
    public static void setMessage(String message, int seconds) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            setMessage(player, message, seconds);
        }
    }

    @Deprecated
    public static void setMessage(final Player player, String message, int seconds) {
        Validate.isTrue(seconds > 0, "秒數必須在 1 秒以上 目前數值 : ", seconds);

        if (hasBar(player)) {
            removeBar(player);
        }

        FakeDragon dragon = getDragon(player, message);
        dragon.name = cleanMessage(message);
        dragon.health = dragon.getMaxHealth();

        final float dragonHealthMinus = dragon.getMaxHealth() / seconds;
        cancelTimer(player);
        timers.put(player.getUniqueId(), Bukkit.getScheduler().runTaskTimer(Main.getPlugin(), new Runnable() {
            @Override
            public void run() {
                FakeDragon drag = getDragon(player, "");
                drag.health -= dragonHealthMinus;
                if (drag.health <= 1) {
                    removeBar(player);
                    cancelTimer(player);
                } else {
                    sendDragon(drag, player);
                }
            }
        }, 20, 20).getTaskId());
        sendDragon(dragon, player);
    }

    @Deprecated
    public static boolean hasBar(Player player) {
        return players.get(player.getUniqueId()) != null;
    }

    @Deprecated
    public static void removeBar(Player player) {
        if (!hasBar(player)) {
            return;
        }

        FakeDragon dragon = getDragon(player, "");
        if (dragon instanceof v1_9) {
            ((v1_9) dragon).getBar().removePlayer(player);
        } else {
            BarUtil.sendPacket(player, getDragon(player, "").getDestroyPacket());
        }

        players.remove(player.getUniqueId());
        cancelTimer(player);
    }

    @Deprecated
    public static void setHealth(Player player, float percent) {
        if (!hasBar(player)) {
            return;
        }

        FakeDragon dragon = getDragon(player, "");
        dragon.health = (percent / 100f) * dragon.getMaxHealth();

        cancelTimer(player);
        if (percent == 0) {
            removeBar(player);
        } else {
            sendDragon(dragon, player);
        }
    }

    @Deprecated
    public static float getHealth(Player player) {
        if (!hasBar(player)) {
            return -1;
        }
        return getDragon(player, "").health;
    }

    @Deprecated
    public static String getMessage(Player player) {
        if (!hasBar(player)) {
            return "";
        }
        return getDragon(player, "").name;
    }

    private static String cleanMessage(String message) {
        if (message.length() > 64) {
            message = message.substring(0, 63);
        }
        return message;
    }

    private static void cancelTimer(Player player) {
        Integer timerID = timers.remove(player.getUniqueId());

        if (timerID != null) {
            Bukkit.getScheduler().cancelTask(timerID);
        }
    }

    private static void sendDragon(FakeDragon dragon, Player player) {
        if (dragon instanceof v1_9) {
            BossBar bar = ((v1_9) dragon).getBar();
            bar.addPlayer(player);
            bar.setProgress(dragon.health / dragon.getMaxHealth());
        } else {
            BarUtil.sendPacket(player, dragon.getMetaPacket(dragon.getWatcher()));
            BarUtil.sendPacket(player, dragon.getTeleportPacket(getDragonLocation(player.getLocation())));
        }
    }

    private static FakeDragon getDragon(Player player, String message) {
        if (hasBar(player)) {
            return players.get(player.getUniqueId());
        } else {
            return addDragon(player, cleanMessage(message));
        }
    }

    private static FakeDragon addDragon(Player player, String message) {
        FakeDragon dragon = BarUtil.newDragon(message, getDragonLocation(player.getLocation()));
        if (dragon instanceof v1_9) {
            BossBar bar = ((v1_9) dragon).getBar();
            bar.addPlayer(player);
        } else {
            BarUtil.sendPacket(player, dragon.getSpawnPacket());
        }

        players.put(player.getUniqueId(), dragon);
        return dragon;
    }

    private static FakeDragon addDragon(Player player, Location loc, String message) {
        FakeDragon dragon = BarUtil.newDragon(message, getDragonLocation(loc));
        if (dragon instanceof v1_9) {
            BossBar bar = ((v1_9) dragon).getBar();
            bar.addPlayer(player);
        } else {
            BarUtil.sendPacket(player, dragon.getSpawnPacket());
        }

        players.put(player.getUniqueId(), dragon);
        return dragon;
    }

    private static Location getDragonLocation(Location loc) {
        if (BarUtil.isBelowGround) {
            loc.subtract(0, 300, 0);
            return loc;
        }

        float pitch = loc.getPitch();
        if (pitch >= 55) {
            loc.add(0, -300, 0);
        } else if (pitch <= -55) {
            loc.add(0, 300, 0);
        } else {
            loc = loc.getBlock().getRelative(getDirection(loc), Main.getPlugin().getServer().getViewDistance() * 16).getLocation();
        }
        return loc;
    }

    private static BlockFace getDirection(Location loc) {
        float dir = Math.round(loc.getYaw() / 90);
        if (dir == -4 || dir == 0 || dir == 4) {
            return BlockFace.SOUTH;
        }
        if (dir == -1 || dir == 3) {
            return BlockFace.EAST;
        }
        if (dir == -2 || dir == 2) {
            return BlockFace.NORTH;
        }
        if (dir == -3 || dir == 1) {
            return BlockFace.WEST;
        }
        return null;
    }

    public static void disable() {
        for (Player player : Main.getPlugin().getServer().getOnlinePlayers()) {
            quit(player);
        }
        players.clear();

        for (int timerID : timers.values()) {
            Bukkit.getScheduler().cancelTask(timerID);
        }
        timers.clear();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void PlayerLoggout(PlayerQuitEvent event) {
        quit(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerKick(PlayerKickEvent event) {
        quit(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerTeleport(final PlayerTeleportEvent event) {
        handleTeleport(event.getPlayer(), event.getTo().clone());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerTeleport(final PlayerRespawnEvent event) {
        handleTeleport(event.getPlayer(), event.getRespawnLocation().clone());
    }

    private void handleTeleport(final Player player, final Location loc) {
        if (!hasBar(player)) {
            return;
        }

        final FakeDragon oldDragon = getDragon(player, "");
        if (oldDragon instanceof v1_9) {
            return;
        }

        Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new Runnable() {
            @Override
            public void run() {
                if (!hasBar(player)) {
                    return;
                }
                float health = oldDragon.health;
                String message = oldDragon.name;
                BarUtil.sendPacket(player, getDragon(player, "").getDestroyPacket());

                players.remove(player.getUniqueId());
                FakeDragon dragon = addDragon(player, loc, message);
                dragon.health = health;
                sendDragon(dragon, player);
            }
        }, 2);
    }

    private static void quit(Player player) {
        removeBar(player);
    }
}
