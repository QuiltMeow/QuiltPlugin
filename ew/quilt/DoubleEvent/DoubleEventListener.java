package ew.quilt.DoubleEvent;

import ew.quilt.plugin.Main;
import ew.quilt.util.ActionBar;
import ew.quilt.util.TimerUtil;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class DoubleEventListener implements Listener {

    private static double EXP_DOUBLE_RATE = 2;
    private static double MONEY_DOUBLE_RATE = 2;

    private static boolean EXP_DOUBLE_EVENT = false;
    private static boolean MONEY_DOUBLE_EVENT = false;

    private static int EXP_DOUBLE_TIME = 60;
    private static int MONEY_DOUBLE_TIME = 60;

    private static int EXP_DOUBLE_SCHEDULE = -1;
    private static int MONEY_DOUBLE_SCHEDULE = -1;

    private static final int DOUBLE_EVENT_NOTICE_TIME = 3;

    @EventHandler
    public void onPlayerExpChange(PlayerExpChangeEvent event) {
        if (EXP_DOUBLE_EVENT) {
            int xp = event.getAmount();
            if (xp > 0) {
                event.setAmount((int) (xp * EXP_DOUBLE_RATE));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPickUp(PlayerPickupItemEvent event) {
        if (MONEY_DOUBLE_EVENT) {
            ItemStack item = event.getItem().getItemStack();
            List<String> moneyLore = modifyMoneyLore(item);
            if (moneyLore != null) {
                item.getItemMeta().setLore(moneyLore);
            }
        }
    }

    public List<String> modifyMoneyLore(ItemStack item) {
        if (item != null && item.hasItemMeta() && item.getItemMeta().hasLore()) {
            List<String> lore = item.getItemMeta().getLore();
            for (int i = 0; i < lore.size(); ++i) {
                String loreString = lore.get(i);
                if (loreString.endsWith(" MoneyDropMoney")) {
                    String pickUp = loreString.replace(" MoneyDropMoney", "");
                    try {
                        double amount = Double.parseDouble(pickUp);
                        lore.set(i, amount * MONEY_DOUBLE_RATE + " MoneyDropMoney");
                        return lore;
                    } catch (NumberFormatException ex) {
                        System.err.println("金錢數值轉換發生例外狀況 : " + ex);
                    }
                }
            }
        }
        return null;
    }

    public static void activeDoubleExpEvent(double rate, int time) {
        if (EXP_DOUBLE_EVENT) {
            Bukkit.broadcastMessage(ChatColor.RED + "經驗加倍活動已在進行中 !");
            return;
        }

        EXP_DOUBLE_TIME = time;
        final long EXP_EVENT_END_TIME = System.currentTimeMillis() + EXP_DOUBLE_TIME * 1000 * 60;
        final int NOTICE_TIME = TimerUtil.minuteToTick(DOUBLE_EVENT_NOTICE_TIME);
        EXP_DOUBLE_SCHEDULE = Main.getPlugin().getServer().getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
            @Override
            public void run() {
                final long CURRENT = System.currentTimeMillis();
                if (CURRENT >= EXP_EVENT_END_TIME) {
                    Main.getPlugin().getServer().getScheduler().cancelTask(EXP_DOUBLE_SCHEDULE);
                    EXP_DOUBLE_SCHEDULE = -1;
                    EXP_DOUBLE_EVENT = false;
                    Bukkit.broadcastMessage(ChatColor.YELLOW + "經驗加倍活已結束 !");
                    return;
                }

                final long REMAINING_MINUTE = (EXP_EVENT_END_TIME - CURRENT) / 1000 / 60;
                // Bukkit.broadcastMessage(ChatColor.GREEN + "經驗加倍活動進行中 ! 剩餘 " + REMAINING_MINUTE + " 分鐘 !");
                for (Player online : Bukkit.getOnlinePlayers()) {
                    ActionBar.send(online, ChatColor.GREEN + "經驗加倍活動進行中 ! 剩餘 " + REMAINING_MINUTE + " 分鐘 !");
                }
            }
        }, NOTICE_TIME, NOTICE_TIME);

        if (EXP_DOUBLE_SCHEDULE == -1) {
            Bukkit.broadcastMessage(ChatColor.RED + "經驗加倍活動啟用失敗 !");
            return;
        }

        EXP_DOUBLE_RATE = rate;
        EXP_DOUBLE_EVENT = true;
        Bukkit.broadcastMessage(ChatColor.GREEN + "經驗加倍活動已開始 ! 持續時間 : " + time + " 分鐘 !");
    }

    public static void activeDoubleMoneyEvent(double rate, int time) {
        if (MONEY_DOUBLE_EVENT) {
            Bukkit.broadcastMessage(ChatColor.RED + "金錢加倍活動已在進行中 !");
            return;
        }

        MONEY_DOUBLE_TIME = time;
        final long MONEY_EVENT_END_TIME = System.currentTimeMillis() + MONEY_DOUBLE_TIME * 1000 * 60;
        final int NOTICE_TIME = TimerUtil.minuteToTick(DOUBLE_EVENT_NOTICE_TIME);
        MONEY_DOUBLE_SCHEDULE = Main.getPlugin().getServer().getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
            @Override
            public void run() {
                final long CURRENT = System.currentTimeMillis();
                if (CURRENT >= MONEY_EVENT_END_TIME) {
                    Main.getPlugin().getServer().getScheduler().cancelTask(MONEY_DOUBLE_SCHEDULE);
                    MONEY_DOUBLE_SCHEDULE = -1;
                    MONEY_DOUBLE_EVENT = false;
                    Bukkit.broadcastMessage(ChatColor.YELLOW + "金錢加倍活已結束 !");
                    return;
                }

                final long REMAINING_MINUTE = (MONEY_EVENT_END_TIME - CURRENT) / 1000 / 60;
                // Bukkit.broadcastMessage(ChatColor.GREEN + "金錢加倍活動進行中 ! 剩餘 " + REMAINING_MINUTE + " 分鐘 !");
                for (Player online : Bukkit.getOnlinePlayers()) {
                    ActionBar.send(online, ChatColor.GREEN + "金錢加倍活動進行中 ! 剩餘 " + REMAINING_MINUTE + " 分鐘 !");
                }
            }
        }, NOTICE_TIME, NOTICE_TIME);

        if (MONEY_DOUBLE_SCHEDULE == -1) {
            Bukkit.broadcastMessage(ChatColor.RED + "金錢加倍活動啟用失敗 !");
            return;
        }

        MONEY_DOUBLE_RATE = rate;
        MONEY_DOUBLE_EVENT = true;
        Bukkit.broadcastMessage(ChatColor.GREEN + "金錢加倍活動已開始 ! 持續時間 : " + time + " 分鐘 !");
    }

    public static void forceStopDoubleExpEvent() {
        if (EXP_DOUBLE_SCHEDULE != -1) {
            Main.getPlugin().getServer().getScheduler().cancelTask(EXP_DOUBLE_SCHEDULE);
            EXP_DOUBLE_SCHEDULE = -1;
        }
        EXP_DOUBLE_EVENT = false;
        Bukkit.broadcastMessage(ChatColor.YELLOW + "經驗加倍活動已強制停止 !");
    }

    public static void forceStopDoubleMoneyEvent() {
        if (MONEY_DOUBLE_SCHEDULE != -1) {
            Main.getPlugin().getServer().getScheduler().cancelTask(MONEY_DOUBLE_SCHEDULE);
            MONEY_DOUBLE_SCHEDULE = -1;
        }
        MONEY_DOUBLE_EVENT = false;
        Bukkit.broadcastMessage(ChatColor.YELLOW + "金錢加倍活動已強制停止 !");
    }
}
