package ew.quilt.util.URL;

import ew.quilt.plugin.Main;
import java.io.File;

public class FileHandler {

    private static final String DOWNLOAD_PATH = "plugins/Images";
    private static final String SCRIPT_PATH = "plugins/scripts";

    public static void init() {
        initImagePath();
        initScriptPath();
    }

    public static void initImagePath() {
        File path = new File(DOWNLOAD_PATH);
        if (!path.exists()) {
            Main.getPlugin().getLogger().info("圖片下載位置不存在 正在建立 ...");
            boolean result = false;

            try {
                path.mkdir();
                result = true;
            } catch (SecurityException se) {
                Main.getPlugin().getLogger().warning("圖片下載位置建立失敗 ...");
            }

            if (result) {
                Main.getPlugin().getLogger().info("圖片下載位置建立完成");
            }
        }
    }

    public static void initScriptPath() {
        File path = new File(SCRIPT_PATH);
        if (!path.exists()) {
            Main.getPlugin().getLogger().info("腳本位置不存在 正在建立 ...");
            boolean result = false;

            try {
                path.mkdir();
                result = true;
            } catch (SecurityException se) {
                Main.getPlugin().getLogger().warning("腳本位置建立失敗 ...");
            }

            if (result) {
                Main.getPlugin().getLogger().info("腳本位置建立完成");
            }
        }
    }

    public static boolean checkFileExist(String fileName) {
        File file = new File(DOWNLOAD_PATH + "/" + fileName);
        return file.exists() && !file.isDirectory();
    }

    public static boolean isValidFileType(String type) {
        switch (type.toUpperCase()) {
            case "JPG":
            case "JPEG":
            case "PNG": {
                return true;
            }
        }
        return false;
    }

    public static String getDownloadPath() {
        return DOWNLOAD_PATH;
    }
}
