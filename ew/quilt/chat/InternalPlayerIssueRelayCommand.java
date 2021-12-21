package ew.quilt.chat;

import ew.quilt.command.CommandDispatcher;
import ew.quilt.util.StringUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InternalPlayerIssueRelayCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 1) {
            return false;
        }
        if (!(sender instanceof Player)) {
            return false;
        }
        String command = StringUtil.joinStringFrom(args, 0);
        CommandDispatcher.issuePlayerCommand((Player) sender, command);
        return true;
    }
}
