package net.dogemines.framework.data.registry;

public class RegistryObject<T> {
    private final String id;
    private final T value;

    public RegistryObject(String id, T value) {
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
