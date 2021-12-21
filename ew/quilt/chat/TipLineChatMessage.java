package ew.quilt.chat;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;

public class TipLineChatMessage extends LineChatMessage {

    private final List<HoverHelper> message = new ArrayList<>();

    public TipLineChatMessage(String player) {
        super(player);
    }

    public TipLineChatMessage(Player player) {
        super(player);
    }

    public List<HoverHelper> getMessage() {
        return message;
    }
}
