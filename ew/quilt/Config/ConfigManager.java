package ew.quilt.Config;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import ew.quilt.Config.Version.SimpleVersion;
import ew.quilt.plugin.Main;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.PluginManager;

public class ConfigManager {

    public static final String VERSION = Bukkit.getBukkitVersion().substring(2, 6);

    public static final SimpleVersion SIMPLE_VERSION = SimpleVersion.valueOf("v" + Main.getPlugin().getServer().getVersion().split(":")[1].replace(")", "").trim().replace(".", "_"));
    public static final String PACKAGE_VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

    public static final String SURVIVAL_EWG_WORLD = "world_survive";
    public static final String SURVIVAL_ORIGIN_WORLD = "world_origin_survive";
    public static final String ADMIN_WORLD = "world_admin";

    public static final int DIRECTION_SPEED = 3;
    public static final int BAR_MESSAGE_TIME = 10;

    public static final String AUTHOR_UUID = "75498a49-a1b6-4701-847d-ab3e1a193f1f";

    public static void reloadConfig() {
        Main.getPlugin().reloadConfig();
    }

    public static PluginManager getPluginManager() {
        return Bukkit.getPluginManager();
    }

    public static Logger getLogger() {
        return Bukkit.getLogger();
    }

    public static void console(String message) {
        System.out.println(message);
    }

    public static void log(String message) {
        getLogger().log(Level.INFO, message);
    }

    public static Server getServer() {
        return Bukkit.getServer();
    }

    public static void stopServer() {
        getServer().shutdown();
    }

    public static void saveAll() {
        getServer().savePlayers();
    }

    public static ProtocolManager getProtocolManager() {
        return ProtocolLibrary.getProtocolManager();
    }

    public static PluginLoader getPluginLoader() {
        return Main.getPlugin().getPluginLoader();
    }

    public static ConsoleCommandSender getConsoleCommandSender() {
        return Bukkit.getConsoleSender();
    }

    public static boolean isProtectWorld(World world) {
        switch (world.getName().toLowerCase()) {
            case SURVIVAL_EWG_WORLD:
            case SURVIVAL_ORIGIN_WORLD:
            case "world": {
                return true;
            }
            default: {
                return false;
            }
        }
    }

    public static boolean isAuthorSender(CommandSender sender) {
        return (sender instanceof Player) && ((Player) sender).getUniqueId().toString().equalsIgnoreCase(ConfigManager.AUTHOR_UUID);
    }

    public static void checkConfig() {
        Plugin plugin = Main.getPlugin();
        try {
            if (!plugin.getDataFolder().exists()) {
                plugin.getDataFolder().mkdirs();
            }
            File file = new File(plugin.getDataFolder(), "config.yml");
            if (!file.exists()) {
                plugin.saveDefaultConfig();
                plugin.getLogger().info("找不到設定檔案 正在建立 ...");
            }
        } catch (Exception ex) {
            plugin.getLogger().log(Level.SEVERE, "檢查設定檔案時發生例外狀況 : " + ex);
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
    }

    public static FileConfiguration getConfig() {
        return Main.getPlugin().getConfig();
    }
}
