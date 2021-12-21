package ew.quilt.BedHeal;

import ew.quilt.Config.ConfigManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BedHealListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerBedEnterEvent(PlayerBedEnterEvent event) {
        Player player = event.getPlayer();
        String worldName = player.getWorld().getName();
        if (worldName.equalsIgnoreCase(ConfigManager.SURVIVAL_EWG_WORLD) || worldName.equalsIgnoreCase(ConfigManager.SURVIVAL_ORIGIN_WORLD)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 300, 1));
        }
    }
}
