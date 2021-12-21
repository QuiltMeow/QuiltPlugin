package ew.quilt.bypass;

import ew.quilt.plugin.Main;
import ew.quilt.util.TimerUtil;
import org.bukkit.Bukkit;

public class IdleTimeoutBypass {

    private static final int IDLE_BYPASS = 60 * 24 * 7;

    public static void registerIdleTimeoutBypass() {
        Main.getPlugin().getServer().getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
            @Override
            public void run() {
                Bukkit.getServer().setIdleTimeout(IDLE_BYPASS);
            }
        }, TimerUtil.secondToTick(1), TimerUtil.secondToTick(1));
    }
}
