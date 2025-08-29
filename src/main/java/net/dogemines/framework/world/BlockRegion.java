// Object that represents a 3D region with various methods.
// 2023 - Piggy Gaming

package net.dogemines.framework.world;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.function.Consumer;

public class BlockRegion {

    private final int minX, minY, minZ;
    private final int maxX, maxY, maxZ;
    private final World world;

    public BlockRegion(int x1, int y1, int z1, int x2, int y2, int z2, World world) {
        this.world = world;
        this.minX = Math.min(x1, x2);
        this.maxX = Math.max(x1, x2);

        this.minY = Math.min(y1, y2);
        this.maxY = Math.max(y1, y2);

        this.minZ = Math.min(z1, z2);
        this.maxZ = Math.max(z1, z2);
    }

    public BlockRegion(Location startLoc, Location endLoc, World world) {
        this(startLoc.getBlockX(), startLoc.getBlockY(), startLoc.getBlockZ(), endLoc.getBlockX(), endLoc.getBlockY(), endLoc.getBlockZ(), world);
    }

    public boolean contains(Location loc) {
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();

        return x >= minX && x <= maxX
            && y >= minY && y <= maxY
            && z >= minZ && z <= maxZ;
    }

    public boolean contains(Block block) {
        return contains(block.getLocation());
    }

    /*public void worldEditFill(BlockType block) {
        World weWorld = FaweAPI.getWorld(DogeMines.WORLD.getName());
        BlockState state = block.getDefaultState();

        Bukkit.getScheduler().runTaskAsynchronously(DogeMines.getPlugin(), () -> {

            try (EditSession editSession = WorldEdit.getInstance().newEditSession(weWorld)) {

                BlockVector3 v1 = BlockVector3.at(minX, minY, minZ);
                BlockVector3 v2 = BlockVector3.at(maxX, maxY, maxZ);

                Region region = new CuboidRegion(weWorld, v1, v2);

                editSession.setBlocks(region, state);
                editSession.flushQueue();
            }

        });

    }*/

    public void fill(Material material) {
        forEachBlock((block) -> {
            block.setType(material);
        });
    }

    public void forEachBlock(Consumer<Block> action) {
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    action.accept(world.getBlockAt(x, y, z));
                }
            }
        }
    }

    public BlockRegion getTopLayer() {
        return new BlockRegion(this.minX, this.maxY, this.minZ, this.maxX, this.maxY, this.maxZ, this.world);
    }

    public BlockRegion getAllButTop() {
        return new BlockRegion(this.minX, this.minY, this.minZ, this.maxX, this.maxY - 1, this.maxZ, this.world);
    }

}
