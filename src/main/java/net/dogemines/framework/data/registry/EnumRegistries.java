package net.dogemines.framework.data.registry;

import net.dogemines.framework.block.CustomBlock;
import net.dogemines.framework.item.CustomItem;
import net.dogemines.framework.sound.CustomSoundEvent;
import net.dogemines.framework.sound.SoundCategory;
import org.bukkit.entity.Player;

public final class EnumRegistries {

    public interface EnumRegistry<T> extends RegistryObject<T> {
        @Override
        T getValue();

        @Override
        String getId();
    }

    public interface Item extends EnumRegistry<CustomItem> {}
    public interface Block extends EnumRegistry<CustomBlock> {}
    public interface Sound extends EnumRegistry<CustomSoundEvent> {
        default void playSound(Player player, SoundCategory category) {
            getValue().playSound(player, category);
        }
    }
}