package net.dogemines.framework.util;

import net.minecraft.network.protocol.game.ClientboundSetPlayerInventoryPacket;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class NMSUtil {
    /**
     * send a nms packet so the player's inventory isn't actually set on the server but visible to the client.
     * if clicked, the inventory would update though. (similar to a "ghost item")
     */
    public static void setPlayerInventory(Player player, int slot, Material material) {
        ClientboundSetPlayerInventoryPacket inventoryPacket = new ClientboundSetPlayerInventoryPacket(slot, net.minecraft.world.item.ItemStack.fromBukkitCopy(ItemStack.of(material)));
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        serverPlayer.connection.send(inventoryPacket);
    }
}
