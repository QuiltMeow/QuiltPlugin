package ew.quilt.AdminChat;

import ew.quilt.Config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminChatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (sender.hasPermission("quilt.admin.chat.use")) {
            if (args.length == 0) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "該指令必須在遊戲內由玩家使用");
                    return false;
                }
                if (AdminChat.getOPChatPlayer().contains(sender.getName())) {
                    AdminChat.getOPChatPlayer().remove(sender.getName());
                    AdminChat.getAdminChatPlayer().add(sender.getName());

                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', AdminChat.getOPToggleMessage().replaceAll("%toggle%", "關閉")));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', AdminChat.getToggleMessage().replaceAll("%toggle%", "開啟")));
                    return true;
                } else {
                    if (AdminChat.getAdminChatPlayer().contains(sender.getName())) {
                        AdminChat.getAdminChatPlayer().remove(sender.getName());
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', AdminChat.getToggleMessage().replaceAll("%toggle%", "關閉")));
                        return true;
                    }
                    AdminChat.getAdminChatPlayer().add(sender.getName());
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', AdminChat.getToggleMessage().replaceAll("%toggle%", "開啟")));
                    return true;
                }
            }
            String chat = "";
            for (int i = 0; i < args.length; i++) {
                chat = chat.concat(args[i]);
                chat = chat.concat(" ");
            }
            for (Player admin : Bukkit.getOnlinePlayers()) {
                String format = AdminChat.getAdminChatFormat().replaceAll("%player%", sender.getName()).replaceAll("%message%", chat).replaceAll("%double_arrow%", "»");
                if (admin.hasPermission("quilt.admin.chat") || ConfigManager.isAuthorSender(admin)) {
                    admin.sendMessage(ChatColor.translateAlternateColorCodes('&', format));
                    if (AdminChat.shouldPlaySound()) {
                        try {
                            admin.playSound(admin.getLocation(), Sound.valueOf("NOTE_PLING"), 1, 1);
                        } catch (IllegalArgumentException ex) {
                            admin.playSound(admin.getLocation(), Sound.BLOCK_NOTE_PLING, 1, 1);
                        }
                    }
                }
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', AdminChat.getNoAdminPermissionMessage()));
            return false;
        }
        return true;
    }
}
