package ew.quilt.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

// 特定版本載入
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldEvent;
import net.minecraft.server.v1_12_R1.BlockPosition;

public class RecordUtil {

    public static void playRecord(Player player, Location location, Material record) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutWorldEvent(1005, new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()), record.getId(), false));
    }

    public static void stopRecord(Player player, Location location) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutWorldEvent(1005, new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()), 0, false));
    }
}
