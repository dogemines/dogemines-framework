package net.dogemines.framework.menu;

import net.dogemines.framework.data.registry.RegistryObject;
import net.dogemines.framework.data.resource.UnicodeChar;
import net.dogemines.framework.item.CustomItemStack;
import net.dogemines.framework.test.DefaultChars;
import net.dogemines.framework.test.DefaultItems;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static net.dogemines.framework.util.ArrayUtil.splitArray;

public  class MenuBuilder<T extends MenuBuilder<T>> {
    protected final ItemStack[] contents;
    protected final HashMap<Integer, InventoryMenu.ClickCallback> clickCallbacks = new HashMap<>();
    protected final InventorySizes size;
    protected final Component name;
    protected boolean hasCooldown = false;

    public MenuBuilder(InventorySizes size, String name) {
        this.contents = new ItemStack[size.getSize()];
        this.name = Component.text(name, NamedTextColor.WHITE);
        this.size = size;
    }

    public T setWithCallback(int index, ItemStack stack, InventoryMenu.ClickCallback clickCallback) {
        contents[index] = stack;
        clickCallbacks.put(index, clickCallback);
        return self();
    }
    public T set(int index, ItemStack stack) {
        contents[index] = stack;
        return self();
    }
    public T set(int index, InventoryMenuSlot stack) {
        if (stack.clickCallback() != null) {
            setWithCallback(index, stack.stack(), stack.clickCallback());
        }
        else {
            set(index, stack.stack());
        }
        return self();
    }
    public T fill(ItemStack stack) {
        Arrays.fill(contents, stack);
        return self();
    }
    public T fill() {
        return fill(CustomItemStack.of(DefaultItems.INVENTORY_FILLER));
    }

    // useful for menu-type inventories, the items are arranged automatically.
    public T arrange(ItemStack[] items) {
        arrangeGeneric(items, 1);
        return self();
    }
    public T arrange(InventoryMenuSlot[] items) {
        arrangeGeneric(items, 1);
        return self();
    }
    public T arrange(InventoryMenuSlot[] items, int spacing) {
        arrangeGeneric(items, spacing);
        return self();
    }
    public T arrange(ItemStack[] items, int spacing) {
        arrangeGeneric(items, spacing);
        return self();
    }
    protected <A> int[] arrangeGeneric(A[] items, int itemSpacing) {
        List<A[]> itemRows = splitArray(items, (7 + itemSpacing) / (itemSpacing + 1));
        int[] returnIndexes = new int[items.length];
        int currentItem = 0;

        // 4 per row max
        for (int i = 0; i < itemRows.size(); i++) {
            A[] rowItems = itemRows.get(i);
            int n = rowItems.length;

            int width = n + (n - 1) * itemSpacing; // the length of the row including spacing. (not padding)
            int leftPadding = (9 - width) / 2; // the padding before the first item of the row
            int rowOffset = (i * 2 + 1) * 9; // skip every other row

            for (int j = 0; j < n; j++) {
                int slot = rowOffset + leftPadding + (j * (itemSpacing + 1));
                returnIndexes[currentItem] = slot;
                currentItem++;
                A item = rowItems[j];
                switch (item) {
                    case ItemStack stack -> set(slot, stack);
                    case InventoryMenuSlot slotItem -> set(slot, slotItem);
                    default -> throw new IllegalArgumentException("Unsupported type: " + item.getClass());
                }
            }
        }

        return returnIndexes;
    }

    public T border(ItemStack newStack) {
        // set the first 9 elements
        for (int i = 0; i < 9 && i < contents.length; i++) {
            contents[i] = newStack;
        }

        // set the last 9 elements
        for (int i = Math.max(0, contents.length - 9); i < contents.length; i++) {
            contents[i] = newStack;
        }

        // set every 9th and 10th element
        for (int i = 9; i < contents.length - 9; i += 9) {
            contents[i] = newStack;
            contents[i + 8] = newStack;
        }

        return self();
    }

    public T setHasCooldown(boolean hasCooldown) {
        this.hasCooldown = hasCooldown;
        return self();
    }

    @SuppressWarnings("unchecked")
    protected T self() {
        return (T) this;
    }


    //interfaces
    public interface Buildable {
        InventoryMenu build();
    }


    //builders
    public static class InventoryMenuBuilder extends MenuBuilder<InventoryMenuBuilder> implements Buildable {
        public InventoryMenuBuilder(InventorySizes size, String name) {
            super(size, name);
        }
        public InventoryMenuBuilder(InventorySizes size, String name, UnicodeChar inventoryBackground) {
            super(size,
                    DefaultChars.NEG_SPACE_BEFORE_CHAR.asString()
                    + inventoryBackground.asString()
                    + DefaultChars.NEG_SPACE_BEFORE_NAME.asString()
                    + name
            );
        }
        public InventoryMenuBuilder(InventorySizes size, String name, RegistryObject<UnicodeChar> inventoryBackground) {
            this(size, name, inventoryBackground.getValue());
        }

        public InventoryMenu build() {
            return new InventoryMenu(name, contents, clickCallbacks, hasCooldown);
        }
    }


}