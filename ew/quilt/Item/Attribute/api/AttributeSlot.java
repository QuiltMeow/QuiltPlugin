package ew.quilt.Item.Attribute.api;

public enum AttributeSlot {
    MAINHAND("mainhand", 1),
    OFFHAND("offhand", 2),
    HEAD("head", 3),
    CHEST("chest", 4),
    LEGS("legs", 5),
    FEET("feet", 6);

    private final String slotString;
    private final int slot;

    AttributeSlot(String slotString, int slot) {
        this.slotString = slotString;
        this.slot = slot;
    }

    public String getSlotString() {
        return slotString;
    }

    public int getSlot() {
        return slot;
    }
}
