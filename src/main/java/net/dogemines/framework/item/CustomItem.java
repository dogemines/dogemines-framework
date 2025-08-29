package net.dogemines.framework.item;

import com.google.gson.JsonObject;
import net.dogemines.framework.DogeMinesFramework;
import net.dogemines.framework.data.registry.Registrable;
import net.dogemines.framework.data.registry.Registries;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomItem implements Registrable {
    private static final NamespacedKey ID_KEY = new NamespacedKey(DogeMinesFramework.NAMESPACE, "item_id");
    private final Material material;
    private final Component name;
    private final ItemSettings settings;
    private final String itemId;

    public CustomItem(Material material, Component name, String itemId, ItemSettings settings) {
        this.material = material;
        this.name = name;
        this.settings = settings;
        this.itemId = itemId;
    }
    public CustomItem(Material material, Component name, String itemId) {
        this(material, name, itemId, ItemSettings.DEFAULT);
    }

    //getter methods
    public Material getMaterial() {
        return material;
    }
    public Component getName() {
        return name;
    }
    public ItemSettings getSettings() {
        return settings;
    }

    @Override
    public String getId() {
        return itemId;
    }

    //note to self: please don't make anymore middleware interfaces. just fix the shitty resource pack code instead of making it even more shitty.
    public interface modelMiddleware {
        void modifyModelJSON(JsonObject model);
    }
    public interface itemMiddleware {
        void modifyItemJSON(JsonObject item);
    }


    //static utility methods
    public static @Nullable String getIdFromItemStack(@NotNull ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();
        PersistentDataContainer itemPDC = meta.getPersistentDataContainer();

        if (itemPDC.has(ID_KEY)) {
            return itemPDC.get(ID_KEY, PersistentDataType.STRING);
        }
        return null;
    }

    public static @Nullable CustomItem fromItemStack(ItemStack stack) {
        String itemId = getIdFromItemStack(stack);
        if (itemId != null) {
            return Registries.ITEM.get(itemId);
        }
        return null;
    }

    //overridable methods
    public void modifyItemMeta(ItemMeta itemMeta) {
        if (settings.hasModel()) {
            itemMeta.setItemModel(new NamespacedKey(DogeMinesFramework.NAMESPACE, itemId));
        }
        itemMeta.itemName(name);
        itemMeta.getPersistentDataContainer().set(ID_KEY, PersistentDataType.STRING, itemId);
    }
    public void onClick(Player player, CustomItemStack itemStack) {}
}