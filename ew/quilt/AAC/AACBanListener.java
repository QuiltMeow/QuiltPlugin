package ew.quilt.AAC;

import ew.quilt.command.CommandDispatcher;
import me.konsolas.aac.api.HackType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import me.konsolas.aac.api.PlayerViolationEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class AACBanListener implements Listener {

    @EventHandler
    public void onPlayerViolation(PlayerViolationEvent event) {
        if (event.getHackType() == HackType.BADPACKETS && event.getViolations() > 300) {
            String name = event.getPlayer().getName();
            CommandDispatcher.issueServerCommand("ban-ip " + name + " 嘗試崩潰伺服器");
            CommandDispatcher.issueServerCommand("ban " + name + " 嘗試崩潰伺服器");
            Bukkit.broadcastMessage(ChatColor.RED + "玩家 " + name + " 嘗試崩潰伺服器並已遭到永久鎖定");
            event.setCancelled(true);
        }
    }
}
