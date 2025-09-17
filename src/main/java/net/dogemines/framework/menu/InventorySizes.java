package net.dogemines.framework.menu;

import org.jetbrains.annotations.ApiStatus;

public enum InventorySizes {
    CHEST(27, 70),
    DOUBLE_CHEST(54, 124);

    private final int size;
    private final int unicodeHeight;
    InventorySizes(int size, int unicodeHeight) {
        this.size = size;
        this.unicodeHeight = unicodeHeight;
    }

    public int getSize() {
        return size;
    }

    @ApiStatus.Internal
    public int getUnicodeHeight() {
        return unicodeHeight;
    }
}