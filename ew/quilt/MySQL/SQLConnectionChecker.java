package ew.quilt.MySQL;

import ew.quilt.plugin.Main;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import org.bukkit.Bukkit;

public class SQLConnectionChecker extends Thread {

    private static final boolean CHECK_FAIL_SHUTDOWN = true;
    private static final boolean SHOW_SQL_STATUS = false;

    private final int interval;
    private final boolean keepAlive;

    public SQLConnectionChecker(int interval, boolean keepAlive) {
        this.interval = interval;
        this.keepAlive = keepAlive;
    }

    @Override
    public void run() {
        while (!interrupted()) {
            Connection con = DatabaseConnection.getConnection();
            if (con == null) {
                showDatabaseError();
                processShutdown();
            } else if (keepAlive) {
                try (Statement stmt = con.createStatement()) {
                    try (ResultSet rs = stmt.executeQuery("SHOW PROCESSLIST")) {
                        if (SHOW_SQL_STATUS) {
                            Main.getPlugin().getLogger().log(Level.ALL, "資料庫連線時間已更新");
                        }
                        /* while (rs.next()) {
                            System.out.println(rs.getString("Host"));
                            System.out.println(rs.getString("Id"));
                            System.out.println(rs.getString("User"));
                            System.out.println(rs.getString("db"));
                            System.out.println(rs.getString("Command"));
                            System.out.println(rs.getString("state"));
                            System.out.println(rs.getString("info"));
                        } */
                    }
                } catch (SQLException ex) {
                    showDatabaseError();
                    processShutdown();
                }
            }
            try {
                Thread.sleep(interval * 1000);
            } catch (InterruptedException ex) {
                Main.getPlugin().getLogger().log(Level.WARNING, "發生例外狀況 資料庫線程檢查中止 : " + ex);
                break;
            }
        }
    }

    private void processShutdown() {
        if (!CHECK_FAIL_SHUTDOWN) {
            return;
        }
        Bukkit.getServer().shutdown();
    }

    private void showDatabaseError() {
        Bukkit.broadcast("資料庫發生錯誤 請檢查伺服器狀態 ...", "quilt.notice.sql.fail");
    }
}
