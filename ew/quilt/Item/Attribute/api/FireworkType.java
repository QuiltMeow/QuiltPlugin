package ew.quilt.Item.Attribute.api;

public enum FireworkType {
    SMALL_BALL(Byte.valueOf("0")),
    BIG_BALL(Byte.valueOf("1")),
    STAR(Byte.valueOf("2")),
    CREEPER(Byte.valueOf("3")),
    BOOM(Byte.valueOf("4"));

    private final byte fireworkType;

    FireworkType(byte fireworkType) {
        this.fireworkType = fireworkType;
    }

    public byte getFireworkType() {
        return fireworkType;
    }
}
