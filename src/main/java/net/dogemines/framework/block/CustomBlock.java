package net.dogemines.framework.block;

import net.dogemines.framework.data.BasicModel;
import net.dogemines.framework.data.ResourcePack;
import net.dogemines.framework.data.registry.Registries;
import net.dogemines.framework.data.registry.RegistryObject;
import net.dogemines.framework.item.CustomItem;
import net.dogemines.framework.item.CustomItemStack;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

//a collection of CustomBlockStates
public class CustomBlock {
    private final int blockStrength;
    private final List<BlockPredicate> blockPredicates = new ArrayList<>();

    public CustomBlock(BlockPredicate defaultState, int blockStrength) {
        this.blockStrength = blockStrength;
        blockPredicates.add(defaultState);
    }
    public CustomBlock(int blockStrength) {
        this(MultipleFacingPredicate.createUnused(), blockStrength);
    }

    //methods
    public CustomBlock addBlockState(BlockPredicate blockPredicate) {
        blockPredicates.add(blockPredicate);
        return this;
    }

    public void onBreak(Player player, String blockId) {
        //if there exists an item with the same id as this block, give it to the player
        RegistryObject<CustomItem> blockItem = Registries.ITEM.get(blockId);
        if (blockItem != null) {
            player.give(CustomItemStack.of(blockItem));
        }
    }

    //getter methods
    public BlockPredicate getDefaultState() {
        return blockPredicates.getFirst();
    }
    public BlockPredicate getState(int index) {
        return blockPredicates.get(index);
    }
    public int getBlockStrength() {
        return blockStrength;
    }

    //overridable methods
    public void onClick(Player player, Block block) {}
}
