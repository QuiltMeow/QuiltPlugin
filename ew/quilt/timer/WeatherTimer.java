package ew.quilt.timer;

import ew.quilt.Config.ConfigManager;
import ew.quilt.plugin.Main;
import ew.quilt.util.Randomizer;
import ew.quilt.util.TimerUtil;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class WeatherTimer {

    public static void registerWeatherTimer() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
            @Override
            public void run() {
                World[] toLightning = {Bukkit.getWorld(ConfigManager.SURVIVAL_EWG_WORLD), Bukkit.getWorld(ConfigManager.SURVIVAL_ORIGIN_WORLD)};
                for (World world : toLightning) {
                    if (world == null) {
                        continue;
                    }
                    if (world.hasStorm()) {
                        List<Player> playerList = world.getPlayers();
                        if (playerList.isEmpty()) {
                            continue;
                        }
                        int chance = Randomizer.nextInt(100);
                        if (chance < 30) {
                            Player target = playerList.get(Randomizer.nextInt(playerList.size()));
                            Location location = target.getLocation();
                            if (world.getHighestBlockYAt(location) < location.getY()) {
                                world.strikeLightning(location);
                            }
                        }
                    }
                }
            }
        }, TimerUtil.minuteToTick(10), TimerUtil.minuteToTick(1));
    }
}
