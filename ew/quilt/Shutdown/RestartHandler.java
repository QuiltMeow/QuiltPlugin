package ew.quilt.Shutdown;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;

public class RestartHandler {

    public void restart() {
        try {
            Class<?> hookClass = Class.forName("java.lang.ApplicationShutdownHooks");
            Field field = hookClass.getDeclaredField("hooks");
            field.setAccessible(true);
            List<Thread> threadList = new ArrayList<>();
            Map<?, ?> hookMap = (Map<?, ?>) field.get(null);
            for (Object thread : hookMap.values()) {
                threadList.add((Thread) thread);
            }
            for (Thread thread : threadList) {
                Runtime.getRuntime().removeShutdownHook(thread);
            }
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    try {
                        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
                        List<String> argument = runtimeMXBean.getInputArguments();
                        List<String> argumentList = new ArrayList<>();
                        argumentList.add("java");
                        for (String arg : argument) {
                            argumentList.add(" -" + arg);
                        }
                        argumentList.add(" -jar " + Bukkit.class.getProtectionDomain().getCodeSource().getLocation().getPath());
                        new ProcessBuilder(argumentList).start();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            Bukkit.shutdown();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
