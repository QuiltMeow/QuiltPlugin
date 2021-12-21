package ew.quilt.SaturationPreview;

import ew.quilt.plugin.Main;
import ew.quilt.util.reflection.minecraft.NMSClassResolver;
import ew.quilt.util.reflection.minecraft.OBCClassResolver;
import ew.quilt.util.reflection.resolver.ConstructorResolver;
import ew.quilt.util.reflection.resolver.FieldResolver;
import ew.quilt.util.reflection.resolver.MethodResolver;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.ItemStack;

public class SaturationPreviewListener implements Listener {

    private static final NMSClassResolver nmsClassResolver = new NMSClassResolver();
    private static final OBCClassResolver obcClassResolver = new OBCClassResolver();

    private static final Class<?> ItemStack = nmsClassResolver.resolveSilent(new String[]{"ItemStack"});
    private static final Class<?> ItemFood = nmsClassResolver.resolveSilent(new String[]{"ItemFood"});
    private static final Class<?> EntityPlayer = nmsClassResolver.resolveSilent(new String[]{"EntityPlayer"});
    private static final Class<?> PlayerConnection = nmsClassResolver.resolveSilent(new String[]{"PlayerConnection"});
    private static final Class<?> PacketPlayOutUpdateHealth = nmsClassResolver.resolveSilent(new String[]{"PacketPlayOutUpdateHealth"});
    private static final Class<?> CraftItemStack = obcClassResolver.resolveSilent(new String[]{"inventory.CraftItemStack"});
    private static final Class<?> CraftPlayer = obcClassResolver.resolveSilent(new String[]{"entity.CraftPlayer"});

    private static final MethodResolver CraftItemStackMethodResolver = new MethodResolver(CraftItemStack);
    private static final MethodResolver CraftPlayerMethodResolver = new MethodResolver(CraftPlayer);
    private static final MethodResolver ItemStackMethodResolver = new MethodResolver(ItemStack);
    private static final MethodResolver ItemFoodMethodResolver = new MethodResolver(ItemFood);
    private static final MethodResolver PlayerConnectionMethodResolver = new MethodResolver(PlayerConnection);

    private static final FieldResolver EntityPlayerFieldResolver = new FieldResolver(EntityPlayer);

    private static final ConstructorResolver PacketPlayOutUpdateHealthConstructorResolver = new ConstructorResolver(PacketPlayOutUpdateHealth);

    private final Map<UUID, PreviewTask> previewTaskMap = new HashMap<>();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onHoldFood(PlayerItemHeldEvent event) {
        ItemStack itemStack = event.getPlayer().getInventory().getItem(event.getNewSlot());
        cancelTask(event.getPlayer());
        if (itemStack != null && itemStack.getAmount() > 0 && itemStack.getType() != Material.AIR) {
            Object nmsItemStack = CraftItemStackMethodResolver.resolveWrapper(new String[]{"asNMSCopy"}).invokeSilent(null, new Object[]{itemStack});
            Object nmsItem = ItemStackMethodResolver.resolveWrapper(new String[]{"getItem"}).invokeSilent(nmsItemStack, new Object[0]);
            if (ItemFood.isAssignableFrom(nmsItem.getClass())) {
                startTask(event.getPlayer(), ((int) ItemFoodMethodResolver.resolveWrapper(new String[]{"getNutrition"}).invokeSilent(nmsItem, new Object[]{nmsItemStack})));
            }
        }
    }

    @EventHandler
    public void onPlayerToggleSprint(PlayerToggleSprintEvent event) {
        if (event.getPlayer().getFoodLevel() < 6 && previewTaskMap.containsKey(event.getPlayer().getUniqueId())) {
            event.getPlayer().setSprinting(false);
        }
    }

    private void startTask(Player player, int level) {
        cancelTask(player);
        PreviewTask task = new PreviewTask(player, level);
        previewTaskMap.put(player.getUniqueId(), task);
        task.runTaskTimer(Main.getPlugin(), 10, 10);
    }

    private void cancelTask(Player player) {
        if (previewTaskMap.containsKey(player.getUniqueId())) {
            ((PreviewTask) previewTaskMap.remove(player.getUniqueId())).cancel();
        }
    }

    public static ConstructorResolver getPacketPlayOutUpdateHealthConstructorResolver() {
        return PacketPlayOutUpdateHealthConstructorResolver;
    }

    public static MethodResolver getCraftPlayerMethodResolver() {
        return CraftPlayerMethodResolver;
    }

    public static MethodResolver getPlayerConnectionMethodResolver() {
        return PlayerConnectionMethodResolver;
    }

    public static FieldResolver getEntityPlayerFieldResolver() {
        return EntityPlayerFieldResolver;
    }
}
