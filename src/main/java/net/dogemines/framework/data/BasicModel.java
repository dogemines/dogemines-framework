package net.dogemines.framework.data;

import com.google.gson.JsonObject;

public class BasicModel {
    private final String parent;
    private final String texture;
    private final boolean isBlock;

    public BasicModel(String parent, String texture, boolean isBlock) {
        this.parent = parent;
        this.texture = texture;
        this.isBlock = isBlock;
    }

    public JsonObject getJson() {
        JsonObject model = new JsonObject();
        model.addProperty("parent", parent);

        JsonObject textures = new JsonObject();
        textures.addProperty(isBlock ? "all" : "layer0", texture);

        model.add("textures", textures);

        return model;
    }
}
