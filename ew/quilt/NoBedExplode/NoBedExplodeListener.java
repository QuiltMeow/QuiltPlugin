package ew.quilt.NoBedExplode;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class NoBedExplodeListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBedInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().toString().toLowerCase().contains("bed") && !event.getClickedBlock().toString().toLowerCase().contains("bedrock")) {
                Block block = event.getClickedBlock();
                Player player = event.getPlayer();
                if (block.getWorld().getName().equalsIgnoreCase("world_nether")) {
                    player.sendMessage(ChatColor.YELLOW + "§B[防爆系統] §C你不能在地獄睡覺");
                    event.setCancelled(true);
                } else if (block.getWorld().getName().equalsIgnoreCase("world_the_end")) {
                    player.sendMessage(ChatColor.YELLOW + "§B[防爆系統] §C你不能在終界睡覺");
                    event.setCancelled(true);
                }
            }
        }
    }
}
