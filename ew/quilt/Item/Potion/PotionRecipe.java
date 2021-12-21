package ew.quilt.Item.Potion;

import ew.quilt.plugin.Main;
import ew.quilt.util.TimerUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PotionRecipe {

    private static final ItemStack RESISTANCE_POTION = getResistancePotion();

    public static void loadResistancePotionRecipe() {
        ShapedRecipe sr = new ShapedRecipe(new NamespacedKey(Main.getPlugin(), "potion"), RESISTANCE_POTION);
        sr.shape(new String[]{"AAA", "BCB", "ADA"});
        sr.setIngredient('A', Material.AIR);
        sr.setIngredient('B', Material.IRON_INGOT);
        sr.setIngredient('C', Material.POTION);
        sr.setIngredient('D', Material.LAVA_BUCKET);
        Bukkit.getServer().addRecipe(sr);
    }

    public static ItemStack getResistancePotion() {
        ItemStack resistancePotion = new ItemStack(Material.POTION, 1);
        PotionMeta resistancePotionMeta = (PotionMeta) resistancePotion.getItemMeta();
        resistancePotionMeta.setDisplayName("§B抗性藥水");
        resistancePotionMeta.addCustomEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, TimerUtil.secondToTick(60), 0), true);
        resistancePotion.setItemMeta(resistancePotionMeta);
        return resistancePotion;
    }
}
