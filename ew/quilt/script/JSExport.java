package ew.quilt.script;

import ew.quilt.Config.ConfigManager;
import ew.quilt.command.CommandDispatcher;
import ew.quilt.runtime.console.ConsoleHandler;
import ew.quilt.util.Compatible;
import ew.quilt.util.EntityHelper;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.PluginManager;

public class JSExport {

    public List<Player> getOnlinePlayer() {
        return Compatible.getOnlinePlayers();
    }

    public Player getPlayer(String name) {
        return Bukkit.getPlayer(name);
    }

    public void issueServerCommand(String command) {
        CommandDispatcher.issueServerCommand(command);
    }

    public void issuePlayerCommand(Player player, String command) {
        CommandDispatcher.issuePlayerCommand(player, command);
    }

    public Server getServer() {
        return ConfigManager.getServer();
    }

    public PluginManager getPluginManager() {
        return ConfigManager.getPluginManager();
    }

    public PluginLoader getPluginLoader() {
        return ConfigManager.getPluginLoader();
    }

    public void executeCMD(CommandSender sender, String command) {
        ConsoleHandler.runtimeExecute(sender, "cmd /c " + command);
    }

    public ConsoleCommandSender getConsole() {
        return ConfigManager.getConsoleCommandSender();
    }

    public World getWorld(String name) {
        return Bukkit.getWorld(name);
    }

    public List<Entity> getEntity() {
        return EntityHelper.getAllEntity();
    }
}
