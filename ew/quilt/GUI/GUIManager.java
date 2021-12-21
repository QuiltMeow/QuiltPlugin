package ew.quilt.GUI;

import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GUIManager {

    public static void openEmptyChest(Player player, String message) {
        Inventory inv = Bukkit.createInventory(null, InventoryType.CHEST, message);
        player.closeInventory();
        player.openInventory(inv);
    }

    public static void openEmptyBigChest(Player player, String message) {
        Inventory inv = Bukkit.createInventory(null, 54, message);
        player.closeInventory();
        player.openInventory(inv);
    }

    public static void openEmptyHopper(Player player, String message) {
        Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, message);
        player.closeInventory();
        player.openInventory(inv);
    }

    public static void openEmptyDispenser(Player player, String message) {
        Inventory inv = Bukkit.createInventory(null, InventoryType.DISPENSER, message);
        player.closeInventory();
        player.openInventory(inv);
    }

    public static void openChest(Player player, String name, int slot, Map<Integer, ItemStack> item) {
        Inventory inv = Bukkit.createInventory(null, slot, name);

        // 物品欄起始位置 : 0
        for (Map.Entry<Integer, ItemStack> data : item.entrySet()) {
            inv.setItem(data.getKey(), data.getValue());
        }
        player.closeInventory();
        player.openInventory(inv);
    }
}
