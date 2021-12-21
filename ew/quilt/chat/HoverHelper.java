package ew.quilt.chat;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HoverHelper {

    private List<String> partList = new ArrayList<>();
    private List<String> cleanPartList = new ArrayList<>();
    private String combined = "";
    private String combinedClean = "";

    public void clear() {
        partList = new ArrayList<>();
        cleanPartList = new ArrayList<>();
        combined = "";
        combinedClean = "";
    }

    public HoverHelper add(String text) {
        return add(text, null, null, null);
    }

    public HoverHelper add(String text, String hoverText) {
        return add(text, hoverText, null, null);
    }

    public HoverHelper add(String text, String hoverText, String command) {
        return add(text, hoverText, command, null);
    }

    public HoverHelper add(String text, String hoverText, String command, String suggestion) {
        if (text == null) {
            return this;
        }
        String f = "{\"text\":\"" + ChatColor.translateAlternateColorCodes('&', text) + "\"";

        String last = ChatColor.getLastColors(text);
        if (last != null && !last.isEmpty()) {
            ChatColor color = ChatColor.getByChar(last.replace("§", ""));
            if (color != null) {
                f += ",\"color\":\"" + color.name().toLowerCase() + "\"";
            }
        }
        if (hoverText != null) {
            f += ",\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"" + ChatColor.translateAlternateColorCodes('&', hoverText) + "\"}]}}";
        }
        if (suggestion != null) {
            f += ",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"" + suggestion + "\"}";
        }
        if (command != null) {
            if (!command.startsWith("/")) {
                command = "/" + command;
            }
            f += ",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"" + command + "\"}";
        }
        f += "}";
        partList.add(f);
        cleanPartList.add(ChatColor.translateAlternateColorCodes('&', text));
        return this;
    }

    public HoverHelper remove(int index) {
        partList.remove(index);
        cleanPartList.remove(index);
        return this;
    }

    public HoverHelper combine() {
        String f = "";
        for (String part : partList) {
            if (f.isEmpty()) {
                f = "[\"\",";
            } else {
                f += ",";
            }
            f += part;
        }
        if (!f.isEmpty()) {
            f += "]";
        }
        combined = f;
        return this;
    }

    public HoverHelper combineClean() {
        String f = "";
        for (String part : cleanPartList) {
            f += part;
        }
        combinedClean = f;
        return this;
    }

    public HoverHelper show(Player player) {
        if (combined.isEmpty()) {
            combine();
        }
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + player.getName() + " " + combined);
        return this;
    }

    public HoverHelper showClean(Player player) {
        if (combinedClean.isEmpty()) {
            combineClean();
        }
        player.sendMessage(combined);
        return this;
    }

    public HoverHelper show(CommandSender sender) {
        if (combined.isEmpty()) {
            combine();
        }
        if (sender instanceof Player) {
            show((Player) sender);
        } else {
            sender.sendMessage(combineClean().combinedClean);
        }
        return this;
    }

    public String getRaw() {
        if (combined.isEmpty()) {
            combine();
        }
        return combined;
    }

    public HoverHelper broadcast() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            show(player);
        }
        return this;
    }

    public boolean isEmpty() {
        return partList.isEmpty();
    }

    public List<String> getCleanText() {
        return cleanPartList;
    }
}
