package net.dogemines.framework.data.registry;

import org.bukkit.NamespacedKey;

public interface RegistryObject<T> {
    public NamespacedKey getKey();
    public T getValue();

    default String getKeyString() {
        return getKey().toString();
    }
    default String getNamespace() {
        return getKey().getNamespace();
    }
    default String getId() {
        return getKey().getKey();
    }
}
