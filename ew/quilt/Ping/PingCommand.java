package ew.quilt.Ping;

import ew.quilt.Config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PingCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 1) { // 自身 Ping
            if (sender instanceof Player) {
                if (sender.hasPermission("quilt.ping")) {
                    sender.sendMessage("§3[網路延遲] §F你的網路延遲 §7" + getPing((Player) sender) + " 毫秒");
                } else {
                    sender.sendMessage("§C權限不足 無法使用該指令");
                    return false;
                }
            } else {
                sender.sendMessage("§C該指令必須在遊戲內由玩家使用");
                return false;
            }
        } else if (sender.hasPermission("quilt.ping.other")) { // 取得其他玩家 Ping
            boolean found = false;
            for (Player player : Bukkit.getOnlinePlayers()) {
                String name = player.getName();
                if (name.equalsIgnoreCase(args[0])) {
                    sender.sendMessage("§3[網路延遲] §F玩家 §7" + name + " §F的網路延遲 §7" + getPing(player) + " 毫秒");
                    found = true;
                    break;
                }
            }
            if (found != true) {
                sender.sendMessage("§C找不到目標玩家 \"" + args[0] + "\"");
                return false;
            }
        } else {
            sender.sendMessage("§C權限不足 無法使用該指令");
            return false;
        }
        return true;
    }

    public static int getPing(Player player) {
        try {
            Object ePlayer = Class.forName("org.bukkit.craftbukkit." + ConfigManager.PACKAGE_VERSION + ".entity.CraftPlayer").getMethod("getHandle", new Class[0]).invoke(player, new Object[0]);
            return ((Integer) ePlayer.getClass().getDeclaredField("ping").get(ePlayer));
        } catch (Exception ex) {
            System.err.println("取得網路延遲時發生例外狀況 : " + ex);
        }
        return 0;
    }
}
