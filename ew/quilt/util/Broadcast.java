package ew.quilt.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Broadcast {

    public static void broadcastOPMessage(Player source, String message, boolean repeatToSource) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.isOp() && (source != player || repeatToSource)) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            }
        }
    }

    public static void broadcastNonOPMessage(Player source, String message, boolean repeatToSource) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.isOp() && (source != player || repeatToSource)) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            }
        }
    }

    public static void broadcastPermissionMessage(Player source, String permission, String message, boolean repeatToSource) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission(permission) && (source != player || repeatToSource)) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            }
        }
    }

    public static void broadcastNonPermissionMessage(Player source, String permission, String message, boolean repeatToSource) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.hasPermission(permission) && (source != player || repeatToSource)) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            }
        }
    }

    public static void broadcastMessage(Player source, String message, boolean repeatToSource, double distance, Location from) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            double playerDistance;
            try {
                playerDistance = player.getLocation().distance(from);
            } catch (IllegalArgumentException ex) {
                continue;
            }
            if (playerDistance <= distance && (source != player || repeatToSource)) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            }
        }
    }
}
