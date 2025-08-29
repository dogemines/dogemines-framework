package net.dogemines.framework.npc;

import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.jetbrains.annotations.NotNull;

public class FakeListener extends ServerGamePacketListenerImpl {

    public FakeListener(MinecraftServer server, Connection connection, ServerPlayer npc) {
        super(server, connection, npc, CommonListenerCookie.createInitial(npc.gameProfile, false));
    }

    //packets aren't actually sent
    //adding a fake listener that just ignores all packets simply makes the server happy.
    @Override
    public void send(@NotNull Packet<?> packet) {
    }
}
