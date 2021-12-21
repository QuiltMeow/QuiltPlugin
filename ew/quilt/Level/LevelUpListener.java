package ew.quilt.Level;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import ew.quilt.Config.ConfigManager;
import java.lang.reflect.InvocationTargetException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLevelChangeEvent;

public class LevelUpListener implements Listener {

    private static final int FADE_IN = 1;
    private static final int FADE_OUT = 1;
    private static final int TIME = 3;

    private static final String LEVEL_UP_TITLE = "&CL&6e&Ev&Ae&9l &5U&Dp &8!";
    private static final String LEVEL_UP_SUB_TITLE = "&3目前等級 &7: &b%level%";

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLevelUp(PlayerLevelChangeEvent event) {
        if (event.getNewLevel() > event.getOldLevel()) {
            sendTitle(event.getPlayer(), LEVEL_UP_TITLE.replaceAll("&", "§"), 0, FADE_IN, TIME, FADE_OUT);
            sendTitle(event.getPlayer(), LEVEL_UP_SUB_TITLE.replaceAll("&", "§").replaceAll("%level%", String.valueOf(event.getNewLevel())), 1, FADE_IN, TIME, FADE_OUT);
        }
    }

    public static void sendTitle(Player player, String text, int action, int fadeIn, int time, int fadeOut) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.TITLE);
        if (action == 0) {
            packet.getTitleActions().write(0, EnumWrappers.TitleAction.TITLE);
        } else {
            packet.getTitleActions().write(0, EnumWrappers.TitleAction.SUBTITLE);
        }
        packet.getChatComponents().write(0, WrappedChatComponent.fromText(text));
        packet.getIntegers().write(0, fadeIn);
        packet.getIntegers().write(1, time);
        packet.getIntegers().write(2, fadeOut);
        try {
            ConfigManager.getProtocolManager().sendServerPacket(player, packet, false);
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        }
    }
}
