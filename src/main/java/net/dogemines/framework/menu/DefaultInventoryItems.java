package net.dogemines.framework.menu;

import net.dogemines.framework.data.registry.EnumRegistries;
import net.dogemines.framework.item.CustomItem;
import net.dogemines.framework.item.OversizedGuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

public enum DefaultInventoryItems implements EnumRegistries.Item {
    INVENTORY_FILLER(new OversizedGuiItem(Material.GLASS_PANE,  Component.text(" "))),
    INVENTORY_BACK(new OversizedGuiItem(Material.GLASS_PANE,  Component.text("<-- Back", TextColors.PASTEL_BLUE.getColor()))),
    INVENTORY_NEXT(new OversizedGuiItem(Material.GLASS_PANE,  Component.text("Next -->", TextColors.PASTE_PURPLE.getColor())));

    private final CustomItem item;

    DefaultInventoryItems(CustomItem item) {
        this.item = item;
    }

    @Override
    public CustomItem getValue() {
        return item;
    }

    @Override
    public String getId() {
        return this.name().toLowerCase();
    }
}
