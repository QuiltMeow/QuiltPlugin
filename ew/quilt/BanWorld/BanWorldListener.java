package ew.quilt.BanWorld;

import ew.quilt.Config.ConfigManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class BanWorldListener implements Listener {

    private static final List<String> BAN_LIST = new ArrayList<>(Arrays.asList(new String[]{ConfigManager.ADMIN_WORLD}));

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        String worldName = event.getTo().getWorld().getName();
        if (BAN_LIST.contains(worldName)) {
            Player player = event.getPlayer();
            if (!player.hasPermission("quilt.world.enter")) {
                player.sendMessage(ChatColor.RED + "你沒有前往 " + worldName + " 世界的權限");
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPortal(PlayerPortalEvent event) {
        Location to = event.getTo();
        if (to == null) {
            return;
        }
        String worldName = to.getWorld().getName();
        if (BAN_LIST.contains(worldName)) {
            Player player = event.getPlayer();
            if (!player.hasPermission("quilt.world.enter")) {
                player.sendMessage(ChatColor.RED + "你沒有前往 " + worldName + " 世界的權限");
                event.setCancelled(true);
            }
        }
    }
}
