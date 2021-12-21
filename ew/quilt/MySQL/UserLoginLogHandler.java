package ew.quilt.MySQL;

import ew.quilt.plugin.Main;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class UserLoginLogHandler implements Listener {

    private static final String SQL_TABLE = DatabaseConnection.getPrefix() + "login";

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String ip = player.getAddress().getHostString();
        Date date = new Date();

        Connection con = DatabaseConnection.getConnection();
        try (PreparedStatement ps = con.prepareStatement("INSERT INTO `" + SQL_TABLE + "` (`uuid`, `name`, `ip`, `lastlogin`) VALUES (?, ?, ?, ?)")) {
            ps.setString(1, player.getUniqueId().toString());
            ps.setString(2, player.getName());
            ps.setString(3, ip);
            ps.setTimestamp(4, new Timestamp(date.getTime()));
            ps.executeUpdate();
        } catch (SQLException ex) {
            Main.getPlugin().getLogger().log(Level.WARNING, "發生資料庫例外狀況 : " + ex);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        String uuid = event.getPlayer().getUniqueId().toString();
        Date date = new Date();

        Connection con = DatabaseConnection.getConnection();
        try (PreparedStatement pss = con.prepareStatement("SELECT MAX(`id`) FROM `" + SQL_TABLE + "` WHERE `uuid` = ?")) {
            pss.setString(1, uuid);
            try (ResultSet rss = pss.executeQuery()) {
                if (rss.next()) {
                    int id = rss.getInt(1);
                    try (PreparedStatement psu = con.prepareStatement("UPDATE `" + SQL_TABLE + "` SET `lastlogout` = ? WHERE `id` = ? AND `uuid` = ?")) {
                        psu.setTimestamp(1, new Timestamp(date.getTime()));
                        psu.setInt(2, id);
                        psu.setString(3, uuid);
                        psu.executeUpdate();
                    }
                }
            }
        } catch (SQLException ex) {
            Main.getPlugin().getLogger().log(Level.WARNING, "發生資料庫例外狀況 : " + ex);
        }
    }

    public static long getTotalOnlineTime(String name) {
        List<PlayerLoginInformation> info = getLoginLog(name, false);
        if (info == null || info.isEmpty()) {
            return -1;
        }
        long ret = 0;
        for (PlayerLoginInformation player : info) {
            long current = player.getLastOnlineTime();
            if (current == -1) {
                continue;
            }
            ret += current;
        }
        return ret;
    }

    public static List<PlayerLoginInformation> getNameHistoryByUUID(String uuid) {
        PlayerLoginInformation player = getPlayerByUUID(uuid);
        if (player == null) {
            return null;
        }
        List<PlayerLoginInformation> result = query("uuid", player.getUUID());
        if (result == null) {
            return null;
        }

        List<PlayerLoginInformation> ret = new ArrayList<>();
        for (PlayerLoginInformation info : result) {
            String current = info.getName();

            boolean shouldAdd = true;
            for (PlayerLoginInformation check : ret) {
                if (check.getName().equals(current)) {
                    shouldAdd = false;
                    break;
                }
            }

            if (shouldAdd) {
                ret.add(info);
            }
        }
        return ret;
    }

    public static List<PlayerLoginInformation> getNameHistoryByName(String name) {
        PlayerLoginInformation player = getPlayerByName(name);
        if (player == null) {
            return null;
        }
        return getNameHistoryByUUID(player.getUUID());
    }

    public static List<PlayerLoginInformation> getLoginLog(String name, boolean ip) {
        List<PlayerLoginInformation> ret;
        if (!ip) {
            ret = query("name", name);
            if (ret == null || ret.isEmpty()) {
                return null;
            }
            return ret;
        }
        ret = new ArrayList<>();

        List<PlayerLoginInformation> nameList = query("name", name);
        if (nameList == null || nameList.isEmpty()) {
            return null;
        }
        Set<String> ipList = new HashSet<>();
        for (PlayerLoginInformation info : nameList) {
            ipList.add(info.getIPAddress());
        }

        for (String playerIP : ipList) {
            List<PlayerLoginInformation> query = query("ip", playerIP);
            if (query == null || query.isEmpty()) {
                continue;
            }
            ret.addAll(query);
        }
        return ret;
    }

    private static List<PlayerLoginInformation> query(String field, String value) {
        List<PlayerLoginInformation> ret = new ArrayList<>();
        String query = "SELECT * FROM `" + SQL_TABLE + "` WHERE `" + field + "` = ? ORDER BY `id` DESC";
        Connection con = DatabaseConnection.getConnection();
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, value);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ret.add(new PlayerLoginInformation(rs.getLong("id"), rs.getString("uuid"), rs.getString("name"), rs.getString("ip"), rs.getTimestamp("lastlogin"), rs.getTimestamp("lastlogout")));
                }
            }
        } catch (SQLException ex) {
            Main.getPlugin().getLogger().log(Level.WARNING, "發生資料庫例外狀況 : " + ex);
            return null;
        }
        return ret;
    }

    private static PlayerLoginInformation getPlayer(String value, int type) {
        StringBuilder query = new StringBuilder("SELECT * FROM `" + SQL_TABLE + "` WHERE ");
        switch (type) {
            case 0: {
                query.append("`uuid`");
                break;
            }
            case 1: {
                query.append("`name`");
                break;
            }
            default: {
                return null;
            }
        }
        query.append(" = ? ORDER BY `id` DESC");

        Connection con = DatabaseConnection.getConnection();
        try (PreparedStatement ps = con.prepareStatement(query.toString())) {
            ps.setString(1, value);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new PlayerLoginInformation(rs.getLong("id"), rs.getString("uuid"), rs.getString("name"), rs.getString("ip"), rs.getTimestamp("lastlogin"), rs.getTimestamp("lastlogout"));
                }
            }
        } catch (SQLException ex) {
            Main.getPlugin().getLogger().log(Level.WARNING, "發生資料庫例外狀況 : " + ex);
        }
        return null;
    }

    public static List<PlayerLoginInformation> getSamePlayerByName(String name) {
        PlayerLoginInformation info = getPlayerByName(name);
        if (info == null) {
            return null;
        }
        return getSamePlayerByIP(info.getIPAddress());
    }

    public static List<PlayerLoginInformation> getSamePlayerByIP(String ip) {
        List<PlayerLoginInformation> result = query("ip", ip);
        if (result == null || result.isEmpty()) {
            return null;
        }
        List<PlayerLoginInformation> ret = new ArrayList<>();
        for (PlayerLoginInformation info : result) {
            String uuid = info.getUUID();

            boolean shouldAdd = true;
            for (PlayerLoginInformation check : ret) {
                if (check.getUUID().equals(uuid)) {
                    shouldAdd = false;
                    break;
                }
            }

            if (shouldAdd) {
                ret.add(info);
            }
        }
        return ret;
    }

    public static PlayerLoginInformation getPlayerByUUID(String uuid) {
        return getPlayer(uuid, 0);
    }

    public static PlayerLoginInformation getPlayerByName(String name) {
        return getPlayer(name, 1);
    }
}
