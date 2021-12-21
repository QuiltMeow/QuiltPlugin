package ew.quilt.Item.Potion;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PotionStackListener implements Listener {

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerClick(InventoryClickEvent event) {
        if (event.getInventory().getType() != InventoryType.CRAFTING) {
            return;
        }
        if (event.getClick() != ClickType.SHIFT_RIGHT) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();
        if (item.getTypeId() != 373 && item.getTypeId() != 335 && item.getTypeId() != 438 && item.getTypeId() != 441) {
            return;
        }
        PlayerInventory inv = player.getInventory();
        ItemStack[] is = inv.getContents();

        int amount = 0;
        for (int i = 0; i < is.length; ++i) {
            ItemStack invItem = is[i];
            if (item.isSimilar(invItem)) {
                amount += invItem.getAmount();
                inv.removeItem(invItem);
            }
        }

        while (amount > 64) {
            ItemStack invItem = item.clone();
            invItem.setAmount(64);
            inv.addItem(new ItemStack[]{invItem});
            amount -= 64;
        }
        item.setAmount(amount);
        inv.addItem(new ItemStack[]{item});
        event.setCancelled(true);
    }

    @EventHandler
    public void onDrinkMilk(PlayerItemConsumeEvent event) {
        ItemStack item = event.getItem();
        if (item.getType() == Material.MILK_BUCKET && item.getAmount() > 1) {
            event.getPlayer().getInventory().addItem(new ItemStack(Material.BUCKET, 1));
        }
    }
}
