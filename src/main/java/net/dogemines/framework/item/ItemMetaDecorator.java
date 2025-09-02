package net.dogemines.framework.item;

import net.dogemines.framework.DogeMinesFramework;
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

    public void modifyItemMeta(ItemMeta itemMeta, String itemId, CustomItem item) {
        if (hasModel) {
            itemMeta.setItemModel(new NamespacedKey(DogeMinesFramework.NAMESPACE, itemId));
        }
        itemMeta.itemName(item.getName());
        itemMeta.getPersistentDataContainer().set(ID_KEY, PersistentDataType.STRING, itemId);
    }
}
