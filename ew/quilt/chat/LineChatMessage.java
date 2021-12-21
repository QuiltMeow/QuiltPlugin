package ew.quilt.chat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public abstract class LineChatMessage {

    private final String player;

    public LineChatMessage(String player) {
        this.player = player;
    }

    public LineChatMessage(Player player) {
        this.player = player.getName();
    }

    public String getName() {
        return player;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(player);
    }
}
