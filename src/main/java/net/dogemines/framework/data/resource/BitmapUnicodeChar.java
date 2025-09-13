package net.dogemines.framework.data.resource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.NamespacedKey;

import java.util.HashSet;

public class BitmapUnicodeChar implements UnicodeChar {
    private final String filepath;
    private final int ascent;
    private final int height;
    private final char character;

    private static char offset = '\uE000';

    public BitmapUnicodeChar(String filepath, int ascent, int height, Character character) {
        this.ascent = ascent;
        this.height = height;
        this.filepath = filepath;
        this.character = character;
    }
    public BitmapUnicodeChar(String filepath, int ascent, int height) {
        this.ascent = ascent;
        this.height = height;
        this.filepath = filepath;

        //primitive types don't hold references when a new variable is assigned
        this.character = offset;
        offset += 1;
    }

    public JsonObject getJson(String namespace) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "bitmap");
        json.addProperty("file", namespace + ":chars/" + filepath);
        json.addProperty("ascent", ascent);
        json.addProperty("height", height);

        JsonArray chars = new JsonArray();
        chars.add(charToString(character));

        json.add("chars", chars);

        return json;
    }

    private static String charToString(char c) {
        return String.format("\\u%04X", (int) c); // u + 4 digit hex
    }

    @Override
    public char getCharacter() {
        return this.character;
    }

    public static class SpaceAdvance implements UnicodeChar {
        private static final HashSet<SpaceAdvance> ALL_ADVANCES = new HashSet<>();

        private final char character;
        private final int space;
        public SpaceAdvance(int space) {
            this.character = offset;
            this.space = space;
            ALL_ADVANCES.add(this);

            offset += 1;
        }

        public static JsonObject getEntireJson() {
            JsonObject json = new JsonObject();
            json.addProperty("type", "space");

            JsonObject advances = new JsonObject();
            for (SpaceAdvance advance : ALL_ADVANCES) {
                advances.addProperty(charToString(advance.character), advance.space);
            }

            json.add("advances", advances);

            return json;
        }

        @Override
        public char getCharacter() {
            return this.character;
        }

    }
}
