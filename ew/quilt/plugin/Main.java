package ew.quilt.plugin;

// import ew.quilt.AAC.AACBanListener;
import ew.quilt.AAC.AACHideListener;
import ew.quilt.AdminChat.AdminChat;
import ew.quilt.AdminChat.AdminChatCommand;
import ew.quilt.AdminChat.OPChatCommand;
import ew.quilt.AdminChat.PlayerRequestHelpCommand;
import ew.quilt.Item.Attribute.AttributeCommand;
import ew.quilt.BanWorld.BanWorldListener;
import ew.quilt.Bar.api.BarAPI;
import ew.quilt.BedHeal.BedHealListener;
import ew.quilt.Blood.BloodListener;
import ew.quilt.ColorAnvil.ColorAnvilListener;
import ew.quilt.Economy.EconomyManager;
import ew.quilt.DeathExp.DeathExpListener;
// import ew.quilt.PortalTP.DoorTPBlockListener;
import ew.quilt.DoubleEvent.DoubleEventListener;
import ew.quilt.Economy.PayListener;
import ew.quilt.Elevator.ElevatorListener;
import ew.quilt.Enchant.EnchantCommand;
import ew.quilt.Explode.ExplodeListener;
// import ew.quilt.FastRespawn.FastRespawnListener;
import ew.quilt.Funny.AimListener;
import ew.quilt.Funny.BombListener;
import ew.quilt.Funny.EMOCommand;
import ew.quilt.Funny.Poop.EatPoopListener;
import ew.quilt.Funny.FunnyListener;
import ew.quilt.Hopper.AntiHopperTransfer;
import ew.quilt.LiquidFlow.LiquidFlowListener;
import ew.quilt.Item.Lore.LoreCommand;
import ew.quilt.Funny.Poop.PoopCommand;
import ew.quilt.Funny.ThroughVoidListener;
import ew.quilt.Item.Potion.PotionStackListener;
import ew.quilt.Item.RecipeManager;
// import ew.quilt.Level.LevelUpListener;
import ew.quilt.MobSpawner.MobSpawnerEggBlock;
import ew.quilt.MobSpawner.MobSpawnerRateControlListener;
// import ew.quilt.NoAFKFishing.NoAFKFishingListener;
// import ew.quilt.NoAFKFishing.NoAFKFishingRegularUpdate;
import ew.quilt.NoBedExplode.NoBedExplodeListener;
// import ew.quilt.NoDeathMessage.NoDeathMessageListener;
import ew.quilt.NoNetherRoof.NoNetherRoofListener;
// import ew.quilt.PigZombie.PigZombieSpawnListener;
import ew.quilt.Ping.PingCommand;
import ew.quilt.Ping.PingFixListener;
import ew.quilt.PlayerJoinQuit.PlayerJoinEffectListener;
import ew.quilt.PlayerJoinQuit.PlayerJoinQuitMessageListener;
import ew.quilt.protect.AntiVoidDeath;
import ew.quilt.protect.ForceGameMode;
import ew.quilt.patch.RailDropDuplicateExploit;
import ew.quilt.RedStone.RedStoneClockPreventer;
// import ew.quilt.SaturationPreview.SaturationPreviewListener;
import ew.quilt.Shutdown.ShutdownHandler;
import ew.quilt.Sign.SignCommand;
import ew.quilt.Sign.SignListener;
// import ew.quilt.Spawn.SpawnFrostDisableListener;
// import ew.quilt.Spawn.TPSpawnListener;
import ew.quilt.protect.TPProtectListener;
import ew.quilt.Trash.TrashCommand;
import ew.quilt.chat.MultiLineChatCommand;
import ew.quilt.command.AboutCommand;
import ew.quilt.command.DCCommand;
import ew.quilt.command.IPCommand;
import ew.quilt.command.QuiltCommand;
import ew.quilt.command.RandomCommand;
import ew.quilt.command.UUIDCommand;
import ew.quilt.head.HeadCommand;
import ew.quilt.head.HeadListener;
import ew.quilt.head.HeadManager;
import ew.quilt.BlackList.BlackListPlayerControl;
import ew.quilt.Config.ConfigManager;
import ew.quilt.Funny.AllPotionEffectCommand;
import ew.quilt.Funny.DoctorCommand;
import ew.quilt.Funny.SpecialBombListener;
// import ew.quilt.MySQL.SQLConnectionChecker;
// import ew.quilt.MySQL.UserLoginLogHandler;
import ew.quilt.bypass.PermissionOperation;
import ew.quilt.chat.ChatFilter;
import ew.quilt.protect.MapMaliciousLoadDetector;
import ew.quilt.chat.InternalChatCommand;
import ew.quilt.chat.InternalMessageSendCommand;
import ew.quilt.chat.InternalPlayerIssueRelayCommand;
import ew.quilt.chat.TipLineChatBuilderCommand;
import ew.quilt.chat.TipLineChatCommand;
import ew.quilt.command.MapLoadDetectorCommand;
// import ew.quilt.entity.EntitySpawnRequester;
// import ew.quilt.patch.ResidenceTPExploit;
import ew.quilt.script.JSCommand;
import ew.quilt.timer.WeatherTimer;
import ew.quilt.util.URL.FileHandler;
import ew.quilt.util.URL.URLCommand;
import java.util.LinkedList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main plugin;

    @Override
    public void onEnable() {
        getLogger().info("Quilt Plugin 正在啟用 ...");
        plugin = this;
        ConfigManager.checkConfig();
        EconomyManager.setupEconomy();

        getCommand("quilt").setExecutor(new QuiltCommand());
        getCommand("attribute").setExecutor(new AttributeCommand());
        getCommand("trash").setExecutor(new TrashCommand());
        getCommand("ping").setExecutor(new PingCommand());
        getCommand("lore").setExecutor(new LoreCommand());
        getCommand("sign").setExecutor(new SignCommand());
        getCommand("poop").setExecutor(new PoopCommand());
        getCommand("url").setExecutor(new URLCommand());
        getCommand("random").setExecutor(new RandomCommand());
        getCommand("emo").setExecutor(new EMOCommand());
        getCommand("uuid").setExecutor(new UUIDCommand());
        getCommand("head").setExecutor(new HeadCommand());
        getCommand("ip").setExecutor(new IPCommand());
        getCommand("about").setExecutor(new AboutCommand());
        getCommand("enchant").setExecutor(new EnchantCommand());
        getCommand("dc").setExecutor(new DCCommand());
        getCommand("maploaddetector").setExecutor(new MapLoadDetectorCommand());
        getCommand("qjs").setExecutor(new JSCommand());
        getCommand("allpotioneffect").setExecutor(new AllPotionEffectCommand());
        getCommand("doctor").setExecutor(new DoctorCommand());
        getCommand("findid").setExecutor(new ItemIdCommand());

        getCommand("multilinechat").setExecutor(new MultiLineChatCommand());
        getCommand("tiplinechat").setExecutor(new TipLineChatCommand());
        getCommand("tiplinechatbuilder").setExecutor(new TipLineChatBuilderCommand());

        getCommand("adminchat").setExecutor(new AdminChatCommand());
        getCommand("opchat").setExecutor(new OPChatCommand());
        getCommand("helpadmin").setExecutor(new PlayerRequestHelpCommand());

        getCommand("qicc").setExecutor(new InternalChatCommand());
        getCommand("qicms").setExecutor(new InternalMessageSendCommand());
        getCommand("qicpir").setExecutor(new InternalPlayerIssueRelayCommand());

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PayListener(), this);

        // pm.registerEvents(new DoorTPBlockListener(), this);
        pm.registerEvents(new ExplodeListener(), this);
        // pm.registerEvents(new PigZombieSpawnListener(), this);
        // pm.registerEvents(new TPSpawnListener(), this);
        // pm.registerEvents(new SpawnFrostDisableListener(), this);
        pm.registerEvents(new BedHealListener(), this);
        pm.registerEvents(new PingFixListener(), this);
        pm.registerEvents(new LiquidFlowListener(), this);
        pm.registerEvents(new BanWorldListener(), this);
        pm.registerEvents(new DoubleEventListener(), this);
        pm.registerEvents(new MobSpawnerRateControlListener(), this);
        pm.registerEvents(new NoBedExplodeListener(), this);
        // pm.registerEvents(new NoDeathMessageListener(), this);
        pm.registerEvents(new NoNetherRoofListener(), this);
        pm.registerEvents(new PlayerJoinQuitMessageListener(), this);
        // pm.registerEvents(new FastRespawnListener(), this);
        pm.registerEvents(new TPProtectListener(), this);
        pm.registerEvents(new DeathExpListener(), this);
        pm.registerEvents(new SignListener(), this);
        pm.registerEvents(new BarAPI(), this);
        pm.registerEvents(new MobSpawnerEggBlock(), this);
        pm.registerEvents(new BloodListener(), this);
        pm.registerEvents(new AdminChat(), this);
        pm.registerEvents(new PotionStackListener(), this);
        pm.registerEvents(new PlayerJoinEffectListener(), this);
        pm.registerEvents(new ColorAnvilListener(), this);
        // pm.registerEvents(new SaturationPreviewListener(), this);
        // pm.registerEvents(new LevelUpListener(), this);
        pm.registerEvents(new AntiVoidDeath(), this);
        pm.registerEvents(new ElevatorListener(), this);
        pm.registerEvents(new ForceGameMode(), this);
        pm.registerEvents(new HeadListener(), this);
        HeadManager.loadHead();
        HeadManager.loadHeadName();
        // EntitySpawnRequester.registerEntityRequester();

        pm.registerEvents(new AntiHopperTransfer(), this);
        pm.registerEvents(new FunnyListener(), this);
        pm.registerEvents(new BombListener(), this);
        pm.registerEvents(new EatPoopListener(), this);
        pm.registerEvents(new ThroughVoidListener(), this);
        pm.registerEvents(new AimListener(), this);
        pm.registerEvents(new SpecialBombListener(), this);
        pm.registerEvents(new PermissionOperation(), this);
        pm.registerEvents(new MoreCatListener(), this);
        pm.registerEvents(new CommandOverrideListener(), this);
        pm.registerEvents(new FastLeafDecay(), this);
        WeatherTimer.registerWeatherTimer();
        ServerVersionListener.registerServerVersion();

        pm.registerEvents(new RedStoneClockPreventer(), this);
        RedStoneClockPreventer.startClearTimer();

        // pm.registerEvents(new NoAFKFishingListener(), this);
        // getServer().getScheduler().scheduleSyncRepeatingTask(this, new NoAFKFishingRegularUpdate(), 0, 20);
        pm.registerEvents(new BlackListPlayerControl(), this);
        BlackListPlayerControl.registerNoReceiveChat();
        BlackListPlayerControl.registerWatchDog();

        AACHideListener.addAACCommandListener();
        pm.registerEvents(new ChatFilter(), this);
        // pm.registerEvents(new AACBanListener(), this);

        pm.registerEvents(new MapMaliciousLoadDetector(), this);
        MapMaliciousLoadDetector.registerMapLoadDetectTimer();

        pm.registerEvents(new RailDropDuplicateExploit(), this);
        // pm.registerEvents(new ResidenceTPExploit(), this);

        // pm.registerEvents(new UserLoginLogHandler(), this);
        // new SQLConnectionChecker(300, true).start();
        Runtime.getRuntime().addShutdownHook(new ShutdownHandler());
        RecipeManager.initRecipe();
        FileHandler.init();
        getLogger().info("喵嗚 ~");
    }

    @Override
    public void onDisable() {
        getLogger().info("正在關閉插件 ...");

        List<Player> playerList = new LinkedList<>(Bukkit.getOnlinePlayers());
        for (Player player : playerList) {
            World world = player.getWorld();
            String worldName = world.getName();
            if (worldName.equalsIgnoreCase("world_nether") || worldName.equalsIgnoreCase("world_the_end")) {
                World spawn = Bukkit.getWorld("world");
                player.teleport(spawn.getSpawnLocation());
            }
        }
        BarAPI.disable();
        getServer().resetRecipes();

        plugin = null;
        super.onDisable();
    }

    public static Main getPlugin() {
        return plugin;
    }
}
