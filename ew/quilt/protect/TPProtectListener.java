package ew.quilt.protect;

import ew.quilt.plugin.Main;
import ew.quilt.util.TimerUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class TPProtectListener implements Listener {

    @EventHandler
    public void onPlayerCommandPreProcess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("quilt.tp.protect.bypass")) {
            return;
        }
        String command = event.getMessage().toLowerCase();
        if (!(command.contains("tpaccept") || command.contains("tpdeny")) && (command.contains("tp") || command.contains("home") || command.contains("back") || command.contains("tele"))) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
                @Override
                public void run() {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, TimerUtil.secondToTick(5), 5));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, TimerUtil.secondToTick(5), 100));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, TimerUtil.secondToTick(5), 100));
                }
            });
        }
    }
}
