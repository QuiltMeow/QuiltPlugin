package ew.quilt.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AboutCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        sender.sendMessage("伺服器正在執行棉被家族服務端架構 (遊戲版本 1.12.2) (實作 API 版本 : 1.12.2-R0.1-SNAPSHOT)");
        sender.sendMessage("正在檢查新版本 請稍後 ...");
        sender.sendMessage("您目前正在執行最新版本");
        return true;
    }
}
