package net.dogemines.framework;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.dogemines.framework.test.DefaultBlocks;
import net.dogemines.framework.data.RPHttpServer;
import net.dogemines.framework.data.ResourcePack;
import net.dogemines.framework.data.registry.Registries;
import net.dogemines.framework.menu.DefaultInventoryItems;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class DogeMinesFramework extends JavaPlugin {

    public static final String NAMESPACE = "dogeframework";
    public static final String VERSION = "1.0alpha";
    private static ResourcePack resourcePack;
    private static RPHttpServer httpServer;

    private static DogeMinesFramework instance;

    @Override
    public void onEnable() {
        instance = this;
        // Plugin startup logic

        //register default enums
        Registries.ITEM.registerEnum(DefaultInventoryItems.class);
        Registries.BLOCK.registerEnum(DefaultBlocks.class);

        //register commands
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            Commands registrar = commands.registrar();
            RegistryCommands.registerCommands(registrar);
        });

        //run code when the server fully starts (all plugins are loaded)
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {

            //make all registries immutable
            Registries.makeRegistriesImmutable();

            //generate resource pack
            resourcePack = new ResourcePack();
            httpServer = new RPHttpServer(this, resourcePack.getFile());
            getLogger().info("Resource pack hosted at: " + getPackURL());

        }, 0);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (httpServer != null) {
            httpServer.stop();
        }
    }

    public static String getPackURL() {
        return httpServer.getResourceURL();
    }
    public static ResourcePack getResourcePack() {
        return resourcePack;
    }
    public static JavaPlugin getInstance() {
        return instance;
    }
}
