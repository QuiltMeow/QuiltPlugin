package ew.quilt.plugin;

import com.comphenix.protocol.PacketType.Status.Server;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerOptions;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import java.util.Arrays;

public class ServerVersionListener {

    public static void registerServerVersion() {
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(Main.getPlugin(), ListenerPriority.HIGHEST, Arrays.asList(Server.OUT_SERVER_INFO), ListenerOptions.ASYNC) {
            @Override
            public void onPacketSending(PacketEvent event) {
                WrappedServerPing ping = event.getPacket().getServerPings().read(0);
                ping.setVersionName("§B貓貓服務端");
            }
        });
    }
}
