package ew.quilt.Economy;

import java.util.HashMap;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PayListener implements Listener {

    private static final String COMMAND_HELP = "§E指令使用方法 : /pay [玩家 ID] [金額] - 轉帳至其他玩家";

    private final Economy eco = EconomyManager.getEconomy();

    private final HashMap<String, Double> payMoney = new HashMap<>();
    private final HashMap<String, String> payTo = new HashMap<>();

    @EventHandler
    public void onPlayerCommandPreProcess(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage();
        Player player = event.getPlayer();
        String name = player.getName();

        String type = command.split(" ")[0];
        if (type.equalsIgnoreCase("/yes")) {
            if (payMoney.get(name.toLowerCase()) != null && payTo.get(name.toLowerCase()) != null) {
                double money = payMoney.get(name.toLowerCase());
                String to = payTo.get(name.toLowerCase());
                if (eco.has(player, money) && eco.hasAccount(to)) {
                    eco.withdrawPlayer(player, money);
                    Player target = Bukkit.getPlayer(to);
                    if (target != null) {
                        eco.depositPlayer(target, money);
                        target.sendMessage("§E玩家 §B" + name + " §E對你轉帳了 §A" + money + " §E金錢");
                    } else {
                        eco.depositPlayer(to, money);
                    }
                    event.getPlayer().sendMessage("§E你成功轉帳至玩家 §B" + to + " §A" + money + " §E金錢");
                } else {
                    event.getPlayer().sendMessage("§4你沒有足夠的金錢或目標玩家尚未開戶");
                }
            } else {
                event.getPlayer().sendMessage("§E需要先輸入轉帳資訊才能確認 !");
            }
            payMoney.put(name.toLowerCase(), null);
            payTo.put(name.toLowerCase(), null);
            event.setCancelled(true);
        } else if (type.equalsIgnoreCase("/no")) {
            if (payMoney.get(name.toLowerCase()) != null || payTo.get(name.toLowerCase()) != null) {
                payMoney.put(name.toLowerCase(), null);
                payTo.put(name.toLowerCase(), null);
                player.sendMessage("§E交易已取消");
            } else {
                player.sendMessage("§E你沒有任何進行中的交易");
            }
            event.setCancelled(true);
        } else if (type.equalsIgnoreCase("/pay")) {
            String[] args = command.substring(command.indexOf(" ") + 1).split(" ");
            if (args.length < 2) {
                player.sendMessage(COMMAND_HELP);
                event.setCancelled(true);
                return;
            }

            double money;
            try {
                money = Double.parseDouble(args[1]);
            } catch (Exception ex) {
                player.sendMessage("§4請輸入正確的金額");
                event.setCancelled(true);
                return;
            }
            if (money <= 0) {
                player.sendMessage("§4轉帳金額必須大於 0");
                event.setCancelled(true);
                return;
            }

            if (name.equalsIgnoreCase(args[0])) {
                player.sendMessage("§4不能對自己轉帳 !");
                event.setCancelled(true);
                return;
            }

            player.sendMessage("§E你確定要對玩家 §B" + args[0] + " §E轉帳 §A" + money + " §E金錢嗎 ? 確認請輸入 /yes 取消請輸入 /no");
            payMoney.put(name.toLowerCase(), money);
            payTo.put(name.toLowerCase(), args[0]);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        String name = event.getPlayer().getName();
        payMoney.put(name.toLowerCase(), null);
        payTo.put(name.toLowerCase(), null);
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        String name = event.getPlayer().getName();
        payMoney.put(name.toLowerCase(), null);
        payTo.put(name.toLowerCase(), null);
    }
}
