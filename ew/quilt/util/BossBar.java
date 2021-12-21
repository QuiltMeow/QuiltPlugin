package ew.quilt.util;

import ew.quilt.Bar.api.BarAPI;
import org.bukkit.entity.Player;

public class BossBar {

    public static void messageBar(String message, Player target, int second) {
        BarAPI.setMessage(target, message, second);
    }
}
