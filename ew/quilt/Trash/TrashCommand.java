package ew.quilt.Trash;

import ew.quilt.GUI.GUIManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TrashCommand implements CommandExecutor {

    private static final String MESSAGE = "§9垃圾桶 - 關閉垃圾桶後將自動清理";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§C該指令必須在遊戲內由玩家使用");
            return false;
        }
        Player player = (Player) sender;
        if (args.length <= 0) {
            GUIManager.openEmptyBigChest(player, MESSAGE);
        } else {
            switch (args[0].toUpperCase()) {
                case "CHEST": {
                    GUIManager.openEmptyChest(player, MESSAGE);
                    break;
                }
                case "HOPPER": {
                    GUIManager.openEmptyHopper(player, MESSAGE);
                    break;
                }
                case "DISPENSER": {
                    GUIManager.openEmptyDispenser(player, MESSAGE);
                    break;
                }
                default: {
                    GUIManager.openEmptyBigChest(player, MESSAGE);
                    break;
                }
            }
        }
        sender.sendMessage("§A你打開了垃圾桶");
        return true;
    }
}
