package ew.quilt.util.reflection.minecraft;

import ew.quilt.util.reflection.resolver.AccessUtil;
import java.lang.reflect.Method;
import org.bukkit.entity.Entity;

public class MinecraftResolver {

    private static OBCClassResolver obcClassResolver = new OBCClassResolver();
    private static Class<?> craftEntity;

    static {
        try {
            craftEntity = (Class<?>) MinecraftResolver.obcClassResolver.resolve(new String[]{"entity.CraftEntity"});
        } catch (ReflectiveOperationException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Object getHandle(final Object object) throws ReflectiveOperationException {
        Method method;
        try {
            method = AccessUtil.setAccessible(object.getClass().getDeclaredMethod("getHandle", (Class<?>[]) new Class[0]));
        } catch (ReflectiveOperationException ex) {
            method = AccessUtil.setAccessible(craftEntity.getDeclaredMethod("getHandle", (Class<?>[]) new Class[0]));
        }
        return method.invoke(object, new Object[0]);
    }

    public static Entity getBukkitEntity(final Object object) throws ReflectiveOperationException {
        Method method;
        try {
            method = AccessUtil.setAccessible(object.getClass().getDeclaredMethod("getBukkitEntity", (Class<?>[]) new Class[0]));
        } catch (ReflectiveOperationException ex) {
            method = AccessUtil.setAccessible(craftEntity.getDeclaredMethod("getHandle", (Class<?>[]) new Class[0]));
        }
        return (Entity) method.invoke(object, new Object[0]);
    }

    public static Object getHandleSilent(Object object) {
        try {
            return getHandle(object);
        } catch (Exception ex) {
        }
        return null;
    }
}
