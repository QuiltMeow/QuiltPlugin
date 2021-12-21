package ew.quilt.Funny.Poop;

import ew.quilt.plugin.Main;
import ew.quilt.util.TimerUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EatPoopListener implements Listener {

    private static final ItemStack POOP = getPoopItem();

    public static void loadRecipe() {
        ShapedRecipe sr = new ShapedRecipe(new NamespacedKey(Main.getPlugin(), "poop"), POOP);
        sr.shape(new String[]{"AAA", "AAA", "AAA"});
        sr.setIngredient('A', Material.ROTTEN_FLESH);
        Bukkit.getServer().addRecipe(sr);
    }

    public static ItemStack getPoopItem() {
        ItemStack item = new ItemStack(351, 1, (short) 500, (byte) 11);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§A大坨糞便");

        List<String> lore = new ArrayList<>(Arrays.asList(new String[]{"§B可以吃的大坨糞便"}));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEatPoop(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        ItemStack itemCheck = player.getInventory().getItemInOffHand();
        if (item == null || item.isSimilar(itemCheck)) {
            return;
        }
        if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && item.isSimilar(POOP)) {
            if (item.getAmount() > 1) {
                item.setAmount(item.getAmount() - 1);
            } else {
                player.setItemInHand(new ItemStack(Material.AIR));
            }
            if (player.getHealthScale() <= player.getHealth() + 10) {
                player.setHealth(player.getHealthScale());
            } else {
                player.setHealth(player.getHealth() + 10);
            }
            player.removePotionEffect(PotionEffectType.CONFUSION);
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, TimerUtil.secondToTick(20), 0));
            player.sendMessage("§5你吃了一口大坨糞便 !");
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 1, 1);
        }
    }
}
