package net.dogemines.framework.item;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

public class InventoryItem extends CustomItem implements CustomItem.modelMiddleware, CustomItem.itemMiddleware {

    private double inventoryScale = 1.13;

    public InventoryItem(Material material, Component name, String itemId) {
        super(material, name, itemId, ItemSettings.DEFAULT);
    }

    public InventoryItem setInventoryScale(double inventoryScale) {
        this.inventoryScale = inventoryScale;
        return this;
    }

    @Override
    public void modifyModelJSON(JsonObject model) {
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
    public void modifyItemJSON(JsonObject item) {
        item.addProperty("oversized_in_gui", true);
    }
}
