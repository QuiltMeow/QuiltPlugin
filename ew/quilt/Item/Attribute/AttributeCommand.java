package ew.quilt.Item.Attribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import ew.quilt.Item.Attribute.api.AttributesEditorAPI;
import ew.quilt.Config.ConfigManager;

public class AttributeCommand implements CommandExecutor {

    public static int version;
    public static List<Integer> allowedType, allowedSlot = Arrays.asList(1, 2, 3, 4, 5, 6);
    public static List<Integer> allowedEnchantment, allowedFlags = Arrays.asList(1, 2, 3, 4, 5, 6);
    public static List<Integer> allowedPotionEffects, allowedFireworkType = Arrays.asList(0, 1, 2, 3, 4);
    public static List<Integer> allowedGeneration = Arrays.asList(0, 1, 2, 3);
    private List<String> versionList;

    public AttributeCommand() {
        versionList = new ArrayList<>();
        versionList.add("6");
        versionList.add("7");
        versionList.add("8");
        versionList.add("9");
        versionList.add("10");
        versionList.add("11");
        versionList.add("12");
        versionList.add("13");

        for (String localVersion : versionList) {
            if (ConfigManager.VERSION.startsWith(localVersion)) {
                version = Integer.valueOf(localVersion);
                break;
            }
        }

        if (version == 10 || version == 11 || version == 12 || version == 13) {
            allowedType = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

            switch (version) {
                case 13: {
                    allowedEnchantment = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 16, 17, 18, 19, 20, 21, 32, 33, 34, 35, 48, 49, 50, 51, 61, 62, 8, 9, 70, 10, 71, 22, 65, 66, 67, 68);
                    break;
                }
                case 12: {
                    allowedEnchantment = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 16, 17, 18, 19, 20, 21, 32, 33, 34, 35, 48, 49, 50, 51, 61, 62, 8, 9, 70, 10, 71, 22);
                    break;
                }
                default: {
                    allowedEnchantment = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 16, 17, 18, 19, 20, 21, 32, 33, 34, 35, 48, 49, 50, 51, 61, 62, 8, 9, 70);
                    break;
                }
            }

            allowedPotionEffects = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27);
        }
        if (version < 7) {
            allowedEnchantment = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 16, 17, 18, 19, 20, 21, 32, 33, 34, 35, 48, 49, 50, 51);
        }
        if (version == 7) {
            allowedEnchantment = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 16, 17, 18, 19, 20, 21, 32, 33, 34, 35, 48, 49, 50, 51, 61, 62);
        }
        if (version == 8) {
            allowedEnchantment = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 16, 17, 18, 19, 20, 21, 32, 33, 34, 35, 48, 49, 50, 51, 61, 62, 8);
        }
        if (version < 9) {
            allowedPotionEffects = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23);
        }
        if (version == 9) {
            String cache = ConfigManager.VERSION.substring(4, 5);
            allowedPotionEffects = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27);
            allowedEnchantment = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 16, 17, 18, 19, 20, 21, 32, 33, 34, 35, 48, 49, 50, 51, 61, 62, 8, 9, 70);
            if (cache.contains("R")) {
                allowedType = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
            } else {
                allowedType = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
            }
        } else if (version <= 8) {
            allowedType = Arrays.asList(1, 2, 3, 4, 5);
        }
    }

    @Override
    public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
        try {
            if (arg0 instanceof Player) {
                Player player = (Player) arg0;
                if (!player.hasPermission("quilt.attribute.editor.use")) {
                    player.sendMessage(ChatColor.RED + "您沒有使用該指令權限");
                    return false;
                }
                ItemStack is = AttributesEditorAPI.getAvailableItemInHand(player);
                if (is == null) {
                    player.sendMessage(ChatColor.RED + "手中的物品無效 無法進行編輯");
                    return false;
                }
                if (arg3.length == 1) {
                    if (!arg3[0].equalsIgnoreCase("remove") && !arg3[0].equalsIgnoreCase("unbreakable")) {
                        return false;
                    }
                    if (arg3[0].equalsIgnoreCase("remove")) {
                        try {
                            is = AttributesEditorAPI.removeAttribute(is);
                            if (is != null) {
                                AttributesEditorAPI.setItemInHand(player, is);
                                player.sendMessage(ChatColor.GREEN + "藍字屬性移除成功");
                                return true;
                            } else {
                                player.sendMessage(ChatColor.RED + "物品沒有藍字屬性 無需移除");
                                return false;
                            }
                        } catch (Exception ex) {
                            AttributesEditorAPI.printStackTrace(player, ex);
                            return false;
                        }
                    }
                    if (arg3[0].equalsIgnoreCase("unbreakable")) {
                        ItemMeta im = is.getItemMeta();
                        try {
                            if (im.spigot().isUnbreakable()) {
                                im.spigot().setUnbreakable(false);
                            } else {
                                im.spigot().setUnbreakable(true);
                            }
                            is.setItemMeta(im);
                            AttributesEditorAPI.setItemInHand(player, is);
                            player.sendMessage(ChatColor.GREEN + "為物品修改無法破壞屬性成功");
                            return true;
                        } catch (Exception ex) {
                            player.sendMessage(ChatColor.RED + "該指令只能在 Spigot 及其衍生服務端上使用");
                            return false;
                        }
                    }
                }
                if (arg3.length == 2) {
                    if (!arg3[0].equalsIgnoreCase("display") && !arg3[0].equalsIgnoreCase("hide") && !arg3[0].equalsIgnoreCase("potion") && !arg3[0].equalsIgnoreCase("firework") && !arg3[0].equalsIgnoreCase("enchbook") && !arg3[0].equalsIgnoreCase("book")) {
                        return false;
                    }
                    if (arg3[0].equalsIgnoreCase("enchbook")) {
                        if (!arg3[1].equalsIgnoreCase("remove")) {
                            return false;
                        }
                        try {
                            is = AttributesEditorAPI.removeStoredEnchantment(is);
                            if (is != null) {
                                AttributesEditorAPI.setItemInHand(player, is);
                                player.sendMessage(ChatColor.GREEN + "附魔書屬性移除成功");
                                return true;
                            } else {
                                player.sendMessage(ChatColor.RED + "物品沒有附魔書屬性 無需移除");
                                return false;
                            }
                        } catch (Exception ex) {
                            AttributesEditorAPI.printStackTrace(player, ex);
                            return false;
                        }
                    }
                    if (arg3[0].equalsIgnoreCase("firework")) {
                        if (!arg3[1].equalsIgnoreCase("remove")) {
                            return false;
                        }
                        try {
                            is = AttributesEditorAPI.removeFireworks(is);
                            if (is != null) {
                                AttributesEditorAPI.setItemInHand(player, is);
                                player.sendMessage(ChatColor.GREEN + "煙火屬性移除成功");
                                return true;
                            } else {
                                player.sendMessage(ChatColor.RED + "物品沒有煙火屬性 無需移除");
                                return false;
                            }
                        } catch (Exception ex) {
                            AttributesEditorAPI.printStackTrace(player, ex);
                            return false;
                        }
                    }
                    if (arg3[0].equalsIgnoreCase("potion")) {
                        if (!arg3[1].equalsIgnoreCase("remove")) {
                            return false;
                        }
                        try {
                            is = AttributesEditorAPI.removePotionEffects(is);
                            if (is != null) {
                                AttributesEditorAPI.setItemInHand(player, is);
                                player.sendMessage(ChatColor.GREEN + "藥水效果移除成功");
                                return true;
                            } else {
                                player.sendMessage(ChatColor.RED + "物品沒有藥水效果 無需移除");
                                return false;
                            }
                        } catch (Exception ex) {
                            AttributesEditorAPI.printStackTrace(player, ex);
                            return false;
                        }
                    }
                    if (arg3[0].equalsIgnoreCase("hide")) {
                        int hideType;
                        try {
                            hideType = Integer.valueOf(arg3[1]);
                        } catch (Exception ex) {
                            sendInvavidMessage(player);
                            return false;
                        }
                        try {
                            if (!AttributesEditorAPI.isAllowedHideType(hideType)) {
                                player.sendMessage(ChatColor.RED + "該屬性類型無效");
                                return false;
                            }
                            ItemFlag sHide = AttributesEditorAPI.decodeHideType(hideType);
                            ItemMeta im = is.getItemMeta();
                            if (im.hasItemFlag(sHide)) {
                                im.removeItemFlags(sHide);
                                is.setItemMeta(im);
                                AttributesEditorAPI.setItemInHand(player, is);
                                player.sendMessage(ChatColor.GREEN + "成功顯示指定屬性");
                                return true;
                            } else {
                                im.addItemFlags(sHide);
                                is.setItemMeta(im);
                                AttributesEditorAPI.setItemInHand(player, is);
                                player.sendMessage(ChatColor.GREEN + "成功隱藏指定屬性");
                                return true;
                            }
                        } catch (Exception ex) {
                            player.sendMessage(ChatColor.RED + "您的遊戲版本無法進行該操作");
                            return false;
                        }
                    }
                    if (arg3[0].equalsIgnoreCase("display")) {
                        String newDisplay = ChatColor.translateAlternateColorCodes('&', arg3[1]);
                        ItemMeta im = is.getItemMeta();
                        im.setDisplayName(newDisplay);
                        is.setItemMeta(im);
                        AttributesEditorAPI.setItemInHand(player, is);
                        player.sendMessage(ChatColor.GREEN + "成功修改物品名稱為 " + ChatColor.RESET + newDisplay);
                        return true;
                    }
                    if (arg3[0].equalsIgnoreCase("book")) {
                        try {
                            BookMeta bm = (BookMeta) is.getItemMeta();
                            bm.addPage(ChatColor.translateAlternateColorCodes('&', arg3[1]));
                            is.setItemMeta(bm);
                            AttributesEditorAPI.setItemInHand(player, is);
                            player.sendMessage(ChatColor.GREEN + "成功新增了一頁書");
                            return true;
                        } catch (ClassCastException ex) {
                            player.sendMessage(ChatColor.RED + "您手中的物品不是書類物品");
                            return false;
                        }
                    }
                }
                if (arg3.length == 3) {
                    if (!arg3[0].equalsIgnoreCase("lore") && !arg3[0].equalsIgnoreCase("enchant") && !arg3[0].equalsIgnoreCase("firework") && !arg3[0].equalsIgnoreCase("enchbook") && !arg3[0].equalsIgnoreCase("book")) {
                        return false;
                    }
                    if (arg3[0].equalsIgnoreCase("enchbook")) {
                        short enchid, level;
                        try {
                            int ench = Integer.valueOf(arg3[1]);
                            if (!AttributesEditorAPI.isAllowedEnchantment(ench)) {
                                player.sendMessage(ChatColor.RED + "您的版本不支援該附魔類型");
                                return false;
                            }
                            enchid = Short.valueOf(arg3[1]);
                            level = Short.valueOf(arg3[2]);
                            if (level < 0) {
                                player.sendMessage(ChatColor.RED + "您想附魔負數等級 ? 這是不安全的操作");
                                return false;
                            }
                        } catch (Exception ex) {
                            sendInvavidMessage(player);
                            return false;
                        }
                        try {
                            is = AttributesEditorAPI.addStoredEnchantment(is, enchid, level);
                            AttributesEditorAPI.setItemInHand(player, is);
                            player.sendMessage(ChatColor.GREEN + "附魔書屬性新增成功");
                            return true;
                        } catch (Exception ex) {
                            AttributesEditorAPI.printStackTrace(player, ex);
                            return false;
                        }
                    }
                    if (arg3[0].equalsIgnoreCase("firework")) {
                        if (!arg3[1].equalsIgnoreCase("flight")) {
                            return false;
                        }
                        byte flight;
                        try {
                            flight = Byte.valueOf(arg3[2]);
                        } catch (Exception ex) {
                            sendInvavidMessage(player);
                            return false;
                        }
                        try {
                            is = AttributesEditorAPI.changeFireworksFlightTime(is, flight);
                            AttributesEditorAPI.setItemInHand(player, is);
                            player.sendMessage(ChatColor.GREEN + "煙火飛行時間修改成功");
                            return true;
                        } catch (Exception ex) {
                            AttributesEditorAPI.printStackTrace(player, ex);
                            return false;
                        }
                    }
                    if (arg3[0].equalsIgnoreCase("enchant")) {
                        int enchantType, level;
                        try {
                            enchantType = Integer.valueOf(arg3[1]);
                            level = Integer.valueOf(arg3[2]);
                            if (!AttributesEditorAPI.isAllowedEnchantment(enchantType)) {
                                player.sendMessage(ChatColor.RED + "您的版本不支援該附魔類型");
                                return false;
                            }
                            if (level < 0) {
                                player.sendMessage(ChatColor.RED + "您想附魔負數等級 ? 這是不安全的操作");
                                return false;
                            }
                        } catch (Exception ex) {
                            sendInvavidMessage(player);
                            return false;
                        }
                        if (level > 0) {
                            is.addUnsafeEnchantment(Enchantment.getById(enchantType), level);
                        } else if (level == 0) {
                            if (is.containsEnchantment(Enchantment.getById(enchantType))) {
                                is.removeEnchantment(Enchantment.getById(enchantType));
                                AttributesEditorAPI.setItemInHand(player, is);
                                player.sendMessage(ChatColor.GREEN + "成功移除附魔");
                                return true;
                            } else {
                                player.sendMessage(ChatColor.RED + "您手中的道具不存在該附魔");
                                return false;
                            }
                        }
                        AttributesEditorAPI.setItemInHand(player, is);
                        player.sendMessage(ChatColor.GREEN + "修改附魔成功");
                        return true;
                    }
                    if (arg3[0].equalsIgnoreCase("lore")) {
                        int location;
                        try {
                            location = Integer.valueOf(arg3[1]) - 1;
                            if (location < 0) {
                                player.sendMessage(ChatColor.RED + "Lore 位置越界 無法修改");
                                return false;
                            }
                        } catch (Exception ex) {
                            sendInvavidMessage(player);
                            return false;
                        }
                        ItemMeta im = is.getItemMeta();
                        List<String> nowLore = im.hasLore() ? im.getLore() : new ArrayList<>();
                        int loreLength = nowLore.size();
                        if (location > loreLength) {
                            player.sendMessage(ChatColor.RED + "Lore 位置越界 無法修改");
                            return false;
                        }
                        if (location == loreLength) {
                            if (!arg3[2].equalsIgnoreCase("remove")) {
                                nowLore.add(ChatColor.translateAlternateColorCodes('&', arg3[2]));
                            } else {
                                player.sendMessage(ChatColor.RED + "Lore 位置越界 無法修改");
                                return false;
                            }
                        } else {
                            if (!arg3[2].equalsIgnoreCase("remove")) {
                                nowLore.set(location, ChatColor.translateAlternateColorCodes('&', arg3[2]));
                            } else {
                                nowLore.remove(location);
                            }
                        }
                        im.setLore(nowLore);
                        is.setItemMeta(im);
                        AttributesEditorAPI.setItemInHand(player, is);
                        player.sendMessage(ChatColor.GREEN + "修改 Lore 成功");
                        return true;
                    }
                    if (arg3[0].equalsIgnoreCase("book")) {
                        int page;
                        try {
                            BookMeta im = (BookMeta) is.getItemMeta();
                            if (arg3[1].equalsIgnoreCase("author")) {
                                im.setAuthor(ChatColor.translateAlternateColorCodes('&', arg3[2]));
                                is.setItemMeta(im);
                                AttributesEditorAPI.setItemInHand(player, is);
                                player.sendMessage(ChatColor.GREEN + "修改作者成功");
                                return true;
                            }
                            if (arg3[1].equalsIgnoreCase("title")) {
                                im.setTitle(ChatColor.translateAlternateColorCodes('&', arg3[2]));
                                is.setItemMeta(im);
                                AttributesEditorAPI.setItemInHand(player, is);
                                player.sendMessage(ChatColor.GREEN + "修改標題成功");
                                return true;
                            }
                            if (arg3[1].equalsIgnoreCase("generation")) {
                                try {
                                    int type = Integer.valueOf(arg3[2]);
                                    if (AttributesEditorAPI.isAllowedGeneration(type)) {
                                        im.setGeneration(AttributesEditorAPI.decodeGeneration(type));
                                        is.setItemMeta(im);
                                        AttributesEditorAPI.setItemInHand(player, is);
                                        player.sendMessage(ChatColor.GREEN + "修改成書版本成功");
                                        return true;
                                    } else {
                                        player.sendMessage(ChatColor.RED + "您的版本不存在該成書版本數值");
                                        return false;
                                    }
                                } catch (Exception ex) {
                                    player.sendMessage(ChatColor.RED + "您的版本無法進行該操作");
                                    return false;
                                }
                            }
                            if (arg3[1].equalsIgnoreCase("remove")) {
                                if (!arg3[2].equalsIgnoreCase("all")) {
                                    page = Integer.valueOf(arg3[2]);
                                    if (page > im.getPageCount() || page <= 0) {
                                        player.sendMessage(ChatColor.RED + "不存在的頁數或無法移除");
                                        return false;
                                    }
                                    List<String> pages = im.getPages();
                                    List<String> cache = new ArrayList<>();
                                    for (Object c : pages) {
                                        String s = (String) c;
                                        cache.add(s);
                                    }
                                    cache.remove(page - 1);
                                    im.setPages(cache);
                                    is.setItemMeta(im);
                                    AttributesEditorAPI.setItemInHand(player, is);
                                    player.sendMessage(ChatColor.GREEN + "移除指定頁數成功");
                                } else {
                                    im.setPages(Arrays.asList(""));
                                    is.setItemMeta(im);
                                    AttributesEditorAPI.setItemInHand(player, is);
                                    player.sendMessage(ChatColor.GREEN + "移除全部頁數成功");
                                }
                                return true;
                            } else {
                                page = Integer.valueOf(arg3[1]);
                                if (page > im.getPageCount() || page <= 0) {
                                    player.sendMessage(ChatColor.RED + "不存在的頁數");
                                    return false;
                                }
                                im.setPage(page, ChatColor.translateAlternateColorCodes('&', arg3[2]));
                                is.setItemMeta(im);
                                AttributesEditorAPI.setItemInHand(player, is);
                                player.sendMessage(ChatColor.GREEN + "修改指定頁數成功");
                                return true;
                            }
                        } catch (ClassCastException ex) {
                            player.sendMessage(ChatColor.RED + "您手中的物品不是書類物品或參數無效");
                            return false;
                        }
                    }
                }
                if (arg3.length == 4) {
                    if (!arg3[0].equalsIgnoreCase("add")) {
                        return false;
                    }
                    int type, slot;
                    double amount;
                    try {
                        type = Integer.valueOf(arg3[1]);
                        slot = Integer.valueOf(arg3[2]);
                        amount = Double.valueOf(arg3[3]);
                    } catch (Exception ex) {
                        sendInvavidMessage(player);
                        return false;
                    }
                    List<String> cache = AttributesEditorAPI.decodeTypeAndSlot(type, slot);
                    try {
                        is = AttributesEditorAPI.addAttribute(is, cache.get(0), cache.get(1), amount);
                        AttributesEditorAPI.setItemInHand(player, is);
                        player.sendMessage(ChatColor.GREEN + "藍字屬性新增成功");
                        System.out.println(is);
                        return true;
                    } catch (Exception ex) {
                        AttributesEditorAPI.printStackTrace(player, ex);
                        return false;
                    }
                }
                if (arg3.length == 5) {
                    if (!arg3[0].equalsIgnoreCase("potion")) {
                        return false;
                    }
                    byte id, amplifier, showParticles;
                    int duration;
                    try {
                        id = Byte.valueOf(arg3[1]);
                        if (!AttributesEditorAPI.isAllowedPotionEffect(id)) {
                            player.sendMessage(ChatColor.RED + "您的版本不支援該藥水效果");
                            return false;
                        }
                        int inputAmp = Integer.valueOf(arg3[2]);
                        if (inputAmp <= 0 || inputAmp > 128) {
                            throw new Exception();
                        }
                        amplifier = (byte) (inputAmp - 1);
                        duration = Integer.valueOf(arg3[3]) * 20;
                        int c = Integer.valueOf(arg3[4]);
                        if (c != 0 && c != 1) {
                            throw new Exception();
                        }
                        showParticles = Byte.valueOf(arg3[4]);
                    } catch (Exception ex) {
                        sendInvavidMessage(player);
                        return false;
                    }
                    try {
                        is = AttributesEditorAPI.addPotionEffect(is, id, amplifier, showParticles, duration);
                        AttributesEditorAPI.setItemInHand(player, is);
                        player.sendMessage(ChatColor.GREEN + "藥水效果新增成功");
                        return true;
                    } catch (Exception ex) {
                        AttributesEditorAPI.printStackTrace(player, ex);
                        return false;
                    }
                }
                if (arg3.length == 10) {
                    if (!arg3[0].equalsIgnoreCase("fwstar") && !arg3[0].equalsIgnoreCase("firework")) {
                        return false;
                    }
                    if (arg3[0].equalsIgnoreCase("firework")) {
                        byte flicker, trail, type;
                        int colors, fadeColors;
                        try {
                            int c1 = Integer.valueOf(arg3[1]);
                            int c2 = Integer.valueOf(arg3[2]);
                            int c3 = Integer.valueOf(arg3[3]);
                            if (c1 != 0 && c1 != 1) {
                                throw new Exception();
                            }
                            if (c2 != 0 && c2 != 1) {
                                throw new Exception();
                            }
                            if (!AttributesEditorAPI.isAllowedFireworkType(c3)) {
                                player.sendMessage(ChatColor.RED + "您的版本不支援該煙火爆炸形態");
                                return false;
                            }
                            flicker = Byte.valueOf(arg3[1]);
                            trail = Byte.valueOf(arg3[2]);
                            type = Byte.valueOf(arg3[3]);
                            colors = AttributesEditorAPI.decodeColors(Integer.valueOf(arg3[4]), Integer.valueOf(arg3[5]), Integer.valueOf(arg3[6]));
                            fadeColors = AttributesEditorAPI.decodeColors(Integer.valueOf(arg3[7]), Integer.valueOf(arg3[8]), Integer.valueOf(arg3[9]));
                            if (colors >= 16777216 || fadeColors >= 16777216) {
                                player.sendMessage(ChatColor.RED + "RGB 顏色溢出 [三色值各應不大於 255 且應不小於 0]");
                                return false;
                            }
                        } catch (Exception ex) {
                            sendInvavidMessage(player);
                            return false;
                        }

                        try {
                            is = AttributesEditorAPI.addFireworks(is, flicker, trail, type, colors, fadeColors);
                            AttributesEditorAPI.setItemInHand(player, is);
                            player.sendMessage(ChatColor.GREEN + "煙火屬性新增成功");
                            return true;
                        } catch (Exception ex) {
                            AttributesEditorAPI.printStackTrace(player, ex);
                            return false;
                        }
                    }
                    byte flicker, trail, type;
                    int colors, fadeColors;
                    try {
                        int c1 = Integer.valueOf(arg3[1]);
                        int c2 = Integer.valueOf(arg3[2]);
                        int c3 = Integer.valueOf(arg3[3]);
                        if (c1 != 0 && c1 != 1) {
                            throw new Exception();
                        }
                        if (c2 != 0 && c2 != 1) {
                            throw new Exception();
                        }
                        if (!AttributesEditorAPI.isAllowedFireworkType(c3)) {
                            player.sendMessage(ChatColor.RED + "您的版本不支援該煙火爆炸形態");
                            return false;
                        }
                        flicker = Byte.valueOf(arg3[1]);
                        trail = Byte.valueOf(arg3[2]);
                        type = Byte.valueOf(arg3[3]);
                        colors = AttributesEditorAPI.decodeColors(Integer.valueOf(arg3[4]), Integer.valueOf(arg3[5]), Integer.valueOf(arg3[6]));
                        fadeColors = AttributesEditorAPI.decodeColors(Integer.valueOf(arg3[7]), Integer.valueOf(arg3[8]), Integer.valueOf(arg3[9]));
                        if (colors >= 16777216 || fadeColors >= 16777216) {
                            player.sendMessage(ChatColor.RED + "RGB 顏色溢出 [三色值各應不大於 255 且應不小於 0]");
                            return false;
                        }
                    } catch (Exception ex) {
                        sendInvavidMessage(player);
                        return false;
                    }
                    try {
                        is = AttributesEditorAPI.changeFireworkCharge(is, flicker, trail, type, colors, fadeColors);
                        AttributesEditorAPI.setItemInHand(player, is);
                        player.sendMessage(ChatColor.GREEN + "煙火之星修改成功");
                        return true;
                    } catch (Exception ex) {
                        AttributesEditorAPI.printStackTrace(player, ex);
                        return false;
                    }
                }
            } else {
                arg0.sendMessage("必須是玩家才能執行該指令");
                return false;
            }
            return false;
        } catch (Exception ex) {
            arg0.sendMessage(ChatColor.RED + "指令執行時發生例外狀況 !");
            return false;
        }
    }

    private void sendInvavidMessage(Player player) {
        player.sendMessage(ChatColor.RED + "您輸入的參數無效");
    }
}
