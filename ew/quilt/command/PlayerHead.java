package ew.quilt.command;

import ew.quilt.plugin.Main;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class PlayerHead {

    public static ItemStack getHead(String name) {
        return getHead(name, true);
    }

    public static ItemStack getHead(String name, boolean check) {
        if (check) {
            OfflinePlayer player = Main.getPlugin().getServer().getOfflinePlayer(name);
            if (player != null) {
                name = player.getName();
            }
        }

        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skull = (SkullMeta) item.getItemMeta();
        skull.setDisplayName("§6" + name.replaceAll("_", ""));
        skull.setOwner(name);
        item.setItemMeta(skull);
        return item;
    }
}
