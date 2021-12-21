package ew.quilt.chat;

import ew.quilt.Config.ConfigManager;
import ew.quilt.util.StringUtil;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TipLineChatCommand implements CommandExecutor {

    private static final List<TipLineChatMessage> MESSAGE_LIST = new LinkedList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("quilt.tip.line.chat") && !ConfigManager.isAuthorSender(sender)) {
            sendNoPermission(sender);
            return false;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "該指令必須由玩家在遊戲中使用");
            return false;
        }
        if (args.length <= 0) {
            sender.sendMessage(ChatColor.YELLOW + "/tipLineChat add [訊息] - 新增一條簡單訊息");
            sender.sendMessage(ChatColor.YELLOW + "/tipLineChat addh [訊息] [提示] - 新增一條提示訊息");
            sender.sendMessage(ChatColor.YELLOW + "/tipLineChat addc [訊息] [提示] [指令] - 新增一條指令提示訊息");
            sender.sendMessage(ChatColor.YELLOW + "/tipLineChat adds [訊息] [提示] [建議] - 新增一條建議訊息");
            sender.sendMessage(ChatColor.YELLOW + "/tipLineChat remove [索引] - 刪除指定索引訊息");
            sender.sendMessage(ChatColor.YELLOW + "/tipLineChat clear - 清空所有訊息");
            sender.sendMessage(ChatColor.YELLOW + "/tipLineChat info - 預覽目前訊息");
            sender.sendMessage(ChatColor.YELLOW + "/tipLineChat send - 送出所有訊息");
            sender.sendMessage(ChatColor.YELLOW + "/tipLineChat sendh [訊息] [提示] - 直接送出單行提示訊息");
            sender.sendMessage(ChatColor.YELLOW + "/tipLineChat sendc [訊息] [提示] [指令] - 直接送出單行指令提示訊息");
            sender.sendMessage(ChatColor.YELLOW + "/tipLineChat sends [訊息] [提示] [建議] [指令] - 直接送出單行建議訊息");
            sender.sendMessage(ChatColor.YELLOW + "/tipLineChat sph [玩家] [訊息] [提示] - 直接送出單行提示訊息至指定玩家");
            sender.sendMessage(ChatColor.YELLOW + "/tipLineChat spc [玩家] [訊息] [提示] [指令] - 直接送出單行指令提示訊息至指定玩家");
            sender.sendMessage(ChatColor.YELLOW + "/tipLineChat sps [玩家] [訊息] [提示] [建議] [指令] - 直接送出單行建議訊息至指定玩家");
            return true;
        }
        TipLineChatMessage message = getMessage((Player) sender);
        switch (args[0].toLowerCase()) {
            case "add": {
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 /tipLineChat add [訊息]");
                    return false;
                }
                String toAdd = ChatColor.translateAlternateColorCodes('&', StringUtil.joinStringFrom(args, 1));
                message.getMessage().add(new HoverHelper().add(toAdd));
                sender.sendMessage(ChatColor.GREEN + "訊息新增完成 !");
                return true;
            }
            case "addh": {
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 /tipLineChat addh [訊息] [提示]");
                    return false;
                }
                String plain = ChatColor.translateAlternateColorCodes('&', args[1].replaceAll("_", " "));
                String hover = ChatColor.translateAlternateColorCodes('&', args[2].replaceAll("_", " "));
                message.getMessage().add(new HoverHelper().add(plain, hover));
                sender.sendMessage(ChatColor.GREEN + "訊息新增完成 !");
                return true;
            }
            case "addc": {
                if (args.length < 4) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 /tipLineChat addc [訊息] [提示] [指令]");
                    return false;
                }
                String plain = ChatColor.translateAlternateColorCodes('&', args[1].replaceAll("_", " "));
                String hover = ChatColor.translateAlternateColorCodes('&', args[2].replaceAll("_", " "));
                String command = StringUtil.joinStringFrom(args, 3);
                message.getMessage().add(new HoverHelper().add(plain, hover, command));
                sender.sendMessage(ChatColor.GREEN + "訊息新增完成 !");
                return true;
            }
            case "adds": {
                if (args.length < 4) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 /tipLineChat adds [訊息] [提示] [建議]");
                    return false;
                }
                String plain = ChatColor.translateAlternateColorCodes('&', args[1].replaceAll("_", " "));
                String hover = ChatColor.translateAlternateColorCodes('&', args[2].replaceAll("_", " "));
                String suggestion = StringUtil.joinStringFrom(args, 3);
                message.getMessage().add(new HoverHelper().add(plain, hover, null, suggestion));
                sender.sendMessage(ChatColor.GREEN + "訊息新增完成 !");
                return true;
            }
            case "remove": {
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 /tipLineChat remove [索引]");
                    return false;
                }
                int toRemove;
                try {
                    toRemove = Integer.parseInt(args[1]);
                } catch (NumberFormatException ex) {
                    sender.sendMessage(ChatColor.RED + "輸入數值錯誤 ...");
                    return false;
                }
                if (toRemove < 0 || toRemove >= message.getMessage().size()) {
                    sender.sendMessage(ChatColor.RED + "輸入索引無效 ...");
                    return false;
                }
                message.getMessage().remove(toRemove);
                if (message.getMessage().isEmpty()) {
                    clearMessage((Player) sender);
                }
                sender.sendMessage(ChatColor.GREEN + "訊息移除完成 !");
                return true;
            }
            case "clear": {
                clearMessage((Player) sender);
                sender.sendMessage(ChatColor.GREEN + "訊息已全部清除 !");
                return true;
            }
            case "info": {
                List<HoverHelper> toSend = message.getMessage();
                if (toSend.isEmpty()) {
                    sender.sendMessage(ChatColor.RED + "目前沒有任何訊息 ...");
                    return false;
                }
                sender.sendMessage(ChatColor.GREEN + "訊息預覽 :");
                for (HoverHelper send : toSend) {
                    send.show(sender);
                }
                return true;
            }
            case "send": {
                List<HoverHelper> toSend = message.getMessage();
                if (toSend.isEmpty()) {
                    sender.sendMessage(ChatColor.RED + "目前沒有任何訊息 ...");
                    return false;
                }
                for (HoverHelper send : toSend) {
                    send.broadcast();
                }
                return true;
            }
            case "sendh": {
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 /tipLineChat sendh [訊息] [提示]");
                    return false;
                }
                String plain = ChatColor.translateAlternateColorCodes('&', args[1].replaceAll("_", " "));
                String hover = ChatColor.translateAlternateColorCodes('&', args[2].replaceAll("_", " "));
                new HoverHelper().add(plain, hover).broadcast();
                return true;
            }
            case "sendc": {
                if (args.length < 4) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 /tipLineChat sendc [訊息] [提示] [指令]");
                    return false;
                }
                String plain = ChatColor.translateAlternateColorCodes('&', args[1].replaceAll("_", " "));
                String hover = ChatColor.translateAlternateColorCodes('&', args[2].replaceAll("_", " "));
                String command = StringUtil.joinStringFrom(args, 3);
                new HoverHelper().add(plain, hover, command).broadcast();
                return true;
            }
            case "sends": {
                if (args.length < 4) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 /tipLineChat sends [訊息] [提示] [建議]");
                    return false;
                }
                String plain = ChatColor.translateAlternateColorCodes('&', args[1].replaceAll("_", " "));
                String hover = ChatColor.translateAlternateColorCodes('&', args[2].replaceAll("_", " "));
                String suggestion = StringUtil.joinStringFrom(args, 3);
                new HoverHelper().add(plain, hover, null, suggestion).broadcast();
                return true;
            }
            case "sph": {
                if (args.length < 4) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 /tipLineChat sph [玩家] [訊息] [提示]");
                    return false;
                }
                Player player = Bukkit.getPlayer(args[1]);
                if (player == null) {
                    sender.sendMessage(ChatColor.RED + "指定玩家不在線上 ...");
                    return false;
                }
                String plain = ChatColor.translateAlternateColorCodes('&', args[2].replaceAll("_", " "));
                String hover = ChatColor.translateAlternateColorCodes('&', args[3].replaceAll("_", " "));
                new HoverHelper().add(plain, hover).show(player);
                sender.sendMessage(ChatColor.GREEN + "訊息已發送至目標玩家");
                return true;
            }
            case "spc": {
                if (args.length < 5) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 /tipLineChat spc [玩家] [訊息] [提示] [指令]");
                    return false;
                }
                Player player = Bukkit.getPlayer(args[1]);
                if (player == null) {
                    sender.sendMessage(ChatColor.RED + "指定玩家不在線上 ...");
                    return false;
                }
                String plain = ChatColor.translateAlternateColorCodes('&', args[2].replaceAll("_", " "));
                String hover = ChatColor.translateAlternateColorCodes('&', args[3].replaceAll("_", " "));
                String command = StringUtil.joinStringFrom(args, 4);
                new HoverHelper().add(plain, hover, command).show(player);
                sender.sendMessage(ChatColor.GREEN + "訊息已發送至目標玩家");
                return true;
            }
            case "sps": {
                if (args.length < 5) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 /tipLineChat sps [玩家] [訊息] [提示] [建議]");
                    return false;
                }
                Player player = Bukkit.getPlayer(args[1]);
                if (player == null) {
                    sender.sendMessage(ChatColor.RED + "指定玩家不在線上 ...");
                    return false;
                }
                String plain = ChatColor.translateAlternateColorCodes('&', args[2].replaceAll("_", " "));
                String hover = ChatColor.translateAlternateColorCodes('&', args[3].replaceAll("_", " "));
                String suggestion = StringUtil.joinStringFrom(args, 4);
                new HoverHelper().add(plain, hover, null, suggestion).show(player);
                sender.sendMessage(ChatColor.GREEN + "訊息已發送至目標玩家");
                return true;
            }
            default: {
                sender.sendMessage(ChatColor.RED + "輸入指令無效 ...");
                return false;
            }
        }
    }

    public static void clearMessage(Player player) {
        Iterator<TipLineChatMessage> itr = MESSAGE_LIST.iterator();
        while (itr.hasNext()) {
            TipLineChatMessage message = itr.next();
            if (message.getName().equals(player.getName())) {
                itr.remove();
                return;
            }
        }
    }

    public static TipLineChatMessage getMessage(Player player) {
        TipLineChatMessage ret;
        String name = player.getName();
        for (TipLineChatMessage message : MESSAGE_LIST) {
            if (message.getName().equals(name)) {
                return message;
            }
        }
        ret = new TipLineChatMessage(name);
        MESSAGE_LIST.add(ret);
        return ret;
    }

    public static void sendNoPermission(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "權限不足 無法使用該指令 !");
    }
}
