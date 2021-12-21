package ew.quilt.chat;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;

public class MultiLineChatMessage extends LineChatMessage {

    private final List<String> message = new ArrayList<>();

    public MultiLineChatMessage(String player) {
        super(player);
    }

    public MultiLineChatMessage(Player player) {
        super(player);
    }

    public List<String> getMessage() {
        return message;
    }
}
