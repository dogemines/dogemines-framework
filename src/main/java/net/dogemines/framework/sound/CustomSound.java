package net.dogemines.framework.sound;

import com.google.gson.JsonObject;
import net.dogemines.framework.DogeMinesFramework;

public record CustomSound(
        String filename,
        Float volume,
        Float pitch,
        Integer weight,
        Boolean stream,
        Integer attenuationDistance,
        Boolean preload,
        String type
) {

    private void addIfExists(JsonObject obj, String key, Object value) {
        switch (value) {
            case null -> {
                return; // skip nulls
            }
            case String s -> obj.addProperty(key, s);
            case Number n -> obj.addProperty(key, n);
            case Boolean b -> obj.addProperty(key, b);
            default -> throw new IllegalArgumentException("Unsupported type for JSON property: " + value.getClass());
        }

    }

    public JsonObject generateSoundObject() {
        JsonObject jsonObject = new JsonObject();

        addIfExists(jsonObject, "name", filename);
        addIfExists(jsonObject, "volume", volume);
        addIfExists(jsonObject, "pitch", pitch);
        addIfExists(jsonObject, "weight", weight);
        addIfExists(jsonObject, "stream", stream);
        addIfExists(jsonObject, "attenuation_distance", attenuationDistance);
        addIfExists(jsonObject, "preload", preload);
        addIfExists(jsonObject, "type", type);

        return jsonObject;
    }

    public static class Builder {
        private String filename;
        private Float volume;
        private Float pitch;
        private Integer weight;
        private Boolean stream;
        private Integer attenuationDistance;
        private Boolean preload;
        private String type;

        public Builder name(String name) {
            this.filename = DogeMinesFramework.NAMESPACE + ":" + name;
            return this;
        }

        public Builder volume(float volume) {
            this.volume = volume;
            return this;
        }

        public Builder pitch(float pitch) {
            this.pitch = pitch;
            return this;
        }

        public Builder weight(int weight) {
            this.weight = weight;
            return this;
        }

        public Builder stream(boolean stream) {
            this.stream = stream;
            return this;
        }

        public Builder attenuationDistance(int attenuationDistance) {
            this.attenuationDistance = attenuationDistance;
            return this;
        }

        public Builder preload(boolean preload) {
            this.preload = preload;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public CustomSound build() {
            return new CustomSound(
                    filename,
                    volume,
                    pitch,
                    weight,
                    stream,
                    attenuationDistance,
                    preload,
                    type
            );
        }
    }
}
