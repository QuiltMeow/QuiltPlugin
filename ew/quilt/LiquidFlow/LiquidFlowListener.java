package ew.quilt.LiquidFlow;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

public class LiquidFlowListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLiquidFlow(BlockFromToEvent event) {
        if (event.getBlock().getWorld().getEnvironment() == Environment.NETHER) {
            return;
        }
        Material material = event.getBlock().getType();
        if (isLiquid(material)) {
            Block toBlock = event.getToBlock();
            Location tempLocation = new Location(toBlock.getWorld(), toBlock.getX(), toBlock.getY(), toBlock.getZ());
            for (int i = 20; i >= 5; i -= 5) {
                tempLocation.setY(toBlock.getLocation().getBlockY() + i);
                Material higherBlock = tempLocation.getBlock().getType();
                if (!isLiquid(higherBlock)) {
                    return;
                }
            }
            event.setCancelled(true);
        }
    }

    public static boolean isLiquid(Material type) {
        switch (type) {
            case WATER:
            case STATIONARY_WATER:
            case LAVA:
            case STATIONARY_LAVA: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
}
