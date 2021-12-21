package ew.quilt.command;

import ew.quilt.util.Compatible;
import java.util.LinkedList;
import java.util.List;
import org.bukkit.entity.Player;

public class MultiClientCheck {

    public static List<String> getMultiPlayer() {
        List<String> ret = new LinkedList<>();
        List<Player> allCharacter = Compatible.getOnlinePlayers();

        List<String> singleIPList = new LinkedList<>();
        List<Player> multiList = new LinkedList<>();
        for (Player player : allCharacter) {
            if (singleIPList.isEmpty()) {
                singleIPList.add(player.getAddress().getHostString());
            } else {
                boolean skip = false;
                String IP = player.getAddress().getHostString();
                for (String singleIP : singleIPList) {
                    if (singleIP.equals(IP)) {
                        skip = true;
                        multiList.add(player);
                        break;
                    }
                }
                if (!skip) {
                    singleIPList.add(IP);
                }
            }
        }

        if (multiList.isEmpty()) {
            ret.add("目前沒有任何分身玩家在線上");
        } else {
            StringBuilder count = new StringBuilder();
            count.append("共計線上分身玩家 : ").append(multiList.size()).append(" 位\r\n");
            ret.add(count.toString());

            for (Player multi : multiList) {
                StringBuilder sb = new StringBuilder();
                sb.append(multi.getName()).append(" IP : ").append(multi.getAddress().getHostString());
                ret.add(sb.toString());
            }
        }
        return ret;
    }
}
