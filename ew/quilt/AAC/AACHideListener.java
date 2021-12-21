package ew.quilt.AAC;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import ew.quilt.plugin.Main;

public class AACHideListener {

    public static void addAACCommandListener() {
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(Main.getPlugin(), new PacketType[]{PacketType.Play.Server.CHAT}) {
            @Override
            public void onPacketSending(PacketEvent event) {
                if (event.getPacketType() == PacketType.Play.Server.CHAT) {
                    WrappedChatComponent wcc = (WrappedChatComponent) event.getPacket().getChatComponents().read(0);
                    if (wcc == null) {
                        return;
                    }
                    String ready = wcc.getJson();
                    if (!event.getPlayer().hasPermission("quilt.aac.use")) {
                        if (ready.contains("[AAC]")) {
                            event.setCancelled(true);
                        } else if (ready.contains("\"text\":\"AAC")) {
                            event.setCancelled(true);
                        } else if (ready.contains("\"text\":\"ID: aac")) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        });
    }
}
