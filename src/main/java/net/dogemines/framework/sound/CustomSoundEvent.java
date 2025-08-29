package net.dogemines.framework.sound;

import net.dogemines.framework.DogeMinesFramework;
import net.dogemines.framework.data.registry.Registrable;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CustomSoundEvent implements Registrable {
    private final boolean replace;
    private final String subtitle;
    private final String id;
    private final ArrayList<CustomSound> soundList = new ArrayList<>();

    public CustomSoundEvent(boolean replace, String subtitle, String id) {
        this.replace = replace;
        this.subtitle = subtitle;
        this.id = DogeMinesFramework.NAMESPACE + "." + id;
    }

    public CustomSoundEvent(String id) {
        this(false, null, id);
    }

    //builder pattern to add sounds
    public CustomSoundEvent add(CustomSound customSound) {
        soundList.add(customSound);
        return this;
    }

    //helper method to create a sound event entirely from the id. "." are replaced with "/" for the path of the sound file.
    public static CustomSoundEvent fromId(String id) {
        return fromId(id, 1.0F);
    }

    public static CustomSoundEvent fromId(String id, float volume) {
        return new CustomSoundEvent(id).add(
                new CustomSound.Builder()
                        .name(id.replace(".", "/"))
                        .volume(volume)
                        .build()
        );
    }

    public ArrayList<CustomSound> getSounds() {
        return soundList;
    }

    @Override
    public String getId() {
        return id;
    }

    public void playSound(Player player, SoundCategory category) {
        player.playSound(player.getLocation(), this.getId(), category.getCategory(), 1, 1);
    }
}