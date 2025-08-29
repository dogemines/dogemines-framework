package net.dogemines.framework.menu;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemStackBuilder {
    private final ItemStack stack;
    private final ItemMeta meta;
    private final Material material;
    private final int count;
    private final List<Component> lore = new ArrayList<>();

    public ItemStackBuilder(Material material, int count) {
        this.material = material;
        this.count = count;

        this.stack = new ItemStack(material, count);
        this.meta = stack.getItemMeta();
    }
    public ItemStackBuilder(Material material) {
        this(material, 1);
    }

    public ItemStackBuilder name(Component component) {
        meta.itemName(component);
        return this;
    }
    public ItemStackBuilder addLore(Component component) {
        lore.add(component);
        return this;
    }

    public ItemStack build() {
        meta.lore(lore);
        stack.setItemMeta(meta);
        return stack;
    }
}
