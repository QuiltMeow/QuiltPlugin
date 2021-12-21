package ew.quilt.command;

import ew.quilt.util.Randomizer;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class RandomCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        int random = Randomizer.nextInt(100) + 1;
        if (sender instanceof Player) {
            List<Entity> entityList = ((Player) sender).getNearbyEntities(20, 20, 20);
            for (Entity entity : entityList) {
                if (entity instanceof Player) {
                    ((Player) entity).sendMessage(ChatColor.GREEN + "[隨機數字] 玩家 " + sender.getName() + " 產生隨機數字 : " + random);
                }
            }
        } else {
            sender.sendMessage(ChatColor.GREEN + "[隨機數字] 產生隨機數字 : " + random);
        }
        return true;
    }
}
