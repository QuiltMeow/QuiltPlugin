package ew.quilt.util;

import java.util.Map;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class EnchantUtil {

    public static void removeEnchant(ItemStack item, Enchantment enchant) {
        Map<Enchantment, Integer> enchantData = item.getEnchantments();
        if (enchantData.get(enchant) != null) {
            enchantData.remove(enchant);
        }
    }
}
