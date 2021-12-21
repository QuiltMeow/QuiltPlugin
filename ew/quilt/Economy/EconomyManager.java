package ew.quilt.Economy;

import ew.quilt.plugin.Main;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EconomyManager {

    private static Economy economy = null;

    public static boolean setupEconomy() {
        /* if (Main.getPlugin().getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        } */

        RegisteredServiceProvider<Economy> rsp = Main.getPlugin().getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }

        economy = (Economy) rsp.getProvider();
        return economy != null;
    }

    public static Economy getEconomy() {
        return economy;
    }

    public static boolean payMoney(Player player, double money) {
        if (economy == null) {
            return false;
        }
        if (!economy.hasAccount(player)) {
            return false;
        }
        if (money <= 0) {
            return false;
        }
        if (economy.getBalance(player) < money) {
            return false;
        }
        economy.withdrawPlayer(player, money);
        return true;
    }

    public static boolean gainMoney(Player player, double money) {
        if (economy == null) {
            return false;
        }
        if (!economy.hasAccount(player)) {
            return false;
        }
        if (money <= 0) {
            return false;
        }
        economy.depositPlayer(player, money);
        return true;
    }

    public static double getBalance(Player player) {
        if (economy == null) {
            return 0;
        }
        if (!economy.hasAccount(player)) {
            return 0;
        }
        return economy.getBalance(player);
    }

    public static boolean trade(Player send, Player receive, double money) {
        if (!payMoney(send, money)) {
            return false;
        }
        if (!gainMoney(receive, money)) {
            gainMoney(send, money);
            return false;
        }
        return true;
    }
}
