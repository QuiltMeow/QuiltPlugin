package ew.quilt.DeathExp;

import java.util.HashMap;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class DeathExpListener implements Listener {

    public final HashMap<String, Integer> exp = new HashMap<>();

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            Player player = (Player) entity;
            if (!player.hasPermission("quilt.keep.exp") && player.getHealth() <= 0) {
                exp.put(player.getName(), player.getTotalExperience​());
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        Integer deathExp = exp.get(name);
        if (deathExp == null) {
            return;
        }
        exp.remove(name);
        player.setTotalExperience((int) (deathExp * 0.9));
    }
}
