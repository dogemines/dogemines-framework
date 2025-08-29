package net.dogemines.framework.data.registry;

import net.dogemines.framework.item.CustomItem;
import net.dogemines.framework.sound.CustomSoundEvent;
import org.jetbrains.annotations.ApiStatus;

import java.util.*;

public class Registries<T extends Registrable> extends Registry<T> {
    //define registries
    public static final Registries<CustomItem> ITEM = new Registries<>();
    public static final Registries<CustomSoundEvent> SOUND_EVENTS = new Registries<>();

    //registry logic
    public void register(T toRegister, String id) {
        RegistryObject<T> registryObject = new RegistryObject<>(id, toRegister);
        this.set(id, registryObject);
    }

    public <E extends Enum<E> & EnumRegistries.EnumRegistry<T>> void registerEnum(Class<E> enumRegistry) {
        for (E constant : enumRegistry.getEnumConstants()) {
            T value = constant.getValue();
            this.register(value, value.getId());
        }
    }

    //sets the immutable property for all StorageClasses. Should only be used internally.
    @ApiStatus.Internal
    public static void makeRegistriesImmutable() {
        for (Registry<?> registry : REGISTRIES) {
            registry.makeImmutable();
        }
    }
}

class Registry<T> {
    static final List<Registry<?>> REGISTRIES = new ArrayList<>();
    private final Map<String, RegistryObject<T>> registry;

    private boolean immutable = false;

    void makeImmutable() {
        immutable = true;
    }

    public T get(String key) {
        return registry.get(key).getValue();
    }
    public void set(String key, RegistryObject<T> value) {
        if (immutable) {
            throw new ImmutableRegistryException("Tried to set to an immutable registry! Custom objects should be registered before the server starts, as registries are automatically made immutable upon startup.");
        }
        registry.put(key, value);
    }
    public void set(RegistryObject<T> object) {
        set(object.getId(), object);
    }
    public boolean has(String key) {
        return registry.containsKey(key);
    }

    public Collection<RegistryObject<T>> getPairs() {
        return registry.values();
    }

    public Registry() {
        registry = new HashMap<>();
        REGISTRIES.add(this);
    }
}

class ImmutableRegistryException extends RuntimeException {
    public ImmutableRegistryException(String message) {
        super(message);
    }
}