package ew.quilt.chat.event;

import ew.quilt.Economy.EconomyManager;
import ew.quilt.plugin.Main;
import ew.quilt.util.MathCaptcha;
import ew.quilt.util.Randomizer;
import ew.quilt.util.TimerUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEvent implements Listener {

    private static final String[] CURRENT = new String[]{null, null};

    public static void registerChatEvent() {
        Main.getPlugin().getServer().getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
            @Override
            public void run() {
                synchronized (CURRENT) {
                    String[] captcha = MathCaptcha.getCaptcha();
                    for (int i = 0; i < CURRENT.length; ++i) {
                        CURRENT[i] = captcha[i];
                    }
                    Bukkit.broadcastMessage(ChatColor.YELLOW + "[搶答活動] " + ChatColor.GREEN + "第一位回答者可獲得獎勵 !");
                    Bukkit.broadcastMessage("請回答 : " + CURRENT[0] + "?");
                }
            }
        }, TimerUtil.minuteToTick(5), TimerUtil.minuteToTick(5));
    }

    public static void setQuestionNull() {
        for (int i = 0; i < CURRENT.length; ++i) {
            CURRENT[i] = null;
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        synchronized (CURRENT) {
            if (CURRENT[1] == null) {
                return;
            }
            String message = event.getMessage();
            if (message.equalsIgnoreCase(CURRENT[1])) {
                setQuestionNull();
                Player player = event.getPlayer();
                double money = Randomizer.nextDouble(10);
                EconomyManager.gainMoney(player, money);
                player.sendMessage(ChatColor.GREEN + "回答正確 ! 恭喜您獲得 " + money + " 金錢 !");
            }
        }
    }
}
