package net.dogemines.framework.menu;

import net.dogemines.framework.data.registry.EnumRegistries;
import net.dogemines.framework.item.CustomItem;
import net.dogemines.framework.item.InventoryItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

public enum DefaultInventoryItems implements EnumRegistries.Item {
    INVENTORY_FILLER(new InventoryItem(Material.GLASS_PANE,  Component.text(" "), "inventory_filler")),
    INVENTORY_BACK(new InventoryItem(Material.GLASS_PANE,  Component.text("<-- Back", TextColors.PASTEL_BLUE.getColor()), "inventory_back")),
    INVENTORY_NEXT(new InventoryItem(Material.GLASS_PANE,  Component.text("Next -->", TextColors.PASTE_PURPLE.getColor()), "inventory_next"));

    private final CustomItem item;

    DefaultInventoryItems(CustomItem item) {
        this.item = item;
    }

    @Override
    public CustomItem getValue() {
        return item;
    }
}
