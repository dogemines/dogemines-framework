package net.dogemines.framework.data.registry;

import net.dogemines.framework.block.CustomBlock;
import net.dogemines.framework.item.CustomItem;
import net.dogemines.framework.menu.InventoryMenu;
import net.dogemines.framework.sound.CustomSoundEvent;
import net.dogemines.framework.sound.SoundCategory;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class EnumRegistries {

    public interface EnumRegistry<T> {
        T getValue();
    }

    public interface Item extends EnumRegistry<CustomItem> {}
    public interface Block extends EnumRegistry<CustomBlock> {}
    public interface Menu extends EnumRegistry<InventoryMenu> {
        default void openInventory(@NotNull Player player) {
            getValue().openInventory(player);
        }
    }
    public interface Sound extends EnumRegistry<CustomSoundEvent> {
        default void playSound(Player player, SoundCategory category) {
            getValue().playSound(player, category);
        }
    }
}