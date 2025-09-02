package net.dogemines.framework.item;

import net.dogemines.framework.block.BlockPredicate;
import net.dogemines.framework.block.CustomBlock;
import net.dogemines.framework.data.registry.RegistryObject;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.meta.BlockDataMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class BlockItem extends CustomItem {
    private final CustomBlock block;
    private final BlockPredicate defaultState;

    public BlockItem(@NotNull CustomBlock block, Component name, ItemMetaDecorator metaDecorator) {
        super(block.getDefaultState().getMaterial(), name, metaDecorator);
        this.block = block;
        this.defaultState = block.getDefaultState();
    }
    public BlockItem(CustomBlock block, Component name) {
        this(block, name, ItemMetaDecorator.DEFAULT);
    }
    public BlockItem(@NotNull RegistryObject<CustomBlock> block, Component name) {
        this(block.getValue(), name);
    }

    //getter methods
    public CustomBlock getBlock() {
        return block;
    }

    @Override
    public void modifyItemMeta(ItemMeta itemMeta, String itemId) {
        BlockDataMeta blockDataMeta = (BlockDataMeta) itemMeta;
        blockDataMeta.setBlockData(defaultState.getBlockData());

        super.modifyItemMeta(blockDataMeta, itemId);
    }
}
