package ew.quilt.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandDispatcher {

    public static void issueServerCommand(String command) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
    }

    public static void issuePlayerCommand(Player player, String command) {
        Bukkit.dispatchCommand(player, command);
    }

    public static void issueCommand(CommandSender sender, String command) {
        Bukkit.dispatchCommand(sender, command);
    }
}
