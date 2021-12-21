package ew.quilt.util.URL;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class URLCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("quilt.url.download")) {
            sender.sendMessage(ChatColor.RED + "權限不足 無法使用該指令 !");
            return false;
        }
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "指令使用方法 : /url [連結] [檔案名稱]");
            return false;
        }
        String link = args[0];
        String fileName = args[1];
        new Download(sender, link, fileName).start();
        return true;
    }
}
