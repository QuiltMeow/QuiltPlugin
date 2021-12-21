package ew.quilt.protect;

import ew.quilt.plugin.Main;
import ew.quilt.util.TimerUtil;
import java.awt.Point;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class MapMaliciousLoadDetector implements Listener {

    private static final int CHECK_TIME = 60;
    private static final int LIST_LIMIT = 10;
    private static final int MIN_ENABLE_ONLINE_PLAYER = 35;

    private static final Map<String, Point> LAST_LOCATION = new HashMap<>();
    private static final Map<String, String> LAST_WORLD = new HashMap<>();
    private static final Set<String> USE_TP_FEATURE = new HashSet<>();

    private static final Map<String, Double> PLAYER_DISTANCE = new HashMap<>();
    private static Date LAST_CHECK_TIME = null;

    public static Comparator<Map.Entry<String, Double>> distanceComparator = new Comparator<Map.Entry<String, Double>>() {
        @Override
        public int compare(Map.Entry<String, Double> left, Map.Entry<String, Double> right) {
            return left.getValue() > right.getValue() ? -1 : left.getValue() < right.getValue() ? 1 : 0;
        }
    };

    public static void checkMapLoad() {
        List<Player> onlinePlayer = new ArrayList<>(Bukkit.getServer().getOnlinePlayers());
        if (onlinePlayer.isEmpty() || onlinePlayer.size() < MIN_ENABLE_ONLINE_PLAYER) {
            return;
        }

        PLAYER_DISTANCE.clear();
        for (Player player : onlinePlayer) { // 計算每個人走的路程
            if (player.hasPermission("quilt.map.load.check.bypass")) {
                continue;
            }

            String name = player.getName();
            if (!LAST_LOCATION.containsKey(name) || !player.getWorld().getName().equals(LAST_WORLD.get(name)) || USE_TP_FEATURE.contains(name)) { // 如果沒有記錄過 / 世界轉換 / 使用傳送指令
                LAST_LOCATION.put(name, new Point(player.getLocation().getBlockX(), player.getLocation().getBlockZ()));
                LAST_WORLD.put(name, player.getWorld().getName());
            } else { // 如果有記錄過
                Point lastPoint = LAST_LOCATION.get(name); // 得到上一次的 X Z
                Point current = new Point(player.getLocation().getBlockX(), player.getLocation().getBlockZ());
                double distance = lastPoint.distance(current);

                PLAYER_DISTANCE.put(name, distance);
                LAST_LOCATION.put(name, new Point(player.getLocation().getBlockX(), player.getLocation().getBlockZ())); // 保存該次記錄 提供給下一次呼叫
            }
        }
        USE_TP_FEATURE.clear();
        LAST_CHECK_TIME = new Date();
    }

    public static void sendCurrentInfo(CommandSender sender) {
        if (PLAYER_DISTANCE.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "目前尚無任何跑圖記錄 ...");
            return;
        }

        List<Map.Entry<String, Double>> output = new LinkedList<>(PLAYER_DISTANCE.entrySet());
        Collections.sort(output, distanceComparator);

        int limit = Math.min(PLAYER_DISTANCE.size(), LIST_LIMIT);
        sender.sendMessage("[跑圖檢測] 列出跑圖前 " + limit + " 名 :");

        int index = 0;
        for (Map.Entry<String, Double> entry : output) {
            if (entry.getValue() <= 0) {
                sender.sendMessage(ChatColor.RED + "所有玩家目前皆為靜止狀態 ...");
                break;
            }

            int distance = (int) Math.ceil(entry.getValue());
            String name = entry.getKey();
            Player player = Bukkit.getPlayer(name);
            sender.sendMessage(ChatColor.YELLOW + "編號 " + (++index) + " : " + name + " 位於世界 : " + (player == null ? "已離線" : player.getWorld().getName()) + " 距離 : " + distance);
            if (index >= limit) {
                break;
            }
        }

        if (LAST_CHECK_TIME != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sender.sendMessage(ChatColor.GREEN + "最後一次檢查時間 : " + sdf.format(LAST_CHECK_TIME));
        } else {
            sender.sendMessage(ChatColor.RED + "無法取得最後檢查時間 ...");
        }
    }

    public static void registerMapLoadDetectTimer() {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
            @Override
            public void run() {
                checkMapLoad();
            }
        }, TimerUtil.secondToTick(CHECK_TIME), TimerUtil.secondToTick(CHECK_TIME));
    }

    @EventHandler
    public void onPlayerTeleport(final PlayerTeleportEvent event) {
        USE_TP_FEATURE.add(event.getPlayer().getName());
    }
}
