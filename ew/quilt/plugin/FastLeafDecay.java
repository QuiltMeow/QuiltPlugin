package ew.quilt.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;

public class FastLeafDecay implements Listener {

    private static final long BREAK_DELAY = 5, DECAY_DELAY = 2;
    private static final boolean SPAWN_PARTICLE = true, PLAY_SOUND = true;

    private static final byte PLAYER_PLACE_BIT = 4;
    private static final BlockFace[] NEIGHBOR = {
        BlockFace.UP, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.DOWN
    };

    private final Set<Block> scheduleBlock = new HashSet<>();

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event) {
        onBlockRemove(event.getBlock(), BREAK_DELAY);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onLeaveDecay(LeavesDecayEvent event) {
        onBlockRemove(event.getBlock(), DECAY_DELAY);
    }

    private boolean isLog(Material material) {
        switch (material) {
            case LOG:
            case LOG_2: {
                return true;
            }
            default: {
                return false;
            }
        }
    }

    private boolean isLeaf(Material material) {
        switch (material) {
            case LEAVES:
            case LEAVES_2: {
                return true;
            }
            default: {
                return false;
            }
        }
    }

    private boolean isPersistent(Block block) {
        return (block.getData() & PLAYER_PLACE_BIT) != 0;
    }

    private int getDistance(Block block) {
        List<Block> pending = new ArrayList<>();
        pending.add(block);

        Set<Block> done = new HashSet<>();
        Map<Block, Integer> distanceMap = new HashMap<>();
        distanceMap.put(block, 0);
        while (!pending.isEmpty()) {
            Block current = pending.remove(0);
            done.add(current);
            int distance = distanceMap.get(current);
            if (distance >= 5) {
                return distance;
            }
            for (BlockFace face : new BlockFace[]{BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST}) {
                Block neighbor = current.getRelative(face);
                if (done.contains(neighbor)) {
                    continue;
                }
                if (isLog(neighbor.getType())) {
                    return distance + 1;
                } else if (isLeaf(neighbor.getType())) {
                    pending.add(neighbor);
                    distanceMap.put(neighbor, distance + 1);
                }
            }
        }
        return 5;
    }

    private void onBlockRemove(Block oldBlock, long delay) {
        if (!isLog(oldBlock.getType()) && !isLeaf(oldBlock.getType())) {
            return;
        }
        for (BlockFace neighborFace : NEIGHBOR) {
            Block block = oldBlock.getRelative(neighborFace);
            if (!isLeaf(block.getType())) {
                continue;
            }
            if (isPersistent(block)) {
                continue;
            }
            if (scheduleBlock.contains(block)) {
                continue;
            }
            scheduleBlock.add(block);
            Main.getPlugin().getServer().getScheduler().runTaskLater(Main.getPlugin(), () -> decay(block), delay);
        }
    }

    private void decay(Block block) {
        if (!scheduleBlock.remove(block)) {
            return;
        }
        if (!isLeaf(block.getType())) {
            return;
        }
        if (isPersistent(block)) {
            return;
        }
        if (getDistance(block) <= 4) {
            return;
        }
        LeavesDecayEvent event = new LeavesDecayEvent(block);
        Main.getPlugin().getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }
        if (SPAWN_PARTICLE) {
            block.getWorld().spawnParticle(Particle.BLOCK_DUST, block.getLocation().add(0.5, 0.5, 0.5), 8, 0.2, 0.2, 0.2, 0, block.getType().getNewData(block.getData()));
        }
        if (PLAY_SOUND) {
            block.getWorld().playSound(block.getLocation(), Sound.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS, 0.05F, 1.2F);
        }
        block.breakNaturally();
    }
}
