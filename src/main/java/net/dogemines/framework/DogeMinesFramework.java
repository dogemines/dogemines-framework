package net.dogemines.framework;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.dogemines.framework.data.registry.DogeRegistry;
import net.dogemines.framework.data.resource.ResourcePackEvents;
import net.dogemines.framework.menu.InventoryEventHandler;
import net.dogemines.framework.test.DefaultBlocks;
import net.dogemines.framework.data.resource.RPHttpServer;
import net.dogemines.framework.data.resource.ResourcePack;
import net.dogemines.framework.data.registry.Registries;
import net.dogemines.framework.test.DefaultChars;
import net.dogemines.framework.test.DefaultItems;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;

public final class DogeMinesFramework extends JavaPlugin {

    public static final String NAMESPACE = "dogeframework";
    private static ResourcePack resourcePack;

    private static DogeMinesFramework instance;

    @Override
    public void onEnable() {
        instance = this;
        // Plugin startup logic
        PluginManager manager = getServer().getPluginManager();

        //register default enums
        Registries.addUsedNamespace(NAMESPACE); //required for resource pack generation
        Registries.ITEM.registerEnum(DefaultItems.class);
        Registries.BLOCK.registerEnum(DefaultBlocks.class);
        Registries.UNICODE_CHAR.registerEnum(DefaultChars.class);

        //register commands
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            Commands registrar = commands.registrar();
            FrameworkCommands.registerCommands(registrar);
        });

        //register events
        manager.registerEvents(new InventoryEventHandler(), this);

        //run code when the server fully starts (all plugins are loaded)
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {

            //make all registries immutable
            DogeRegistry.makeRegistriesImmutable();

            //generate resource pack
            resourcePack = new ResourcePack(new RPHttpServer(this));
            getLogger().info("Resource pack hosted at: " + resourcePack.getHostingMethod().getResourceURL());

            //once resource pack is hosted, then register resource pack events.
            manager.registerEvents(new ResourcePackEvents(), this);

        }, 0);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        resourcePack.getHostingMethod().disable();
    }

    public static ResourcePack getResourcePack() {
        return resourcePack;
    }
    public static JavaPlugin getInstance() {
        return instance;
    }

    public static void warning(String message) {
        getInstance().getLogger().warning(message);
    }
    public static void info(String message) {
        getInstance().getLogger().info(message);
    }

    @ApiStatus.Internal
    public static NamespacedKey createNamespace(String id) {
        return new NamespacedKey(NAMESPACE, id);
    }
}
