package ew.quilt.plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CommandBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class CommandOverrideListener implements Listener {

    private static final List<String> VERSION_COMMAND = new ArrayList<>(Arrays.asList(new String[]{
        "/bukkit:ver",
        "/bukkit:version",
        "/ver",
        "/version",
        "/about",
        "/bukkit:about",
        "/icanhasbukkit"
    }));

    private static final List<String> HELP_COMMAND = new ArrayList<>(Arrays.asList(new String[]{
        "/minecraft:help",
        "/help",
        "/?",
        "/bukkit:help",
        "/bukkit:?"
    }));

    private static final List<String> PLUGIN_COMMAND = new ArrayList<>(Arrays.asList(new String[]{
        "/bukkit:pl",
        "/bukkit:plugins",
        "/pl",
        "/plugins"
    }));

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCommandPreProcess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String playerCommand = event.getMessage().toLowerCase();
        String pattern = playerCommand.split(" ")[0];
        for (String command : VERSION_COMMAND) {
            if (pattern.equalsIgnoreCase(command)) {
                player.sendMessage("伺服器正在執行棉被家族服務端架構 (遊戲版本 1.15.2) (實作 API 版本 : 1.15.2-R0.1-SNAPSHOT)");
                player.sendMessage("正在檢查新版本 請稍後 ...");
                player.sendMessage("您目前正在執行最新版本");
                event.setCancelled(true);
                return;
            }
        }
        for (String command : HELP_COMMAND) {
            if (pattern.equalsIgnoreCase(command)) {
                player.sendMessage(ChatColor.YELLOW + "當個創世神 (英語 : Minecraft) 是一款沙盒遊戲");
                player.sendMessage(ChatColor.YELLOW + "玩家可以在一個隨機生成的 3D 世界內，以帶材質貼圖的立方體為基礎進行遊戲");
                player.sendMessage(ChatColor.YELLOW + "遊戲中的其他特色包括探索世界、採集資源、合成物品及生存冒險等");
                event.setCancelled(true);
                return;
            }
        }
        for (String command : PLUGIN_COMMAND) {
            if (pattern.equalsIgnoreCase(command)) {
                player.sendMessage("已安裝的插件 (1) : " + ChatColor.GREEN + "QuiltPlugin");
                event.setCancelled(true);
                return;
            }
        }
    }

    public static void removeBlock(Block block) {
        new BukkitRunnable() {
            @Override
            public void run() {
                block.setType(Material.AIR);
            }
        }.runTaskLater(Main.getPlugin(), 2);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityChangeBlock(BlockRedstoneEvent event) {
        Block block = event.getBlock();
        if (block != null) {
            BlockState state = block.getState();
            if (state != null) {
                if (state instanceof CommandBlock) {
                    CommandBlock cb = (CommandBlock) state;
                    String blockCommand = cb.getCommand().toLowerCase();
                    if (!blockCommand.startsWith("/")) {
                        blockCommand = "/" + blockCommand;
                    }
                    String pattern = blockCommand.split(" ")[0];
                    for (String command : VERSION_COMMAND) {
                        if (pattern.equalsIgnoreCase(command)) {
                            removeBlock(block);
                            event.setNewCurrent(0);
                            return;
                        }
                    }
                    for (String command : HELP_COMMAND) {
                        if (pattern.equalsIgnoreCase(command)) {
                            removeBlock(block);
                            event.setNewCurrent(0);
                            return;
                        }
                    }
                    for (String command : PLUGIN_COMMAND) {
                        if (pattern.equalsIgnoreCase(command)) {
                            removeBlock(block);
                            event.setNewCurrent(0);
                            return;
                        }
                    }
                }
            }
        }
    }
}
