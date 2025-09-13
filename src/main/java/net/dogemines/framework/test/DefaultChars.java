package net.dogemines.framework.test;

import net.dogemines.framework.DogeMinesFramework;
import net.dogemines.framework.data.registry.EnumRegistries;
import net.dogemines.framework.data.resource.BitmapUnicodeChar;
import net.dogemines.framework.data.resource.UnicodeChar;
import org.bukkit.NamespacedKey;

public enum DefaultChars implements EnumRegistries.UnicodeChar {
    NEG_SPACE_BEFORE_CHAR(new BitmapUnicodeChar.SpaceAdvance(-4)),
    NEG_SPACE_BEFORE_NAME(new BitmapUnicodeChar.SpaceAdvance(-164));

    private final UnicodeChar unicodeChar;

    DefaultChars(UnicodeChar unicodeChar) {
        this.unicodeChar = unicodeChar;
    }

    @Override
    public UnicodeChar getValue() {
        return unicodeChar;
    }

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(DogeMinesFramework.NAMESPACE, this.name().toLowerCase());
    }
}
