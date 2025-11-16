package net.dogemines.framework.sound;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CustomSoundEvent {
    private final NamespacedKey namespacedKey;
    private final boolean replace;
    private final String subtitle;
    private final ArrayList<CustomSound> soundList = new ArrayList<>();

    public CustomSoundEvent(boolean replace, String subtitle, NamespacedKey id) {
        this.replace = replace;
        this.subtitle = subtitle;
        this.namespacedKey = id;
    }

    public CustomSoundEvent(NamespacedKey id) {
        this(false, null, id);
    }

    //builder pattern to add sounds
    public CustomSoundEvent add(CustomSound customSound) {
        soundList.add(customSound);
        return this;
    }

    //helper method to create a sound event entirely from the id. "." are replaced with "/" for the path of the sound file.
    public static CustomSoundEvent fromId(NamespacedKey id) {
        return fromId(id, 1.0F);
    }

    public static CustomSoundEvent fromId(NamespacedKey namespacedKey, float volume) {
        return new CustomSoundEvent(namespacedKey).add(
                new CustomSound.Builder()
                        .name(namespacedKey.asString().replace(".", "/"))
                        .volume(volume)
                        .build()
        );
    }

    public ArrayList<CustomSound> getSounds() {
        return soundList;
    }

    public NamespacedKey getNamespacedKey() {
        return namespacedKey;
    }

    public void playSound(Player player, SoundCategory category) {
        player.playSound(player.getLocation(), namespacedKey.asString(), category.getCategory(), 1, 1);
    }
}