package net.dogemines.framework.test;

import net.dogemines.framework.block.CustomBlock;
import net.dogemines.framework.data.registry.EnumRegistries;

public enum DefaultBlocks implements EnumRegistries.Block {
   TEST_BLOCK(new CustomBlock(10), "test_block");

    private final CustomBlock block;
    private final String blockId;

    DefaultBlocks(CustomBlock block, String blockId) {
        this.block = block;
        this.blockId = blockId;
    }

    @Override
    public CustomBlock getValue() {
        return block;
    }

    @Override
    public String getId() {
        return blockId;
    }
}
