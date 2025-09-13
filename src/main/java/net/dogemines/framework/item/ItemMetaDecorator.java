package net.dogemines.framework.item;

import net.dogemines.framework.DogeMinesFramework;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class ItemMetaDecorator {
    public static final ItemMetaDecorator DEFAULT = new ItemMetaDecorator(true);
    private static final NamespacedKey ID_KEY = new NamespacedKey(DogeMinesFramework.NAMESPACE, "item_id");

    private final boolean hasModel;

    public ItemMetaDecorator(boolean hasModel) {
        this.hasModel = hasModel;
    }

    public boolean hasModel() {
        return hasModel;
    }

    //overridable
    public Component getName(CustomItem item) {
        return item.getName();
    }

    public void modifyItemMeta(ItemMeta itemMeta, NamespacedKey itemId, CustomItem item) {
        if (hasModel) {
            itemMeta.setItemModel(itemId);
        }
        itemMeta.itemName(getName(item));
        itemMeta.getPersistentDataContainer().set(ID_KEY, PersistentDataType.STRING, itemId.asString());
    }
}
