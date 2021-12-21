package ew.quilt.bypass;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.server.ServerListPingEvent;

public class PlayerLimitBypass {

    private static final int MAX_PLAYER = 200;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (event.getResult() == PlayerLoginEvent.Result.KICK_FULL && Bukkit.getOnlinePlayers().size() <= MAX_PLAYER) {
            event.setResult(PlayerLoginEvent.Result.ALLOWED);
        }
    }

    @EventHandler
    public void onClientPing(ServerListPingEvent event) {
        event.setMaxPlayers(MAX_PLAYER);
    }
}
