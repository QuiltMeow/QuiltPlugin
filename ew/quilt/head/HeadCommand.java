package ew.quilt.head;

import ew.quilt.Config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HeadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            switch (args.length) {
                case 0:
                    if (!player.hasPermission("quilt.special.head") && !ConfigManager.isAuthorSender(sender)) {
                        player.sendMessage(ChatColor.RED + "權限不足 無法使用該指令 !");
                        return false;
                    }
                    player.sendMessage(ChatColor.GREEN + "已獲得自身頭顱");
                    HeadManager.giveHead(player, player.getName());
                    return true;
                case 1:
                    if (args[0].equalsIgnoreCase("help")) {
                        player.sendMessage(ChatColor.GREEN + "頭顱指令集 使用說明 :");
                        player.sendMessage("/head " + ChatColor.GOLD + "獲得自身頭顱");
                        player.sendMessage("/head gain [玩家 ID] " + ChatColor.GOLD + "獲得指定玩家頭顱");
                        player.sendMessage("/head give [玩家 ID] [頭顱 ID] " + ChatColor.GOLD + "給予目標玩家指定玩家頭顱");
                        player.sendMessage("/head extra [類型] " + ChatColor.GOLD + "獲得指定類型頭顱");
                        player.sendMessage("/head give [玩家 ID] extra [類型] " + ChatColor.GOLD + "給予目標玩家指定類型頭顱");
                        return true;
                    } else if (args[0].equalsIgnoreCase("give")) {
                        player.sendMessage("/head give [玩家 ID] [頭顱 ID] " + ChatColor.GOLD + "給予目標玩家指定玩家頭顱");
                        player.sendMessage("/head give [玩家 ID] extra [類型] " + ChatColor.GOLD + "給予目標玩家指定類型頭顱");
                        return true;
                    } else {
                        player.sendMessage(ChatColor.RED + "輸入指令無效 !");
                        return false;
                    }
                case 2:
                    if (args[0].equalsIgnoreCase("extra")) {
                        if (!player.hasPermission("quilt.special.head.extra")) {
                            player.sendMessage(ChatColor.RED + "權限不足 無法使用該指令 !");
                            return false;
                        }
                        Head head = HeadManager.getHead(args[1]);
                        if (head == null) {
                            player.sendMessage(ChatColor.RED + "指定頭顱類型不存在 !");
                            return false;
                        }
                        HeadManager.giveHead(player, head);
                        player.sendMessage(ChatColor.GREEN + "已獲得指定類型頭顱");
                        return true;
                    } else if (args[0].equalsIgnoreCase("gain") && !ConfigManager.isAuthorSender(sender)) {
                        if (!player.hasPermission("quilt.special.head.other")) {
                            player.sendMessage(ChatColor.RED + "權限不足 無法使用該指令 !");
                            return false;
                        }
                        String head = args[1];
                        HeadManager.giveHead(player, head);
                        player.sendMessage(ChatColor.GREEN + "已獲得指定玩家頭顱");
                        return true;
                    } else {
                        player.sendMessage(ChatColor.RED + "輸入指令無效 !");
                        return false;
                    }
                case 3:
                    if (args[0].equalsIgnoreCase("give")) {
                        if (!player.hasPermission("quilt.special.head.give")) {
                            player.sendMessage(ChatColor.RED + "權限不足 無法使用該指令 !");
                            return false;
                        }
                        Player giveTo = Bukkit.getPlayer(args[1]);
                        String head = args[2];
                        if (giveTo == null) {
                            player.sendMessage(ChatColor.RED + "指定玩家不在線上 !");
                            return false;
                        }
                        HeadManager.giveHead(giveTo, head);
                        giveTo.sendMessage(ChatColor.GREEN + "已收到指定玩家頭顱");
                        player.sendMessage(ChatColor.GREEN + "已發送指定玩家頭顱至目標玩家");
                        return true;
                    } else {
                        player.sendMessage(ChatColor.RED + "輸入指令無效 !");
                        return false;
                    }
                case 4:
                    if (args[0].equalsIgnoreCase("give") && args[2].equalsIgnoreCase("extra")) {
                        if (!player.hasPermission("quilt.special.head.give.extra")) {
                            player.sendMessage(ChatColor.RED + "權限不足 無法使用該指令 !");
                            return false;
                        }
                        Player giveTo = Bukkit.getPlayer(args[1]);
                        Head head = HeadManager.getHead(args[3]);
                        if (giveTo == null) {
                            player.sendMessage(ChatColor.RED + "指定玩家不在線上 !");
                            return false;
                        }
                        if (head == null) {
                            player.sendMessage(ChatColor.RED + "指定頭顱類型不存在 !");
                            return false;
                        }
                        HeadManager.giveHead(giveTo, head);
                        giveTo.sendMessage(ChatColor.GREEN + "已收到指定類型頭顱");
                        player.sendMessage(ChatColor.GREEN + "已發送指定類型頭顱至目標玩家");
                        return true;
                    } else {
                        player.sendMessage(ChatColor.RED + "輸入指令無效 !");
                        return false;
                    }
                default:
                    player.sendMessage(ChatColor.RED + "輸入指令無效 !");
                    return false;
            }
        } else {
            sender.sendMessage(ChatColor.RED + "該指令必須在遊戲內由玩家使用");
            return false;
        }
    }
}
