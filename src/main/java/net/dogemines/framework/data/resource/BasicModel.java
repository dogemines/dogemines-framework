package net.dogemines.framework.data.resource;

import com.google.gson.JsonObject;
import org.bukkit.NamespacedKey;

public class BasicModel {
    private final String parent;
    private final boolean isBlock;

    public BasicModel(String parent, boolean isBlock) {
        this.parent = parent;
        this.isBlock = isBlock;
    }

    public JsonObject getJson(NamespacedKey itemId) {
        JsonObject model = new JsonObject();
        model.addProperty("parent", parent);

        JsonObject textures = new JsonObject();
        textures.addProperty(isBlock ? "all" : "layer0", getPath(itemId));

        model.add("textures", textures);

        return model;
    }

    public String getPath(NamespacedKey itemId) {
        return ResourcePack.getDogeminesPath(itemId, isBlock);
    }
}
