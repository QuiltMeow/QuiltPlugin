package ew.quilt.protect;

import ew.quilt.Config.ConfigManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class AntiVoidDeath implements Listener {

    @EventHandler
    public void playerDamageEvent(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.VOID && (event.getEntity() instanceof Player) && ConfigManager.isProtectWorld(event.getEntity().getWorld())) {
            Player player = (Player) event.getEntity();
            player.teleport(player.getWorld().getSpawnLocation());
            event.setCancelled(true);
        }
    }
}
