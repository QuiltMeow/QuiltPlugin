package ew.quilt.Funny.Poop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PoopCommand implements CommandExecutor {

    private static final Map<String, Long> POOP_COOL_TIME = new HashMap<>();
    private static final ItemStack DROP_POOP = getDropPoopItem();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "指令使用方法 : /poop [玩家 ID]");
            return false;
        }
        String name = sender.getName();
        Long coolTime = POOP_COOL_TIME.get(name);
        long current = System.currentTimeMillis();
        if (coolTime == null || current > coolTime + 300 * 1000 || sender.isOp()) { // 300 秒
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "找不到目標玩家 !");
                return false;
            }
            ItemStack toDrop = new ItemStack(DROP_POOP);
            World world = target.getWorld();
            Location location = target.getLocation();
            world.dropItem(location, toDrop);
            world.spawnParticle(Particle.WATER_WAKE, location, 20);
            target.sendMessage(ChatColor.LIGHT_PURPLE + "玩家 " + name + " 對你潑了一坨糞便 !");
            sender.sendMessage(ChatColor.GREEN + "已對目標玩家 " + target.getName() + " 潑了一坨糞便 !");
            POOP_COOL_TIME.put(name, current);
            return true;
        } else {
            long remaining = (coolTime + 300 * 1000 - current) / 1000;
            sender.sendMessage(ChatColor.RED + "冷卻時間尚未結束 無法對玩家潑糞便 ! 剩餘冷卻時間 : " + remaining + " 秒");
            return false;
        }
    }

    public static ItemStack getDropPoopItem() {
        ItemStack item = new ItemStack(351, 1, (short) 500, (byte) 3);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§A小坨糞便");

        List<String> lore = new ArrayList<>(Arrays.asList(new String[]{"§B不能吃的小坨糞便", "§C拿來砸人用的"}));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}
