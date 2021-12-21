package ew.quilt.Item;

import ew.quilt.Funny.Poop.EatPoopListener;
import ew.quilt.Item.Potion.PotionRecipe;
import ew.quilt.plugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class RecipeManager {

    public static void initRecipe() {
        EatPoopListener.loadRecipe();
        PotionRecipe.loadResistancePotionRecipe();

        loadGrass();
        loadSlimeBall();
        loadChainMail();
        loadSpiderWeb();
        loadMagma();
        loadNameTag();
        loadExpBottle();
        loadFurnaceRecipe​();
    }

    public static void loadGrass() {
        ShapedRecipe sr = new ShapedRecipe(new NamespacedKey(Main.getPlugin(), "grass"), new ItemStack(Material.GRASS, 1));
        sr.shape(new String[]{"AAA", "ABA", "ACA"});
        sr.setIngredient('A', Material.AIR);
        sr.setIngredient('B', Material.SEEDS);
        sr.setIngredient('C', Material.DIRT);
        Bukkit.getServer().addRecipe(sr);
    }

    public static void loadSlimeBall() {
        ShapedRecipe sr = new ShapedRecipe(new NamespacedKey(Main.getPlugin(), "slime_ball"), new ItemStack(Material.SLIME_BALL, 2));
        sr.shape(new String[]{"ABA", "ACA", "ADA"});
        sr.setIngredient('A', Material.AIR);
        sr.setIngredient('B', Material.SUGAR_CANE);
        sr.setIngredient('C', Material.CLAY_BALL);
        sr.setIngredient('D', Material.WATER_BUCKET);
        Bukkit.getServer().addRecipe(sr);
    }

    public static void loadChainMail() {
        loadChainMailHelmet();
        loadChainMailChestPlate();
        loadChainMailLeggings();
        loadChainMailBoots();
    }

    public static void loadChainMailHelmet() {
        ShapedRecipe sr = new ShapedRecipe(new NamespacedKey(Main.getPlugin(), "chainmail_helmet"), new ItemStack(Material.CHAINMAIL_HELMET, 1));
        sr.shape(new String[]{"BBB", "BAB", "AAA"});
        sr.setIngredient('A', Material.AIR);
        sr.setIngredient('B', Material.IRON_FENCE);
        Bukkit.getServer().addRecipe(sr);
    }

    public static void loadChainMailChestPlate() {
        ShapedRecipe sr = new ShapedRecipe(new NamespacedKey(Main.getPlugin(), "chainmail_chestplate"), new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1));
        sr.shape(new String[]{"BAB", "BBB", "BBB"});
        sr.setIngredient('A', Material.AIR);
        sr.setIngredient('B', Material.IRON_FENCE);
        Bukkit.getServer().addRecipe(sr);
    }

    public static void loadChainMailLeggings() {
        ShapedRecipe sr = new ShapedRecipe(new NamespacedKey(Main.getPlugin(), "chainmail_leggings"), new ItemStack(Material.CHAINMAIL_LEGGINGS, 1));
        sr.shape(new String[]{"BBB", "BAB", "BAB"});
        sr.setIngredient('A', Material.AIR);
        sr.setIngredient('B', Material.IRON_FENCE);
        Bukkit.getServer().addRecipe(sr);
    }

    public static void loadChainMailBoots() {
        ShapedRecipe sr = new ShapedRecipe(new NamespacedKey(Main.getPlugin(), "chainmail_boots"), new ItemStack(Material.CHAINMAIL_BOOTS, 1));
        sr.shape(new String[]{"AAA", "BAB", "BAB"});
        sr.setIngredient('A', Material.AIR);
        sr.setIngredient('B', Material.IRON_FENCE);
        Bukkit.getServer().addRecipe(sr);
    }

    public static void loadSpiderWeb() {
        ShapedRecipe sr = new ShapedRecipe(new NamespacedKey(Main.getPlugin(), "web"), new ItemStack(Material.WEB, 2));
        sr.shape(new String[]{"BAB", "ABA", "BAB"});
        sr.setIngredient('A', Material.AIR);
        sr.setIngredient('B', Material.STRING);
        Bukkit.getServer().addRecipe(sr);
    }

    public static void loadMagma() {
        ShapedRecipe sr = new ShapedRecipe(new NamespacedKey(Main.getPlugin(), "magma"), new ItemStack(Material.MAGMA, 4));
        sr.shape(new String[]{"AAA", "ABA", "AAA"});
        sr.setIngredient('A', Material.COBBLESTONE);
        sr.setIngredient('B', Material.LAVA_BUCKET);
        Bukkit.getServer().addRecipe(sr);
    }

    public static void loadNameTag() {
        ShapedRecipe sr = new ShapedRecipe(new NamespacedKey(Main.getPlugin(), "name_tag"), new ItemStack(Material.NAME_TAG, 1));
        sr.shape(new String[]{"AAB", "ACA", "CAA"});
        sr.setIngredient('A', Material.AIR);
        sr.setIngredient('B', Material.IRON_INGOT);
        sr.setIngredient('C', Material.PAPER);
        Bukkit.getServer().addRecipe(sr);
    }

    public static void loadExpBottle() {
        ShapedRecipe sr = new ShapedRecipe(new NamespacedKey(Main.getPlugin(), "experience_bottle"), new ItemStack(Material.EXP_BOTTLE, 4));
        sr.shape(new String[]{"ABA", "BCB", "ABA"});
        sr.setIngredient('A', Material.AIR);
        sr.setIngredient('B', Material.GLASS_BOTTLE);
        sr.setIngredient('C', Material.ENCHANTED_BOOK);
        Bukkit.getServer().addRecipe(sr);
    }

    public static void loadFurnaceRecipe​() {
        Bukkit.getServer().addRecipe(new FurnaceRecipe(new ItemStack(Material.PACKED_ICE, 1), Material.ICE));
    }
}
