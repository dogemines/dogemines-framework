package net.dogemines.framework.test;

import net.dogemines.framework.DogeMinesFramework;
import net.dogemines.framework.data.registry.EnumRegistries;
import net.dogemines.framework.item.BlockItem;
import net.dogemines.framework.item.CustomItem;
import net.dogemines.framework.item.OversizedGuiItem;
import net.dogemines.framework.menu.TextColors;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

public enum DefaultItems implements EnumRegistries.Item {
    TEST_ITEM(new CustomItem(Material.PAPER, Component.text("Test Item"))),
    TEST_BLOCK(new BlockItem(DefaultBlocks.TEST_BLOCK, Component.text("Test Block"))),

    INVENTORY_FILLER(new OversizedGuiItem(Material.GLASS_PANE, Component.text(" "))),
    INVENTORY_BACK(new OversizedGuiItem(Material.GLASS_PANE, Component.text("<-- Back", TextColors.PASTEL_BLUE.getColor()))),
    INVENTORY_NEXT(new OversizedGuiItem(Material.GLASS_PANE,  Component.text("Next -->", TextColors.PASTE_PURPLE.getColor())));

    private final CustomItem item;

    DefaultItems(CustomItem item) {
        this.item = item;
    }

    @Override
    public CustomItem getValue() {
        return item;
    }

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(DogeMinesFramework.NAMESPACE, this.name().toLowerCase());
    }
}
