package ew.quilt.Bar.api;

import ew.quilt.Bar.api.nms.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BarUtil {

    public static boolean newProtocol = false;
    public static String version;
    public static Class<?> fakeDragonClass = v1_6.class;
    public static boolean isBelowGround = true;

    static {
        detectVersion();
    }

    public static void detectVersion() {
        if (BarAPI.useSpigotHack()) {
            fakeDragonClass = v1_8Fake.class;
            version = "v1_7_R4.";
            isBelowGround = false;
        } else {
            String name = Bukkit.getServer().getClass().getPackage().getName();
            String mcVersion = name.substring(name.lastIndexOf('.') + 1);
            String[] versions = mcVersion.split("_");

            if (versions[0].equals("v1")) {
                int minor = Integer.parseInt(versions[1]);

                if (minor == 7) {
                    newProtocol = true;
                    fakeDragonClass = v1_7.class;
                } else if (minor == 8) {
                    fakeDragonClass = v1_8.class;
                    isBelowGround = false;
                } else if (minor >= 9) {
                    fakeDragonClass = v1_9.class;
                }
            }

            version = mcVersion + ".";
        }
    }

    public static FakeDragon newDragon(String message, Location loc) {
        FakeDragon fakeDragon = null;

        try {
            fakeDragon = (FakeDragon) fakeDragonClass.getConstructor(String.class, Location.class).newInstance(message, loc);
        } catch (IllegalArgumentException | SecurityException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
            ex.printStackTrace();
        }
        return fakeDragon;
    }

    public static void sendPacket(Player p, Object packet) {
        try {
            Object nmsPlayer = getHandle(p);
            Field con_field = nmsPlayer.getClass().getField("playerConnection");
            Object con = con_field.get(nmsPlayer);
            Method packet_method = getMethod(con.getClass(), "sendPacket");
            packet_method.invoke(con, packet);
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException | InvocationTargetException | NoSuchFieldException ex) {
            ex.printStackTrace();
        }
    }

    public static Class<?> getCraftClass(String ClassName) {
        String className = "net.minecraft.server." + version + ClassName;
        Class<?> c = null;
        try {
            c = Class.forName(className);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return c;
    }

    public static Object getHandle(World world) {
        Object nms_entity = null;
        Method entity_getHandle = getMethod(world.getClass(), "getHandle");
        try {
            nms_entity = entity_getHandle.invoke(world);
        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException ex) {
            ex.printStackTrace();
        }
        return nms_entity;
    }

    public static Object getHandle(Entity entity) {
        Object nms_entity = null;
        Method entity_getHandle = getMethod(entity.getClass(), "getHandle");
        try {
            nms_entity = entity_getHandle.invoke(entity);
        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException ex) {
            ex.printStackTrace();
        }
        return nms_entity;
    }

    public static Field getField(Class<?> cl, String field_name) {
        try {
            Field field = cl.getDeclaredField(field_name);
            return field;
        } catch (SecurityException | NoSuchFieldException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Method getMethod(Class<?> cl, String method, Class<?>[] args) {
        for (Method m : cl.getMethods()) {
            if (m.getName().equals(method) && ClassListEqual(args, m.getParameterTypes())) {
                return m;
            }
        }
        return null;
    }

    public static Method getMethod(Class<?> cl, String method, Integer args) {
        for (Method m : cl.getMethods()) {
            if (m.getName().equals(method) && args.equals(m.getParameterTypes().length)) {
                return m;
            }
        }
        return null;
    }

    public static Method getMethod(Class<?> cl, String method) {
        for (Method m : cl.getMethods()) {
            if (m.getName().equals(method)) {
                return m;
            }
        }
        return null;
    }

    public static boolean ClassListEqual(Class<?>[] l1, Class<?>[] l2) {
        boolean equal = true;

        if (l1.length != l2.length) {
            return false;
        }
        for (int i = 0; i < l1.length; i++) {
            if (l1[i] != l2[i]) {
                equal = false;
                break;
            }
        }
        return equal;
    }
}
