package ew.quilt.NoAFKFishing;

import ew.quilt.plugin.Main;
import org.bukkit.World;

public class NoAFKFishingRegularUpdate extends Thread {

    private int currentDay = 0;

    @Override
    public void run() {
        World world = Main.getPlugin().getServer().getWorlds().get(0);

        int day = (int) (world.getFullTime() / 24000);
        if (Math.abs(day - currentDay) >= 1) {
            NoAFKFishingListener.getEventLocationList().clear();
            currentDay = day;
        }

        for (int i = 0; i < NoAFKFishingListener.getEventLocationList().size(); ++i) {
            NoAFKFishingUtil.playNormalEffect(NoAFKFishingListener.getEventLocationList().get(i));
        }
    }
}
