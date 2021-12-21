package ew.quilt.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class EntityHelper {

    public static Collection<Entity> getNearbyEntity(Location location, int radius) {
        int chunkRadius = radius < 16 ? 1 : (radius - (radius % 16)) / 16;
        HashSet<Entity> radiusEntity = new HashSet<>();

        for (int chunkX = 0 - chunkRadius; chunkX <= chunkRadius; ++chunkX) {
            for (int chunkZ = 0 - chunkRadius; chunkZ <= chunkRadius; ++chunkZ) {
                int x = (int) location.getX(), y = (int) location.getY(), z = (int) location.getZ();
                for (Entity entity : new Location(location.getWorld(), x + (chunkX * 16), y, z + (chunkZ * 16)).getChunk().getEntities()) {
                    if (entity.getLocation().distance(location) <= radius && entity.getLocation().getBlock() != location.getBlock()) {
                        radiusEntity.add(entity);
                    }
                }
            }
        }
        return radiusEntity;
    }

    public static Collection<Player> getNearbyPlayer(Location location, int radius) {
        Collection<Player> ret = new HashSet<>();
        Collection<Entity> entityList = getNearbyEntity(location, radius);

        for (Entity entity : entityList) {
            if (entity instanceof Player) {
                ret.add((Player) entity);
            }
        }
        return ret;
    }

    public static boolean isOutside(Entity entity) {
        Location location = entity.getLocation();
        return entity.getWorld().getHighestBlockYAt(location) < location.getY();
    }

    public static List<Entity> getAllEntity() {
        List<Entity> ret = new ArrayList<>();
        for (World world : Bukkit.getWorlds()) {
            ret.addAll(world.getEntities());
        }
        return ret;
    }

    public static Location getNearByLocation(Entity entity, int offset) {
        World world = entity.getWorld();
        Location location = entity.getLocation();
        double x, z;
        x = location.getX() + ((Randomizer.nextInt() % 2) == 0 ? Randomizer.nextInt(offset) : -Randomizer.nextInt(offset));
        z = location.getZ() + ((Randomizer.nextInt() % 2) == 0 ? Randomizer.nextInt(offset) : -Randomizer.nextInt(offset));
        return new Location(world, x, location.getY(), z);
    }

    public static boolean canSpawnMonster(Location location) {
        Block block = location.getBlock();
        Block upBlock = new Location(location.getWorld(), location.getX(), location.getY() + 1, location.getZ()).getBlock();
        return block.getType() == Material.AIR && upBlock.getType() == Material.AIR;
    }
}
