package ew.quilt.MySQL;

import ew.quilt.Config.ConfigManager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

public class DatabaseConnection {

    private static final ThreadLocal<Connection> CON = new DatabaseConnection.ThreadLocalConnection();

    public static final int CLOSE_CURRENT_RESULT = 1;
    public static final int KEEP_CURRENT_RESULT = 2;
    public static final int CLOSE_ALL_RESULTS = 3;
    public static final int SUCCESS_NO_INFO = -2;
    public static final int EXECUTE_FAILED = -3;
    public static final int RETURN_GENERATED_KEYS = 1;
    public static final int NO_GENERATED_KEYS = 2;

    private static final String SQL_HOST = ConfigManager.getConfig().getString("MySQL.host");
    private static final int SQL_PORT = ConfigManager.getConfig().getInt("MySQL.port", 3306);
    private static final String SQL_USER = ConfigManager.getConfig().getString("MySQL.user");
    private static final String SQL_PASSWORD = ConfigManager.getConfig().getString("MySQL.password");
    private static final String SQL_DATABASE = ConfigManager.getConfig().getString("MySQL.database");
    private static final String SQL_TABLE_PREFIX = ConfigManager.getConfig().getString("MySQL.prefix");

    public static final Connection getConnection() {
        return CON.get();
    }

    public static final void closeAll() throws SQLException {
        for (final Connection connection : DatabaseConnection.ThreadLocalConnection.ALL_CONNECTION) {
            if (connection != null) {
                connection.close();
            }
        }
    }

    private static final class ThreadLocalConnection extends ThreadLocal<Connection> {

        public static final Collection<Connection> ALL_CONNECTION = new LinkedList<Connection>();

        @Override
        protected final Connection initialValue() {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (final ClassNotFoundException ex) {
                System.err.println("發生錯誤：" + ex);
            }
            try {
                final Connection con = DriverManager.getConnection("jdbc:mysql://" + SQL_HOST + ":" + SQL_PORT + "/" + SQL_DATABASE + "?autoReconnect=true&characterEncoding=UTF-8", SQL_USER, SQL_PASSWORD);
                ALL_CONNECTION.add(con);
                return con;
            } catch (SQLException ex) {
                System.err.println("發生錯誤：" + ex);
                return null;
            }
        }
    }

    public static String getPrefix() {
        return SQL_TABLE_PREFIX;
    }
}
