package net.dogemines.framework.menu;

import net.dogemines.framework.DogeMinesFramework;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InventoryMenu implements InventoryHolder, Clickable, PlayerOpenable {

    private final Inventory inventory;
    private final Map<Integer, ClickCallback> clickCallbacks;
    private final boolean hasCooldown;
    private final ArrayList<UUID> inCooldown = new ArrayList<>();

    public InventoryMenu(Component name, ItemStack[] contents, HashMap<Integer, ClickCallback> clickCallbacks, boolean hasCooldown) {
        this.inventory = Bukkit.getServer().createInventory(this, contents.length, name);
        inventory.setContents(contents);

        this.clickCallbacks = clickCallbacks;
        this.hasCooldown = hasCooldown;
    }

    public Map<Integer, ClickCallback> getClickCallbacks() {
        return clickCallbacks;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }

    @Override
    public void openInventory(@NotNull Player player) {
        UUID playerUUID = player.getUniqueId();
        if (!inCooldown.contains(playerUUID)) {
            player.openInventory(inventory);

            if (hasCooldown) {
                inCooldown.add(playerUUID);
                Bukkit.getScheduler().scheduleSyncDelayedTask(DogeMinesFramework.getInstance(), () -> {
                    inCooldown.remove(playerUUID);
                }, 60);
            }
        }
    }

    public int getLastSlotIndex() {
        return inventory.getSize() - 1;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        int slot = event.getSlot();
        if (clickCallbacks.containsKey(slot)) {
            clickCallbacks.get(slot).onClick((Player) event.getWhoClicked());
        }
    }

    //interfaces
    public interface ClickCallback {
        void onClick(Player player);
    }

}

interface Clickable {
    void onClick(InventoryClickEvent event);
}