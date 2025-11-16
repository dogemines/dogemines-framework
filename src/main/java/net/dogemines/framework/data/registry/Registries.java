package net.dogemines.framework.data.registry;

import net.dogemines.framework.block.CustomBlock;
import net.dogemines.framework.data.resource.UnicodeChar;
import net.dogemines.framework.item.CustomItem;
import net.dogemines.framework.sound.CustomSoundEvent;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashSet;

public final class Registries {
    //define registries
    public static final DogeRegistry<CustomItem> ITEM = new DogeRegistry<>();
    public static final DogeRegistry<CustomSoundEvent> SOUND_EVENT = new DogeRegistry<>();
    public static final DogeRegistry<CustomBlock> BLOCK = new DogeRegistry<>();
    public static final DogeRegistry<UnicodeChar> UNICODE_CHAR = new DogeRegistry<>();

    private static final HashSet<String> ALL_NAMESPACES = new HashSet<>();

    public static void addUsedNamespace(String namespace) {
        ALL_NAMESPACES.add(namespace);
    }

    @ApiStatus.Internal
    public static HashSet<String> getAllNamespaces() {
        return ALL_NAMESPACES;
    }
}