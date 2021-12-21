package ew.quilt.chat;

import ew.quilt.util.StringUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class InternalMessageSendCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 1) {
            return false;
        }
        String message = StringUtil.joinStringFrom(args, 0);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        return true;
    }
}
