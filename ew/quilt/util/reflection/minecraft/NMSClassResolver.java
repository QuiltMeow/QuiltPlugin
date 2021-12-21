package ew.quilt.util.reflection.minecraft;

import ew.quilt.Config.ConfigManager;
import ew.quilt.util.reflection.resolver.ClassResolver;

public class NMSClassResolver extends ClassResolver {

    @Override
    public Class resolve(String... names) throws ClassNotFoundException {
        for (int i = 0; i < names.length; ++i) {
            if (!names[i].startsWith("net.minecraft.server")) {
                names[i] = ("net.minecraft.server." + ConfigManager.PACKAGE_VERSION + "." + names[i]);
            }
        }
        return super.resolve(names);
    }
}
