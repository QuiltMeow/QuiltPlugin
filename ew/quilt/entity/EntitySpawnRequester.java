package ew.quilt.entity;

import ew.quilt.plugin.Main;
import ew.quilt.util.Randomizer;
import ew.quilt.util.TimerUtil;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class EntitySpawnRequester {

    private static final int DISTANCE_LOW = 10;
    private static final int DISTANCE_HIGH = 500;
    private static final int SPAWN_CHANCE = 30;
    private static final int SPAWN_OFFSET = 10;

    private static final List<EntityType> SPAWN_ENTITY = new ArrayList<>(Arrays.asList(new EntityType[]{
        EntityType.CREEPER,
        EntityType.ENDERMAN,
        EntityType.SKELETON,
        EntityType.SLIME,
        EntityType.SPIDER,
        EntityType.WITCH,
        EntityType.ZOMBIE,
        EntityType.ZOMBIE_VILLAGER
    }));

    private static final Map<String, Point> PLAYER_IDLE = new HashMap<>();

    public static void registerEntityRequester() {
        Main.getPlugin().getServer().getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    String name = player.getName();
                    if (!PLAYER_IDLE.containsKey(name)) {
                        PLAYER_IDLE.put(name, new Point(player.getLocation().getBlockX(), player.getLocation().getBlockZ()));
                        continue;
                    }
                    Point lastPoint = PLAYER_IDLE.get(name);
                    Point current = new Point(player.getLocation().getBlockX(), player.getLocation().getBlockZ());
                    PLAYER_IDLE.put(name, new Point(player.getLocation().getBlockX(), player.getLocation().getBlockZ()));
                    double distance = lastPoint.distance(current);
                    if (distance <= DISTANCE_LOW || distance >= DISTANCE_HIGH) {
                        continue;
                    }
                    if (Randomizer.nextInt(100) < SPAWN_CHANCE) {
                        int addX = Randomizer.nextInt() % 2 == 0 ? Randomizer.nextInt(SPAWN_OFFSET + 1) : -Randomizer.nextInt(SPAWN_OFFSET + 1);
                        int addZ = Randomizer.nextInt() % 2 == 0 ? Randomizer.nextInt(SPAWN_OFFSET + 1) : -Randomizer.nextInt(SPAWN_OFFSET + 1);
                        Location spawn = player.getLocation().add(addX, 0, addZ);
                        if (spawn.getBlock().getType() != Material.AIR) {
                            continue;
                        }
                        player.getWorld().spawnEntity(spawn, SPAWN_ENTITY.get(Randomizer.nextInt(SPAWN_ENTITY.size())));
                    }
                }
            }
        }, TimerUtil.secondToTick(30), TimerUtil.secondToTick(30));
    }
}
