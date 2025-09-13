package net.dogemines.framework.test;

import net.dogemines.framework.DogeMinesFramework;
import net.dogemines.framework.block.CustomBlock;
import net.dogemines.framework.data.registry.EnumRegistries;
import org.bukkit.NamespacedKey;

public enum DefaultBlocks implements EnumRegistries.Block {
   TEST_BLOCK(CustomBlock.auto(10));

    private final CustomBlock block;

    DefaultBlocks(CustomBlock block) {
        this.block = block;
    }

    @Override
    public CustomBlock getValue() {
        return block;
    }

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(DogeMinesFramework.NAMESPACE, this.name().toLowerCase());
    }
}
