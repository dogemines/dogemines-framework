package net.dogemines.framework.item;

import net.dogemines.framework.block.CustomBlock;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

public class BlockItem extends CustomItem {
    private final CustomBlock block;

    public BlockItem(Material material, Component name, String itemId, ItemSettings settings, CustomBlock block) {
        super(material, name, itemId, settings);
        this.block = block;
    }

    private CustomBlock getBlock() {
        return this.block;
    }
}
