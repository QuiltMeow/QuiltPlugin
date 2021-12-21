package ew.quilt.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class UUIDCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("quilt.uuid")) {
            sender.sendMessage(ChatColor.RED + "很抱歉 你沒有使用指令權限 !");
            return false;
        }
        if (args.length <= 0) {
            sender.sendMessage(ChatColor.RED + "指令使用方法 : /uuid [玩家 ID]");
            return false;
        }
        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
        if (player == null) {
            sender.sendMessage(ChatColor.RED + "目標玩家沒有登入伺服器紀錄 !");
            return false;
        }
        sender.sendMessage(ChatColor.GREEN + "玩家 " + player.getName() + " UUID " + player.getUniqueId());
        return true;
    }
}
