package net.dogemines.framework.block;

import net.dogemines.framework.data.registry.Registries;
import net.dogemines.framework.data.registry.RegistryObject;
import net.dogemines.framework.item.CustomItem;
import net.dogemines.framework.item.CustomItemStack;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

//a collection of CustomBlockStates
public class CustomBlock {
    private final int blockStrength;
    private final List<CustomBlockState> blockStates;
    private final boolean isFaced;

    private CustomBlock(List<CustomBlockState> blockStates, int blockStrength, boolean isFaced) {
        this.blockStrength = blockStrength;
        this.blockStates = blockStates;
        this.isFaced = isFaced;
    }

    //public constructors
    public CustomBlock(List<CustomBlockState> blockstates, int blockStrength) {
        this(blockstates, blockStrength, false);
    }
    public CustomBlock(CustomBlockState defaultState, int blockStrength) {
        this(List.of(defaultState), blockStrength);
    };


    //factory methods
    @Contract("_ -> new")
    public static @NotNull CustomBlock auto(int blockStrength) {
        return new CustomBlock(new MultipartBlockState(), blockStrength);
    }

    @Contract("_, _ -> new")
    public static @NotNull CustomBlock faced(BlockPredicate predicate, int blockStrength) {
        return new CustomBlock(List.of(
                new MultipartBlockState(predicate, 0, 0),
                new MultipartBlockState(predicate, 90, 0),
                new MultipartBlockState(predicate, 180, 0),
                new MultipartBlockState(predicate, 270, 0)
        ), blockStrength, true);
    }

    @Contract("_ -> new")
    public static @NotNull CustomBlock faced(int blockStrength) {
        return faced(MultipleFacingPredicate.createUnused(), blockStrength);
    }


    //methods
    public void onBreak(Player player, NamespacedKey blockId) {
        //if there exists an item with the same id as this block, give it to the player
        RegistryObject<CustomItem> blockItem = Registries.ITEM.get(blockId);
        if (blockItem != null) {
            player.give(CustomItemStack.of(blockItem));
        }
    }

    public void place(Block block) {
        getDefaultState().setBlock(block);
    }

    //should be used if there's a player associated with the block place. Orients the block depending on player facing direction.
    public void orient(Block block, Player player) {
        if (isFaced) {
            BlockFace face = player.getFacing();
            getByFace(face).setBlock(block);
        }
    }

    private CustomBlockState getByFace(@NotNull BlockFace face) {
        return switch (face) {
            case EAST -> getState(1);
            case SOUTH -> getState(2);
            case WEST -> getState(3);
            default -> getState(0); // NORTH
        };
    }


    //getter methods
    public CustomBlockState getDefaultState() {
        return blockStates.getFirst();
    }
    public CustomBlockState getState(int index) {
        return blockStates.get(index);
    }
    public int getBlockStrength() {
        return blockStrength;
    }
    public List<CustomBlockState> getBlockStates() {
        return blockStates;
    }


    //overridable methods
    public void onClick(Player player, Block block) {}
}
