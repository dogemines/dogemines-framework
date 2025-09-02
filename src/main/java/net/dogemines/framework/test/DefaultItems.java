package net.dogemines.framework.test;

import net.dogemines.framework.data.registry.EnumRegistries;
import net.dogemines.framework.item.BlockItem;
import net.dogemines.framework.item.CustomItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

public enum DefaultItems implements EnumRegistries.Item {
    TEST_ITEM(new CustomItem(Material.PAPER, Component.text("Test Item")), "test_item"),
    TEST_BLOCK(new BlockItem(DefaultBlocks.TEST_BLOCK, Component.text("Test Block")), "test_block");

    private final CustomItem item;
    private final String itemId;

    DefaultItems(CustomItem item, String itemId) {
        this.item = item;
        this.itemId = itemId;
    }

    @Override
    public CustomItem getValue() {
        return item;
    }

    @Override
    public String getId() {
        return itemId;
    }
}
