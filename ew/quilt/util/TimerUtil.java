package ew.quilt.util;

import java.text.NumberFormat;
import org.bukkit.Bukkit;

public class TimerUtil {

    public static double tickToSecond(int tick) {
        return (double) tick / 20;
    }

    public static int secondToTick(int second) {
        return second * 20;
    }

    public static int minuteToTick(int minute) {
        return secondToTick(minute * 60);
    }

    public static int hourToTick(int hour) {
        return secondToTick(hour * 60 * 60);
    }

    public static double getTPS() {
        double ret = Bukkit.getServer().getTPS()[0];
        if (ret > 20) {
            ret = 20;
        }
        NumberFormat NF = NumberFormat.getNumberInstance();
        NF.setMaximumFractionDigits(3);
        return Double.parseDouble(NF.format(ret));
    }
}
