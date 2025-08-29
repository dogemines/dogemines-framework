package net.dogemines.framework.item;

import net.dogemines.framework.DogeMinesFramework;
import net.dogemines.framework.data.registry.EnumRegistries;
import net.dogemines.framework.data.registry.Registries;
import net.dogemines.framework.data.registry.RegistryObject;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

//A custom item contains ItemData and is defined by an itemId which is stored in the ItemStack.
public class CustomItemStack extends ItemStack {
    private final CustomItem customItem;

    public CustomItemStack(@NotNull CustomItem customItem, int amount) {
        super(customItem.getMaterial(), amount);
        this.customItem = customItem;

        ItemMeta meta = this.getItemMeta();

        //leave modification to item meta up to the SimpleItem class.
        customItem.modifyItemMeta(meta);

        this.setItemMeta(meta);
    }


    //factory methods
    @Contract("_, _ -> new")
    public static @NotNull CustomItemStack of(@NotNull RegistryObject<? extends CustomItem> registry, int amount) {
        return new CustomItemStack(registry.getValue(), amount);
    }

    @Contract("_, _ -> new")
    public static @NotNull CustomItemStack of(EnumRegistries.@NotNull Item itemRegistry, int amount) {
        return new CustomItemStack(itemRegistry.getValue(), amount);
    }

    @Contract("_ -> new")
    public static @NotNull CustomItemStack of(EnumRegistries.@NotNull Item itemRegistry) {
        return new CustomItemStack(itemRegistry.getValue(), 1);
    }


    //utility methods
    public boolean equalsStack(ItemStack stack) {
        return Objects.equals(CustomItem.getIdFromItemStack(stack), customItem.getId());
    }

    public static @Nullable CustomItemStack fromItemStack(ItemStack stack) {
        if (stack instanceof CustomItemStack customItemStack) {
            return customItemStack;
        }
        else {
            //reconstruct CustomItemStack from the stack's PDC
            CustomItem item = CustomItem.fromItemStack(stack);
            if (item != null) {
                return new CustomItemStack(item, stack.getAmount());
            }
            return null;
        }
    }

}