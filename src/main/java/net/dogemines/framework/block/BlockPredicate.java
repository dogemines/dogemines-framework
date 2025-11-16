package net.dogemines.framework.block;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.dogemines.framework.DogeMinesFramework;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface BlockPredicate {
    void setBlock(Block block);
    BlockData getBlockData();
    Material getMaterial();

    static @Nullable JsonObject getPredicateJson(BlockPredicate blockPredicate) {
        if (blockPredicate instanceof Unmodeled) { return null; }
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create()
                .toJsonTree(blockPredicate)
                .getAsJsonObject();
    }

    //if you want an unmodeled block with custom properties
    class Unmodeled implements BlockPredicate {
        private final Material material;
        private final BlockData blockData;
        private final String predicateId;

        public Unmodeled(@NotNull Material material, String unmodeledStateId) {
            this.material = material;
            this.blockData = material.createBlockData();
            this.predicateId = unmodeledStateId;
        }

        @Override
        public void setBlock(@NotNull Block block) {
            block.setBlockData(blockData);

            final PersistentDataContainer chunkPDC = block.getChunk().getPersistentDataContainer();
            chunkPDC.set(getChunkRelativeKey(block), PersistentDataType.STRING, predicateId);
        }

        @Override
        public BlockData getBlockData() {
            return blockData;
        }

        @Override
        public Material getMaterial() {
            return material;
        }

        @Contract("_ -> new")
        private static @NotNull NamespacedKey getChunkRelativeKey(Block block) {
            return new NamespacedKey(DogeMinesFramework.NAMESPACE, "unmodeled_state_id_" + getChunkRelativeCoordinate(block));
        }

        private static int getChunkRelativeCoordinate(@NotNull Block block) {
            final int relX = (block.getX() % 16 + 16) % 16;
            final int relZ = (block.getZ() % 16 + 16) % 16;
            final int relY = block.getY();
            return (relY & 0xFFFF) | ((relX & 0xFF) << 16) | ((relZ & 0xFF) << 24);
        }
    }
}