package ew.quilt.Enchant;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EnchantCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "該指令必須在遊戲內由玩家使用");
            return false;
        }
        if (!sender.hasPermission("quilt.enchant.use")) {
            sendNoPermission(sender);
            return false;
        }
        Player player = (Player) sender;
        switch (args.length) {
            case 1: {
                String option = args[0].trim();
                if (option.equalsIgnoreCase("all")) {
                    if (player.hasPermission("quilt.enchant.all")) {
                        Enchant.enchantAll(player);
                    } else {
                        sendNoPermission(player);
                        return false;
                    }
                } else if (option.equalsIgnoreCase("god")) {
                    if (player.hasPermission("quilt.enchant.god")) {
                        Enchant.enchantGod(player);
                    } else {
                        sendNoPermission(player);
                        return false;
                    }
                } else if (option.equalsIgnoreCase("none")) {
                    Enchant.enchantNone(player);
                } else if (option.equalsIgnoreCase("info")) {
                    Enchant.enchantInfo(player);
                } else if (option.equalsIgnoreCase("type")) {
                    Enchant.showType(player);
                } else {
                    Enchant.showSyntax(sender);
                    return false;
                }
                return true;
            }
            case 2: {
                String enchantment = Enchant.parseEnchantment(args[0].trim());
                if (enchantment == null) {
                    return false;
                }
                short level;
                try {
                    level = Short.valueOf(args[1].trim());
                } catch (NumberFormatException ex) {
                    sender.sendMessage(ChatColor.RED + "等級輸入錯誤 ...");
                    return false;
                }
                Enchant.enchantSingle(player, enchantment, level);
                return true;
            }
            default: {
                Enchant.showSyntax(sender);
                return false;
            }
        }
    }

    public static void sendNoPermission(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "權限不足 無法使用該指令 !");
    }
}
