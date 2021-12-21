package ew.quilt.plugin;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemIdCommand implements CommandExecutor {

    public static class ItemInfo {

        private final String name;
        private final int id;

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }

        public ItemInfo(String name, int id) {
            this.name = name;
            this.id = id;
        }
    }

    public static ItemInfo getItemInfo(ItemStack item) {
        return new ItemInfo(item.getType().name(), item.getTypeId());
    }

    private static void showItemInfo(CommandSender sender, ItemStack item) {
        ItemInfo info = getItemInfo(item);
        sender.sendMessage(ChatColor.GREEN + "道具名稱 : " + info.getName() + " 代碼 : " + info.getId());
    }

    private static void showItemInfo(CommandSender sender, Material material) {
        sender.sendMessage(ChatColor.GREEN + "道具名稱 : " + material.name() + " 代碼 : " + material.getId());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        switch (args.length) {
            case 0: {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "該指令必須在遊戲中由玩家使用");
                    return true;
                }
                Player player = (Player) sender;
                ItemStack item = player.getInventory().getItemInMainHand();
                showItemInfo(sender, item);
                break;
            }
            case 1: {
                try {
                    int id = Integer.parseInt(args[0]);
                    Material material = Material.getMaterial(id);
                    if (material == null) {
                        sender.sendMessage(ChatColor.RED + "找不到目標道具");
                        return true;
                    }
                    showItemInfo(sender, material);
                } catch (NumberFormatException ex) {
                    String name = args[0];
                    Material material = Material.getMaterial(name);
                    if (material == null) {
                        sender.sendMessage(ChatColor.RED + "找不到目標道具");
                        return true;
                    }
                    showItemInfo(sender, material);
                }
                break;
            }
            case 2: {
                if (!args[0].equalsIgnoreCase("slot")) {
                    sender.sendMessage(ChatColor.RED + "指令格式錯誤");
                    return false;
                }
                try {
                    int slot = Integer.parseInt(args[1]);
                    if (!(sender instanceof Player)) {
                        sender.sendMessage(ChatColor.RED + "該指令必須在遊戲中由玩家使用");
                        return true;
                    }
                    Player player = (Player) sender;
                    ItemStack item = player.getInventory().getItem(slot);
                    if (item == null) {
                        sender.sendMessage(ChatColor.RED + "找不到目標道具");
                        return true;
                    }
                    showItemInfo(sender, item);
                } catch (NumberFormatException ex) {
                    sender.sendMessage(ChatColor.RED + "輸入數值錯誤");
                    return false;
                }
                break;
            }
        }
        return true;
    }
}
