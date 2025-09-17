package net.dogemines.framework.data.registry;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.ApiStatus;

import java.util.*;

public class DogeRegistry<T> {
    static final List<DogeRegistry<?>> REGISTRIES = new ArrayList<>();
    private final Map<NamespacedKey, RegistryObject<T>> registry;

    private boolean immutable = false;

    void makeImmutable() {
        immutable = true;
    }

    public RegistryObject<T> get(NamespacedKey key) {
        return registry.get(key);
    }
    public void set(NamespacedKey key, RegistryObject<T> value) {
        if (immutable && !registry.containsKey(key)) {
            throw new ImmutableRegistryException();
        }
        if (registry.containsKey(key)) {
            throw new DuplicateRegistryException(key.toString());
        }
        registry.put(key, value);
    }
    public void set(RegistryObject<T> object) {
        set(object.getKey(), object);
    }
    public boolean has(NamespacedKey key) {
        return registry.containsKey(key);
    }

    public Collection<RegistryObject<T>> getPairs() {
        return registry.values();
    }

    private static String formatNamespace(String id, String namespace) {
        return namespace + ":" + id;
    }

    //registry logic
    public void register(T toRegister, NamespacedKey key) {
        RegisteredObject<T> registryObject = new RegisteredObject<>(key, toRegister);
        this.set(key, registryObject);
    }

    public <E extends Enum<E> & EnumRegistries.EnumRegistry<T>> void registerEnum(Class<E> enumRegistry) {
        for (E constant : enumRegistry.getEnumConstants()) {
            this.register(constant.getValue(), constant.getKey());
        }
    }

    public DogeRegistry() {
        registry = new HashMap<>();
        REGISTRIES.add(this);
    }

    //sets the immutable property for all StorageClasses. Should only be used internally.
    @ApiStatus.Internal
    public static void makeRegistriesImmutable() {
        for (DogeRegistry<?> dogeRegistry : DogeRegistry.REGISTRIES) {
            dogeRegistry.makeImmutable();
        }
    }

    public static class ImmutableRegistryException extends RuntimeException {
        public ImmutableRegistryException() {
            super("Tried to set to an immutable registry! Custom objects should be registered before the server starts, as registries are automatically made immutable upon startup.");
        }
    }
    public static class DuplicateRegistryException extends RuntimeException {
        public DuplicateRegistryException(String key) {
            super("Tried to register object with id " + key + " but a value with the same id already exists in the same registry! Make sure to check Registry#has first.");
        }
    }

    //for some ungodly reason i cannot have multiple top level classes
    public static class RegisteredObject<T> implements RegistryObject<T> {
        private final NamespacedKey key;
        private final T value;

        public RegisteredObject(NamespacedKey key, T value) {
            this.key = key;
            this.value = value;
        }

        public NamespacedKey getKey() {
            return key;
        }
        public T getValue() {
            return value;
        }
    }

}