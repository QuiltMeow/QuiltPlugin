package ew.quilt.Explode;

// import com.bekvon.bukkit.residence.Residence;
// import com.bekvon.bukkit.residence.protection.ClaimedResidence;
// import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class ExplodeListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityExplode(EntityExplodeEvent event) {
        switch (event.getEntityType()) {
            case PRIMED_TNT:
            case MINECART_TNT: {
                return;
            }
        }
        event.setCancelled(true);
    }

    /* @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityExplode(final EntityExplodeEvent event) {
        if (!event.getEntity().getWorld().getName().equalsIgnoreCase("world_nether")) {
            switch (event.getEntityType()) {
                case ENDER_CRYSTAL:
                case PRIMED_TNT:
                case MINECART_TNT:
                case WITHER_SKULL:
                case FIREBALL: {
                    event.setCancelled(true);
                    break;
                }
            }
        }
    } */

 /* @EventHandler(priority = EventPriority.HIGHEST)
    public void onResidenceExplodeProtect(final EntityExplodeEvent event) {
        Location location = event.getLocation();
        ClaimedResidence res = Residence.getInstance().getResidenceManager().getByLoc(location);
        if (res != null && !res.getPermissions().has("explode", false)) {
            event.setCancelled(true);
        }
    } */
}
