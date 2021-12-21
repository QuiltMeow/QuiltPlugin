package ew.quilt.Elevator;

import ew.quilt.plugin.Main;
import ew.quilt.util.TimerUtil;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class ElevatorListener implements Listener {

    private static final Material ELEVATOR_MATERIAL = Material.IRON_BLOCK;

    private static final int MIN_GAP = 2;
    private static final int MAX_GAP = 254;

    private static final boolean PLAY_SOUND = true;

    private static final boolean DELAY = true;
    private static final int DELAY_SECOND = 1;

    private static final Map<String, Location> PLAYER_WAITING_ELEVATOR = new HashMap<>();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        if (PLAYER_WAITING_ELEVATOR.containsKey(name)) {
            return;
        }
        if (event.getTo().getY() <= event.getFrom().getY()) {
            return;
        }
        if (!checkEssential(player)) {
            return;
        }
        for (int i = 0; i < MIN_GAP; ++i) {
            if (player.getLocation().add(0, i, 0).getBlock().getType().isSolid()) {
                return;
            }
        }

        int finalAddIndex = -1;
        for (int i = MIN_GAP; i <= MAX_GAP;) {
            if (player.getLocation().add(0, i, 0).getBlockY() > 254) {
                return;
            }
            if (!player.getLocation().add(0, i, 0).getBlock().getType().isSolid()) {
                ++i;
            } else {
                if (player.getLocation().add(0, i, 0).getBlock().getType() == ELEVATOR_MATERIAL) {
                    if (player.getLocation().add(0, i + 1, 0).getBlock().getType() == Material.AIR && player.getLocation().add(0, i + 2, 0).getBlock().getType() == Material.AIR) {
                        finalAddIndex = i;
                        break;
                    }
                    return;
                }
                return;
            }
        }

        if (finalAddIndex != -1) {
            PLAYER_WAITING_ELEVATOR.put(name, player.getLocation());
            PLAYER_WAITING_ELEVATOR.put(name, player.getLocation());
            if (DELAY) {
                delayTeleport(name, finalAddIndex + 1, true);
            } else {
                teleportPlayer(name, finalAddIndex + 1, true);
            }
        }
    }

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        if (PLAYER_WAITING_ELEVATOR.containsKey(name)) {
            return;
        }
        if (!checkEssential(player)) {
            return;
        }
        if (player.isSneaking()) {
            return;
        }
        for (int i = 0; i < MIN_GAP; ++i) {
            if (player.getLocation().subtract(0, 2 + i, 0).getBlock().getType().isSolid()) {
                return;
            }
        }

        int finalSubtract = -1;
        for (int i = MIN_GAP + 2; i <= MAX_GAP + 2;) {
            if (player.getLocation().subtract(0, i, 0).getBlockY() < 1) {
                return;
            }
            if (!player.getLocation().subtract(0, i, 0).getBlock().getType().isSolid()) {
                ++i;
            } else {
                if (player.getLocation().subtract(0, i, 0).getBlock().getType() == ELEVATOR_MATERIAL) {
                    finalSubtract = i;
                    break;
                }
                return;
            }
        }

        if (finalSubtract != -1) {
            PLAYER_WAITING_ELEVATOR.put(name, player.getLocation());
            if (DELAY) {
                delayTeleport(name, finalSubtract - 1, false);
            } else {
                teleportPlayer(name, finalSubtract - 1, false);
            }
        }
    }

    public void playSound(Player player) {
        if (PLAY_SOUND) {
            Sound teleportingSound = Sound.ENTITY_ENDERMEN_TELEPORT;
            player.getWorld().playSound(player.getLocation(), teleportingSound, 1, 0);
        }
    }

    public void teleportPlayer(String name, int destY, boolean up) {
        Player player = Bukkit.getPlayer(name);
        if (player != null) {
            if (up) {
                player.teleport(PLAYER_WAITING_ELEVATOR.get(name).add(0, destY, 0));
            } else {
                player.teleport(PLAYER_WAITING_ELEVATOR.get(name).subtract(0, destY, 0));
            }
            playSound(player);
        }
        PLAYER_WAITING_ELEVATOR.remove(name);
    }

    public void delayTeleport(final String name, final int destY, final boolean up) {
        Player player = Bukkit.getPlayer(name);
        if (player != null) {
            player.sendMessage(ChatColor.YELLOW + "電梯啟動中 請稍後 " + DELAY_SECOND + " 秒 ...");
        }
        Bukkit.getServer().getScheduler().runTaskLater(Main.getPlugin(), new Runnable() {
            @Override
            public void run() {
                teleportPlayer(name, destY, up);
            }
        }, TimerUtil.secondToTick(DELAY_SECOND));
    }

    public boolean checkEssential(Player player) {
        if (ELEVATOR_MATERIAL == null) {
            return false;
        }
        if (MIN_GAP >= MAX_GAP) {
            return false;
        }
        Block standingBlock = player.getLocation().subtract(0, 1, 0).getBlock();
        return standingBlock.getType() == ELEVATOR_MATERIAL;
    }
}
