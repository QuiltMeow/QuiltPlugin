package ew.quilt.PlayerJoinQuit;

import ew.quilt.util.FireworkUtil;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerJoinEffectListener implements Listener {

    @EventHandler
    public void onVIPPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("quilt.vip.player.join.effect")) {
            return;
        }
        player.spawnParticle(Particle.SPELL_MOB, player.getLocation(), 1000);
    }

    @EventHandler
    public void onFirstPlayerJoin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPlayedBefore()) {
            Firework fw = (Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
            FireworkUtil.addRandomFireworkEffect(fw);
        }
    }
}
