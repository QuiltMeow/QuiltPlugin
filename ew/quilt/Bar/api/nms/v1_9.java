package ew.quilt.Bar.api.nms;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

public class v1_9 extends FakeDragon {

    private BossBar bar;

    public v1_9(String name, Location loc) {
        super(name, loc);
        bar = Bukkit.createBossBar(name, BarColor.PINK, BarStyle.SOLID);
    }

    public BossBar getBar() {
        return bar;
    }

    @Override
    public Object getSpawnPacket() {
        return null;
    }

    @Override
    public Object getDestroyPacket() {
        return null;
    }

    @Override
    public Object getMetaPacket(Object watcher) {
        return null;
    }

    @Override
    public Object getTeleportPacket(Location loc) {
        return null;
    }

    @Override
    public Object getWatcher() {
        return null;
    }
}
