package net.dogemines.framework.block;

import com.google.gson.JsonObject;
import net.dogemines.framework.data.resource.BasicModel;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;

public interface CustomBlockState {
    public JsonObject getJson(NamespacedKey blockId);
    public BlockPredicate getPredicate();
    public void setBlock(Block block);
    public BasicModel getModel();
}
