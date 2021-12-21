package ew.quilt.AdminChat;

import ew.quilt.util.StringUtil;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerRequestHelpCommand implements CommandExecutor {

    private static final Map<String, Long> LAST_SEND_MESSAGE = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length <= 0) {
            sender.sendMessage(ChatColor.RED + "請輸入訊息內容 ...");
            return false;
        }
        String name = sender.getName();
        Long coolTime = LAST_SEND_MESSAGE.get(name);
        long current = System.currentTimeMillis();
        if (coolTime == null || current > coolTime + 30 * 1000) { // 30 秒
            String message = "&C[求助管理員] &F" + sender.getName() + " &A» &B" + StringUtil.joinStringFrom(args, 0);
            for (Player admin : Bukkit.getOnlinePlayers()) {
                if (admin.hasPermission("quilt.admin.help")) {
                    admin.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                }
            }
            LAST_SEND_MESSAGE.put(name, current);
            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "您必須間隔 30 秒才能再傳送求助管理員訊息");
            return false;
        }
    }
}
