package ew.quilt.Config.Version;

import org.bukkit.Bukkit;

public class VersionChecker {

    private static final Version VERSION = getCurrent();

    public static Version getVersion() {
        return VERSION;
    }

    public static Version getCurrent() {
        String[] splitted = Bukkit.getServer().getClass().getPackage().getName().split("\\.");
        String bukkitVersion = splitted[splitted.length - 1];
        for (Version version : Version.values()) {
            if (version.name().equalsIgnoreCase(bukkitVersion)) {
                return version;
            }
        }
        return null;
    }

    public static boolean isLower(Version version) {
        return VERSION.getValue() < version.getValue();
    }

    public static boolean isLowerEquals(Version version) {
        return VERSION.getValue() <= version.getValue();
    }

    public static boolean isHigher(Version version) {
        return VERSION.getValue() > version.getValue();
    }

    public static boolean isHigherEquals(Version version) {
        return VERSION.getValue() >= version.getValue();
    }
}
