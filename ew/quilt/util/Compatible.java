package ew.quilt.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Compatible {

    public static List<Player> getOnlinePlayers() {
        try {
            Method method = Bukkit.class.getDeclaredMethod("getOnlinePlayers", new Class[0]);
            Object result = method.invoke(Bukkit.getServer(), new Object[0]);
            if ((result instanceof Player[])) {
                return Arrays.asList((Player[]) result);
            }
            return (List) result;
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }
}
