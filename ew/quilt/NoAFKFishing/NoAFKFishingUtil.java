package ew.quilt.NoAFKFishing;

import org.bukkit.Location;
import org.bukkit.Particle;

public class NoAFKFishingUtil {

    public static final double RADIUS = NoAFKFishingListener.getDistanceBetweenFishing();
    public static final int P_NUMBER = (int) Math.round(RADIUS * RADIUS * 4);

    public static void playNormalEffect(Location location) {
        location.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, location, P_NUMBER, RADIUS, 0, RADIUS);
    }
}
