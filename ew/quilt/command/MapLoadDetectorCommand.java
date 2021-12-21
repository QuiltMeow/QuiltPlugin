package ew.quilt.command;

import ew.quilt.protect.MapMaliciousLoadDetector;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MapLoadDetectorCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("quilt.map.load.info")) {
            sender.sendMessage(ChatColor.RED + "權限不足 無法使用該指令 ...");
            return false;
        }

        MapMaliciousLoadDetector.sendCurrentInfo(sender);
        return true;
    }
}
