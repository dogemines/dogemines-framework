package net.dogemines.framework.sound;

public enum SoundCategory {
    CUSTOM_MUSIC(org.bukkit.SoundCategory.RECORDS),
    CUSTOM_SOUNDS(org.bukkit.SoundCategory.VOICE);

    private final org.bukkit.SoundCategory category;
    SoundCategory(org.bukkit.SoundCategory category) {
        this.category = category;
    }

    public org.bukkit.SoundCategory getCategory() {
        return category;
    }
}
