package ew.quilt.Item.Lore;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class LoreCommand implements CommandExecutor {

    private static enum Action {
        NAME, OWNER, ADD, DELETE, SET, INSERT, CLEAR, UNDO
    }

    private static final HashMap<String, LinkedList<ItemStack>> UNDO = new HashMap<String, LinkedList<ItemStack>>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            sendHelp(sender);
            return false;
        }
        Player player = (Player) sender;

        ItemStack item = player.getEquipment().getItemInMainHand();
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            meta = Bukkit.getItemFactory().getItemMeta(item.getType());
            if (meta == null) {
                player.sendMessage("§4你手上的物品不支援新增 Lore");
                return false;
            }
        }

        if (args.length < 1) {
            sendHelp(sender);
            return false;
        }

        List<String> lore = meta.getLore();
        if (lore == null) {
            lore = new LinkedList<>();
        }

        Action action;
        try {
            action = Action.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException notEnum) {
            sendHelp(sender);
            return false;
        }

        String id = player.getName() + "'" + item.getType().name();
        if (action != Action.UNDO) {
            if (!UNDO.containsKey(id)) {
                UNDO.put(id, new LinkedList<>());
            }

            LinkedList<ItemStack> list = UNDO.get(id);
            list.addFirst(item.clone());

            while (list.size() > 5) {
                list.removeLast();
            }
        }

        switch (action) {
            case NAME:
                if (!sender.hasPermission("quilt.lore.name") || args.length < 2) {
                    sendHelp(sender);
                    return false;
                }

                String name = concatArgs(sender, args, 1);
                if (name.contains("|")) {
                    int max = name.replaceAll("§[0-9a-klmnor]", "").length();
                    Iterator<String> itr = lore.iterator();
                    while (itr.hasNext()) {
                        max = Math.max(max, itr.next().replaceAll("§[0-9a-klmnor]", "").length());
                    }
                    int spaces = max - name.replaceAll("§[0-9a-klmnor]", "").length() - 1;
                    String space = " ";
                    for (int i = 1; i < spaces * 1.5; i++) {
                        space += " ";
                    }
                    name = name.replace("|", space);
                }

                meta.setDisplayName(name);
                break;
            case OWNER:
                if (!sender.hasPermission("quilt.lore.owner") || args.length < 2) {
                    sendHelp(sender);
                    return false;
                }

                if (!(meta instanceof SkullMeta)) {
                    player.sendMessage("§4你只能對頭顱設定擁有者");
                    return false;
                }

                ((SkullMeta) meta).setOwner(args[1]);
                break;
            case ADD:
                if (!sender.hasPermission("quilt.lore")) {
                    sendHelp(sender);
                    return false;
                }
                if (args.length < 2) {
                    lore.add("");
                    break;
                }

                lore.add(concatArgs(sender, args, 1));
                break;
            case DELETE:
                if (!sender.hasPermission("quilt.lore")) {
                    sendHelp(sender);
                    return false;
                }

                switch (args.length) {
                    case 1:
                        if (lore.size() < 1) {
                            player.sendMessage("§4沒有任何物品可以移除");
                            return false;
                        }

                        lore.remove(lore.size() - 1);
                        break;
                    case 2:
                        int index;
                        try {
                            index = Integer.parseInt(args[1]) - 1;
                        } catch (Exception ex) {
                            return false;
                        }

                        if (lore.size() <= index || index < 0) {
                            player.sendMessage("§4輸入行數無效 !");
                            return false;
                        }

                        lore.remove(index);
                        break;
                    default:
                        return false;
                }
                break;
            case SET:
                if (!sender.hasPermission("quilt.lore") || args.length < 3) {
                    sendHelp(sender);
                    return false;
                }

                int index;
                try {
                    index = Integer.parseInt(args[1]) - 1;
                } catch (Exception ex) {
                    return false;
                }

                if (lore.size() <= index || index < 0) {
                    player.sendMessage("§4輸入行數無效 !");
                    return false;
                }

                lore.set(index, concatArgs(sender, args, 2));
                break;
            case INSERT:
                if (!sender.hasPermission("quilt.lore") || args.length < 3) {
                    sendHelp(sender);
                    return false;
                }

                int i;
                try {
                    i = Integer.parseInt(args[1]) - 1;
                } catch (Exception ex) {
                    return false;
                }

                if (lore.size() <= i || i < 0) {
                    player.sendMessage("§4輸入行數無效 !");
                    return false;
                }

                lore.add(i, concatArgs(sender, args, 2));
                break;
            case CLEAR:
                if (!sender.hasPermission("quilt.lore") || args.length != 1) {
                    sendHelp(sender);
                    return false;
                }

                lore.clear();
                break;
            case UNDO:
                if (args.length != 1) {
                    return false;
                }

                LinkedList<ItemStack> list = UNDO.get(id);
                if (list == null) {
                    player.sendMessage("§4你尚未修改該道具 !");
                    return false;
                }
                if (list.size() < 1) {
                    player.sendMessage("§4你無法對該道具繼續進行復原了 !");
                    return false;
                }

                ItemStack undoneItem = list.removeFirst();
                if (!item.isSimilar(undoneItem) && item.getType() != Material.SKULL_ITEM) {
                    player.sendMessage("§4你尚未修改該道具 !");
                    return false;
                }

                int stackSize = item.getAmount();
                if (undoneItem.getAmount() != stackSize) {
                    undoneItem.setAmount(stackSize);
                }

                player.getEquipment().setItemInMainHand(undoneItem);
                player.sendMessage("§5已對該道具最後一次修改進行復原 !");
                return true;
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
        player.sendMessage("§5Lore 已成功修改");
        return true;
    }

    private static String concatArgs(CommandSender sender, String[] args, int first) {
        StringBuilder sb = new StringBuilder();
        if (first > args.length) {
            return "";
        }
        for (int i = first; i <= args.length - 1; i++) {
            sb.append(" ");
            sb.append(ChatColor.translateAlternateColorCodes('&', args[i]));
        }
        String string = sb.substring(1);

        char[] charArray = string.toCharArray();
        boolean modified = false;
        for (int i = 0; i < charArray.length; i++) {
            if (charArray[i] == ChatColor.COLOR_CHAR && FormatType.isFormatType(charArray[i + 1])) {
                if (!sender.hasPermission("quilt.lore.format") || !sender.hasPermission("quilt.lore.format." + String.valueOf(charArray[i + 1]).toUpperCase())) {
                    charArray[i] = '?';
                    modified = true;
                }
            }
        }

        return modified ? String.copyValueOf(charArray) : string;
    }

    private static void sendHelp(CommandSender sender) {
        sender.sendMessage("§ELore 說明頁面 :");
        sender.sendMessage("§5每個指令都將修改手上的道具");
        if (sender.hasPermission("quilt.lore.format")) {
            sender.sendMessage("§5您可以使用 §6& §5符號變更文字樣式");
        }
        if (sender.hasPermission("quilt.lore.name")) {
            sender.sendMessage("§2/lore name [自訂名稱] §B設定道具顯示名稱");
        }
        if (sender.hasPermission("quilt.lore.owner")) {
            sender.sendMessage("§2/lore owner [玩家 ID] §B設定頭顱擁有者");
        }
        if (sender.hasPermission("quilt.lore")) {
            sender.sendMessage("§2/lore add [文字] §B新增一行 Lore 資訊");
            sender.sendMessage("§2/lore set [行數] [文字] §B變更指定行數 Lore 資訊");
            sender.sendMessage("§2/lore insert [行數] [文字] §B在指定行數插入 Lore 資訊");
            sender.sendMessage("§2/lore delete [(可選) 行數] §B刪除指令行數 Lore 資訊 (預設刪除最後一行)");
            sender.sendMessage("§2/lore clear §B清除該道具所有 Lore 資訊");
        }
        sender.sendMessage("§2/lore undo §B復原最後一次修改 (最多可復原最近 5 條紀錄)");
    }
}
