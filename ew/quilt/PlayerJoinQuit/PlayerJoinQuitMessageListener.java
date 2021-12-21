package ew.quilt.PlayerJoinQuit;

import ew.quilt.util.TitleUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinQuitMessageListener implements Listener {

    private static final String JOIN_MESSAGE = "玩家 %player%§F 加入了遊戲";
    private static final String QUIT_MESSAGE = "玩家 %player%§F 離開了遊戲";
    private static final String JOIN_TITLE = "歡迎 &6%player%";
    private static final String JOIN_SUB_TITLE = "&A歡迎來到 &B貓貓國度";

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String joinMessage = JOIN_MESSAGE.replaceAll("%player%", event.getPlayer().getDisplayName());
        event.setJoinMessage(joinMessage);

        TitleUtil.sendTitle(event.getPlayer(), 20, 90, 20, JOIN_TITLE, JOIN_SUB_TITLE);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        String quitMessage = QUIT_MESSAGE.replaceAll("%player%", event.getPlayer().getDisplayName());
        event.setQuitMessage(quitMessage);
    }
}
