package net.dogemines.framework.data.resource;

import net.dogemines.framework.DogeMinesFramework;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ResourcePackEvents implements Listener {
    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        ResourcePack pack = DogeMinesFramework.getResourcePack();
        player.setResourcePack(pack.getHostingMethod().getResourceURL(), pack.getSha1(), Component.text("Resource pack is REQUIRED to play.", NamedTextColor.RED), true);
    }
}
