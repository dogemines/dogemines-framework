package net.dogemines.framework.menu;

import net.kyori.adventure.text.format.TextColor;

public enum TextColors {
    PASTEL_BLUE(TextColor.color(112, 174, 234)),
    PASTE_PURPLE(TextColor.color(137, 153, 234));

    private final TextColor color;

    TextColors(TextColor color) {
        this.color = color;
    }

    public TextColor getColor() {
        return color;
    }
}
