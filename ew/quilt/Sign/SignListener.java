package ew.quilt.Sign;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignListener implements Listener {

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        String[] lines = event.getLines();
        for (int i = 0; i < lines.length; ++i) {
            event.setLine(i, ChatColor.translateAlternateColorCodes('&', lines[i]));
        }
    }
}
