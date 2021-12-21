package ew.quilt.Funny;

import ew.quilt.Config.ConfigManager;
import ew.quilt.util.TimerUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class AllPotionEffectCommand implements CommandExecutor {

    private static final int EFFECT_TIME = 300;
    private static final int EFFECT_LEVEL = 2;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("quilt.funny.potion") && !ConfigManager.isAuthorSender(sender)) {
            sendNoPermission(sender);
            return false;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "該指令必須由玩家在遊戲中使用");
            return false;
        }
        Player player = (Player) sender;
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, TimerUtil.secondToTick(EFFECT_TIME), EFFECT_LEVEL));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, TimerUtil.secondToTick(EFFECT_TIME), EFFECT_LEVEL));
        player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, TimerUtil.secondToTick(EFFECT_TIME), EFFECT_LEVEL));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, TimerUtil.secondToTick(EFFECT_TIME), EFFECT_LEVEL));
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, TimerUtil.secondToTick(EFFECT_TIME), EFFECT_LEVEL));
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, TimerUtil.secondToTick(EFFECT_TIME), EFFECT_LEVEL));
        player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, TimerUtil.secondToTick(EFFECT_TIME), EFFECT_LEVEL));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, TimerUtil.secondToTick(EFFECT_TIME), EFFECT_LEVEL));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, TimerUtil.secondToTick(EFFECT_TIME), EFFECT_LEVEL));
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, TimerUtil.secondToTick(EFFECT_TIME), EFFECT_LEVEL));
        player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, TimerUtil.secondToTick(EFFECT_TIME), EFFECT_LEVEL));
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, TimerUtil.secondToTick(EFFECT_TIME), EFFECT_LEVEL));
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, TimerUtil.secondToTick(EFFECT_TIME), EFFECT_LEVEL));
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, TimerUtil.secondToTick(EFFECT_TIME), EFFECT_LEVEL));
        player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, TimerUtil.secondToTick(EFFECT_TIME), EFFECT_LEVEL));
        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, TimerUtil.secondToTick(EFFECT_TIME), EFFECT_LEVEL));
        player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, TimerUtil.secondToTick(EFFECT_TIME), EFFECT_LEVEL));
        player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, TimerUtil.secondToTick(EFFECT_TIME), EFFECT_LEVEL));
        player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, TimerUtil.secondToTick(EFFECT_TIME), EFFECT_LEVEL));
        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, TimerUtil.secondToTick(EFFECT_TIME), EFFECT_LEVEL));
        player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, TimerUtil.secondToTick(EFFECT_TIME), EFFECT_LEVEL));
        player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, TimerUtil.secondToTick(EFFECT_TIME), EFFECT_LEVEL));
        player.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, TimerUtil.secondToTick(EFFECT_TIME), EFFECT_LEVEL));
        player.addPotionEffect(new PotionEffect(PotionEffectType.UNLUCK, TimerUtil.secondToTick(EFFECT_TIME), EFFECT_LEVEL));
        return true;
    }

    public static void sendNoPermission(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "權限不足 無法使用該指令 !");
    }
}
