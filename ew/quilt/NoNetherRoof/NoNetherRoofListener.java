package ew.quilt.NoNetherRoof;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class NoNetherRoofListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.getWorld().getEnvironment() == World.Environment.NETHER) {
            if (player.hasPermission("quilt.nether.roof")) {
                return;
            }

            int top = 127;
            if (player.getLocation().getY() > top) {
                Location toSpawn = new Location(player.getLocation().getWorld(), player.getLocation().getBlockX() + 0.5, top, player.getLocation().getBlockZ() + 0.5);
                toSpawn.subtract(0, 1, 0).getBlock().setType(Material.AIR);
                toSpawn.subtract(0, 1, 0).getBlock().setType(Material.AIR);
                toSpawn.subtract(0, 1, 0).getBlock().setType(Material.NETHERRACK);
                player.teleport(toSpawn.add(0, 1, 0));
                player.sendMessage(ChatColor.RED + "不允許到達地獄頂端 !");
            }
        }
    }
}
