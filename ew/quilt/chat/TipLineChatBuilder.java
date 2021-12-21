package ew.quilt.chat;

import org.bukkit.entity.Player;

public class TipLineChatBuilder extends LineChatMessage {

    private final HoverHelper message = new HoverHelper();

    public TipLineChatBuilder(String player) {
        super(player);
    }

    public TipLineChatBuilder(Player player) {
        super(player);
    }

    public HoverHelper getMessage() {
        return message;
    }
}
