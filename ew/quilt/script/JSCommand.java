package ew.quilt.script;

import ew.quilt.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JSCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("quilt.js")) {
            sendNoPermission(sender);
            return false;
        }
        if (args.length <= 0) {
            sender.sendMessage(ChatColor.YELLOW + "/qjs execute [指令] - 執行指令腳本");
            sender.sendMessage(ChatColor.YELLOW + "/qjs executeConsole [指令] - 指定主控台執行指令腳本");
            sender.sendMessage(ChatColor.YELLOW + "/qjs executeTarget [玩家] [指令] - 指定玩家執行指令腳本");
            sender.sendMessage(ChatColor.YELLOW + "/qjs load [檔案名稱] - 指定執行檔案指令腳本");
            sender.sendMessage(ChatColor.YELLOW + "/qjs loadConsole [檔案名稱] - 指定主控台執行檔案指令腳本");
            sender.sendMessage(ChatColor.YELLOW + "/qjs loadTarget [玩家] [檔案名稱] - 指定玩家執行檔案指令腳本");
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "exe":
            case "execute": {
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 /qjs execute [指令]");
                    return false;
                }
                String command = StringUtil.joinStringFrom(args, 1);
                boolean success = JSExecuteLibrary.getInstance().executeScript(sender, command);
                if (success) {
                    sender.sendMessage(ChatColor.GREEN + "腳本指令已執行");
                }
                return success;
            }
            case "exec":
            case "execonsole":
            case "executeconsole": {
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 /qjs executeConsole [指令]");
                    return false;
                }
                String command = StringUtil.joinStringFrom(args, 1);
                boolean success = JSExecuteLibrary.getInstance().executeScript(command);
                if (success) {
                    sender.sendMessage(ChatColor.GREEN + "腳本指令已執行");
                } else {
                    sender.sendMessage(ChatColor.RED + "腳本指令執行失敗 ...");
                }
                return success;
            }
            case "exet":
            case "exetarget":
            case "executetarget": {
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 /qjs executeTarget [玩家] [指令]");
                    return false;
                }
                Player player = Bukkit.getPlayer(args[1]);
                if (player == null) {
                    sender.sendMessage(ChatColor.RED + "指定玩家不在線上 ...");
                    return false;
                }
                String command = StringUtil.joinStringFrom(args, 2);
                boolean success = JSExecuteLibrary.getInstance().executeScript(player, command);
                if (success) {
                    sender.sendMessage(ChatColor.GREEN + "腳本指令已執行");
                } else {
                    sender.sendMessage(ChatColor.RED + "腳本指令執行失敗 ...");
                }
                return success;
            }
            case "load": {
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 /qjs load [檔案名稱]");
                    return false;
                }
                String file = StringUtil.joinStringFrom(args, 1);
                boolean success = JSExecuteLibrary.getInstance().executeJSFile(sender, file);
                if (success) {
                    sender.sendMessage(ChatColor.GREEN + "腳本指令已執行");
                }
                return success;
            }
            case "loadc":
            case "loadconsole": {
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 /qjs loadConsole [檔案名稱]");
                    return false;
                }
                String file = StringUtil.joinStringFrom(args, 1);
                boolean success = JSExecuteLibrary.getInstance().executeJSFile(file);
                if (success) {
                    sender.sendMessage(ChatColor.GREEN + "腳本指令已執行");
                } else {
                    sender.sendMessage(ChatColor.RED + "腳本指令執行失敗 ...");
                }
                return success;
            }
            case "loadt":
            case "loadtarget": {
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 /qjs loadTarget [玩家] [檔案名稱]");
                    return false;
                }
                Player player = Bukkit.getPlayer(args[1]);
                if (player == null) {
                    sender.sendMessage(ChatColor.RED + "指定玩家不在線上 ...");
                    return false;
                }
                String file = StringUtil.joinStringFrom(args, 2);
                boolean success = JSExecuteLibrary.getInstance().executeJSFile(player, file);
                if (success) {
                    sender.sendMessage(ChatColor.GREEN + "腳本指令已執行");
                } else {
                    sender.sendMessage(ChatColor.RED + "腳本指令執行失敗 ...");
                }
                return success;
            }
            default: {
                sender.sendMessage(ChatColor.RED + "輸入指令無效 ...");
                return false;
            }
        }
    }

    public static void sendNoPermission(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "權限不足 無法使用該指令 !");
    }
}
