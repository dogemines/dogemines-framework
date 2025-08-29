package net.dogemines.framework.menu;

public enum InventorySizes {
    CHEST(27),
    DOUBLE_CHEST(54);

    private final int size;
    InventorySizes(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}