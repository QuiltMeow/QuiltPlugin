package ew.quilt.chat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatFilter implements Listener {

    private static final List<String> BAD_MESSAGE = new ArrayList<>(Arrays.asList(new String[]{
        "ddos",
        "drdos",
        "8591",
        "hack"
    }));

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage().toLowerCase().replaceAll(" ", "");
        if (message.contains("bang")) {
            event.getPlayer().sendMessage(ChatColor.RED + "NO BANG !");
            event.setCancelled(true);
        }
        for (String check : BAD_MESSAGE) {
            if (message.contains(check)) {
                event.setCancelled(true);
            }
        }
    }
}
