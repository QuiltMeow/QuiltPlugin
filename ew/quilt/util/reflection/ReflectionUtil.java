package ew.quilt.util.reflection;

import ew.quilt.Config.ConfigManager;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.logging.Level;
import org.bukkit.Bukkit;

public class ReflectionUtil {

    private static boolean forge = false;

    static {
        try {
            Bukkit.getServer().getClass().getClassLoader().loadClass("net.minecraft.nbt.NBTBase");
            forge = true;
        } catch (ClassNotFoundException ignored) {
        }
    }

    public static Field getField(Class<?> clazz, String fname) throws Exception {
        Field f;
        try {
            f = clazz.getDeclaredField(fname);
        } catch (Exception ex) {
            f = clazz.getField(fname);
        }
        f.setAccessible(true);
        Field modifiers = Field.class.getDeclaredField("modifiers");
        modifiers.setAccessible(true);
        modifiers.setInt(f, f.getModifiers() & ~Modifier.FINAL);
        return f;
    }

    public static Object getObject(Object obj, String fname) throws Exception {
        return getField(obj.getClass(), fname).get(obj);
    }

    public static Object getObject(Class<?> clazz, Object obj, String fname) throws Exception {
        return getField(clazz, fname).get(obj);
    }

    public static void setObject(Object obj, String fname, Object value) throws Exception {
        getField(obj.getClass(), fname).set(obj, value);
    }

    public static void setObject(Class<?> clazz, Object obj, String fname, Object value) throws Exception {
        getField(clazz, fname).set(obj, value);
    }

    public static Method getMethod(Class<?> clazz, String mname) throws Exception {
        Method m = null;
        mname = methodCauldronMagic(clazz, mname);
        try {
            m = clazz.getDeclaredMethod(mname);
        } catch (Exception ex) {
            try {
                m = clazz.getMethod(mname);
            } catch (Exception exAgain) {
                for (Method me : clazz.getDeclaredMethods()) {
                    if (me.getName().equalsIgnoreCase(mname)) {
                        m = me;
                    }
                    break;
                }
                if (m == null) {
                    for (Method me : clazz.getMethods()) {
                        if (me.getName().equalsIgnoreCase(mname)) {
                            m = me;
                        }
                        break;
                    }
                }
            }
        }

        if (m == null) {
            throw new NullPointerException();
        }
        m.setAccessible(true);
        return m;
    }

    public static Method getMethod(Class<?> clazz, String mname, Class<?>... args) throws Exception {
        Method m = null;
        mname = methodCauldronMagic(clazz, mname);
        try {
            m = clazz.getDeclaredMethod(mname, args);
        } catch (Exception ex) {
            try {
                m = clazz.getMethod(mname, args);
            } catch (Exception exAgain) {
                for (Method me : clazz.getDeclaredMethods()) {
                    if (me.getName().equalsIgnoreCase(mname)) {
                        m = me;
                    }
                    break;
                }
                if (m == null) {
                    for (Method me : clazz.getMethods()) {
                        if (me.getName().equalsIgnoreCase(mname)) {
                            m = me;
                        }
                        break;
                    }
                }
            }
        }

        if (m == null) {
            throw new NullPointerException();
        }
        m.setAccessible(true);
        return m;
    }

    public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... args) throws Exception {
        Constructor<?> c = clazz.getConstructor(args);
        c.setAccessible(true);
        return c;
    }

    public static Enum<?> getEnum(Class<?> clazz, String enumname, String constant) throws Exception {
        Class<?> c = Class.forName(clazz.getName() + "$" + enumname);
        Enum<?>[] econstants = (Enum<?>[]) c.getEnumConstants();
        for (Enum<?> e : econstants) {
            if (e.name().equalsIgnoreCase(constant)) {
                return e;
            }
        }
        throw new Exception("找不到列舉常數 " + constant);
    }

    public static Enum<?> getEnum(Class<?> clazz, String constant) throws Exception {
        Class<?> c = Class.forName(clazz.getName());
        Enum<?>[] econstants = (Enum<?>[]) c.getEnumConstants();
        for (Enum<?> e : econstants) {
            if (e.name().equalsIgnoreCase(constant)) {
                return e;
            }
        }
        throw new Exception("找不到列舉常數 " + constant);
    }

    public static Class<?> getNMSClass(String clazz) throws Exception {
        String nmsPackname = "net.minecraft.server." + ConfigManager.PACKAGE_VERSION + ".";
        nmsPackname = classCauldronMagic(nmsPackname, clazz);
        return Class.forName(nmsPackname + clazz);
    }

    public static Class<?> getBukkitClass(String clazz) throws Exception {
        String cbPackname = "org.bukkit.craftbukkit." + ConfigManager.PACKAGE_VERSION + ".";
        return Class.forName(cbPackname + clazz);
    }

    public static Object invokeMethod(Class<?> clazz, Object obj, String method, Class<?>[] args, Object... initargs) throws Exception {
        return getMethod(clazz, method, args).invoke(obj, initargs);
    }

    public static Object invokeMethod(Class<?> clazz, Object obj, String method) throws Exception {
        return getMethod(clazz, method).invoke(obj, new Object[]{});
    }

    public static Object invokeMethod(Class<?> clazz, Object obj, String method, Object... initargs) throws Exception {
        return getMethod(clazz, method).invoke(obj, initargs);
    }

    public static Object invokeMethod(Object obj, String method) throws Exception {
        return getMethod(obj.getClass(), method).invoke(obj, new Object[]{});
    }

    public static Object invokeMethod(Object obj, String method, Object[] initargs) throws Exception {
        return getMethod(obj.getClass(), method).invoke(obj, initargs);
    }

    public static Object invokeConstructor(Class<?> clazz, Class<?>[] args, Object... initargs) throws Exception {
        return getConstructor(clazz, args).newInstance(initargs);
    }

    private static String methodCauldronMagic(Class<?> clazz, String mname) {
        if (!forge || !(clazz.getName().startsWith("net.minecraft"))) {
            return mname;
        }
        String original = mname;
        if (clazz.getName().equals("net.minecraft.item.ItemStack")) {
            if (mname.equals("hasTag")) {
                mname = "func_77942_o";
            }
            if (mname.equals("getTag")) {
                mname = "func_77978_p";
            }
            if (mname.equals("setTag")) {
                mname = "func_77982_d";
            }
        }
        if (clazz.getName().equals("net.minecraft.nbt.NBTTagCompound")) {
            if (mname.equals("get")) {
                mname = "func_74781_a";
            }
            if (mname.equals("set")) {
                mname = "func_74782_a";
            }
            if (mname.equals("remove")) {
                mname = "func_82580_o";
            }
            if (mname.equals("hasKey")) {
                mname = "func_74764_b";
            }
            if (mname.equals("setByte")) {
                mname = "func_74774_a";
            }
            if (mname.equals("setIntArray")) {
                mname = "func_74783_a";
            }
            if (mname.equals("setString")) {
                mname = "func_74778_a";
            }
            if (mname.equals("setDouble")) {
                mname = "func_74780_a";
            }
            if (mname.equals("setInt")) {
                mname = "func_74768_a";
            }
            if (mname.equals("setShort")) {
                mname = "func_74777_a";
            }
        }
        if (clazz.getName().equals("net.minecraft.nbt.NBTTagList")) {
            if (mname.equals("add")) {
                mname = "func_74742_a";
            }
        }
        if (!(mname.equalsIgnoreCase(original))) {
            Bukkit.getLogger().log(Level.INFO, "套用 Cauldron 取代方法 " + clazz.getName() + " " + original + " 為 " + mname);
        }
        return mname;
    }

    private static String classCauldronMagic(String nmsPackname, String clazz) {
        if (!forge || !(nmsPackname.startsWith("net.minecraft.server"))) {
            return nmsPackname;
        }
        String original = nmsPackname;
        String subpack = "";
        if (clazz.startsWith("NBT")) {
            subpack = "nbt.";
        }
        if (clazz.startsWith("Item")) {
            subpack = "item.";
        }
        nmsPackname = "net.minecraft." + subpack;
        Bukkit.getLogger().log(Level.INFO, "套用 Cauldron 取代類別 " + original + " " + clazz + " 為 " + nmsPackname + " " + clazz);
        return nmsPackname;
    }

    public static Object getPrivateField(String fieldName, Class targetClass, Object object) throws Exception {
        Field field;
        field = targetClass.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(object);
    }
}
