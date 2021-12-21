package ew.quilt.Bar.api.nms;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import ew.quilt.Bar.api.BarUtil;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class v1_6 extends FakeDragon {

    private static final Integer EntityID = 6000;

    public v1_6(String name, Location loc) {
        super(name, loc);
    }

    @SuppressWarnings("deprecation")
    @Override
    public Object getSpawnPacket() {
        Class<?> mob_class = BarUtil.getCraftClass("Packet24MobSpawn");
        Object mobPacket = null;
        try {
            mobPacket = mob_class.newInstance();

            Field a = BarUtil.getField(mob_class, "a");
            a.setAccessible(true);
            a.set(mobPacket, EntityID);

            Field b = BarUtil.getField(mob_class, "b");
            b.setAccessible(true);
            b.set(mobPacket, EntityType.ENDER_DRAGON.getTypeId());

            Field c = BarUtil.getField(mob_class, "c");
            c.setAccessible(true);
            c.set(mobPacket, getX());

            Field d = BarUtil.getField(mob_class, "d");
            d.setAccessible(true);
            d.set(mobPacket, getY());

            Field e = BarUtil.getField(mob_class, "e");
            e.setAccessible(true);
            e.set(mobPacket, getZ());

            Field f = BarUtil.getField(mob_class, "f");
            f.setAccessible(true);
            f.set(mobPacket, (byte) ((int) (getPitch() * 256F / 360F)));

            Field g = BarUtil.getField(mob_class, "g");
            g.setAccessible(true);
            g.set(mobPacket, (byte) ((int) 0));

            Field h = BarUtil.getField(mob_class, "h");
            h.setAccessible(true);
            h.set(mobPacket, (byte) ((int) (getYaw() * 256F / 360F)));

            Field i = BarUtil.getField(mob_class, "i");
            i.setAccessible(true);
            i.set(mobPacket, getXvel());

            Field j = BarUtil.getField(mob_class, "j");
            j.setAccessible(true);
            j.set(mobPacket, getYvel());

            Field k = BarUtil.getField(mob_class, "k");
            k.setAccessible(true);
            k.set(mobPacket, getZvel());

            Object watcher = getWatcher();
            Field t = BarUtil.getField(mob_class, "t");
            t.setAccessible(true);
            t.set(mobPacket, watcher);
        } catch (InstantiationException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
        return mobPacket;
    }

    @Override
    public Object getDestroyPacket() {
        Class<?> packet_class = BarUtil.getCraftClass("Packet29DestroyEntity");
        Object packet = null;
        try {
            packet = packet_class.newInstance();

            Field a = BarUtil.getField(packet_class, "a");
            a.setAccessible(true);
            a.set(packet, new int[]{EntityID});
        } catch (InstantiationException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
        return packet;
    }

    @Override
    public Object getMetaPacket(Object watcher) {
        Class<?> packet_class = BarUtil.getCraftClass("Packet40EntityMetadata");
        Object packet = null;
        try {
            packet = packet_class.newInstance();

            Field a = BarUtil.getField(packet_class, "a");
            a.setAccessible(true);
            a.set(packet, EntityID);

            Method watcher_c = BarUtil.getMethod(watcher.getClass(), "c");
            Field b = BarUtil.getField(packet_class, "b");
            b.setAccessible(true);
            b.set(packet, watcher_c.invoke(watcher));
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            ex.printStackTrace();
        }
        return packet;
    }

    @Override
    public Object getTeleportPacket(Location loc) {
        Class<?> packet_class = BarUtil.getCraftClass("Packet34EntityTeleport");
        Object packet = null;
        try {
            packet = packet_class.newInstance();

            Field a = BarUtil.getField(packet_class, "a");
            a.setAccessible(true);
            a.set(packet, EntityID);

            Field b = BarUtil.getField(packet_class, "b");
            b.setAccessible(true);
            b.set(packet, (int) Math.floor(loc.getX() * 32));

            Field c = BarUtil.getField(packet_class, "c");
            c.setAccessible(true);
            c.set(packet, (int) Math.floor(loc.getY() * 32));

            Field d = BarUtil.getField(packet_class, "d");
            d.setAccessible(true);
            d.set(packet, (int) Math.floor(loc.getZ() * 32));

            Field e = BarUtil.getField(packet_class, "e");
            e.setAccessible(true);
            e.set(packet, (byte) ((int) (loc.getYaw() * 256F / 360F)));

            Field f = BarUtil.getField(packet_class, "f");
            f.setAccessible(true);
            f.set(packet, (byte) ((int) (loc.getPitch() * 256F / 360F)));
        } catch (InstantiationException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
        return packet;
    }

    @Override
    public Object getWatcher() {
        Class<?> watcher_class = BarUtil.getCraftClass("DataWatcher");
        Object watcher = null;
        try {
            watcher = watcher_class.newInstance();

            Method a = BarUtil.getMethod(watcher_class, "a", new Class<?>[]{int.class, Object.class});
            a.setAccessible(true);

            a.invoke(watcher, 0, isVisible() ? (byte) 0 : (byte) 0x20);
            a.invoke(watcher, 6, (Float) (float) health);
            a.invoke(watcher, 7, (Integer) (int) 0);
            a.invoke(watcher, 8, (Byte) (byte) 0);
            a.invoke(watcher, 10, (String) name);
            a.invoke(watcher, 11, (Byte) (byte) 1);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            ex.printStackTrace();
        }
        return watcher;
    }
}
