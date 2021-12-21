package ew.quilt.NoAFKFishing;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class NoAFKFishingListener implements Listener {

    private static final boolean USING_HUNGER_FUNCTION = true;
    private static final boolean USING_HP_FUNCTION = true;
    private static final boolean USING_CANCELING_FISH_CAUGHT_FUNCTION = true;

    private static final int EXTRA_HUNGER_LEVEL = 2;
    private static final double HPLIMIT = 0.7;
    private static final double DISTANCE_BETWEEN_FISHING = 1;

    private static final String PLUGIN_PREFIX = "[釣魚] ";
    private static final String YOU_TAKE_EXTRA_HUNGER = "你因釣魚消而耗體力，§E飽食度下降了 §F點數 : ";
    private static final String YOUR_HP_IS_NOT_ENOUGH_TO_GO_FISHING = "你的血量目前過低，釣魚血量至少需要 ";
    private static final String YOU_HAVE_TO_CHANGE_A_PLACE_AT_LEAST = "你必須換個地方釣魚，這裡已經沒有魚了 魚鉤至少§E距離目前位置 §F格數 : ";

    private static final int EVENT_LOCATION_LIST_MAX = 40;

    private static final List<Location> EVENT_LOCATION_LIST = new ArrayList<>();
    private int currentReplaceLocationIndex = 0;

    private boolean isValidLocation(Location location) {
        for (int i = 0; i < EVENT_LOCATION_LIST.size(); ++i) {
            if (EVENT_LOCATION_LIST.get(i).getWorld().equals(location.getWorld()) && EVENT_LOCATION_LIST.get(i).distance(location) < DISTANCE_BETWEEN_FISHING) {
                return false;
            }
        }

        if (EVENT_LOCATION_LIST.size() < EVENT_LOCATION_LIST_MAX) {
            EVENT_LOCATION_LIST.add(location);
        } else {
            EVENT_LOCATION_LIST.set(currentReplaceLocationIndex++, location);
            currentReplaceLocationIndex %= EVENT_LOCATION_LIST_MAX;
        }
        return true;
    }

    @EventHandler
    public void onPlayerCaughtFish(PlayerFishEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("quilt.afk.fishing")) {
            return;
        }

        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            if (USING_HUNGER_FUNCTION) {
                player.setFoodLevel(player.getFoodLevel() - EXTRA_HUNGER_LEVEL);
                player.setSaturation(player.getFoodLevel() - EXTRA_HUNGER_LEVEL);
                player.sendMessage(PLUGIN_PREFIX + YOU_TAKE_EXTRA_HUNGER + EXTRA_HUNGER_LEVEL);
            }

            if (USING_HP_FUNCTION) {
                if (event.getPlayer().getHealth() / event.getPlayer().getMaxHealth() < HPLIMIT) {
                    event.getPlayer().sendMessage(PLUGIN_PREFIX + YOUR_HP_IS_NOT_ENOUGH_TO_GO_FISHING + Math.round(HPLIMIT * 100) + " %");
                    event.setCancelled(true);
                }
            }

            if (USING_CANCELING_FISH_CAUGHT_FUNCTION) {
                Location hookLocation = event.getHook().getLocation();
                if (!isValidLocation(hookLocation)) {
                    player.sendMessage(PLUGIN_PREFIX + YOU_HAVE_TO_CHANGE_A_PLACE_AT_LEAST + DISTANCE_BETWEEN_FISHING);
                    event.setCancelled(true);
                }
            }
        }
    }

    public static double getDistanceBetweenFishing() {
        return DISTANCE_BETWEEN_FISHING;
    }

    public static List<Location> getEventLocationList() {
        return EVENT_LOCATION_LIST;
    }
}
