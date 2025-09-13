package net.dogemines.framework.data.registry;

import net.dogemines.framework.block.CustomBlock;
import net.dogemines.framework.item.CustomItem;
import net.dogemines.framework.sound.CustomSoundEvent;
import net.dogemines.framework.sound.SoundCategory;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

public final class EnumRegistries {

    interface EnumRegistry<T> extends RegistryObject<T> { }

    public interface Item extends EnumRegistry<CustomItem> {}
    public interface Block extends EnumRegistry<CustomBlock> {}
    public interface Sound extends EnumRegistry<CustomSoundEvent> {
        default void playSound(Player player, SoundCategory category) {
            getValue().playSound(player, category);
        }

        @Override
        default NamespacedKey getKey() {
            return getValue().getNamespacedKey();
        }
    }
    public interface UnicodeChar extends EnumRegistry<net.dogemines.framework.data.resource.UnicodeChar> {
        default char getUnicode() {
            return getValue().getCharacter();
        }
        default String asString() {
            return getValue().asString();
        }
    }
}