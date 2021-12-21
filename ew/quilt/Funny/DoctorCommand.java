package ew.quilt.Funny;

import ew.quilt.Economy.EconomyManager;
import ew.quilt.chat.HoverHelper;
import ew.quilt.plugin.Main;
import ew.quilt.util.TimerUtil;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DoctorCommand implements CommandExecutor {

    private static final List<String> APPLY_DOCTOR = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "該指令必須由玩家在遊戲中使用");
            return false;
        }
        Player player = (Player) sender;
        String name = player.getName();
        if (args.length <= 0) {
            if (isApplyDoctor(name)) {
                return false;
            }
            APPLY_DOCTOR.add(name);
            player.sendMessage(ChatColor.YELLOW + "差不多醫生 " + ChatColor.WHITE + "發來了訊息 :");
            player.sendMessage("哎呀 經過我嚴密的檢查 你得了" + ChatColor.RED + "不治之症");
            player.sendMessage("你可以選擇付 " + ChatColor.AQUA + "200 渺小的手術費" + ChatColor.WHITE + "我可能會幫你醫治好");
            player.sendMessage(ChatColor.LIGHT_PURPLE + "請問你是否要付手術費醫治你的疾病 ?");

            HoverHelper hh = new HoverHelper();
            hh.add("[" + ChatColor.GREEN + "好 請幫我醫治吧 !" + ChatColor.WHITE + "]", "支付 200 手術費", "/doctor yes");
            hh.add(" ");
            hh.add("[" + ChatColor.RED + "不 這手術費根本坑錢啊 !" + ChatColor.WHITE + "]", "我不願意支付", "/doctor no");
            hh.show(player);

            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
                @Override
                public void run() {
                    if (isApplyDoctor(name)) {
                        Player player = Bukkit.getPlayer(name);
                        if (player != null) {
                            player.sendMessage(ChatColor.RED + "休想無視我說的話 !");
                            player.sendMessage("(結果你被醫師害死了 ...)");
                            player.setHealth(0);
                        }
                        APPLY_DOCTOR.remove(name);
                    }
                }
            }, TimerUtil.secondToTick(20));
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "yes": {
                if (!isApplyDoctor(name)) {
                    return false;
                }
                APPLY_DOCTOR.remove(name);
                double money = EconomyManager.getBalance(player);
                if (money < 200) {
                    EconomyManager.payMoney(player, money);
                    player.sendMessage(ChatColor.YELLOW + "雖然你錢不夠 我們還是很仁慈的接受");
                    player.sendMessage(ChatColor.RED + "但很不幸地 手術還是失敗了");
                    player.sendMessage("(結果你還是死了 ...)");
                } else {
                    EconomyManager.payMoney(player, 200);
                    player.sendMessage(ChatColor.RED + "很不幸地 治療失敗 我已經盡力了 ...");
                    player.sendMessage("(結果你還是死了 ...)");
                }
                player.setHealth(0);
                return true;
            }
            case "no": {
                if (!isApplyDoctor(name)) {
                    return false;
                }
                APPLY_DOCTOR.remove(name);
                player.sendMessage(ChatColor.RED + "你怎麼可以放棄治療呢 ?");
                player.sendMessage("(結果你死了 ...)");
                player.setHealth(0);
                return true;
            }
            default: {
                return false;
            }
        }
    }

    public boolean isApplyDoctor(String name) {
        for (String check : APPLY_DOCTOR) {
            if (check.equals(name)) {
                return true;
            }
        }
        return false;
    }
}
