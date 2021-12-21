package ew.quilt.head;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class HeadManager {

    private static final List<Head> extraHead = new ArrayList<>();
    private static final List<String> headsName = new ArrayList<>();

    public static void giveHead(Player player, Head head) {
        ItemStack h = new ItemStack(397, 1, (short) 3);
        SkullMeta meta = (SkullMeta) h.getItemMeta();
        meta.setOwner(head.getPlayerName());
        meta.setDisplayName(ChatColor.GOLD + head.getName().replace("_", ""));
        h.setItemMeta(meta);
        player.getInventory().addItem(new ItemStack[]{h});
        player.updateInventory();
    }

    public static void giveHead(Player player, String head) {
        ItemStack h = new ItemStack(397, 1, (short) 3);
        SkullMeta meta = (SkullMeta) h.getItemMeta();
        meta.setOwner(head);
        meta.setDisplayName(ChatColor.GOLD + head.replace("_", ""));
        h.setItemMeta(meta);
        player.getInventory().addItem(new ItemStack[]{h});
        player.updateInventory();
    }

    public static void dropHead(Location location, Head head) {
        ItemStack h = new ItemStack(397, 1, (short) 3);
        SkullMeta meta = (SkullMeta) h.getItemMeta();
        meta.setOwner(head.getPlayerName());
        meta.setDisplayName(ChatColor.GOLD + head.getName().replace("_", ""));
        h.setItemMeta(meta);
        location.getWorld().playEffect(location, Effect.SMOKE, BlockFace.UP);
        location.getWorld().dropItem(location, h);
    }

    public static void dropCreeperHead(Location location) {
        ItemStack h = new ItemStack(397, 1, (short) 4);
        SkullMeta meta = (SkullMeta) h.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Creeper");
        h.setItemMeta(meta);
        location.getWorld().playEffect(location, Effect.SMOKE, BlockFace.UP);
        location.getWorld().dropItem(location, h);
    }

    public static void dropZombieHead(Location location) {
        ItemStack h = new ItemStack(397, 1, (short) 2);
        SkullMeta meta = (SkullMeta) h.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Zombie");
        h.setItemMeta(meta);
        location.getWorld().playEffect(location, Effect.SMOKE, BlockFace.UP);
        location.getWorld().dropItem(location, h);
    }

    public static void dropWitherHead(Location location) {
        ItemStack h = new ItemStack(397, 1, (short) 1);
        SkullMeta meta = (SkullMeta) h.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "WitherSkull");
        h.setItemMeta(meta);
        location.getWorld().playEffect(location, Effect.SMOKE, BlockFace.UP);
        location.getWorld().dropItem(location, h);
    }

    public static void dropSkeletonHead(Location location) {
        ItemStack h = new ItemStack(397, 1, (short) 0);
        SkullMeta meta = (SkullMeta) h.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Skeleton");
        h.setItemMeta(meta);
        location.getWorld().playEffect(location, Effect.SMOKE, BlockFace.UP);
        location.getWorld().dropItem(location, h);
    }

    public static List<String> getHeadName() {
        return headsName;
    }

    public static void loadHeadName() {
        headsName.clear();
        for (int i = 0; i < extraHead.size(); ++i) {
            headsName.add(((Head) extraHead.get(i)).getName());
        }
    }

    public static List<Head> getHead() {
        return extraHead;
    }

    public static Head getHead(String headName) {
        for (Head head : extraHead) {
            if (head.getName().equalsIgnoreCase(headName)) {
                return head;
            }
        }
        return null;
    }

    public static void loadHead() {
        extraHead.add(new Head("Blaze", "MHF_Blaze"));
        extraHead.add(new Head("Cave_Spider", "MHF_CaveSpider"));
        extraHead.add(new Head("Chicken", "MHF_Chicken"));
        extraHead.add(new Head("Cow", "MHF_Cow"));
        extraHead.add(new Head("Enderman", "MHF_Enderman"));
        extraHead.add(new Head("Ghast", "MHF_Ghast"));
        extraHead.add(new Head("Iron_Golem", "MHF_Golem"));
        extraHead.add(new Head("Herobrine", "MHF_Herobrine"));
        extraHead.add(new Head("Magma_Cube", "MHF_LavaSlime"));
        extraHead.add(new Head("Mushroom_Cow", "MHF_MushroomCow"));
        extraHead.add(new Head("Ocelot", "MHF_Ocelot"));
        extraHead.add(new Head("Pig", "MHF_Pig"));
        extraHead.add(new Head("Pig_Zombie", "MHF_PigZombie"));
        extraHead.add(new Head("Sheep", "MHF_Sheep"));
        extraHead.add(new Head("Slime", "MHF_Slime"));
        extraHead.add(new Head("Spider", "MHF_Spider"));
        extraHead.add(new Head("Squid", "MHF_Squid"));
        extraHead.add(new Head("Villager", "MHF_Villager"));
        extraHead.add(new Head("Cactus", "MHF_Cactus"));
        extraHead.add(new Head("Cake", "MHF_Cake"));
        extraHead.add(new Head("Chest", "MHF_Chest"));
        extraHead.add(new Head("Melon", "MHF_Melon"));
        extraHead.add(new Head("OakLog", "MHF_OakLog"));
        extraHead.add(new Head("Pumpkin", "MHF_Pumpkin"));
        extraHead.add(new Head("TNT", "MHF_TNT"));
        extraHead.add(new Head("TNT2", "MHF_TNT2"));
        extraHead.add(new Head("ArrowUp", "MHF_ArrowUp"));
        extraHead.add(new Head("ArrowDown", "MHF_ArrowDown"));
        extraHead.add(new Head("ArrowLeft", "MHF_ArrowLeft"));
        extraHead.add(new Head("ArrowRight", "MHF_ArrowRight"));
        extraHead.add(new Head("Exclamation", "MHF_Exclamation"));
        extraHead.add(new Head("Question", "MHF_Question"));
        extraHead.add(new Head("Cam", "FHG_Cam"));
        extraHead.add(new Head("HalloweenPumpkin", "_pumpkin99_"));
        extraHead.add(new Head("HalloweenZombie", "FoidzaFlow"));
        extraHead.add(new Head("HalloweenOrc", "Swedman"));
        extraHead.add(new Head("HalloweenSkull", "SuperDAIU"));
        extraHead.add(new Head("HalloweenMage", "thetaco8808"));
        extraHead.add(new Head("DerpMan", "groopo"));
        extraHead.add(new Head("Jukebox", "C418"));
        extraHead.add(new Head("Zombie", "decyg"));
        extraHead.add(new Head("Kratos", "kratos007"));
        extraHead.add(new Head("TV", "Cheapshot"));
        extraHead.add(new Head("Wither1", "Wither"));
        extraHead.add(new Head("Wither2", "MHF_Wither"));
        extraHead.add(new Head("GrassBlock", "MoulaTime"));
        extraHead.add(new Head("SonicTheHedgehog", "sonic022"));
        extraHead.add(new Head("DiamondOre", "akaBruce"));
        extraHead.add(new Head("RedstoneOre", "annayirb"));
        extraHead.add(new Head("NetherRack", "Numba_one_Stunna"));
        extraHead.add(new Head("GlowstoneBlock", "samstine11"));
        extraHead.add(new Head("EmeraldOre", "Tereneckla"));
        extraHead.add(new Head("RedstoneLamp", "AutoSoup"));
        extraHead.add(new Head("Bookshelf", "BowAimbot"));
        extraHead.add(new Head("EnderEye", "Edna_I"));
        extraHead.add(new Head("Help", "jarrettgabe"));
        extraHead.add(new Head("HayBale", "Bendablob"));
        extraHead.add(new Head("SpongeBlock", "pomi44"));
        extraHead.add(new Head("Radio", "uioz"));
        extraHead.add(new Head("WoodBlock", "terryxu"));
        extraHead.add(new Head("StoneBlock", "Robbydeezle"));
        extraHead.add(new Head("TrollFace", "Trollface20"));
        extraHead.add(new Head("Chewbacca", "albino_donkey"));
        extraHead.add(new Head("StoneBrick", "moosify"));
        extraHead.add(new Head("SpaceRock", "speedblader03"));
        extraHead.add(new Head("Furnace", "NegativeZeroTV"));
        extraHead.add(new Head("Hamburger", "simbasbestbud"));
        extraHead.add(new Head("Bacon", "Baconator52"));
        extraHead.add(new Head("Penguin", "Haribo98"));
        extraHead.add(new Head("Earth", "Coasted"));
        extraHead.add(new Head("CommandBlock", "monkey354"));
        extraHead.add(new Head("Potato", "CraftPotato13"));
        extraHead.add(new Head("Toaster", "AcE_whatever"));
        extraHead.add(new Head("ChineseDragon", "Morhaus"));
        extraHead.add(new Head("SantaClaus", "Santa314"));
        extraHead.add(new Head("Apple", "MHF_Apple"));
        extraHead.add(new Head("Apple2", "KylexDavis"));
        extraHead.add(new Head("Coconuts", "KyleWDM"));
        extraHead.add(new Head("Snowman1", "GLaDOS"));
        extraHead.add(new Head("Snowman2", "Goodle"));
        extraHead.add(new Head("Soulsand", "Njham"));
        extraHead.add(new Head("Gift1", "KoviMC"));
        extraHead.add(new Head("Gift2", "CruXXx"));
        extraHead.add(new Head("Gift3", "I_Xenon_I"));
        extraHead.add(new Head("Gift4", "snipper4561"));
        extraHead.add(new Head("Gift5", "tkinell"));
        extraHead.add(new Head("Noteblock", "PixelJuke"));
        extraHead.add(new Head("DarkLeaves", "half_bit"));
        extraHead.add(new Head("CompanionCube", ""));
        extraHead.add(new Head("BeachBall", "PurplePenguinLPs"));
        extraHead.add(new Head("Dice1", "jmars213"));
        extraHead.add(new Head("Dice2", "gumbo632"));
        extraHead.add(new Head("Dispenser", "scemm"));
        extraHead.add(new Head("Ender_Dragon", "KingEndermen"));
        extraHead.add(new Head("SolidSnake", "Solid_Snake3"));
        extraHead.add(new Head("Fox", "hugge75"));
        extraHead.add(new Head("Pirate", "samsamsam1234"));
        extraHead.add(new Head("Dwarf", "JoeCMGIS"));
        extraHead.add(new Head("AngryLizard", "gothreaux"));
        extraHead.add(new Head("Mario", "nicariox42"));
        extraHead.add(new Head("BookStack", "abb3_1337"));
        extraHead.add(new Head("Cupcake", "ChoclateMuffin"));
        extraHead.add(new Head("Popcorn", "ZachWarnerHD"));
        extraHead.add(new Head("Podzol", "PhasePvP"));
        extraHead.add(new Head("MossStone", "Khrenan"));
        extraHead.add(new Head("NoTexture", "ddrl46"));
        extraHead.add(new Head("Clock", "nikx004"));
        extraHead.add(new Head("EnderChest", "_Brennian"));
        extraHead.add(new Head("Monitor", "Alistor"));
        extraHead.add(new Head("HealthBag", "godman21"));
    }
}
