package net.dogemines.framework.menu;

import org.bukkit.inventory.ItemStack;

public record InventoryMenuSlot(ItemStack stack, InventoryMenu.ClickCallback clickCallback) {
    public InventoryMenuSlot(ItemStack stack) {
        this(stack, null);
    }
    public InventoryMenuSlot(ItemStackBuilder builder, InventoryMenu.ClickCallback callback) {
        this(builder.build(), callback);
    }
    public InventoryMenuSlot(ItemStackBuilder builder) {
        this(builder.build());
    }
}