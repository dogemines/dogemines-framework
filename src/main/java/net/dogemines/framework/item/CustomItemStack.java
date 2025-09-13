package net.dogemines.framework.item;

import net.dogemines.framework.data.registry.RegistryObject;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

//A custom item contains ItemData and is defined by an itemId which is stored in the ItemStack.
public class CustomItemStack extends ItemStack {
    private final CustomItem customItem;
    private final NamespacedKey itemId;

    private CustomItemStack(@NotNull CustomItem customItem, NamespacedKey itemId, int amount) {
        super(customItem.getMaterial(), amount);
        this.customItem = customItem;
        this.itemId = itemId;

        ItemMeta meta = this.getItemMeta();

        //leave modification to item meta up to the SimpleItem class.
        customItem.modifyItemMeta(meta, itemId);

        this.setItemMeta(meta);
    }


    //factory methods
    @Contract("_, _ -> new")
    public static @NotNull CustomItemStack of(@NotNull RegistryObject<CustomItem> registry, int amount) {
        return new CustomItemStack(registry.getValue(), registry.getKey(), amount);
    }

    @Contract("_ -> new")
    public static @NotNull CustomItemStack of(@NotNull RegistryObject<CustomItem> registry) {
        return new CustomItemStack(registry.getValue(), registry.getKey(), 1);
    }

    /**
     * If stack is an instance of CustomItemStack, return that, if not, return a new CustomItemStack using the stack's stored id.
     * @param stack the ItemStack to check.
     * @return a CustomItemStack either from a cast or reconstructed.
     */
    public static @Nullable CustomItemStack fromItemStack(ItemStack stack) {
        if (stack instanceof CustomItemStack customItemStack) {
            return customItemStack;
        }
        else {
            //reconstruct CustomItemStack from the stack's PDC
            RegistryObject<CustomItem> item = CustomItem.fromItemStack(stack);
            if (item != null) {
                return new CustomItemStack(item.getValue(), item.getKey(), stack.getAmount());
            }
            return null;
        }
    }


    //utility methods

    /**
     * Check whether another ItemStack equals this one using its stored itemID.
     * @param stack The other ItemStack to check this stack's ID with.
     * @return Whether the IDs are equal.
     */
    public boolean idEquals(ItemStack stack) {
        return Objects.equals(CustomItem.getIdFromItemStack(stack), itemId);
    }

}