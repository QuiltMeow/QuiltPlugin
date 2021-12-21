package ew.quilt.NoDeathMessage;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class NoDeathMessageListener implements Listener {

    @EventHandler
    public void onNoDeathMessage(PlayerDeathEvent event) {
        String reason = event.getDeathMessage();
        event.getEntity().sendMessage(reason);
        event.setDeathMessage(null);
    }
}
