package net.dogemines.framework.item;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class OversizedGuiItem extends CustomItem implements CustomItem.modelMiddleware, CustomItem.itemMiddleware {

    private double inventoryScale = 1.13;

    public OversizedGuiItem(Material material, Component name) {
        super(material, name, ItemMetaDecorator.DEFAULT);
    }
    public OversizedGuiItem(Material material, Component name, ItemMetaDecorator decorator) {
        super(material, name, decorator);
    }

    public OversizedGuiItem setInventoryScale(double inventoryScale) {
        this.inventoryScale = inventoryScale;
        return this;
    }

    @Override
    public void modifyModelJSON(@NotNull JsonObject model) {
        JsonObject display = new JsonObject();
        JsonObject gui = new JsonObject();
        JsonArray scale = new JsonArray();

        scale.add(inventoryScale);
        scale.add(inventoryScale);
        scale.add(inventoryScale);

        gui.add("scale", scale);
        display.add("gui", gui);
        model.add("display", display);
    }

    @Override
    public void modifyItemJSON(@NotNull JsonObject item) {
        item.addProperty("oversized_in_gui", true);
    }
}
