package net.dogemines.framework.block;

import com.google.gson.JsonObject;
import net.dogemines.framework.DogeMinesFramework;
import net.dogemines.framework.item.BlockItem;
import net.dogemines.framework.item.CustomItemStack;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class CustomBlock {
    private final BlockItem blockItem;
    private final String blockId;
    private final BlockPredicate predicate;
    private final Material baseMaterial;

    public CustomBlock(String blockId, BlockPredicate predicate, BlockItem item) {
        this.blockId = blockId;
        this.predicate = predicate;
        this.baseMaterial = item.getMaterial();
        this.blockItem = item;
    }

    public CustomBlock(String blockId, BlockItem item) {
        this(blockId, null, item);
    }

    public void setBlock(Block block) {
        if (predicate != null) {
            predicate.setBlock(block);
        }
        else {
            block.setType(baseMaterial);

            final PersistentDataContainer chunkPDC = block.getChunk().getPersistentDataContainer();
            chunkPDC.set(getChunkRelativeKey(block), PersistentDataType.STRING, blockId);
        }
    }

    public void onBreak(Player player, Block block) {
        player.give(new CustomItemStack(blockItem, 1));
    }

    public String getId() {
        return blockId;
    }
    
    public BlockItem getBlockItem() {
        return blockItem;
    }

    public interface BlockPredicate {
        JsonObject getJson();
        void setBlock(Block block);
    }

    @Contract("_ -> new")
    private static @NotNull NamespacedKey getChunkRelativeKey(Block block) {
        return new NamespacedKey(DogeMinesFramework.NAMESPACE, "block_id_" + getChunkRelativeCoordinate(block));
    }

    private static int getChunkRelativeCoordinate(@NotNull Block block) {
        final int relX = (block.getX() % 16 + 16) % 16;
        final int relZ = (block.getZ() % 16 + 16) % 16;
        final int relY = block.getY();
        return (relY & 0xFFFF) | ((relX & 0xFF) << 16) | ((relZ & 0xFF) << 24);
    }
}
