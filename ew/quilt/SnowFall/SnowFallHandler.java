package ew.quilt.SnowFall;

import ew.quilt.plugin.Main;
import ew.quilt.util.Randomizer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.minecraft.server.v1_12_R1.EnumParticle;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldParticles;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.scheduler.BukkitTask;

public class SnowFallHandler {

    private static final int AMOUNT = 150;
    private static final int DISTANCE_HORIZONTAL = 10;
    private static final int DISTANCE_VERTICAL = 10;
    private static final float SPEED = 0;

    private static BukkitTask task = null;

    public static void startSnow() {
        if (task != null) {
            return;
        }

        task = new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    for (int i = 0; i < AMOUNT; ++i) {
                        Location location = player.getLocation().add(randomDouble(DISTANCE_HORIZONTAL * -1, DISTANCE_HORIZONTAL), randomDouble(DISTANCE_VERTICAL * -1, DISTANCE_VERTICAL), randomDouble(DISTANCE_HORIZONTAL * -1, DISTANCE_HORIZONTAL));
                        if (location.getBlock().isEmpty()) {
                            if (location.getWorld().getHighestBlockYAt(location) < location.getY()) {
                                sendParticle(player, location, SPEED);
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(Main.getPlugin(), 0, 1);
    }

    public static void stopSnow() {
        if (task != null) {
            Bukkit.getScheduler().cancelTask(task.getTaskId());
            task = null;
        }
    }

    private static void sendParticle(Player player, Location location, float speed) {
        sendParticle(player, location, speed, 1);
    }

    public static void sendParticle(Player player, Location location, float speed, int amount) {
        EnumParticle enumParticle = EnumParticle.FIREWORKS_SPARK;
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(enumParticle, true, (float) location.getX(), (float) location.getY(), (float) location.getZ(), 0, 0, 0, speed, amount, null);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    public static double randomDouble(int min, int max) {
        return min + (max - min) * Randomizer.nextDouble();
    }
}
