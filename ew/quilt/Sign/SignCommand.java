package ew.quilt.Sign;

import java.util.Set;
import java.util.regex.Pattern;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SignCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            if (sender instanceof Player) {
                if (!sender.hasPermission("quilt.sign.use")) {
                    sender.sendMessage(ChatColor.RED + "權限不足 無法使用該指令");
                    return false;
                }
                if (args.length == 2) {
                    if (isInteger(args[0]) && Integer.parseInt(args[0]) >= 1 && Integer.parseInt(args[0]) <= 4) {
                        Player player = (Player) sender;
                        Block block = player.getTargetBlock((Set<Material>) null, 10);
                        if (block.getType() == Material.SIGN_POST || block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN) {
                            Sign sign = (Sign) block.getState();
                            if (args[1].equalsIgnoreCase("clear")) {
                                sign.setLine(Integer.parseInt(args[0]) - 1, "");
                            } else {
                                sign.setLine(Integer.parseInt(args[0]) - 1, ChatColor.translateAlternateColorCodes('&', args[1]).replace("_", " "));
                            }
                            sign.update();
                            sender.sendMessage(ChatColor.GRAY + "告示牌修改完成");
                        } else {
                            sender.sendMessage(ChatColor.RED + "你指向的方塊不是告示牌 無法進行修改");
                            return false;
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "輸入行數錯誤");
                        return false;
                    }
                } else {
                    sender.sendMessage(ChatColor.GRAY + "使用方法 : /sign [行數] [文字]");
                    return false;
                }
            } else {
                sender.sendMessage(ChatColor.RED + "該指令必須在遊戲內由玩家使用");
                return false;
            }
            return true;
        } catch (Exception ex) {
            sender.sendMessage(ChatColor.RED + "指令執行時發生例外狀況 !");
            return false;
        }
    }

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
}
