package net.dogemines.framework.item;

import com.google.gson.JsonObject;
import net.dogemines.framework.DogeMinesFramework;
import net.dogemines.framework.data.registry.Registries;
import net.dogemines.framework.data.registry.RegistryObject;
import net.dogemines.framework.data.resource.BasicModel;
import net.dogemines.framework.data.resource.ResourcePack;
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

public class CustomItem {
    private static final NamespacedKey ID_KEY = new NamespacedKey(DogeMinesFramework.NAMESPACE, "item_id");

    private final Material material;
    private final Component name;
    private final ItemMetaDecorator metaDecorator;

    public CustomItem(Material material, Component name, ItemMetaDecorator metaDecorator) {
        this.material = material;
        this.name = name;
        this.metaDecorator = metaDecorator;
    }
    public CustomItem(Material material, String name, ItemMetaDecorator metaDecorator) {
        this(material, Component.text(name), metaDecorator);
    }

    public CustomItem(Material material, Component name) {
        this(material, name, ItemMetaDecorator.DEFAULT);
    }
    public CustomItem(Material material, String name) {
        this(material, name, ItemMetaDecorator.DEFAULT);
    }


    //methods
    public void modifyItemMeta(ItemMeta itemMeta, NamespacedKey itemId) {
        metaDecorator.modifyItemMeta(itemMeta, itemId, this);
    }

    //getter methods
    public Material getMaterial() {
        return material;
    }
    public Component getName() {
        return name;
    }
    public boolean hasModel() {
        return metaDecorator.hasModel();
    }


    //note to self: please don't make anymore middleware interfaces. just fix the shitty resource pack code instead of making it even more shitty.
    public interface modelMiddleware {
        void modifyModelJSON(JsonObject model);
    }
    public interface itemMiddleware {
        void modifyItemJSON(JsonObject item);
    }


    //static utility methods
    public static @Nullable NamespacedKey getIdFromItemStack(@NotNull ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();
        PersistentDataContainer itemPDC = meta.getPersistentDataContainer();

        if (itemPDC.has(ID_KEY)) {
            String id = itemPDC.get(ID_KEY, PersistentDataType.STRING);
            assert id != null;
            return NamespacedKey.fromString(id);
        }
        return null;
    }

    /**
     * Get a CustomItem from an ItemStack using the stored ID in the PDC.
     * @param stack The ItemStack for looking up its stored ID.
     * @return A CustomItem found in the registry with the stored ID, or null if it wasn't found.
     */
    public static @Nullable RegistryObject<CustomItem> fromItemStack(ItemStack stack) {
        NamespacedKey itemId = getIdFromItemStack(stack);
        if (itemId != null) {
            return Registries.ITEM.get(itemId);
        }
        return null;
    }


    //overridable methods
    public void onClick(Player player, CustomItemStack itemStack) {}

    public BasicModel getModel() {
        return new BasicModel(
                ResourcePack.getParent(material, true),
                false
        );
    }
}