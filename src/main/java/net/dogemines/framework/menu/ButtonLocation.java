package net.dogemines.framework.menu;

public enum ButtonLocation {
    LAST_ROW(-9),
    LAST_ROW_MIDDLE(-5),
    LAST_ROW_END(-1);

    private final int inventoryOffset;
    private ButtonLocation(int inventoryOffset) {
        this.inventoryOffset = inventoryOffset;
    }

    public int getInventoryOffset() {
        return inventoryOffset;
    }
}