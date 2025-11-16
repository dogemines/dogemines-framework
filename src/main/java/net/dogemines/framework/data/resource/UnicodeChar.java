package net.dogemines.framework.data.resource;

public interface UnicodeChar {
    char getCharacter();
    default String asString() {
        return Character.toString(getCharacter());
    }
}
