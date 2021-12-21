package ew.quilt.util.reflection.minecraft;

import ew.quilt.Config.ConfigManager;
import ew.quilt.util.reflection.resolver.ClassResolver;

public class OBCClassResolver extends ClassResolver {

    @Override
    public Class resolve(String... names) throws ClassNotFoundException {
        for (int i = 0; i < names.length; ++i) {
            if (!names[i].startsWith("org.bukkit.craftbukkit")) {
                names[i] = ("org.bukkit.craftbukkit." + ConfigManager.PACKAGE_VERSION + "." + names[i]);
            }
        }
        return super.resolve(names);
    }
}
