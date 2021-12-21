package ew.quilt.util;

import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.text.NumberFormat;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class SystemUtil {

    private static final NumberFormat NF = NumberFormat.getNumberInstance();

    public static void getRuntimeData(CommandSender sender) {
        Runtime lRuntime = Runtime.getRuntime();
        long freeM = lRuntime.freeMemory() / 1024 / 1024;
        long maxM = lRuntime.maxMemory() / 1024 / 1024;
        long tM = lRuntime.totalMemory() / 1024 / 1024;
        OperatingSystemMXBean osmb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        ThreadMXBean t = ManagementFactory.getThreadMXBean();

        double[] tps = Bukkit.getServer().getTPS();
        for (int i = 0; i < tps.length; ++i) {
            if (tps[i] > 20) {
                tps[i] = 20;
            }
        }

        sender.sendMessage("本程式執行緒數量 : " + t.getThreadCount());
        sender.sendMessage("JVM 執行緒數量 : " + t.getDaemonThreadCount());
        sender.sendMessage("最大執行緒數量 : " + t.getPeakThreadCount());
        sender.sendMessage("本程式最大記憶體 : " + maxM + " MB");
        sender.sendMessage("本程式可用記憶體 : " + freeM + " MB");
        sender.sendMessage("本程式總記憶體 : " + tM + " MB");
        sender.sendMessage("系統物理記憶體總計 : " + (osmb.getTotalPhysicalMemorySize() / 1024 / 1024) + " MB");
        sender.sendMessage("系統物理記憶體可用 : " + (osmb.getFreePhysicalMemorySize() / 1024 / 1024) + " MB");
        sender.sendMessage("系統交換記憶體總計 : " + (osmb.getTotalSwapSpaceSize() / 1024 / 1024) + " MB");
        sender.sendMessage("系統交換記憶體可用 : " + (osmb.getFreeSwapSpaceSize() / 1024 / 1024) + " MB");

        NF.setMaximumFractionDigits(3);
        sender.sendMessage("CPU 識別 : " + System.getenv("PROCESSOR_IDENTIFIER"));
        sender.sendMessage("CPU 可用核心數 : " + Runtime.getRuntime().availableProcessors());
        sender.sendMessage("CPU 使用率 (本程式) : " + NF.format((osmb.getProcessCpuLoad() * 100)) + " %");
        sender.sendMessage("CPU 使用率 (系統) : " + NF.format((osmb.getSystemCpuLoad() * 100)) + " %");
        sender.sendMessage("CPU 時間 : " + osmb.getProcessCpuTime());

        StringBuilder sb = new StringBuilder("TPS : [1 分鐘 ");
        sb.append(tps[0] >= 20 ? "* " : "").append(NF.format(tps[0])).append("] [5 分鐘 ").append(tps[1] >= 20 ? "* " : "").append(NF.format(tps[1])).append("] [15 分鐘 ").append(tps[2] >= 20 ? "* " : "").append(NF.format(tps[2])).append("]");
        sender.sendMessage(sb.toString());
    }
}
