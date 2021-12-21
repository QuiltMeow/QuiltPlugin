package ew.quilt.GUI;

import ew.quilt.plugin.Main;
import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class IconMenu implements Listener {

    private final String name;
    private final int size;
    private OptionClickEventHandler handler;

    private String[] optionName;
    private ItemStack[] optionIcon;

    public IconMenu(String name, int size, OptionClickEventHandler handler) {
        this.name = name;
        this.size = size;
        this.handler = handler;
        this.optionName = new String[size];
        this.optionIcon = new ItemStack[size];
        Main.getPlugin().getServer().getPluginManager().registerEvents(this, Main.getPlugin());
    }

    public IconMenu setOption(int position, ItemStack icon, String name, String... info) {
        optionName[position] = name;
        optionIcon[position] = setItemNameAndLore(icon, name, info);
        return this;
    }

    public void open(Player player) {
        Inventory inventory = Bukkit.createInventory(player, size, name);
        for (int i = 0; i < optionIcon.length; i++) {
            if (optionIcon[i] != null) {
                inventory.setItem(i, optionIcon[i]);
            }
        }
        player.openInventory(inventory);
    }

    public void destroy() {
        HandlerList.unregisterAll(this);
        handler = null;
        optionName = null;
        optionIcon = null;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getTitle().equals(name)) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            if (slot >= 0 && slot < size && optionName[slot] != null) {
                OptionClickEvent oce = new OptionClickEvent((Player) event.getWhoClicked(), slot, optionName[slot]);
                handler.onOptionClick(oce);
                if (oce.willClose()) {
                    final Player player = (Player) event.getWhoClicked();
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
                        @Override
                        public void run() {
                            player.closeInventory();
                        }
                    }, 1);
                }
                if (oce.willDestroy()) {
                    destroy();
                }
            }
        }
    }

    public interface OptionClickEventHandler {

        public void onOptionClick(OptionClickEvent event);
    }

    public class OptionClickEvent {

        private final Player player;
        private final int position;
        private final String name;
        private boolean close;
        private boolean destroy;

        public OptionClickEvent(Player player, int position, String name) {
            this.player = player;
            this.position = position;
            this.name = name;
            this.close = true;
            this.destroy = false;
        }

        public Player getPlayer() {
            return player;
        }

        public int getPosition() {
            return position;
        }

        public String getName() {
            return name;
        }

        public boolean willClose() {
            return close;
        }

        public boolean willDestroy() {
            return destroy;
        }

        public void setWillClose(boolean close) {
            this.close = close;
        }

        public void setWillDestroy(boolean destroy) {
            this.destroy = destroy;
        }
    }

    private ItemStack setItemNameAndLore(ItemStack item, String name, String[] lore) {
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(name);
        im.setLore(Arrays.asList(lore));
        item.setItemMeta(im);
        return item;
    }
}
