package net.dogemines.framework.menu;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class InventoryPresets {
    public static class Confirmation extends MenuBuilder.InventoryMenuBuilder {
        public Confirmation(InventorySizes size, String name) {
            super(size, name);
        }
    }
}
