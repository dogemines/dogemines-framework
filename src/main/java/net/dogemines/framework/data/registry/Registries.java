package net.dogemines.framework.data.registry;

import net.dogemines.framework.block.BlockPredicate;
import net.dogemines.framework.block.CustomBlock;
import net.dogemines.framework.item.CustomItem;
import net.dogemines.framework.sound.CustomSoundEvent;
import org.jetbrains.annotations.ApiStatus;

import java.util.*;

public class Registries<T> extends Registry<T> {

    //define registries
    public static final Registries<CustomItem> ITEM = new Registries<>();
    public static final Registries<CustomSoundEvent> SOUND_EVENT = new Registries<>();
    public static final Registries<CustomBlock> BLOCK = new Registries<>();

    //registry logic
    public void register(T toRegister, String id) {
        RegisteredObject<T> registryObject = new RegisteredObject<>(id, toRegister);
        this.set(id, registryObject);
    }

    public <E extends Enum<E> & EnumRegistries.EnumRegistry<T>> void registerEnum(Class<E> enumRegistry) {
        for (E constant : enumRegistry.getEnumConstants()) {
            this.register(constant.getValue(), constant.getId());
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

    public RegistryObject<T> get(String key) {
        return registry.get(key);
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

class RegisteredObject<T> implements RegistryObject<T> {
    private final String id;
    private final T value;

    public RegisteredObject(String id, T value) {
        this.id = id;
        this.value = value;
    }

    public String getId() {
        return id;
    }
    public T getValue() {
        return value;
    }
}
