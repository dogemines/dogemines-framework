package net.dogemines.framework.block;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import net.dogemines.framework.data.BasicModel;
import net.dogemines.framework.data.ResourcePack;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.MultipleFacing;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.BitSet;

public class MultipleFacingPredicate implements BlockPredicate {

    //define static lists
    private static final Material[] TYPES = new Material[] {
            Material.BROWN_MUSHROOM_BLOCK,
            Material.RED_MUSHROOM_BLOCK,
            Material.MUSHROOM_STEM
    };
    private static final BitSet[] USED = new BitSet[] {
            new BitSet(64),
            new BitSet(64),
            new BitSet(64)
    };

    //define properties and which to expose to json
    private final MultipleFacing blockData;
    @Expose private final boolean up;
    @Expose private final boolean east;
    @Expose private final boolean down;
    @Expose private final boolean north;
    @Expose private final boolean south;
    @Expose private final boolean west;

    private MultipleFacingPredicate(@NotNull Material material, boolean up, boolean down, boolean north, boolean east, boolean south, boolean west) {
        this.up = up;
        this.down = down;
        this.north = north;
        this.east = east;
        this.south = south;
        this.west = west;

        blockData = (MultipleFacing) material.createBlockData();
        blockData.setFace(BlockFace.DOWN, this.down);
        blockData.setFace(BlockFace.EAST, this.east);
        blockData.setFace(BlockFace.NORTH, this.north);
        blockData.setFace(BlockFace.SOUTH, this.south);
        blockData.setFace(BlockFace.UP, this.up);
        blockData.setFace(BlockFace.WEST, this.west);
    }

    // creates a MultipleFacingPredicate from an int, mapping binary digits to booleans
    // efficient way of storing used up values of predicates as we could just use a BitSet for each material.
    // ex: 61 = 111101 = true, true, true, true, false, true
    @Contract("_, _ -> new")
    private static @NotNull MultipleFacingPredicate fromBinaryInt(int binary, int index) {
        boolean[] flags = new boolean[6];
        for (int i = 0; i < 6; i++) {
            flags[i] = (binary & (1 << i)) != 0;
        }

        USED[index].set(binary);
        return new MultipleFacingPredicate(TYPES[index], flags[0],flags[1],flags[2],flags[3],flags[4],flags[5]);
    }

    /**
     * create a MultipleFacingBlockType with side values that are unused.
     * @return either a new MultipleFacingBlockType or null if all values are used.
     */
    // finds the first unused index (0) in all bitsets. Uses the index as a mask to create a new predicate.
    public static @Nullable MultipleFacingPredicate createUnused() {
        for (int i = 0; i < TYPES.length; i++) {
            int next = USED[i].nextClearBit(0);

            if (next < 64) {
                return fromBinaryInt(next, i);
            }
        }
        return null;
    }

    @Override
    public void setBlock(@NotNull Block block) {
        block.setBlockData(blockData, false);
    }

    @Override
    public BlockData getBlockData() {
        return blockData;
    }

    @Override
    public Material getMaterial() {
        return blockData.getMaterial();
    }

    @Override
    public BasicModel getModel(String blockId) {
        return new BasicModel(
                ResourcePack.getParent(blockData.getMaterial(), false),
                ResourcePack.getDogeminesPath(blockId, true),
                true
        );
    }
}