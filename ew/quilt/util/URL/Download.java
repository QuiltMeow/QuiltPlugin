package ew.quilt.util.URL;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Download extends Thread {

    private final CommandSender sender;
    private final String fileName;
    private final String link;

    private final boolean checkType;

    public Download(CommandSender sender, String link, String fileName) {
        this.sender = sender;
        this.fileName = fileName;
        this.link = link;
        checkType = true;
    }

    public Download(CommandSender sender, String link, String fileName, boolean checkType) {
        this.sender = sender;
        this.fileName = fileName;
        this.link = link;
        this.checkType = checkType;
    }

    @Override
    public void run() {
        String[] splittedFileName = fileName.split("\\.");
        String[] splittedLink = link.split("\\.");

        String fileType = splittedFileName[splittedFileName.length - 1];
        String linkType = splittedLink[splittedLink.length - 1];
        if (checkType) {
            if (!fileType.equals(linkType)) {
                if (sender != null) {
                    sender.sendMessage(ChatColor.RED + "檔案名稱與實際類型不符 ...");
                }
                return;
            }
            if (!FileHandler.isValidFileType(fileType)) {
                if (sender != null) {
                    sender.sendMessage(ChatColor.RED + "無法下載該檔案類型 ...");
                }
                return;
            }
        }

        if (FileHandler.checkFileExist(fileName)) {
            if (sender != null) {
                sender.sendMessage(ChatColor.RED + "檔案下載失敗 目標檔案名稱已存在 ...");
            }
            return;
        }
        try {
            URL url = new URL(link);
            try (ReadableByteChannel rbc = Channels.newChannel(url.openStream())) {
                try (FileOutputStream fos = new FileOutputStream(FileHandler.getDownloadPath() + "/" + fileName)) {
                    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                }
            }
            if (sender != null) {
                sender.sendMessage(ChatColor.GREEN + "檔案下載成功 ! 檔案名稱 : " + fileName);
            }
        } catch (IOException ex) {
            if (sender != null) {
                sender.sendMessage(ChatColor.RED + "檔案下載失敗 請檢查網址與伺服器網路連線是否正常 ...");
            }
        }
    }
}
