package dev.simukraft.net.packet.folk;

import dev.simukraft.entities.folk.EntityFolk;
import dev.simukraft.net.ModPackets;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class RequestFolkDataC2SPacket {

    private UUID id;

    public RequestFolkDataC2SPacket(UUID id) {
        this.id = id;
    }

    public RequestFolkDataC2SPacket(FriendlyByteBuf buf) {
        buf.readUUID();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(id);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            ServerLevel level = player.getLevel();
            EntityFolk folk = (EntityFolk) level.getEntity(id);
            ModPackets.sendToPlayer(new SyncFolkDataS2CPacket(folk.getUUID(), folk.getFolkData()), player);
        });

    }
}
