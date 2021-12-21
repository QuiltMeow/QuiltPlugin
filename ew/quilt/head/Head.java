package ew.quilt.head;

public class Head {

    private final String headName;
    private final String playerName;

    public Head(String name, String playerHeadName) {
        headName = name;
        playerName = playerHeadName;
    }

    public String getName() {
        return headName;
    }

    public String getPlayerName() {
        return playerName;
    }
}
