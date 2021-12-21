package ew.quilt.BlackList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.entity.Player;

public class BlackListPlayer {

    public static final String RED_FLOWER_UUID = "0fdcd579-ce27-41af-8957-db861e25d5e7"; // Redflower

    public static boolean isBlackList(Player player) {
        switch (player.getUniqueId().toString()) {
            case "9502ec25-9cbc-4899-944a-4be6a55b9e88": // Yandi_TW
            case "b976cf1d-d9d8-479c-b548-c9de589729d0": // RamaCebonk
            case "ae8194ab-8bd2-4074-847e-7d2947b6fd4f": // GM_Archer
            case "1568f5c1-3c8b-4a14-839c-e218ab9aaaeb": // Allie_Ccat
            case "143afa4d-7225-44ad-b6d6-38497305c64d": // _1_5
            case "ff3151ae-3787-402d-b9e3-a1cd8318c533": // Baby_Wolf
            case "69bc650f-90f9-4c9b-9e34-a24584be8e55": // CP_boringwolf
            case "5a037426-5b60-4848-a4cd-fcb496fe1833": // GM_wolf
            case "4dbd039a-0ce1-4eb2-a834-9679b74f56c3": // GM_Edward
            case "42502929-8946-40c4-83b9-f59572dfd1d6": // RedBeat_ouo
            case "924a1e09-2176-48eb-90d7-86520f734456": // Thorns_Crime
            case "ecb956da-19f1-46f8-8719-b47d782e6c87": // 000000000000
            case "609fc306-a128-40a7-8903-9df0cd936b61": // forever_kayleigh
            case "e5345e47-cd69-4ecd-9315-62248a7d9b1b": { // Gingrich12356
                return true;
            }
            default: {
                return false;
            }
        }
    }

    @Deprecated
    public static boolean isWhispserLimit(Player player) {
        switch (player.getUniqueId().toString()) {
            case RED_FLOWER_UUID:
            case "424d57a1-5e82-43b7-9874-7c6885ba40c1": { // Redflower_Tw
                return true;
            }
            default: {
                return false;
            }
        }
    }

    // Some MK II Player Put Here (For Collection Only)
    private static final List<String> LIMIT_PLAYER = new ArrayList<>(Arrays.asList(new String[]{
        "a02bd863-ab90-4212-8a16-e07276b5853e" // allen0704
    }));

    public static List<String> getLimitPlayer() {
        return LIMIT_PLAYER;
    }
}
