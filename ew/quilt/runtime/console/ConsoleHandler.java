package ew.quilt.runtime.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ConsoleHandler {

    public static void runtimeExecute(CommandSender sender, String command) {
        String line;
        try {
            Process process = Runtime.getRuntime().exec(command);
            try (BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                while ((line = in.readLine()) != null) {
                    String encodeConvert = new String(line.getBytes("Big5"), "Big5");
                    sender.sendMessage("[後台輸出] " + ChatColor.GREEN + encodeConvert);
                }
            }
        } catch (IOException ex) {
            sender.sendMessage("[後台錯誤] " + ChatColor.RED + "發生例外狀況 : " + ex);
        }
    }

    public static void executeCommand(CommandSender sender, String command) {
        runtimeExecute(sender, "cmd /c " + command);
    }
}
