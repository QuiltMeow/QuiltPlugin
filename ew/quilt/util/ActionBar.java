package ew.quilt.util;

import ew.quilt.Config.Version.VersionChecker;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import ew.quilt.Config.Version.Version;

public class ActionBar {

    private static Object packet;
    private static Method getHandle;
    private static Method sendPacket;
    private static Field playerConnection;
    private static Class<?> nmsChatSerializer;
    private static Class<?> nmsIChatBaseComponent;
    private static Class<?> packetType;
    private static boolean simpleMessages = false;
    private static boolean simpleTitleMessages = false;

    private static Constructor<?> nmsPacketPlayOutTitle;
    private static Class<?> enumTitleAction;
    private static Method fromString;

    private static Class<?> ChatMessageclz;
    private static Class<?> sub;
    private static Object[] consts;

    static {
        try {
            packetType = Class.forName(getPacketPlayOutChat());
            Class<?> typeCraftPlayer = Class.forName(getCraftPlayerClasspath());
            Class<?> typeNMSPlayer = Class.forName(getNMSPlayerClasspath());
            Class<?> typePlayerConnection = Class.forName(getPlayerConnectionClasspath());
            nmsChatSerializer = Class.forName(getChatSerializerClasspath());
            nmsIChatBaseComponent = Class.forName(getIChatBaseComponentClasspath());
            getHandle = typeCraftPlayer.getMethod("getHandle");
            playerConnection = typeNMSPlayer.getField("playerConnection");
            sendPacket = typePlayerConnection.getMethod("sendPacket", Class.forName(getPacketClasspath()));

            if (VersionChecker.isHigher(Version.v1_11_R1)) {
                ChatMessageclz = Class.forName(getChatMessageTypeClasspath());
                consts = ChatMessageclz.getEnumConstants();
                sub = consts[2].getClass();
            }
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | NoSuchFieldException ex) {
            simpleMessages = true;
            Bukkit.getLogger().log(Level.SEVERE, "伺服器無法支援動作條訊息 所有動作條訊息將顯示在對話窗");
        }

        try {
            Class<?> typePacketPlayOutTitle = Class.forName(getPacketPlayOutTitleClasspath());
            enumTitleAction = Class.forName(getEnumTitleActionClasspath());
            nmsPacketPlayOutTitle = typePacketPlayOutTitle.getConstructor(enumTitleAction, nmsIChatBaseComponent);
            fromString = Class.forName(getClassMessageClasspath()).getMethod("fromString", String.class);
        } catch (Exception ex) {
            simpleTitleMessages = true;
            Bukkit.getLogger().log(Level.SEVERE, "伺服器無法支援標題訊息 所有標題訊息將顯示在對話窗");
        }
    }

    public static void sendTitle(Player receivingPacket, Object title) {
        String t = (String) title;
        if (t.contains("%subtitle%")) {
            sendTitle(receivingPacket, t.split("%subtitle%")[0], t.split("%subtitle%")[1]);
        } else {
            sendTitle(receivingPacket, t, "");
        }
    }

    public static void sendTitle(Player receivingPacket, Object title, Object subtitle) {
        if (simpleTitleMessages) {
            receivingPacket.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(title)));
            receivingPacket.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(subtitle)));
            return;
        }
        try {
            if (title != null) {
                Object packetTitle = nmsPacketPlayOutTitle.newInstance(enumTitleAction.getField("TITLE").get(null), ((Object[]) fromString.invoke(null, ChatColor.translateAlternateColorCodes('&', String.valueOf(title))))[0]);
                sendPacket(receivingPacket, packetTitle);
            }
            if (subtitle != null) {
                Object packetSubtitle = nmsPacketPlayOutTitle.newInstance(enumTitleAction.getField("SUBTITLE").get(null), ((Object[]) fromString.invoke(null, ChatColor.translateAlternateColorCodes('&', String.valueOf(subtitle))))[0]);
                sendPacket(receivingPacket, packetSubtitle);
            }
        } catch (Exception ex) {
            simpleTitleMessages = true;
            Bukkit.getLogger().log(Level.SEVERE, "伺服器無法支援標題訊息 所有標題訊息將顯示在對話窗");
        }
    }

    private static void sendPacket(Player player, Object packet) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Object handle = getHandle.invoke(player);
        Object connection = playerConnection.get(handle);
        sendPacket.invoke(connection, packet);
    }

    public static void send(CommandSender sender, String msg) {
        if (sender instanceof Player) {
            send((Player) sender, msg);
        } else {
            sender.sendMessage(msg);
        }
    }

    public static void send(Player receivingPacket, String msg) {
        if (msg != null) {
            msg = msg.replace("%subtitle%", "");
        }
        if (simpleMessages) {
            receivingPacket.sendMessage(msg);
            return;
        }
        try {
            Object serialized = nmsChatSerializer.getMethod("a", String.class).invoke(null, "{\"text\":\"" + ChatColor.translateAlternateColorCodes('&', JSONObject.escape(msg)) + "\"}");
            if (VersionChecker.isHigher(Version.v1_11_R1)) {
                packet = packetType.getConstructor(nmsIChatBaseComponent, sub).newInstance(serialized, consts[2]);
            } else if (VersionChecker.isHigher(Version.v1_7_R4)) {
                packet = packetType.getConstructor(nmsIChatBaseComponent, byte.class).newInstance(serialized, (byte) 2);
            } else {
                packet = packetType.getConstructor(nmsIChatBaseComponent, int.class).newInstance(serialized, 2);
            }
            Object player = getHandle.invoke(receivingPacket);
            Object connection = playerConnection.get(player);
            sendPacket.invoke(connection, packet);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException ex) {
            simpleMessages = true;
            Bukkit.getLogger().log(Level.SEVERE, "伺服器無法支援動作條訊息 所有動作條訊息將顯示在對話窗");
        }
    }

    private static String getCraftPlayerClasspath() {
        return "org.bukkit.craftbukkit." + VersionChecker.getVersion() + ".entity.CraftPlayer";
    }

    private static String getPlayerConnectionClasspath() {
        return "net.minecraft.server." + VersionChecker.getVersion() + ".PlayerConnection";
    }

    private static String getNMSPlayerClasspath() {
        return "net.minecraft.server." + VersionChecker.getVersion() + ".EntityPlayer";
    }

    private static String getPacketClasspath() {
        return "net.minecraft.server." + VersionChecker.getVersion() + ".Packet";
    }

    private static String getIChatBaseComponentClasspath() {
        return "net.minecraft.server." + VersionChecker.getVersion() + ".IChatBaseComponent";
    }

    private static String getChatSerializerClasspath() {
        if (VersionChecker.isLower(Version.v1_8_R2)) {
            return "net.minecraft.server." + VersionChecker.getVersion() + ".ChatSerializer";
        }
        return "net.minecraft.server." + VersionChecker.getVersion() + ".IChatBaseComponent$ChatSerializer";
    }

    private static String getPacketPlayOutChat() {
        return "net.minecraft.server." + VersionChecker.getVersion() + ".PacketPlayOutChat";
    }

    private static String getPacketPlayOutTitleClasspath() {
        return "net.minecraft.server." + VersionChecker.getVersion() + ".PacketPlayOutTitle";
    }

    private static String getEnumTitleActionClasspath() {
        return getPacketPlayOutTitleClasspath() + "$EnumTitleAction";
    }

    private static String getClassMessageClasspath() {
        return "org.bukkit.craftbukkit." + VersionChecker.getVersion() + ".util.CraftChatMessage";
    }

    private static String getChatMessageTypeClasspath() {
        return "net.minecraft.server." + VersionChecker.getVersion() + ".ChatMessageType";
    }
}
