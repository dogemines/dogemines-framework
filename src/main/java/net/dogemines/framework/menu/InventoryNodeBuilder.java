package net.dogemines.framework.menu;

import net.dogemines.framework.item.CustomItemStack;
import net.dogemines.framework.test.DefaultItems;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryNodeBuilder extends MenuBuilder<InventoryNodeBuilder> implements MenuBuilder.Buildable {

    private record LinkWithoutIndex(ItemStack stack, InventoryNodeBuilder node) {}

    private InventoryNodeBuilder parent;
    private final Map<Integer, InventoryNodeBuilder> links = new HashMap<>();

    private final List<ItemStack> arrangeableItems = new ArrayList<>();
    private final List<InventoryNodeBuilder> arrangeableItemsTo = new ArrayList<>();

    public InventoryNodeBuilder(InventorySizes size, String name) {
        super(size, name);
    }

    private void setBack(ButtonLocation buttonLocation) {
        if (parent != null) {
            int index = contents.length + buttonLocation.getOffsetRelative();

            set(index, CustomItemStack.of(DefaultItems.INVENTORY_BACK));
            links.put(index, parent);
        }
    }

    private void setLink(ItemStack stack, int index, InventoryNodeBuilder to) {
        set(index, stack);
        links.put(index, to);
    }

    public InventoryNodeBuilder setBackAndFill(ButtonLocation buttonLocation) {
        super.fill();
        this.setBack(buttonLocation);
        return this;
    }

    //basically adds the itemStack to a list to be arranged later.
    public InventoryNodeBuilder addChildToArrange(InventoryNodeBuilder child, ItemStack link) {
        arrangeableItems.add(link);
        arrangeableItemsTo.add(child);

        child.parent = this;
        return child;
    }

    //hard-sets the item to be at that location
    public InventoryNodeBuilder addChild(InventoryNodeBuilder child, ItemStack link, int index) {
        child.parent = this;
        setLink(link, index, child);

        return child;
    }

    //after all children are added via addChildToArrange, this method should be called to arrange the items and create links.
    public InventoryNodeBuilder arrangeLinks(int spacing) {
        int[] indexes = super.arrangeGeneric(arrangeableItems.toArray(), spacing);

        int i = 0;
        for (int index : indexes) {
            links.put(index, arrangeableItemsTo.get(i));
            i++;
        }
        return this;
    }

    private InventoryMenu createMenu() {
        return new InventoryMenu(name, contents, clickCallbacks, hasCooldown);
    }

    private void setInventoryLinks(InventoryMenu parentMenu) {
        for (Map.Entry<Integer, InventoryNodeBuilder> e : links.entrySet()) {

            InventoryNodeBuilder childBuilder = e.getValue();
            InventoryMenu childMenu = childBuilder.createMenu();

            parentMenu.getClickCallbacks().put(e.getKey(), childMenu::openInventory);

            //for some reason it works if i check if the parent is null? shouldn't it always be defined since this is the parent? so fun!!!
            if (childBuilder.parent != null && !childBuilder.links.isEmpty()) {
                childBuilder.setInventoryLinks(childMenu);
            }

        }
    }

    public InventoryMenu build() {
        InventoryMenu root = createMenu();
        setInventoryLinks(root);
        return root;
    }
}