package ew.quilt.RedStone;

import ew.quilt.Config.ConfigManager;
import ew.quilt.plugin.Main;
import ew.quilt.util.TimerUtil;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class RedStoneClockPreventer implements Listener {

    private static final int MAX_LIMIT_SIGNAL = 160;
    private static final int WAIT_RED_STONE_TIME = TimerUtil.secondToTick(10);

    private static final Map<Block, Integer> RED_STONE_WIRE_BLOCK = new HashMap<>();

    public static void startClearTimer() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
            @Override
            public void run() {
                RED_STONE_WIRE_BLOCK.clear();
            }
        }, TimerUtil.secondToTick(10), TimerUtil.secondToTick(10));
    }

    @EventHandler
    public void onRedstoneChange(BlockRedstoneEvent event) {
        Block block = event.getBlock();
        String worldName = block.getWorld().getName();
        if (block.getType() == Material.REDSTONE_WIRE && (worldName.equalsIgnoreCase(ConfigManager.SURVIVAL_EWG_WORLD) || worldName.equalsIgnoreCase(ConfigManager.SURVIVAL_ORIGIN_WORLD))) {
            if (RED_STONE_WIRE_BLOCK.get(block) == null) {
                RED_STONE_WIRE_BLOCK.put(block, 1);
            } else {
                RED_STONE_WIRE_BLOCK.put(block, RED_STONE_WIRE_BLOCK.get(block) + 1);
            }

            if (RED_STONE_WIRE_BLOCK.get(block) > MAX_LIMIT_SIGNAL) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        block.setType(Material.AIR);
                        RED_STONE_WIRE_BLOCK.remove(block);

                        Main.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
                            @Override
                            public void run() {
                                block.setType(Material.REDSTONE_WIRE);
                            }
                        }, WAIT_RED_STONE_TIME);
                    }
                }.runTaskLater(Main.getPlugin(), 2);
            }
        }
    }
}
