package ew.quilt.Enchant;

import ew.quilt.Config.ConfigManager;
import ew.quilt.Config.Version.SimpleVersion;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class Enchant {

    private static final boolean ENCHANT_UNSAFE = true;
    private static final boolean ENCHANT_BOOK = true;

    static void enchantInfo(Player player) {
        PlayerInventory playerInventory = player.getInventory();
        ItemStack stack = playerInventory.getItemInHand();
        String itemName = stack.getType().name().replaceAll("_", " ");
        itemName = WordUtils.capitalizeFully(itemName);
        Map<Enchantment, Integer> enchantmentMap = new HashMap<>();
        if (stack.hasItemMeta()) {
            ItemMeta im = stack.getItemMeta();
            if (im.hasDisplayName()) {
                itemName = im.getDisplayName();
            }
            if (im.hasEnchants()) {
                enchantmentMap = im.getEnchants();
            }
        }
        int count = 0;

        player.sendMessage(String.format("道具 %s 包含以下附魔 :", new Object[]{itemName}));
        for (Map.Entry<Enchantment, Integer> entry : enchantmentMap.entrySet()) {
            Enchantment enchantment = (Enchantment) entry.getKey();
            int level = entry.getValue();
            ++count;
            player.sendMessage(String.format("%s", new Object[]{prettyEnchant(enchantment, level)}));
        }
        if (count == 0) {
            player.sendMessage("無");
        }
    }

    private static void removeEnchantment(ItemStack stack, Map<Enchantment, Integer> map) {
        Set<Enchantment> enchantmentSet = map.keySet();
        for (Enchantment enchantment : enchantmentSet) {
            stack.removeEnchantment(enchantment);
        }
    }

    static void enchantNone(Player player) {
        PlayerInventory playerInventory = player.getInventory();
        ItemStack stack = playerInventory.getItemInHand();
        Map<Enchantment, Integer> enchantmentMap = stack.getEnchantments();
        for (Map.Entry<Enchantment, Integer> entry : enchantmentMap.entrySet()) {
            Enchantment enchantment = (Enchantment) entry.getKey();
            stack.removeEnchantment(enchantment);
        }
    }

    static void enchantGod(Player player) {
        PlayerInventory playerInventory = player.getInventory();
        ItemStack stack = playerInventory.getItemInHand();
        if (stack.getAmount() == 1) {
            short durability = stack.getDurability();
            short unused = 0;
            if (durability != 0) {
                stack.setDurability(unused);
            }
            stack.addUnsafeEnchantments(ALL);
            removeEnchantment(stack, BAD);
            playerInventory.setItemInHand(stack);
        } else {
            player.sendMessage("無法附魔多個道具");
        }
    }

    static void enchantAll(Player player) {
        PlayerInventory playerInventory = player.getInventory();
        ItemStack stack = playerInventory.getItemInHand();
        if (stack.getAmount() == 1) {
            Material item = stack.getType();
            short durability = stack.getDurability();
            if (durability == 0) {
                if (item.name().endsWith("_HELMET")) {
                    giveAllEnchantment(player, HELMET, false);
                } else if (item.name().endsWith("_CHESTPLATE")) {
                    giveAllEnchantment(player, CHEST_PLATE, false);
                } else if (item.name().endsWith("_LEGGINGS")) {
                    giveAllEnchantment(player, LEGGING, false);
                } else if (item.name().endsWith("_BOOTS")) {
                    giveAllEnchantment(player, BOOT, false);
                } else if (item.name().endsWith("_SWORD")) {
                    giveAllEnchantment(player, SWORD, false);
                } else if (item.name().endsWith("_PICKAXE")) {
                    giveAllEnchantment(player, PICKAXE, false);
                } else if (item.name().endsWith("_AXE")) {
                    giveAllEnchantment(player, AXE, false);
                } else if (item.name().endsWith("_SPADE")) {
                    giveAllEnchantment(player, SPADE, false);
                } else if (item.name().endsWith("_HOE")) {
                    giveAllEnchantment(player, HOE, false);
                } else if (item.name().equalsIgnoreCase("SHEARS")) {
                    giveAllEnchantment(player, SHEAR, false);
                } else if (item.name().equalsIgnoreCase("FLINT_AND_STEEL")) {
                    giveAllEnchantment(player, FLINT_AND_STEEL, false);
                } else if (item.name().equalsIgnoreCase("BOW")) {
                    giveAllEnchantment(player, BOW, false);
                } else if (item.name().equalsIgnoreCase("FISHING_ROD")) {
                    giveAllEnchantment(player, FISHING_ROD, false);
                } else if (item.name().equalsIgnoreCase("CARROT_STICK")) {
                    giveAllEnchantment(player, CARROT_ON_A_STICK, false);
                } else if (item.name().equalsIgnoreCase("BOOK") && ENCHANT_BOOK) {
                    giveAllEnchantment(player, BOOK, true);
                } else if (item.name().equalsIgnoreCase("ENCHANTED_BOOK") && ENCHANT_BOOK) {
                    giveAllEnchantment(player, BOOK, true);
                } else if (item.name().equalsIgnoreCase("SHIELD")) {
                    giveAllEnchantment(player, SHIELD, false);
                } else if (item.name().equalsIgnoreCase("ELYTRA")) {
                    giveAllEnchantment(player, ELYTRA, false);
                } else {
                    player.sendMessage("無法對該道具進行附魔");
                }
            } else {
                player.sendMessage("無法對已使用過道具進行附魔");
            }
        } else {
            player.sendMessage("無法附魔多個道具");
        }
    }

    private static void giveAllEnchantment(Player player, Map<Enchantment, Integer> enchant, boolean unsafe) {
        PlayerInventory playerInventory = player.getInventory();
        ItemStack itemStack = playerInventory.getItemInHand();
        if (itemStack != null) {
            if (unsafe) {
                if (itemStack.getType().equals(Material.BOOK)) {
                    itemStack.setType(Material.ENCHANTED_BOOK);
                }
                itemStack.addUnsafeEnchantments(enchant);
            } else {
                itemStack.addEnchantments(enchant);
            }
            removeEnchantment(itemStack, BAD);
            playerInventory.setItemInHand(itemStack);
        }
    }

    static void enchantSingle(Player player, String enchant, int level) {
        PlayerInventory playerInventory = player.getInventory();
        ItemStack stack = playerInventory.getItemInHand();
        if (stack.getAmount() == 1) {
            Material item = stack.getType();
            short durability = stack.getDurability();
            if (durability == 0) {
                Enchantment enchantment = Enchantment.getByName(enchant);
                if (item.name().endsWith("_HELMET")) {
                    giveSingleEnchantment(player, HELMET, enchantment, level);
                } else if (item.name().endsWith("_CHESTPLATE")) {
                    giveSingleEnchantment(player, CHEST_PLATE, enchantment, level);
                } else if (item.name().endsWith("_LEGGINGS")) {
                    giveSingleEnchantment(player, LEGGING, enchantment, level);
                } else if (item.name().endsWith("_BOOTS")) {
                    giveSingleEnchantment(player, BOOT, enchantment, level);
                } else if (item.name().endsWith("_SWORD")) {
                    giveSingleEnchantment(player, SWORD, enchantment, level);
                } else if (item.name().endsWith("_PICKAXE")) {
                    giveSingleEnchantment(player, PICKAXE, enchantment, level);
                } else if (item.name().endsWith("_AXE")) {
                    giveSingleEnchantment(player, AXE, enchantment, level);
                } else if (item.name().endsWith("_SPADE")) {
                    giveSingleEnchantment(player, SPADE, enchantment, level);
                } else if (item.name().endsWith("_HOE")) {
                    giveSingleEnchantment(player, HOE, enchantment, level);
                } else if (item.name().equalsIgnoreCase("SHEARS")) {
                    giveSingleEnchantment(player, SHEAR, enchantment, level);
                } else if (item.name().equalsIgnoreCase("FLINT_AND_STEEL")) {
                    giveSingleEnchantment(player, FLINT_AND_STEEL, enchantment, level);
                } else if (item.name().equalsIgnoreCase("BOW")) {
                    giveSingleEnchantment(player, BOW, enchantment, level);
                } else if (item.name().equalsIgnoreCase("FISHING_ROD")) {
                    giveSingleEnchantment(player, FISHING_ROD, enchantment, level);
                } else if (item.name().equalsIgnoreCase("CARROT_STICK")) {
                    giveSingleEnchantment(player, CARROT_ON_A_STICK, enchantment, level);
                } else if (item.name().equalsIgnoreCase("BOOK") && ENCHANT_BOOK) {
                    giveSingleEnchantment(player, BOOK, enchantment, level);
                } else if (item.name().equalsIgnoreCase("ENCHANTED_BOOK") && ENCHANT_BOOK) {
                    giveSingleEnchantment(player, BOOK, enchantment, level);
                } else if (item.name().equalsIgnoreCase("SHIELD")) {
                    giveSingleEnchantment(player, SHIELD, enchantment, level);
                } else if (item.name().equalsIgnoreCase("ELYTRA")) {
                    giveSingleEnchantment(player, ELYTRA, enchantment, level);
                } else if (ENCHANT_UNSAFE) {
                    if (level == 0) {
                        if (stack.containsEnchantment(enchantment)) {
                            stack.removeEnchantment(enchantment);
                        }
                    } else {
                        stack.addUnsafeEnchantment(enchantment, level);
                    }
                    playerInventory.setItemInHand(stack);
                } else {
                    player.sendMessage("無法對該道具進行附魔");
                }
            } else {
                player.sendMessage("無法對已使用過道具進行附魔");
            }
        } else {
            player.sendMessage("無法附魔多個道具");
        }
    }

    private static void giveSingleEnchantment(Player player, Map<Enchantment, Integer> allowed, Enchantment enchantment, int level) {
        if (allowed.containsKey(enchantment) || ENCHANT_UNSAFE) {
            PlayerInventory playerInventory = player.getInventory();
            ItemStack stack = playerInventory.getItemInHand();
            if (level == 0) {
                if (stack.containsEnchantment(enchantment)) {
                    stack.removeEnchantment(enchantment);
                    playerInventory.setItemInHand(stack);
                }
            } else {
                if (stack.getType().equals(Material.BOOK)) {
                    stack.setType(Material.ENCHANTED_BOOK);
                }
                level = ENCHANT_UNSAFE ? level : Math.min(allowed.get(enchantment), level);
                stack.addUnsafeEnchantment(enchantment, level);
                playerInventory.setItemInHand(stack);
            }
        }
    }

    static void showSyntax(CommandSender sender) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&C指令使用方法 : /enchant [附魔類型] [等級]"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&C/enchant [all god none info type]"));
    }

    static void showType(CommandSender sender) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6盔甲附魔 :"));
        sender.sendMessage("0 protection");
        sender.sendMessage("1 fire_protection");
        sender.sendMessage("2 feather_falling");
        sender.sendMessage("3 blast_protection");
        sender.sendMessage("4 projectile_protection");
        sender.sendMessage("5 respiration");
        sender.sendMessage("6 aqua_affinity");
        sender.sendMessage("7 thorns");
        sender.sendMessage("8 depth_strider");
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_9) >= 0) {
            sender.sendMessage("9 frost_walker");
        }
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_11) >= 0) {
            sender.sendMessage("10 binding_curse");
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6武器附魔 :"));
        sender.sendMessage("16 sharpness");
        sender.sendMessage("17 smite");
        sender.sendMessage("18 bane_of_arthropods");
        sender.sendMessage("19 knockback");
        sender.sendMessage("20 fire_aspect");
        sender.sendMessage("21 looting");
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_11_1) >= 0) {
            sender.sendMessage("22 sweeping_edge");
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6工具附魔 :"));
        sender.sendMessage("32 efficiency");
        sender.sendMessage("33 silk_touch");
        sender.sendMessage("34 unbreaking");
        sender.sendMessage("35 fortune");
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6弓附魔 :"));
        sender.sendMessage("48 power");
        sender.sendMessage("49 punch");
        sender.sendMessage("50 flame");
        sender.sendMessage("51 infinity");
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6釣竿附魔 :"));
        sender.sendMessage("61 luck");
        sender.sendMessage("62 lure");
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6其他附魔 :"));
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_9) >= 0) {
            sender.sendMessage("70 mending");
        }
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_11) >= 0) {
            sender.sendMessage("71 vanishing_curse");
        }
    }

    private static String prettyEnchant(Enchantment enchantment, int level) {
        String stringLevel = String.valueOf(level);
        switch (level) {
            case 1:
                stringLevel = "I";
                break;
            case 2:
                stringLevel = "II";
                break;
            case 3:
                stringLevel = "III";
                break;
            case 4:
                stringLevel = "IV";
                break;
            case 5:
                stringLevel = "V";
                break;
            case 6:
                stringLevel = "VI";
                break;
            case 7:
                stringLevel = "VII";
                break;
            case 8:
                stringLevel = "VIII";
                break;
            case 9:
                stringLevel = "IX";
                break;
            case 10:
                stringLevel = "X";
        }
        switch (enchantment.getName()) {
            case "PROTECTION_ENVIRONMENTAL":
                return String.format("保護 %s", new Object[]{stringLevel});
            case "PROTECTION_FIRE":
                return String.format("防火 %s", new Object[]{stringLevel});
            case "PROTECTION_FALL":
                return String.format("輕盈 %s", new Object[]{stringLevel});
            case "PROTECTION_EXPLOSIONS":
                return String.format("防爆 %s", new Object[]{stringLevel});
            case "PROTECTION_PROJECTILE":
                return String.format("防彈 %s", new Object[]{stringLevel});
            case "OXYGEN":
                return String.format("水中呼吸 %s", new Object[]{stringLevel});
            case "WATER_WORKER":
                return String.format("親水性 %s", new Object[]{level > 1 ? stringLevel : ""});
            case "THORNS":
                return String.format("尖刺 %s", new Object[]{stringLevel});
            case "DEPTH_STRIDER":
                return String.format("深海漫遊 %s", new Object[]{stringLevel});
            case "FROST_WALKER":
                return String.format("冰霜行者 %s", new Object[]{stringLevel});
            case "BINDING_CURSE":
                return String.format("綁定詛咒 %s", new Object[]{level > 1 ? stringLevel : ""});
            case "DAMAGE_ALL":
                return String.format("鋒利 %s", new Object[]{stringLevel});
            case "DAMAGE_UNDEAD":
                return String.format("不死剋星 %s", new Object[]{stringLevel});
            case "DAMAGE_ARTHROPODS":
                return String.format("節肢剋星 %s", new Object[]{stringLevel});
            case "KNOCKBACK":
                return String.format("擊退 %s", new Object[]{stringLevel});
            case "FIRE_ASPECT":
                return String.format("燃燒 %s", new Object[]{stringLevel});
            case "LOOT_BONUS_MOBS":
                return String.format("掠奪 %s", new Object[]{stringLevel});
            case "SWEEPING":
                return String.format("劍氣 %s", new Object[]{stringLevel});
            case "SWEEPING_EDGE":
                return String.format("劍氣 %s", new Object[]{stringLevel});
            case "DIG_SPEED":
                return String.format("效率 %s", new Object[]{stringLevel});
            case "SILK_TOUCH":
                return String.format("絲綢之觸 %s", new Object[]{level > 1 ? stringLevel : ""});
            case "DURABILITY":
                return String.format("耐久 %s", new Object[]{stringLevel});
            case "LOOT_BONUS_BLOCKS":
                return String.format("幸運 %s", new Object[]{stringLevel});
            case "ARROW_DAMAGE":
                return String.format("強力 %s", new Object[]{stringLevel});
            case "ARROW_KNOCKBACK":
                return String.format("擊退 %s", new Object[]{stringLevel});
            case "ARROW_FIRE":
                return String.format("火焰 %s", new Object[]{level > 1 ? stringLevel : ""});
            case "ARROW_INFINITE":
                return String.format("無限 %s", new Object[]{level > 1 ? stringLevel : ""});
            case "LUCK":
                return String.format("海洋的祝福 %s", new Object[]{stringLevel});
            case "LURE":
                return String.format("魚餌 %s", new Object[]{stringLevel});
            case "MENDING":
                return String.format("經驗修補 %s", new Object[]{level > 1 ? stringLevel : ""});
            case "VANISHING_CURSE":
                return String.format("消失詛咒 %s", new Object[]{level > 1 ? stringLevel : ""});
        }
        return "未知";
    }

    static String parseEnchantment(String enchantment) {
        switch (enchantment.toLowerCase()) {
            case "0":
            case "protection":
            case "protection_environmental":
            case "prot":
                return "PROTECTION_ENVIRONMENTAL";
            case "1":
            case "fire":
            case "fire_protection":
            case "protection_fire":
            case "fireprot":
                return "PROTECTION_FIRE";
            case "2":
            case "feather":
            case "fall":
            case "feather_falling":
            case "featherfall":
            case "featherfalling":
                return "PROTECTION_FALL";
            case "3":
            case "blast":
            case "blast_protection":
            case "protection_blast":
            case "blastprot":
                return "PROTECTION_EXPLOSIONS";
            case "4":
            case "projectile":
            case "projectile_protection":
            case "protection_projectile":
            case "projprot":
                return "PROTECTION_PROJECTILE";
            case "5":
            case "respiration":
            case "resp":
            case "air":
            case "oxygen":
                return "OXYGEN";
            case "6":
            case "aqua_affinity":
            case "aqua":
            case "water_worker":
                return "WATER_WORKER";
            case "7":
            case "thorns":
                return "THORNS";
            case "8":
            case "depth":
            case "strider":
            case "depth_strider":
            case "depthstrider":
                return "DEPTH_STRIDER";
            case "9":
            case "frost_walker":
            case "frost":
                return "FROST_WALKER";
            case "10":
            case "binding_curse":
            case "curse_binding":
            case "curse_bind":
            case "binding":
            case "bind":
                return "BINDING_CURSE";
            case "16":
            case "sharpness":
            case "sharp":
            case "damage_all":
                return "DAMAGE_ALL";
            case "17":
            case "smite":
            case "damage_undead":
                return "DAMAGE_UNDEAD";
            case "18":
            case "bane_of_arthropods":
            case "bane":
            case "damage_arthropods":
                return "DAMAGE_ARTHROPODS";
            case "19":
            case "knockback":
                return "KNOCKBACK";
            case "20":
            case "fire_aspect":
            case "fireapect":
                return "FIRE_ASPECT";
            case "21":
            case "looting":
            case "loot":
            case "loot_bonus_mobs":
                return "LOOT_BONUS_MOBS";
            case "22":
            case "sweep":
            case "sweeping":
            case "sweeping_edge":
                return ConfigManager.SIMPLE_VERSION.equals(SimpleVersion.v1_11_1) || ConfigManager.SIMPLE_VERSION.equals(SimpleVersion.v1_11_2) ? "SWEEPING" : "SWEEPING_EDGE";
            case "32":
            case "efficiency":
            case "eff":
            case "dig_speed":
                return "DIG_SPEED";
            case "33":
            case "silk_touch":
            case "silk":
                return "SILK_TOUCH";
            case "34":
            case "unbreaking":
            case "unbreak":
            case "durability":
                return "DURABILITY";
            case "35":
            case "fortune":
            case "loot_bonus_blocks":
                return "LOOT_BONUS_BLOCKS";
            case "48":
            case "power":
            case "arrow_damage":
                return "ARROW_DAMAGE";
            case "49":
            case "punch":
            case "arrow_knockback":
                return "ARROW_KNOCKBACK";
            case "50":
            case "flame":
            case "arrow_fire":
                return "ARROW_FIRE";
            case "51":
            case "infinity":
            case "arrow_infinite":
                return "ARROW_INFINITE";
            case "61":
            case "luck_of_the_sea":
            case "luck":
                return "LUCK";
            case "62":
            case "lure":
                return "LURE";
            case "70":
            case "mending":
            case "mend":
                return "MENDING";
            case "71":
            case "curse_vanish":
            case "vanish":
            case "vanishing_curse":
                return "VANISHING_CURSE";
        }
        return null;
    }

    private static final Map<Enchantment, Integer> ALL = new HashMap<>();
    private static final Map<Enchantment, Integer> BAD;
    private static final Map<Enchantment, Integer> HELMET;
    private static final Map<Enchantment, Integer> CHEST_PLATE;
    private static final Map<Enchantment, Integer> LEGGING;
    private static final Map<Enchantment, Integer> BOOT;
    private static final Map<Enchantment, Integer> SWORD;
    private static final Map<Enchantment, Integer> PICKAXE;
    private static final Map<Enchantment, Integer> AXE;
    private static final Map<Enchantment, Integer> SPADE;
    private static final Map<Enchantment, Integer> HOE;
    private static final Map<Enchantment, Integer> SHEAR;
    private static final Map<Enchantment, Integer> FLINT_AND_STEEL;
    private static final Map<Enchantment, Integer> BOW;
    private static final Map<Enchantment, Integer> FISHING_ROD;
    private static final Map<Enchantment, Integer> CARROT_ON_A_STICK;
    private static final Map<Enchantment, Integer> SHIELD;
    private static final Map<Enchantment, Integer> ELYTRA;
    private static final Map<Enchantment, Integer> BOOK;

    static {
        ALL.put(Enchantment.getByName("PROTECTION_ENVIRONMENTAL"), 4);
        ALL.put(Enchantment.getByName("PROTECTION_FIRE"), 4);
        ALL.put(Enchantment.getByName("PROTECTION_FALL"), 4);
        ALL.put(Enchantment.getByName("PROTECTION_EXPLOSIONS"), 4);
        ALL.put(Enchantment.getByName("PROTECTION_PROJECTILE"), 4);
        ALL.put(Enchantment.getByName("OXYGEN"), 3);
        ALL.put(Enchantment.getByName("WATER_WORKER"), 1);
        ALL.put(Enchantment.getByName("THORNS"), 3);
        ALL.put(Enchantment.getByName("DEPTH_STRIDER"), 3);
        ALL.put(Enchantment.getByName("DAMAGE_ALL"), 5);
        ALL.put(Enchantment.getByName("DAMAGE_UNDEAD"), 5);
        ALL.put(Enchantment.getByName("DAMAGE_ARTHROPODS"), 5);
        ALL.put(Enchantment.getByName("KNOCKBACK"), 2);
        ALL.put(Enchantment.getByName("FIRE_ASPECT"), 2);
        ALL.put(Enchantment.getByName("LOOT_BONUS_MOBS"), 3);
        ALL.put(Enchantment.getByName("DIG_SPEED"), 5);
        ALL.put(Enchantment.getByName("SILK_TOUCH"), 1);
        ALL.put(Enchantment.getByName("DURABILITY"), 3);
        ALL.put(Enchantment.getByName("LOOT_BONUS_BLOCKS"), 3);
        ALL.put(Enchantment.getByName("ARROW_DAMAGE"), 5);
        ALL.put(Enchantment.getByName("ARROW_KNOCKBACK"), 2);
        ALL.put(Enchantment.getByName("ARROW_FIRE"), 1);
        ALL.put(Enchantment.getByName("ARROW_INFINITE"), 1);
        ALL.put(Enchantment.getByName("LUCK"), 3);
        ALL.put(Enchantment.getByName("LURE"), 3);
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_9) >= 0) {
            ALL.put(Enchantment.getByName("FROST_WALKER"), 2);
        }
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_9) >= 0) {
            ALL.put(Enchantment.getByName("MENDING"), 1);
        }
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_11) >= 0) {
            ALL.put(Enchantment.getByName("BINDING_CURSE"), 1);
        }
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_11) >= 0) {
            ALL.put(Enchantment.getByName("VANISHING_CURSE"), 1);
        }
        if (ConfigManager.SIMPLE_VERSION.equals(SimpleVersion.v1_11_1) || ConfigManager.SIMPLE_VERSION.equals(SimpleVersion.v1_11_2)) {
            ALL.put(Enchantment.getByName("SWEEPING"), 3);
        }
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_12) >= 0) {
            ALL.put(Enchantment.getByName("SWEEPING_EDGE"), 3);
        }

        BAD = new HashMap<>();
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_11) >= 0) {
            BAD.put(Enchantment.getByName("BINDING_CURSE"), 1);
        }
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_11) >= 0) {
            BAD.put(Enchantment.getByName("VANISHING_CURSE"), 1);
        }

        HELMET = new HashMap<>();
        HELMET.put(Enchantment.getByName("PROTECTION_ENVIRONMENTAL"), 4);
        HELMET.put(Enchantment.getByName("PROTECTION_FIRE"), 4);
        HELMET.put(Enchantment.getByName("PROTECTION_EXPLOSIONS"), 4);
        HELMET.put(Enchantment.getByName("PROTECTION_PROJECTILE"), 4);
        HELMET.put(Enchantment.getByName("OXYGEN"), 3);
        HELMET.put(Enchantment.getByName("WATER_WORKER"), 1);
        HELMET.put(Enchantment.getByName("THORNS"), 3);
        HELMET.put(Enchantment.getByName("DURABILITY"), 3);
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_9) >= 0) {
            HELMET.put(Enchantment.getByName("MENDING"), 1);
        }
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_11) >= 0) {
            HELMET.put(Enchantment.getByName("BINDING_CURSE"), 1);
        }
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_11) >= 0) {
            HELMET.put(Enchantment.getByName("VANISHING_CURSE"), 1);
        }

        CHEST_PLATE = new HashMap<>();
        CHEST_PLATE.put(Enchantment.getByName("PROTECTION_ENVIRONMENTAL"), 4);
        CHEST_PLATE.put(Enchantment.getByName("PROTECTION_FIRE"), 4);
        CHEST_PLATE.put(Enchantment.getByName("PROTECTION_EXPLOSIONS"), 4);
        CHEST_PLATE.put(Enchantment.getByName("PROTECTION_PROJECTILE"), 4);
        CHEST_PLATE.put(Enchantment.getByName("THORNS"), 3);
        CHEST_PLATE.put(Enchantment.getByName("DURABILITY"), 3);
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_9) >= 0) {
            CHEST_PLATE.put(Enchantment.getByName("MENDING"), 1);
        }
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_11) >= 0) {
            CHEST_PLATE.put(Enchantment.getByName("BINDING_CURSE"), 1);
        }
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_11) >= 0) {
            CHEST_PLATE.put(Enchantment.getByName("VANISHING_CURSE"), 1);
        }

        LEGGING = new HashMap<>();
        LEGGING.put(Enchantment.getByName("PROTECTION_ENVIRONMENTAL"), 4);
        LEGGING.put(Enchantment.getByName("PROTECTION_FIRE"), 4);
        LEGGING.put(Enchantment.getByName("PROTECTION_EXPLOSIONS"), 4);
        LEGGING.put(Enchantment.getByName("PROTECTION_PROJECTILE"), 4);
        LEGGING.put(Enchantment.getByName("THORNS"), 3);
        LEGGING.put(Enchantment.getByName("DURABILITY"), 3);
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_9) >= 0) {
            LEGGING.put(Enchantment.getByName("MENDING"), 1);
        }
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_11) >= 0) {
            LEGGING.put(Enchantment.getByName("BINDING_CURSE"), 1);
        }
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_11) >= 0) {
            LEGGING.put(Enchantment.getByName("VANISHING_CURSE"), 1);
        }

        BOOT = new HashMap<>();
        BOOT.put(Enchantment.getByName("PROTECTION_ENVIRONMENTAL"), 4);
        BOOT.put(Enchantment.getByName("PROTECTION_FIRE"), 4);
        BOOT.put(Enchantment.getByName("PROTECTION_FALL"), 4);
        BOOT.put(Enchantment.getByName("PROTECTION_EXPLOSIONS"), 4);
        BOOT.put(Enchantment.getByName("PROTECTION_PROJECTILE"), 4);
        BOOT.put(Enchantment.getByName("THORNS"), 3);
        BOOT.put(Enchantment.getByName("DEPTH_STRIDER"), 3);
        BOOT.put(Enchantment.getByName("DURABILITY"), 3);
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_9) >= 0) {
            BOOT.put(Enchantment.getByName("FROST_WALKER"), 2);
        }
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_9) >= 0) {
            BOOT.put(Enchantment.getByName("MENDING"), 1);
        }
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_11) >= 0) {
            BOOT.put(Enchantment.getByName("BINDING_CURSE"), 1);
        }
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_11) >= 0) {
            BOOT.put(Enchantment.getByName("VANISHING_CURSE"), 1);
        }

        SWORD = new HashMap<>();
        SWORD.put(Enchantment.getByName("DAMAGE_ALL"), 5);
        SWORD.put(Enchantment.getByName("DAMAGE_UNDEAD"), 5);
        SWORD.put(Enchantment.getByName("DAMAGE_ARTHROPODS"), 5);
        SWORD.put(Enchantment.getByName("KNOCKBACK"), 2);
        SWORD.put(Enchantment.getByName("FIRE_ASPECT"), 2);
        SWORD.put(Enchantment.getByName("LOOT_BONUS_MOBS"), 3);
        SWORD.put(Enchantment.getByName("DURABILITY"), 3);
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_9) >= 0) {
            SWORD.put(Enchantment.getByName("MENDING"), 1);
        }
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_11) >= 0) {
            SWORD.put(Enchantment.getByName("VANISHING_CURSE"), 1);
        }
        if (ConfigManager.SIMPLE_VERSION.equals(SimpleVersion.v1_11_1) || ConfigManager.SIMPLE_VERSION.equals(SimpleVersion.v1_11_2)) {
            SWORD.put(Enchantment.getByName("SWEEPING"), 3);
        }
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_12) >= 0) {
            SWORD.put(Enchantment.getByName("SWEEPING_EDGE"), 3);
        }

        PICKAXE = new HashMap<>();
        PICKAXE.put(Enchantment.getByName("DIG_SPEED"), 5);
        PICKAXE.put(Enchantment.getByName("SILK_TOUCH"), 1);
        PICKAXE.put(Enchantment.getByName("DURABILITY"), 3);
        PICKAXE.put(Enchantment.getByName("LOOT_BONUS_BLOCKS"), 3);
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_9) >= 0) {
            PICKAXE.put(Enchantment.getByName("MENDING"), 1);
        }
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_11) >= 0) {
            PICKAXE.put(Enchantment.getByName("VANISHING_CURSE"), 1);
        }

        AXE = new HashMap<>();
        AXE.put(Enchantment.getByName("DAMAGE_ALL"), 5);
        AXE.put(Enchantment.getByName("DAMAGE_UNDEAD"), 5);
        AXE.put(Enchantment.getByName("DAMAGE_ARTHROPODS"), 5);
        AXE.put(Enchantment.getByName("DIG_SPEED"), 5);
        AXE.put(Enchantment.getByName("SILK_TOUCH"), 1);
        AXE.put(Enchantment.getByName("DURABILITY"), 3);
        AXE.put(Enchantment.getByName("LOOT_BONUS_BLOCKS"), 3);
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_9) >= 0) {
            AXE.put(Enchantment.getByName("MENDING"), 1);
        }
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_11) >= 0) {
            AXE.put(Enchantment.getByName("VANISHING_CURSE"), 1);
        }

        SPADE = new HashMap<>();
        SPADE.put(Enchantment.getByName("DIG_SPEED"), 5);
        SPADE.put(Enchantment.getByName("SILK_TOUCH"), 1);
        SPADE.put(Enchantment.getByName("DURABILITY"), 3);
        SPADE.put(Enchantment.getByName("LOOT_BONUS_BLOCKS"), 3);
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_9) >= 0) {
            SPADE.put(Enchantment.getByName("MENDING"), 1);
        }
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_11) >= 0) {
            SPADE.put(Enchantment.getByName("VANISHING_CURSE"), 1);
        }

        HOE = new HashMap<>();
        HOE.put(Enchantment.getByName("DURABILITY"), 3);
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_9) >= 0) {
            HOE.put(Enchantment.getByName("MENDING"), 1);
        }
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_11) >= 0) {
            HOE.put(Enchantment.getByName("VANISHING_CURSE"), 1);
        }

        SHEAR = new HashMap<>();
        SHEAR.put(Enchantment.getByName("DIG_SPEED"), 5);
        SHEAR.put(Enchantment.getByName("DURABILITY"), 3);
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_9) >= 0) {
            SHEAR.put(Enchantment.getByName("MENDING"), 1);
        }
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_11) >= 0) {
            SHEAR.put(Enchantment.getByName("VANISHING_CURSE"), 1);
        }

        FLINT_AND_STEEL = new HashMap<>();
        FLINT_AND_STEEL.put(Enchantment.getByName("DURABILITY"), 3);
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_9) >= 0) {
            FLINT_AND_STEEL.put(Enchantment.getByName("MENDING"), 1);
        }
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_11) >= 0) {
            FLINT_AND_STEEL.put(Enchantment.getByName("VANISHING_CURSE"), 1);
        }

        BOW = new HashMap<>();
        BOW.put(Enchantment.getByName("DURABILITY"), 3);
        BOW.put(Enchantment.getByName("ARROW_DAMAGE"), 5);
        BOW.put(Enchantment.getByName("ARROW_KNOCKBACK"), 2);
        BOW.put(Enchantment.getByName("ARROW_FIRE"), 1);
        BOW.put(Enchantment.getByName("ARROW_INFINITE"), 1);
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_9) >= 0) {
            BOW.put(Enchantment.getByName("MENDING"), 1);
        }
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_11) >= 0) {
            BOW.put(Enchantment.getByName("VANISHING_CURSE"), 1);
        }

        FISHING_ROD = new HashMap<>();
        FISHING_ROD.put(Enchantment.getByName("DURABILITY"), 3);
        FISHING_ROD.put(Enchantment.getByName("LUCK"), 3);
        FISHING_ROD.put(Enchantment.getByName("LURE"), 3);
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_9) >= 0) {
            FISHING_ROD.put(Enchantment.getByName("MENDING"), 1);
        }
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_11) >= 0) {
            FISHING_ROD.put(Enchantment.getByName("VANISHING_CURSE"), 1);
        }

        CARROT_ON_A_STICK = new HashMap<>();
        CARROT_ON_A_STICK.put(Enchantment.getByName("DURABILITY"), 3);
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_9) >= 0) {
            CARROT_ON_A_STICK.put(Enchantment.getByName("MENDING"), 1);
        }
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_11) >= 0) {
            CARROT_ON_A_STICK.put(Enchantment.getByName("VANISHING_CURSE"), 1);
        }

        SHIELD = new HashMap<>();
        SHIELD.put(Enchantment.getByName("DURABILITY"), 3);
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_9) >= 0) {
            SHIELD.put(Enchantment.getByName("MENDING"), 1);
        }
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_11) >= 0) {
            SHIELD.put(Enchantment.getByName("VANISHING_CURSE"), 1);
        }

        ELYTRA = new HashMap<>();
        ELYTRA.put(Enchantment.getByName("DURABILITY"), 3);
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_9) >= 0) {
            ELYTRA.put(Enchantment.getByName("MENDING"), 1);
        }
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_11) >= 0) {
            ELYTRA.put(Enchantment.getByName("BINDING_CURSE"), 1);
        }
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_11) >= 0) {
            ELYTRA.put(Enchantment.getByName("VANISHING_CURSE"), 1);
        }

        BOOK = new HashMap<>();
        BOOK.put(Enchantment.getByName("PROTECTION_ENVIRONMENTAL"), 4);
        BOOK.put(Enchantment.getByName("PROTECTION_FIRE"), 4);
        BOOK.put(Enchantment.getByName("PROTECTION_FALL"), 4);
        BOOK.put(Enchantment.getByName("PROTECTION_EXPLOSIONS"), 4);
        BOOK.put(Enchantment.getByName("PROTECTION_PROJECTILE"), 4);
        BOOK.put(Enchantment.getByName("OXYGEN"), 3);
        BOOK.put(Enchantment.getByName("WATER_WORKER"), 1);
        BOOK.put(Enchantment.getByName("THORNS"), 3);
        BOOK.put(Enchantment.getByName("DEPTH_STRIDER"), 3);
        BOOK.put(Enchantment.getByName("DAMAGE_ALL"), 5);
        BOOK.put(Enchantment.getByName("DAMAGE_UNDEAD"), 5);
        BOOK.put(Enchantment.getByName("DAMAGE_ARTHROPODS"), 5);
        BOOK.put(Enchantment.getByName("KNOCKBACK"), 2);
        BOOK.put(Enchantment.getByName("FIRE_ASPECT"), 2);
        BOOK.put(Enchantment.getByName("LOOT_BONUS_MOBS"), 3);
        BOOK.put(Enchantment.getByName("DIG_SPEED"), 5);
        BOOK.put(Enchantment.getByName("SILK_TOUCH"), 1);
        BOOK.put(Enchantment.getByName("DURABILITY"), 3);
        BOOK.put(Enchantment.getByName("LOOT_BONUS_BLOCKS"), 3);
        BOOK.put(Enchantment.getByName("ARROW_DAMAGE"), 5);
        BOOK.put(Enchantment.getByName("ARROW_KNOCKBACK"), 2);
        BOOK.put(Enchantment.getByName("ARROW_FIRE"), 1);
        BOOK.put(Enchantment.getByName("ARROW_INFINITE"), 1);
        BOOK.put(Enchantment.getByName("LUCK"), 3);
        BOOK.put(Enchantment.getByName("LURE"), 3);
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_9) >= 0) {
            BOOK.put(Enchantment.getByName("FROST_WALKER"), 2);
        }
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_9) >= 0) {
            BOOK.put(Enchantment.getByName("MENDING"), 1);
        }
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_11) >= 0) {
            BOOK.put(Enchantment.getByName("BINDING_CURSE"), 1);
        }
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_11) >= 0) {
            BOOK.put(Enchantment.getByName("VANISHING_CURSE"), 1);
        }
        if (ConfigManager.SIMPLE_VERSION.equals(SimpleVersion.v1_11_1) || ConfigManager.SIMPLE_VERSION.equals(SimpleVersion.v1_11_2)) {
            BOOK.put(Enchantment.getByName("SWEEPING"), 3);
        }
        if (ConfigManager.SIMPLE_VERSION.compareTo(SimpleVersion.v1_12) >= 0) {
            BOOK.put(Enchantment.getByName("SWEEPING_EDGE"), 3);
        }
    }
}
