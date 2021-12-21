package ew.quilt.Hopper;

import java.util.List;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.ItemStack;

public class AntiHopperTransfer implements Listener {

    @EventHandler
    public void onHopperTransfer(InventoryMoveItemEvent event) {
        ItemStack item = event.getItem();
        if (item != null && item.hasItemMeta() && item.getItemMeta().hasLore()) {
            List<String> loreList = item.getItemMeta().getLore();
            if (item.getType() == Material.BOOK) {
                for (String lore : loreList) {
                    String[] splitted = lore.split(" : ");
                    if (splitted.length >= 2) {
                        if (splitted[0].contains("經驗值")) {
                            event.setCancelled(true);
                            return;
                        }
                    }
                }
            } else if (item.getType() == Material.APPLE) {
                for (String lore : loreList) {
                    String[] splitted = lore.split(" : ");
                    if (splitted.length >= 2) {
                        if (splitted[0].contains("爆炸半徑")) {
                            event.setCancelled(true);
                            return;
                        }
                    } else if (splitted[0].contains("核彈")) {
                        event.setCancelled(true);
                        return;
                    } else if (splitted[0].contains("死亡炸彈")) {
                        event.setCancelled(true);
                        return;
                    }
                }
            } else if (item.getType() == Material.DIAMOND) {
                for (String lore : loreList) {
                    if (lore.contains("直達地心")) {
                        event.setCancelled(true);
                        return;
                    }
                }
            }
        }
    }
}
