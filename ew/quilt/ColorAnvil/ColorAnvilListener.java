package ew.quilt.ColorAnvil;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;

public class ColorAnvilListener implements Listener {

    @EventHandler
    public void onAnvilGUIClick(InventoryClickEvent event) {
        if (event.getInventory().getType() == InventoryType.ANVIL && event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            AnvilInventory inv = (AnvilInventory) event.getInventory();
            AnvilTask task = AnvilTask.getTask(inv);
            if (task == null) {
                task = new AnvilTask(inv, player);
            }
            if (event.getRawSlot() == 2) {
                ItemStack translatedItem = ColorHandler.getTranslatedItem(player, inv, task);
                event.setCurrentItem(translatedItem);
            }
        }
    }
}
