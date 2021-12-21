package ew.quilt.BlackList;

import com.comphenix.protocol.Packets;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ConnectionSide;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import ew.quilt.plugin.Main;
import ew.quilt.util.TimerUtil;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class BlackListPlayerControl implements Listener {

    @EventHandler
    public void onPlayerWhisper(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String[] args = event.getMessage().split(" ");
        String type = args[0];
        if (BlackListPlayer.isBlackList(player)) {
            if (type.equalsIgnoreCase("/w")
                    || type.equalsIgnoreCase("/whisper")
                    || type.equalsIgnoreCase("/r")
                    || type.equalsIgnoreCase("/reply")
                    || type.equalsIgnoreCase("/m")
                    || type.equalsIgnoreCase("/msg")
                    || type.equalsIgnoreCase("/t")
                    || type.equalsIgnoreCase("/tell")) {
                event.setCancelled(true);
            }
        }
    }

    public static void registerNoReceiveChat() {
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(Main.getPlugin(), ConnectionSide.SERVER_SIDE, Packets.Server.CHAT) {
            @Override
            public void onPacketSending(PacketEvent event) {
                try {
                    if (BlackListPlayer.isBlackList(event.getPlayer())) {
                        event.setCancelled(true);
                    }
                } catch (UnsupportedOperationException ex) {
                }
            }
        });
    }

    public static void registerWatchDog() {
        Main.getPlugin().getServer().getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
            @Override
            public void run() {
                for (String uuid : BlackListPlayer.getLimitPlayer()) {
                    Player player = Bukkit.getPlayer(UUID.fromString(uuid));
                    if (player != null) {
                        player.kickPlayer("java.lang.NullPointerException: null");
                    }
                }
            }
        }, TimerUtil.minuteToTick(3), TimerUtil.minuteToTick(3));
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (BlackListPlayer.isBlackList(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}
