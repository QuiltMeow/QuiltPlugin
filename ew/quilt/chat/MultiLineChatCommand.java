package ew.quilt.chat;

import ew.quilt.Config.ConfigManager;
import ew.quilt.util.StringUtil;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MultiLineChatCommand implements CommandExecutor {

    private static final List<MultiLineChatMessage> MESSAGE_LIST = new LinkedList<>();

    private static final List<List<String>> ASCII_ART = new LinkedList<List<String>>();

    private static final String[] NYAN_CAT = new String[]{
        "`·.,¸,.·*¯`·.,¸,.·....╭━━━━━━━━╮",
        "`·.,¸,.·*¯`·.,¸,.·*¯|:::::::::::::/\\:_:/\\",
        "`·.,¸,.·*¯`·.,¸,.·*<|::::::::::::(｡ ◕‿‿◕).",
        "`·.,¸,.·*¯`·.,¸,.·* ╰O--O----O-O"
    };

    static {
        ASCII_ART.add(Arrays.asList(NYAN_CAT));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("quilt.multi.line.chat") && !ConfigManager.isAuthorSender(sender)) {
            sendNoPermission(sender);
            return false;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "該指令必須由玩家在遊戲中使用");
            return false;
        }
        if (args.length <= 0) {
            sender.sendMessage(ChatColor.YELLOW + "/multiLineChat add [訊息] - 新增一條多行訊息");
            sender.sendMessage(ChatColor.YELLOW + "/multiLineChat remove [索引] - 刪除一條多行訊息");
            sender.sendMessage(ChatColor.YELLOW + "/multiLineChat clear - 清除所有多行訊息");
            sender.sendMessage(ChatColor.YELLOW + "/multiLineChat modify [索引] [訊息] - 修改指定多行訊息");
            sender.sendMessage(ChatColor.YELLOW + "/multiLineChat info - 列出目前多行訊息預覽");
            sender.sendMessage(ChatColor.YELLOW + "/multiLineChat send - 送出多行訊息");
            sender.sendMessage(ChatColor.YELLOW + "/multiLineChat load [索引] - 載入預儲存訊息");
            return true;
        }
        MultiLineChatMessage message = getMessage((Player) sender);
        switch (args[0].toLowerCase()) {
            case "add": {
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 /multiLineChat add [訊息]");
                    return false;
                }
                String toAdd = StringUtil.joinStringFrom(args, 1);
                message.getMessage().add(ChatColor.translateAlternateColorCodes('&', toAdd));
                sender.sendMessage(ChatColor.GREEN + "訊息新增完成 !");
                return true;
            }
            case "remove": {
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 /multiLineChat remove [索引]");
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
            case "modify": {
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 /multiLineChat modify [索引] [訊息]");
                    return false;
                }
                int toModify;
                try {
                    toModify = Integer.parseInt(args[1]);
                } catch (NumberFormatException ex) {
                    sender.sendMessage(ChatColor.RED + "輸入數值錯誤 ...");
                    return false;
                }
                if (toModify < 0 || toModify >= message.getMessage().size()) {
                    sender.sendMessage(ChatColor.RED + "輸入索引無效 ...");
                    return false;
                }
                message.getMessage().set(toModify, ChatColor.translateAlternateColorCodes('&', StringUtil.joinStringFrom(args, 2)));
                sender.sendMessage(ChatColor.GREEN + "訊息修改完成 !");
                return true;
            }
            case "info": {
                List<String> toSend = message.getMessage();
                if (toSend.isEmpty()) {
                    sender.sendMessage(ChatColor.RED + "目前沒有任何訊息 ...");
                    return false;
                }
                sender.sendMessage(ChatColor.GREEN + "訊息預覽 :");
                for (String send : toSend) {
                    sender.sendMessage(send);
                }
                return true;
            }
            case "send": {
                List<String> toSend = message.getMessage();
                if (toSend.isEmpty()) {
                    sender.sendMessage(ChatColor.RED + "目前沒有任何訊息 ...");
                    return false;
                }
                for (String send : toSend) {
                    Bukkit.broadcastMessage(send);
                }
                return true;
            }
            case "load": {
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 /multiLineChat load [索引]");
                    return false;
                }
                int toLoad;
                try {
                    toLoad = Integer.parseInt(args[1]);
                } catch (NumberFormatException ex) {
                    sender.sendMessage(ChatColor.RED + "輸入數值錯誤 ...");
                    return false;
                }
                if (toLoad < 0 || toLoad >= ASCII_ART.size()) {
                    sender.sendMessage(ChatColor.RED + "輸入索引無效 ...");
                    return false;
                }
                List<String> read = ASCII_ART.get(toLoad);
                for (String toAppend : read) {
                    message.getMessage().add(toAppend);
                }
                sender.sendMessage(ChatColor.GREEN + "訊息載入完成 !");
                return true;
            }
            default: {
                sender.sendMessage(ChatColor.RED + "輸入指令無效 ...");
                return false;
            }
        }
    }

    public static void clearMessage(Player player) {
        Iterator<MultiLineChatMessage> itr = MESSAGE_LIST.iterator();
        while (itr.hasNext()) {
            MultiLineChatMessage message = itr.next();
            if (message.getName().equals(player.getName())) {
                itr.remove();
                return;
            }
        }
    }

    public static MultiLineChatMessage getMessage(Player player) {
        MultiLineChatMessage ret;
        String name = player.getName();
        for (MultiLineChatMessage message : MESSAGE_LIST) {
            if (message.getName().equals(name)) {
                return message;
            }
        }
        ret = new MultiLineChatMessage(name);
        MESSAGE_LIST.add(ret);
        return ret;
    }

    public static void sendNoPermission(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "權限不足 無法使用該指令 !");
    }
}
