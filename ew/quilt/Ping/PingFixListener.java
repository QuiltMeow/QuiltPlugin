package ew.quilt.Ping;

import ew.quilt.Config.ConfigManager;
import ew.quilt.plugin.Main;
import ew.quilt.util.Randomizer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PingFixListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        Bukkit.getServer().getScheduler().runTaskLater(Main.getPlugin(), new Runnable() {
            @Override
            public void run() {
                fixPing(event.getPlayer());
            }
        }, 60);
    }

    private void fixPing(Player player) {
        int ping = getPing(player);
        if (ping > 1000 || ping < 0) {
            setPing(player, Randomizer.rand(9, 99));
        }
    }

    private int getPing(Player player) {
        int ping = Randomizer.rand(9, 99);
        try {
            Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + ConfigManager.PACKAGE_VERSION + ".entity.CraftPlayer");
            Object handle = craftPlayerClass.getMethod("getHandle", new Class[0]).invoke(craftPlayerClass.cast(player), new Object[0]);
            ping = handle.getClass().getDeclaredField("ping").getInt(handle);
        } catch (Exception localException) {
        }
        return ping;
    }

    private void setPing(Player player, int ping) {
        try {
            Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + ConfigManager.PACKAGE_VERSION + ".entity.CraftPlayer");
            Object handle = craftPlayerClass.getMethod("getHandle", new Class[0]).invoke(craftPlayerClass.cast(player), new Object[0]);
            handle.getClass().getDeclaredField("ping").set(handle, ping);
        } catch (Exception localException) {
        }
    }
}
