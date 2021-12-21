package ew.quilt.AdminChat;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AdminChat implements Listener {

    private static final boolean PLAY_SOUND = false;

    private static final String ADMIN_FORMAT = "&C『管理員頻道』&A%player% &E%double_arrow% &B%message%";
    private static final String OP_FORMAT = "&C『OP 頻道』&A%player% &E%double_arrow% &B%message%";
    private static final String ADMIN_TOGGLE_CHAT = "管理員頻道啟用狀態 : &5%toggle%";
    private static final String OP_TOGGLE_CHAT = "OP 頻道啟用狀態 : &5%toggle%";
    private static final String NO_ADMIN_PERMISSION = "&C你沒有權限使用管理員頻道";
    private static final String NO_OP_PERMISSION = "&C你沒有權限使用 OP 頻道";

    private static final ArrayList<String> ADMIN_CHAT = new ArrayList<>();
    private static final ArrayList<String> OP_CHAT = new ArrayList<>();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (OP_CHAT.contains(event.getPlayer().getName())) {
            String format = OP_FORMAT.replaceAll("%player%", event.getPlayer().getName()).replaceAll("%message%", event.getMessage()).replaceAll("%double_arrow%", "»");
            for (Player admin : Bukkit.getOnlinePlayers()) {
                if (admin.isOp()) {
                    admin.sendMessage(ChatColor.translateAlternateColorCodes('&', format));
                    if (PLAY_SOUND) {
                        try {
                            admin.playSound(admin.getLocation(), Sound.valueOf("NOTE_PLING"), 1, 1);
                        } catch (IllegalArgumentException ex) {
                            admin.playSound(admin.getLocation(), Sound.BLOCK_NOTE_PLING, 1, 1);
                        }
                    }
                }
            }
            event.setCancelled(true);
        } else if (ADMIN_CHAT.contains(event.getPlayer().getName())) {
            String format = ADMIN_FORMAT.replaceAll("%player%", event.getPlayer().getName()).replaceAll("%message%", event.getMessage()).replaceAll("%double_arrow%", "»");
            for (Player admin : Bukkit.getOnlinePlayers()) {
                if (admin.isOp() || admin.hasPermission("quilt.admin.chat")) {
                    admin.sendMessage(ChatColor.translateAlternateColorCodes('&', format));
                    if (PLAY_SOUND) {
                        try {
                            admin.playSound(admin.getLocation(), Sound.valueOf("NOTE_PLING"), 1, 1);
                        } catch (IllegalArgumentException ex) {
                            admin.playSound(admin.getLocation(), Sound.BLOCK_NOTE_PLING, 1, 1);
                        }
                    }
                }
            }
            event.setCancelled(true);
        }
    }

    public static List<String> getAdminChatPlayer() {
        return ADMIN_CHAT;
    }

    public static List<String> getOPChatPlayer() {
        return OP_CHAT;
    }

    public static boolean shouldPlaySound() {
        return PLAY_SOUND;
    }

    public static String getToggleMessage() {
        return ADMIN_TOGGLE_CHAT;
    }

    public static String getOPToggleMessage() {
        return OP_TOGGLE_CHAT;
    }

    public static String getAdminChatFormat() {
        return ADMIN_FORMAT;
    }

    public static String getOPChatFormat() {
        return OP_FORMAT;
    }

    public static String getNoAdminPermissionMessage() {
        return NO_ADMIN_PERMISSION;
    }

    public static String getNoOPPermissionMessage() {
        return NO_OP_PERMISSION;
    }
}
