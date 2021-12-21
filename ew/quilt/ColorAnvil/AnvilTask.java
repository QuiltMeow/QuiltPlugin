package ew.quilt.ColorAnvil;

import ew.quilt.plugin.Main;
import java.util.Map;
import java.util.WeakHashMap;
import org.bukkit.entity.Player;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.scheduler.BukkitRunnable;

public class AnvilTask extends BukkitRunnable {

    private static final Map<AnvilInventory, AnvilTask> ANVIL_TASK = new WeakHashMap<>();

    private final AnvilInventory inv;
    private final Player player;

    public AnvilTask(AnvilInventory inv, Player player) {
        this.inv = inv;
        this.player = player;
        putTask(inv);
        runTaskTimer(Main.getPlugin(), 1, 3);
    }

    private void putTask(AnvilInventory inv) {
        ANVIL_TASK.put(inv, this);
    }

    @Override
    public void run() {
        if (inv.getViewers().isEmpty()) {
            cancel();
        }
        ColorHandler.getTranslatedItem(player, inv, this);
    }

    public static AnvilTask getTask(AnvilInventory inv) {
        return ANVIL_TASK.get(inv);
    }
}
