package net.dogemines.framework.block;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.MultipleFacing;

public record MushroomBlockPredicate(
    boolean down,
    boolean east,
    boolean north,
    boolean south,
    boolean up,
    boolean west
) implements CustomBlock.BlockPredicate {

    @Override
    public JsonObject getJson() {
       return new Gson().toJsonTree(this).getAsJsonObject();
    }

    @Override
    public void setBlock(Block block) {
        block.setType(Material.BROWN_MUSHROOM_BLOCK, false);
        MultipleFacing mushroom = (MultipleFacing) block.getBlockData();

        mushroom.setFace(BlockFace.DOWN, this.down);
        mushroom.setFace(BlockFace.EAST, this.east);
        mushroom.setFace(BlockFace.NORTH, this.north);
        mushroom.setFace(BlockFace.SOUTH, this.south);
        mushroom.setFace(BlockFace.UP, this.up);
        mushroom.setFace(BlockFace.WEST, this.west);

        block.setBlockData(mushroom, false);
    }
}