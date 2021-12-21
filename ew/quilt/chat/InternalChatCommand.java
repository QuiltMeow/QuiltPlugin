package ew.quilt.chat;

import ew.quilt.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class InternalChatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 1) {
            return false;
        }
        String name = sender.getName();
        String message = ChatColor.translateAlternateColorCodes('&', StringUtil.joinStringFrom(args, 0));
        Bukkit.broadcastMessage("【自動聊天】< " + name + " > " + message);
        return true;
    }
}
