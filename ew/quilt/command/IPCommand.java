package ew.quilt.command;

import ew.quilt.Config.ConfigManager;
import ew.quilt.util.Compatible;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class IPCommand implements CommandExecutor {

    private static final int DATA_EVERY_PAGE = 10;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length <= 0) {
            sender.sendMessage(ChatColor.RED + "輸入指令錯誤 ...");
            return false;
        }
        switch (args[0].toLowerCase()) {
            case "list": {
                if (!sender.hasPermission("quilt.ip.list") && !ConfigManager.isAuthorSender(sender)) {
                    sendNoPermission(sender);
                    return false;
                }
                List<Player> playerList = Compatible.getOnlinePlayers();
                if (playerList.isEmpty()) {
                    sender.sendMessage(ChatColor.RED + "目前沒有任何玩家在線上 ...");
                    return false;
                }

                int page = 1;
                if (args.length >= 2) {
                    try {
                        page = Integer.parseInt(args[1]);
                    } catch (NumberFormatException ex) {
                        sender.sendMessage(ChatColor.RED + "頁數輸入錯誤 ...");
                        return false;
                    }
                    if (page < 1) {
                        sender.sendMessage(ChatColor.RED + "輸入頁數不得小於 1 ...");
                        return false;
                    }
                }
                --page;

                int min = page * DATA_EVERY_PAGE;
                if (min >= playerList.size()) {
                    sender.sendMessage(ChatColor.RED + "輸入頁數已超出資料範圍 ...");
                    return false;
                }

                int max = page * DATA_EVERY_PAGE + DATA_EVERY_PAGE;
                for (int i = min; i < (max > playerList.size() ? playerList.size() : max); ++i) {
                    Player player = playerList.get(i);
                    sender.sendMessage("玩家 ID : " + player.getName() + " IP 位址 : " + player.getAddress().getHostString());
                }
                sender.sendMessage(ChatColor.YELLOW + "第 " + (page + 1) + " 頁 共 " + (playerList.size() / DATA_EVERY_PAGE + (playerList.size() % DATA_EVERY_PAGE != 0 ? 1 : 0)) + " 頁");
                return true;
            }
            case "search": {
                if (!sender.hasPermission("quilt.ip.search")) {
                    sendNoPermission(sender);
                    return false;
                }
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 : /ip search [搜尋字串 (玩家 ID 或 IP)]");
                    return false;
                }
                boolean get = false;
                String target = args[1];

                List<Player> playerList = Compatible.getOnlinePlayers();
                for (Player player : playerList) {
                    if (player.getName().contains(target) || player.getAddress().getHostString().contains(target)) {
                        sender.sendMessage(ChatColor.GREEN + "玩家 ID : " + player.getName() + " IP 位址 : " + player.getAddress().getHostString());
                        get = true;
                    }
                }

                if (!get) {
                    sender.sendMessage(ChatColor.RED + "查無資料 ...");
                    return false;
                }
                return true;
            }
            case "find": {
                if (!sender.hasPermission("quilt.ip.find")) {
                    sendNoPermission(sender);
                    return false;
                }
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 : /ip find [玩家 ID]");
                    return false;
                }
                Player player = Bukkit.getPlayer(args[1]);
                if (player == null) {
                    sender.sendMessage(ChatColor.RED + "找不到目標玩家 ...");
                    return false;
                }
                sender.sendMessage(ChatColor.GREEN + "玩家 ID : " + player.getName() + " IP 位址 : " + player.getAddress().getHostString());
                return true;
            }
            case "multi": {
                if (!sender.hasPermission("quilt.ip.multi")) {
                    sendNoPermission(sender);
                    return false;
                }
                for (String res : MultiClientCheck.getMultiPlayer()) {
                    sender.sendMessage(res);
                }
                return true;
            }
            default: {
                sender.sendMessage(ChatColor.RED + "輸入指令不存在 ...");
                return false;
            }
        }
    }

    public static void sendNoPermission(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "權限不足 無法使用該指令 !");
    }
}
