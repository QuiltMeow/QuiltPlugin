package ew.quilt.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class ItemUtil {

    public static void removeInventoryItem(PlayerInventory inventory, Material type, int amount) {
        for (ItemStack is : inventory.getContents()) {
            if (is != null && is.getType() == type) {
                int newAmount = is.getAmount() - amount;
                if (newAmount > 0) {
                    is.setAmount(newAmount);
                    break;
                } else {
                    inventory.remove(is);
                    amount = -newAmount;
                    if (amount == 0) {
                        break;
                    }
                }
            }
        }
    }

    public static boolean isSword(Material type) {
        return type.name().endsWith("SWORD");
    }
}
