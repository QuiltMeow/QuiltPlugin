package ew.quilt.Item.Attribute.api;

import java.util.Arrays;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta.Generation;
import ew.quilt.Item.Attribute.AttributeCommand;
import ew.quilt.util.reflection.ReflectionUtil;

public class AttributesEditorAPI {

    private static final boolean OUTPUT_ERROR_CONSOLE = false;

    public static List<String> decodeTypeAndSlot(int type, int slot) {
        String sslot = null, stype = null;
        switch (type) {
            case 1:
                stype = "generic.maxHealth";
                break;
            case 2:
                stype = "generic.followRange";
                break;
            case 3:
                stype = "generic.knockbackResistance";
                break;
            case 4:
                stype = "generic.movementSpeed";
                break;
            case 5:
                stype = "generic.attackDamage";
                break;
            case 6:
                stype = "generic.armor";
                break;
            case 7:
                stype = "generic.attackSpeed";
                break;
            case 8:
                stype = "generic.luck";
                break;
            case 9:
                stype = "generic.armorToughness";
                break;
        }
        switch (slot) {
            case 1:
                sslot = "mainhand";
                break;
            case 2:
                sslot = "offhand";
                break;
            case 3:
                sslot = "head";
                break;
            case 4:
                sslot = "chest";
                break;
            case 5:
                sslot = "legs";
                break;
            case 6:
                sslot = "feet";
                break;
        }
        return Arrays.asList(stype, sslot);
    }

    public static ItemFlag decodeHideType(int hideType) {
        switch (hideType) {
            case 1:
                return ItemFlag.HIDE_ATTRIBUTES;
            case 2:
                return ItemFlag.HIDE_DESTROYS;
            case 3:
                return ItemFlag.HIDE_ENCHANTS;
            case 4:
                return ItemFlag.HIDE_PLACED_ON;
            case 5:
                return ItemFlag.HIDE_POTION_EFFECTS;
            case 6:
                return ItemFlag.HIDE_UNBREAKABLE;
        }
        return null;
    }

    public static Generation decodeGeneration(int generation) {
        switch (generation) {
            case 0:
                return Generation.ORIGINAL;
            case 1:
                return Generation.COPY_OF_ORIGINAL;
            case 2:
                return Generation.COPY_OF_COPY;
            case 3:
                return Generation.TATTERED;
        }
        return null;
    }

    public static int decodeColors(int r, int g, int b) {
        if (r > 255 || g > 255 || b > 255 || r < 0 || g < 0 || b < 0) {
            return 16777216;
        }
        return 65536 * r + 256 * g + b;
    }

    public static int getMajorVersion() {
        return AttributeCommand.version;
    }

    public static ItemStack getAvailableItemInHand(Player player) {
        ItemStack is;
        if (AttributeCommand.version >= 9) {
            is = player.getInventory().getItemInMainHand();
        } else {
            is = player.getItemInHand();
        }
        if (is.toString().contains("AIR")) {
            return null;
        }
        return is;
    }

    public static void setItemInHand(Player player, ItemStack itemstack) {
        if (AttributeCommand.version >= 9) {
            player.getInventory().setItemInMainHand(itemstack);
        } else {
            player.setItemInHand(itemstack);
        }
    }

    public static void printStackTrace(Player p, Exception ex) {
        p.sendMessage(ChatColor.RED + "指令執行時發生例外狀況 !");
        if (OUTPUT_ERROR_CONSOLE) {
            ex.printStackTrace();
        }
    }

    public static ItemStack changeFireworksFlightTime(ItemStack itemstack, byte flightTime) throws Exception {
        Object nmsItemStack = ReflectionUtil.invokeMethod(ReflectionUtil.getBukkitClass("inventory.CraftItemStack"), null, "asNMSCopy", new Class[]{ItemStack.class}, new Object[]{itemstack});
        Object tag = ((boolean) ReflectionUtil.invokeMethod(nmsItemStack.getClass(), nmsItemStack, "hasTag")) ? ReflectionUtil.invokeMethod(nmsItemStack.getClass(), nmsItemStack, "getTag") : ReflectionUtil.invokeConstructor(ReflectionUtil.getNMSClass("NBTTagCompound"), new Class[]{}, new Object[]{});
        Object fireworks = ((boolean) ReflectionUtil.invokeMethod(tag.getClass(), tag, "hasKey", new Class[]{String.class}, new Object[]{"Fireworks"})) ? ReflectionUtil.invokeMethod(tag.getClass(), tag, "get", new Class[]{String.class}, new Object[]{"Fireworks"}) : ReflectionUtil.invokeConstructor(ReflectionUtil.getNMSClass("NBTTagCompound"), new Class[]{}, new Object[]{});
        ReflectionUtil.invokeMethod(fireworks.getClass(), fireworks, "setByte", new Class[]{String.class, byte.class}, new Object[]{"Flight", flightTime});
        ReflectionUtil.invokeMethod(tag.getClass(), tag, "set", new Class[]{String.class, ReflectionUtil.getNMSClass("NBTBase")}, new Object[]{"Fireworks", fireworks});
        ReflectionUtil.invokeMethod(nmsItemStack.getClass(), nmsItemStack, "setTag", new Class[]{ReflectionUtil.getNMSClass("NBTTagCompound")}, new Object[]{tag});
        return (ItemStack) ReflectionUtil.invokeMethod(ReflectionUtil.getBukkitClass("inventory.CraftItemStack"), null, "asBukkitCopy", new Class[]{ReflectionUtil.getNMSClass("ItemStack")}, new Object[]{nmsItemStack});
    }

    public static ItemStack changeFireworkCharge(ItemStack itemstack, byte flicker, byte trail, byte type, int colors, int fadeColors) throws Exception {
        Object nmsItemStack = ReflectionUtil.invokeMethod(ReflectionUtil.getBukkitClass("inventory.CraftItemStack"), null, "asNMSCopy", new Class[]{ItemStack.class}, new Object[]{itemstack});
        Object tag = ((boolean) ReflectionUtil.invokeMethod(nmsItemStack.getClass(), nmsItemStack, "hasTag")) ? ReflectionUtil.invokeMethod(nmsItemStack.getClass(), nmsItemStack, "getTag") : ReflectionUtil.invokeConstructor(ReflectionUtil.getNMSClass("NBTTagCompound"), new Class[]{}, new Object[]{});
        Object expCompound = ((boolean) ReflectionUtil.invokeMethod(tag.getClass(), tag, "hasKey", new Class[]{String.class}, new Object[]{"Explosion"})) ? ReflectionUtil.invokeMethod(tag.getClass(), tag, "get", new Class[]{String.class}, new Object[]{"Explosion"}) : ReflectionUtil.invokeConstructor(ReflectionUtil.getNMSClass("NBTTagCompound"), new Class[]{}, new Object[]{});
        ReflectionUtil.invokeMethod(expCompound.getClass(), expCompound, "setByte", new Class[]{String.class, byte.class}, new Object[]{"Flicker", flicker});
        ReflectionUtil.invokeMethod(expCompound.getClass(), expCompound, "setByte", new Class[]{String.class, byte.class}, new Object[]{"Trail", trail});
        ReflectionUtil.invokeMethod(expCompound.getClass(), expCompound, "setByte", new Class[]{String.class, byte.class}, new Object[]{"Type", type});
        ReflectionUtil.invokeMethod(expCompound.getClass(), expCompound, "setIntArray", new Class[]{String.class, int[].class}, new Object[]{"Colors", new int[]{colors}});
        ReflectionUtil.invokeMethod(expCompound.getClass(), expCompound, "setIntArray", new Class[]{String.class, int[].class}, new Object[]{"FadeColors", new int[]{fadeColors}});
        ReflectionUtil.invokeMethod(tag.getClass(), tag, "set", new Class[]{String.class, ReflectionUtil.getNMSClass("NBTBase")}, new Object[]{"Explosion", expCompound});
        ReflectionUtil.invokeMethod(nmsItemStack.getClass(), nmsItemStack, "setTag", new Class[]{ReflectionUtil.getNMSClass("NBTTagCompound")}, new Object[]{tag});
        return (ItemStack) ReflectionUtil.invokeMethod(ReflectionUtil.getBukkitClass("inventory.CraftItemStack"), null, "asBukkitCopy", new Class[]{ReflectionUtil.getNMSClass("ItemStack")}, new Object[]{nmsItemStack});
    }

    public static ItemStack addAttribute(ItemStack itemstack, String type, String slot, double amount) throws Exception {
        Object nmsItemStack = ReflectionUtil.invokeMethod(ReflectionUtil.getBukkitClass("inventory.CraftItemStack"), null, "asNMSCopy", new Class[]{ItemStack.class}, new Object[]{itemstack});
        Object tag = ((boolean) ReflectionUtil.invokeMethod(nmsItemStack.getClass(), nmsItemStack, "hasTag")) ? ReflectionUtil.invokeMethod(nmsItemStack.getClass(), nmsItemStack, "getTag") : ReflectionUtil.invokeConstructor(ReflectionUtil.getNMSClass("NBTTagCompound"), new Class[]{}, new Object[]{});
        Object taglist = ((boolean) ReflectionUtil.invokeMethod(tag.getClass(), tag, "hasKey", new Class[]{String.class}, new Object[]{"AttributeModifiers"})) ? ReflectionUtil.invokeMethod(tag.getClass(), tag, "get", new Class[]{String.class}, new Object[]{"AttributeModifiers"}) : ReflectionUtil.invokeConstructor(ReflectionUtil.getNMSClass("NBTTagList"), new Class[]{}, new Object[]{});
        Object newAttr = ReflectionUtil.invokeConstructor(ReflectionUtil.getNMSClass("NBTTagCompound"), new Class[]{}, new Object[]{});
        ReflectionUtil.invokeMethod(newAttr.getClass(), newAttr, "setString", new Class[]{String.class, String.class}, new Object[]{"AttributeName", type});
        ReflectionUtil.invokeMethod(newAttr.getClass(), newAttr, "setString", new Class[]{String.class, String.class}, new Object[]{"Name", type});
        ReflectionUtil.invokeMethod(newAttr.getClass(), newAttr, "setString", new Class[]{String.class, String.class}, new Object[]{"Slot", slot});
        ReflectionUtil.invokeMethod(newAttr.getClass(), newAttr, "setDouble", new Class[]{String.class, double.class}, new Object[]{"Amount", amount});
        ReflectionUtil.invokeMethod(newAttr.getClass(), newAttr, "setInt", new Class[]{String.class, int.class}, new Object[]{"Operation", 0});
        ReflectionUtil.invokeMethod(newAttr.getClass(), newAttr, "setInt", new Class[]{String.class, int.class}, new Object[]{"UUIDLeast", 894654});
        ReflectionUtil.invokeMethod(newAttr.getClass(), newAttr, "setInt", new Class[]{String.class, int.class}, new Object[]{"UUIDMost", 2872});
        ReflectionUtil.invokeMethod(taglist.getClass(), taglist, "add", new Class[]{ReflectionUtil.getNMSClass("NBTBase")}, new Object[]{newAttr});
        ReflectionUtil.invokeMethod(tag.getClass(), tag, "set", new Class[]{String.class, ReflectionUtil.getNMSClass("NBTBase")}, new Object[]{"AttributeModifiers", taglist});
        ReflectionUtil.invokeMethod(nmsItemStack.getClass(), nmsItemStack, "setTag", new Class[]{ReflectionUtil.getNMSClass("NBTTagCompound")}, new Object[]{tag});
        return (ItemStack) ReflectionUtil.invokeMethod(ReflectionUtil.getBukkitClass("inventory.CraftItemStack"), null, "asBukkitCopy", new Class[]{ReflectionUtil.getNMSClass("ItemStack")}, new Object[]{nmsItemStack});
    }

    public static ItemStack addStoredEnchantment(ItemStack itemstack, short enchType, short level) throws Exception {
        Object nmsItemStack = ReflectionUtil.invokeMethod(ReflectionUtil.getBukkitClass("inventory.CraftItemStack"), null, "asNMSCopy", new Class[]{ItemStack.class}, new Object[]{itemstack});
        Object tag = ((boolean) ReflectionUtil.invokeMethod(nmsItemStack.getClass(), nmsItemStack, "hasTag")) ? ReflectionUtil.invokeMethod(nmsItemStack.getClass(), nmsItemStack, "getTag") : ReflectionUtil.invokeConstructor(ReflectionUtil.getNMSClass("NBTTagCompound"), new Class[]{}, new Object[]{});
        Object taglist = ((boolean) ReflectionUtil.invokeMethod(tag.getClass(), tag, "hasKey", new Class[]{String.class}, new Object[]{"StoredEnchantments"})) ? ReflectionUtil.invokeMethod(tag.getClass(), tag, "get", new Class[]{String.class}, new Object[]{"StoredEnchantments"}) : ReflectionUtil.invokeConstructor(ReflectionUtil.getNMSClass("NBTTagList"), new Class[]{}, new Object[]{});
        Object newStoredEnchantment = ReflectionUtil.invokeConstructor(ReflectionUtil.getNMSClass("NBTTagCompound"), new Class[]{}, new Object[]{});
        ReflectionUtil.invokeMethod(newStoredEnchantment.getClass(), newStoredEnchantment, "setShort", new Class[]{String.class, short.class}, new Object[]{"id", enchType});
        ReflectionUtil.invokeMethod(newStoredEnchantment.getClass(), newStoredEnchantment, "setShort", new Class[]{String.class, short.class}, new Object[]{"lvl", level});
        ReflectionUtil.invokeMethod(taglist.getClass(), taglist, "add", new Class[]{ReflectionUtil.getNMSClass("NBTBase")}, new Object[]{newStoredEnchantment});
        ReflectionUtil.invokeMethod(tag.getClass(), tag, "set", new Class[]{String.class, ReflectionUtil.getNMSClass("NBTBase")}, new Object[]{"StoredEnchantments", taglist});
        ReflectionUtil.invokeMethod(nmsItemStack.getClass(), nmsItemStack, "setTag", new Class[]{ReflectionUtil.getNMSClass("NBTTagCompound")}, new Object[]{tag});
        return (ItemStack) ReflectionUtil.invokeMethod(ReflectionUtil.getBukkitClass("inventory.CraftItemStack"), null, "asBukkitCopy", new Class[]{ReflectionUtil.getNMSClass("ItemStack")}, new Object[]{nmsItemStack});
    }

    public static ItemStack addPotionEffect(ItemStack itemstack, byte potionType, byte amplifier, byte showParticles, int duration) throws Exception {
        Object nmsItemStack = ReflectionUtil.invokeMethod(ReflectionUtil.getBukkitClass("inventory.CraftItemStack"), null, "asNMSCopy", new Class[]{ItemStack.class}, new Object[]{itemstack});
        Object tag = ((boolean) ReflectionUtil.invokeMethod(nmsItemStack.getClass(), nmsItemStack, "hasTag")) ? ReflectionUtil.invokeMethod(nmsItemStack.getClass(), nmsItemStack, "getTag") : ReflectionUtil.invokeConstructor(ReflectionUtil.getNMSClass("NBTTagCompound"), new Class[]{}, new Object[]{});
        Object taglist = ((boolean) ReflectionUtil.invokeMethod(tag.getClass(), tag, "hasKey", new Class[]{String.class}, new Object[]{"CustomPotionEffects"})) ? ReflectionUtil.invokeMethod(tag.getClass(), tag, "get", new Class[]{String.class}, new Object[]{"CustomPotionEffects"}) : ReflectionUtil.invokeConstructor(ReflectionUtil.getNMSClass("NBTTagList"), new Class[]{}, new Object[]{});
        Object newPotionEffect = ReflectionUtil.invokeConstructor(ReflectionUtil.getNMSClass("NBTTagCompound"), new Class[]{}, new Object[]{});
        ReflectionUtil.invokeMethod(newPotionEffect.getClass(), newPotionEffect, "setByte", new Class[]{String.class, byte.class}, new Object[]{"Id", potionType});
        ReflectionUtil.invokeMethod(newPotionEffect.getClass(), newPotionEffect, "setByte", new Class[]{String.class, byte.class}, new Object[]{"Amplifier", amplifier});
        ReflectionUtil.invokeMethod(newPotionEffect.getClass(), newPotionEffect, "setByte", new Class[]{String.class, byte.class}, new Object[]{"ShowParticles", showParticles});
        ReflectionUtil.invokeMethod(newPotionEffect.getClass(), newPotionEffect, "setInt", new Class[]{String.class, int.class}, new Object[]{"Duration", duration});
        ReflectionUtil.invokeMethod(taglist.getClass(), taglist, "add", new Class[]{ReflectionUtil.getNMSClass("NBTBase")}, new Object[]{newPotionEffect});
        ReflectionUtil.invokeMethod(tag.getClass(), tag, "set", new Class[]{String.class, ReflectionUtil.getNMSClass("NBTBase")}, new Object[]{"CustomPotionEffects", taglist});
        ReflectionUtil.invokeMethod(nmsItemStack.getClass(), nmsItemStack, "setTag", new Class[]{ReflectionUtil.getNMSClass("NBTTagCompound")}, new Object[]{tag});
        return (ItemStack) ReflectionUtil.invokeMethod(ReflectionUtil.getBukkitClass("inventory.CraftItemStack"), null, "asBukkitCopy", new Class[]{ReflectionUtil.getNMSClass("ItemStack")}, new Object[]{nmsItemStack});
    }

    public static ItemStack addFireworks(ItemStack itemstack, byte flicker, byte trail, byte type, int colors, int fadeColors) throws Exception {
        Object nmsItemStack = ReflectionUtil.invokeMethod(ReflectionUtil.getBukkitClass("inventory.CraftItemStack"), null, "asNMSCopy", new Class[]{ItemStack.class}, new Object[]{itemstack});
        Object tag = ((boolean) ReflectionUtil.invokeMethod(nmsItemStack.getClass(), nmsItemStack, "hasTag")) ? ReflectionUtil.invokeMethod(nmsItemStack.getClass(), nmsItemStack, "getTag") : ReflectionUtil.invokeConstructor(ReflectionUtil.getNMSClass("NBTTagCompound"), new Class[]{}, new Object[]{});
        Object fireworks = ((boolean) ReflectionUtil.invokeMethod(tag.getClass(), tag, "hasKey", new Class[]{String.class}, new Object[]{"Fireworks"})) ? ReflectionUtil.invokeMethod(tag.getClass(), tag, "get", new Class[]{String.class}, new Object[]{"Fireworks"}) : ReflectionUtil.invokeConstructor(ReflectionUtil.getNMSClass("NBTTagCompound"), new Class[]{}, new Object[]{});
        Object explosionsList = ((boolean) ReflectionUtil.invokeMethod(fireworks.getClass(), fireworks, "hasKey", new Class[]{String.class}, new Object[]{"Explosions"})) ? ReflectionUtil.invokeMethod(fireworks.getClass(), fireworks, "get", new Class[]{String.class}, new Object[]{"Explosions"}) : ReflectionUtil.invokeConstructor(ReflectionUtil.getNMSClass("NBTTagList"), new Class[]{}, new Object[]{});
        Object expCompound = ReflectionUtil.invokeConstructor(ReflectionUtil.getNMSClass("NBTTagCompound"), new Class[]{}, new Object[]{});
        ReflectionUtil.invokeMethod(expCompound.getClass(), expCompound, "setByte", new Class[]{String.class, byte.class}, new Object[]{"Flicker", flicker});
        ReflectionUtil.invokeMethod(expCompound.getClass(), expCompound, "setByte", new Class[]{String.class, byte.class}, new Object[]{"Trail", trail});
        ReflectionUtil.invokeMethod(expCompound.getClass(), expCompound, "setByte", new Class[]{String.class, byte.class}, new Object[]{"Type", type});
        ReflectionUtil.invokeMethod(expCompound.getClass(), expCompound, "setIntArray", new Class[]{String.class, int[].class}, new Object[]{"Colors", new int[]{colors}});
        ReflectionUtil.invokeMethod(expCompound.getClass(), expCompound, "setIntArray", new Class[]{String.class, int[].class}, new Object[]{"FadeColors", new int[]{fadeColors}});
        ReflectionUtil.invokeMethod(explosionsList.getClass(), explosionsList, "add", new Class[]{ReflectionUtil.getNMSClass("NBTBase")}, new Object[]{expCompound});
        ReflectionUtil.invokeMethod(fireworks.getClass(), fireworks, "set", new Class[]{String.class, ReflectionUtil.getNMSClass("NBTBase")}, new Object[]{"Explosions", explosionsList});
        ReflectionUtil.invokeMethod(tag.getClass(), tag, "set", new Class[]{String.class, ReflectionUtil.getNMSClass("NBTBase")}, new Object[]{"Fireworks", fireworks});
        ReflectionUtil.invokeMethod(nmsItemStack.getClass(), nmsItemStack, "setTag", new Class[]{ReflectionUtil.getNMSClass("NBTTagCompound")}, new Object[]{tag});
        return (ItemStack) ReflectionUtil.invokeMethod(ReflectionUtil.getBukkitClass("inventory.CraftItemStack"), null, "asBukkitCopy", new Class[]{ReflectionUtil.getNMSClass("ItemStack")}, new Object[]{nmsItemStack});
    }

    public static ItemStack removeAttribute(ItemStack itemstack) throws Exception {
        Object nmsItemStack = ReflectionUtil.invokeMethod(ReflectionUtil.getBukkitClass("inventory.CraftItemStack"), null, "asNMSCopy", new Class[]{ItemStack.class}, new Object[]{itemstack});
        boolean tag = (boolean) ReflectionUtil.invokeMethod(nmsItemStack.getClass(), nmsItemStack, "hasTag");
        if (!tag) {
            return null;
        }
        Object tag2 = ReflectionUtil.invokeMethod(nmsItemStack.getClass(), nmsItemStack, "getTag");
        boolean taglist = (boolean) ReflectionUtil.invokeMethod(tag2.getClass(), tag2, "hasKey", new Class[]{String.class}, new Object[]{"AttributeModifiers"});
        if (!taglist) {
            return null;
        }
        ReflectionUtil.invokeMethod(tag2.getClass(), tag2, "remove", new Class[]{String.class}, new Object[]{"AttributeModifiers"});
        ReflectionUtil.invokeMethod(nmsItemStack.getClass(), nmsItemStack, "setTag", new Class[]{ReflectionUtil.getNMSClass("NBTTagCompound")}, new Object[]{tag2});
        return (ItemStack) ReflectionUtil.invokeMethod(ReflectionUtil.getBukkitClass("inventory.CraftItemStack"), null, "asBukkitCopy", new Class[]{ReflectionUtil.getNMSClass("ItemStack")}, new Object[]{nmsItemStack});
    }

    public static ItemStack removeStoredEnchantment(ItemStack itemstack) throws Exception {
        Object nmsItemStack = ReflectionUtil.invokeMethod(ReflectionUtil.getBukkitClass("inventory.CraftItemStack"), null, "asNMSCopy", new Class[]{ItemStack.class}, new Object[]{itemstack});
        boolean tag = (boolean) ReflectionUtil.invokeMethod(nmsItemStack.getClass(), nmsItemStack, "hasTag");
        if (!tag) {
            return null;
        }
        Object tag2 = ReflectionUtil.invokeMethod(nmsItemStack.getClass(), nmsItemStack, "getTag");
        boolean taglist = (boolean) ReflectionUtil.invokeMethod(tag2.getClass(), tag2, "hasKey", new Class[]{String.class}, new Object[]{"StoredEnchantments"});
        if (!taglist) {
            return null;
        }
        ReflectionUtil.invokeMethod(tag2.getClass(), tag2, "remove", new Class[]{String.class}, new Object[]{"StoredEnchantments"});
        ReflectionUtil.invokeMethod(nmsItemStack.getClass(), nmsItemStack, "setTag", new Class[]{ReflectionUtil.getNMSClass("NBTTagCompound")}, new Object[]{tag2});
        return (ItemStack) ReflectionUtil.invokeMethod(ReflectionUtil.getBukkitClass("inventory.CraftItemStack"), null, "asBukkitCopy", new Class[]{ReflectionUtil.getNMSClass("ItemStack")}, new Object[]{nmsItemStack});
    }

    public static ItemStack removeFireworks(ItemStack itemstack) throws Exception {
        Object nmsItemStack = ReflectionUtil.invokeMethod(ReflectionUtil.getBukkitClass("inventory.CraftItemStack"), null, "asNMSCopy", new Class[]{ItemStack.class}, new Object[]{itemstack});
        boolean tag = (boolean) ReflectionUtil.invokeMethod(nmsItemStack.getClass(), nmsItemStack, "hasTag");
        if (!tag) {
            return null;
        }
        Object tag2 = ReflectionUtil.invokeMethod(nmsItemStack.getClass(), nmsItemStack, "getTag");
        boolean firework = (boolean) ReflectionUtil.invokeMethod(tag2.getClass(), tag2, "hasKey", new Class[]{String.class}, new Object[]{"Fireworks"});
        if (!firework) {
            return null;
        }
        Object fireworks = ReflectionUtil.invokeMethod(tag2.getClass(), tag2, "get", new Class[]{String.class}, new Object[]{"Fireworks"});
        boolean explosions = (boolean) ReflectionUtil.invokeMethod(fireworks.getClass(), fireworks, "hasKey", new Class[]{String.class}, new Object[]{"Explosions"});
        if (!explosions) {
            return null;
        }
        ReflectionUtil.invokeMethod(fireworks.getClass(), fireworks, "remove", new Class[]{String.class}, new Object[]{"Explosions"});
        ReflectionUtil.invokeMethod(tag2.getClass(), tag2, "set", new Class[]{String.class, ReflectionUtil.getNMSClass("NBTBase")}, new Object[]{"Fireworks", fireworks});
        ReflectionUtil.invokeMethod(nmsItemStack.getClass(), nmsItemStack, "setTag", new Class[]{ReflectionUtil.getNMSClass("NBTTagCompound")}, new Object[]{tag2});
        return (ItemStack) ReflectionUtil.invokeMethod(ReflectionUtil.getBukkitClass("inventory.CraftItemStack"), null, "asBukkitCopy", new Class[]{ReflectionUtil.getNMSClass("ItemStack")}, new Object[]{nmsItemStack});
    }

    public static ItemStack removePotionEffects(ItemStack itemstack) throws Exception {
        Object nmsItemStack = ReflectionUtil.invokeMethod(ReflectionUtil.getBukkitClass("inventory.CraftItemStack"), null, "asNMSCopy", new Class[]{ItemStack.class}, new Object[]{itemstack});
        boolean tag = (boolean) ReflectionUtil.invokeMethod(nmsItemStack.getClass(), nmsItemStack, "hasTag");
        if (!tag) {
            return null;
        }
        Object tag2 = ReflectionUtil.invokeMethod(nmsItemStack.getClass(), nmsItemStack, "getTag");
        boolean taglist = (boolean) ReflectionUtil.invokeMethod(tag2.getClass(), tag2, "hasKey", new Class[]{String.class}, new Object[]{"CustomPotionEffects"});
        if (!taglist) {
            return null;
        }
        ReflectionUtil.invokeMethod(tag2.getClass(), tag2, "remove", new Class[]{String.class}, new Object[]{"CustomPotionEffects"});
        ReflectionUtil.invokeMethod(nmsItemStack.getClass(), nmsItemStack, "setTag", new Class[]{ReflectionUtil.getNMSClass("NBTTagCompound")}, new Object[]{tag2});
        return (ItemStack) ReflectionUtil.invokeMethod(ReflectionUtil.getBukkitClass("inventory.CraftItemStack"), null, "asBukkitCopy", new Class[]{ReflectionUtil.getNMSClass("ItemStack")}, new Object[]{nmsItemStack});
    }

    public static boolean isAllowedEnchantment(int enchid) {
        return AttributeCommand.allowedEnchantment.contains(enchid);
    }

    public static boolean isAllowedFireworkType(int fireworkType) {
        return AttributeCommand.allowedFireworkType.contains(fireworkType);
    }

    public static boolean isAllowedHideType(int hideType) {
        return AttributeCommand.allowedFlags.contains(hideType);
    }

    public static boolean isAllowedPotionEffect(int potionid) {
        return AttributeCommand.allowedPotionEffects.contains(potionid);
    }

    public static boolean isAllowedSlot(int slot) {
        return AttributeCommand.allowedSlot.contains(slot);
    }

    public static boolean isAllowedAttrType(int attrType) {
        return AttributeCommand.allowedType.contains(attrType);
    }

    public static boolean isAllowedGeneration(int generation) {
        return AttributeCommand.allowedGeneration.contains(generation);
    }
}
