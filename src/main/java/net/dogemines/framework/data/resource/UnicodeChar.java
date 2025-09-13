package net.dogemines.framework.data.resource;

import org.bukkit.NamespacedKey;

public interface UnicodeChar {
    char getCharacter();
    default String asString() {
        return Character.toString(getCharacter());
    }
}
