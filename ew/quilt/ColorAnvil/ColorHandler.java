package ew.quilt.ColorAnvil;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ColorHandler {

    private static final int OUTPUT = 2;

    public static ItemStack getTranslatedItem(Player player, AnvilInventory inv, AnvilTask task) {
        ItemStack outputItem = inv.getItem(OUTPUT);
        if (outputItem != null && outputItem.hasItemMeta()) {
            ItemMeta outputItemMeta = outputItem.getItemMeta();
            if (outputItemMeta.hasDisplayName()) {
                ItemStack inputItem = inv.getItem(0);
                if (inputItem != null && inputItem.hasItemMeta()) {
                    ItemMeta inputItemMeta = inputItem.getItemMeta();
                    if (inputItemMeta.hasDisplayName()) {
                        if (outputItemMeta.getDisplayName().replaceAll("&", "").replaceAll("§", "").equals(inputItemMeta.getDisplayName().replaceAll("§", ""))) {
                            outputItemMeta.setDisplayName(inputItemMeta.getDisplayName());
                            outputItem.setItemMeta(outputItemMeta);
                            return outputItem;
                        }
                    }
                }

                String colorName = ChatColor.translateAlternateColorCodes('&', outputItemMeta.getDisplayName());
                outputItemMeta.setDisplayName(colorName);
                outputItem.setItemMeta(outputItemMeta);
            }
        }
        return outputItem;
    }
}
