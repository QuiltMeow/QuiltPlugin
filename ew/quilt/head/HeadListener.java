package ew.quilt.head;

import ew.quilt.util.Randomizer;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class HeadListener implements Listener {

    private static final boolean DROP_PLAYER_HEAD = true;
    private static final int PLAYER_HEAD_CHANCE = 10;
    private static final boolean DROP_EXTRA_EXP = false;

    private static final boolean DROP_MOB_HEAD = false;
    private static final int MOB_HEAD_CHANCE = 5;

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        Location dropLocation = event.getEntity().getLocation().add(0.0, 1.0, 0.0);
        if (entity.getKiller() instanceof Player) {
            if (entity instanceof Player) {
                if (DROP_PLAYER_HEAD) {
                    int chance = Randomizer.nextInt(100);
                    if (chance < PLAYER_HEAD_CHANCE) {
                        if (DROP_EXTRA_EXP) {
                            Float tenPercent = Float.valueOf(event.getDroppedExp() * 10 / 100);
                            if (tenPercent > 0) {
                                entity.getWorld().playSound(dropLocation, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5F, 1);
                                entity.getKiller().setExp(entity.getKiller().getExp() + tenPercent);
                            }
                        }
                        String name = ((Player) entity).getName();
                        HeadManager.dropHead(dropLocation, new Head(name, name));
                    }
                }
            } else if (DROP_MOB_HEAD) {
                int chance = Randomizer.nextInt(100);
                if (chance < MOB_HEAD_CHANCE) {
                    if (entity.getType() == EntityType.CREEPER) {
                        HeadManager.dropCreeperHead(dropLocation);
                    } else if (entity.getType() == EntityType.ZOMBIE) {
                        HeadManager.dropZombieHead(dropLocation);
                    } else if (entity.getType() == EntityType.SKELETON) {
                        HeadManager.dropSkeletonHead(dropLocation);
                    } else if (entity.getType() == EntityType.WITHER_SKULL) {
                        HeadManager.dropWitherHead(dropLocation);
                    } else if (HeadManager.getHead(entity.getType().toString()) != null) {
                        Head head = HeadManager.getHead(entity.getType().toString());
                        HeadManager.dropHead(dropLocation, head);
                    }
                }
            }
        }
    }
}
