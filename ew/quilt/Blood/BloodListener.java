package ew.quilt.Blood;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class BloodListener implements Listener {

    @EventHandler
    public void onBlood(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        Location location = entity.getLocation();
        if (entity.getType() != EntityType.ITEM_FRAME) {
            entity.getWorld().playEffect(location, Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
        }
    }
}
