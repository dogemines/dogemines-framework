package net.dogemines.framework.block;

import com.google.gson.JsonObject;
import net.dogemines.framework.data.resource.BasicModel;
import net.dogemines.framework.data.resource.ResourcePack;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;

public class MultipartBlockState implements CustomBlockState {
    private final BlockPredicate predicate;
    private final int rotX;
    private final int rotY;
    private final boolean uvLock;
    private final BasicModel model;

    //thank you java for making me create 4 constructors for optional properties
    public MultipartBlockState(BlockPredicate predicate, int rotX, int rotY, boolean uvLock) {
        this.predicate = predicate;
        this.rotX = rotX;
        this.rotY = rotY;
        this.uvLock = uvLock;
        this.model = getModel();
    }
    public MultipartBlockState(BlockPredicate predicate, int rotX, int rotY) {
        this(predicate, rotX, rotY, false);
    }
    public MultipartBlockState(int rotX, int rotY) {
        this(MultipleFacingPredicate.createUnused(), rotX, rotY);
    }
    public MultipartBlockState() {
        this(0, 0);
    }

    @Override
    public BlockPredicate getPredicate() {
        return predicate;
    }

    public void setBlock(Block block) {
        predicate.setBlock(block);
    }

    @Override
    public JsonObject getJson(NamespacedKey blockId) {
        JsonObject when = BlockPredicate.getPredicateJson(this.predicate);
        JsonObject apply = new JsonObject();

        apply.addProperty("model", model.getPath(blockId));

        if (rotX > 0) {
            apply.addProperty("x", rotX);
        }
        if (rotY > 0) {
            apply.addProperty("y", rotY);
        }
        if (uvLock) {
            apply.addProperty("uvlock", true); //skip if false
        }

        JsonObject blockState = new JsonObject();
        blockState.add("when", when);
        blockState.add("apply", apply);
        return blockState;
    }

    @Override
    public BasicModel getModel() {
        return new BasicModel(
            ResourcePack.getParent(predicate.getBlockData().getMaterial(), false),
            true
        );
    }
}