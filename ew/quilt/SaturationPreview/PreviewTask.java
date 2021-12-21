package ew.quilt.SaturationPreview;

import ew.quilt.util.reflection.minecraft.MinecraftResolver;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PreviewTask extends BukkitRunnable {

    private final Player player;
    private boolean shown = false;
    private int actualLevel;
    private final int previewLevel;

    @Override
    public synchronized void cancel() throws IllegalStateException {
        super.cancel();
        sendActualPacket();
    }

    public PreviewTask(Player player, int level) {
        this.player = player;
        actualLevel = player.getFoodLevel();
        previewLevel = level;
    }

    @Override
    public void run() {
        if (shown) {
            sendActualPacket();
            shown = false;
        } else {
            sendPreviewPacket();
            shown = true;
        }
        if ((actualLevel = player.getFoodLevel()) >= 20) {
            cancel();
        }
        if (player.getGameMode() != GameMode.SURVIVAL && player.getGameMode() != GameMode.ADVENTURE) {
            cancel();
        }
        if (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR || player.getItemInHand().getAmount() <= 0) {
            cancel();
        }
    }

    private void sendActualPacket() {
        sendPacket(SaturationPreviewListener.getPacketPlayOutUpdateHealthConstructorResolver().resolveWrapper(new Class[][]{{Float.TYPE, Integer.TYPE, Float.TYPE}}).newInstance(new Object[]{SaturationPreviewListener.getCraftPlayerMethodResolver().resolveWrapper(new String[]{"getScaledHealth"}).invokeSilent(player, new Object[0]), Math.min(actualLevel, 20), player.getSaturation()}));
    }

    private void sendPreviewPacket() {
        sendPacket(SaturationPreviewListener.getPacketPlayOutUpdateHealthConstructorResolver().resolveWrapper(new Class[][]{{Float.TYPE, Integer.TYPE, Float.TYPE}}).newInstance(new Object[]{SaturationPreviewListener.getCraftPlayerMethodResolver().resolveWrapper(new String[]{"getScaledHealth"}).invokeSilent(player, new Object[0]), Math.min(actualLevel + previewLevel, 20), player.getSaturation()}));
    }

    private void sendPacket(Object packet) {
        try {
            SaturationPreviewListener.getPlayerConnectionMethodResolver().resolveWrapper(new String[]{"sendPacket"}).invoke(SaturationPreviewListener.getEntityPlayerFieldResolver().resolveWrapper(new String[]{"playerConnection"}).getSilent(MinecraftResolver.getHandle(player)), new Object[]{packet});
        } catch (ReflectiveOperationException ex) {
            throw new RuntimeException(ex);
        }
    }
}
