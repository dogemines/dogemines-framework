package net.dogemines.framework.npc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerNPC extends ServerPlayer {
    private final BlockPos blockPos;

    private static final List<PlayerNPC> ACTIVE_NPCS = new ArrayList<>();

    private PlayerNPC(MinecraftServer server, ServerLevel level, GameProfile gameProfile, ClientInformation clientInformation, BlockPos blockPos) {
        super(server, level, gameProfile, clientInformation);
        this.blockPos = blockPos;

        this.setInvulnerable(true);
        this.setNoGravity(true);

        this.connection = new FakeListener(server, new net.minecraft.network.Connection(PacketFlow.CLIENTBOUND), this);

        sendToAllPlayers();
        ACTIVE_NPCS.add(this);
    }

    //should be used instead of constructor
    public static PlayerNPC create(Location blockPos, String name, PlayerSkin skin) {
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        ServerLevel nmsWorld = ((CraftWorld) blockPos.getWorld()).getHandle();

        GameProfile profile = new GameProfile(UUID.randomUUID(), name);
        if (skin != null) {
            profile.getProperties().put("textures", new Property("textures", skin.getValue(), skin.getSignature()));
        }

        return new PlayerNPC(server, nmsWorld, profile, ClientInformation.createDefault(), new BlockPos(blockPos.getBlockX(), blockPos.getBlockY(), blockPos.getBlockZ()));
    }


    public void sendToAllPlayers() {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            sendToPlayer(onlinePlayer);
        }
    }

    public void sendToPlayer(Player player) {
        ServerGamePacketListenerImpl connection = ((CraftPlayer) player).getHandle().connection;

        //add player packet
        connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, this));
        //add entity packet
        connection.send(new ClientboundAddEntityPacket(this, 0, blockPos));
    }

    public static void sendAllNPCs(Player player) {
        for (PlayerNPC npc : ACTIVE_NPCS) {
            npc.sendToPlayer(player);
        }
    }

}
