package dev.simukraft.net.packet.block;

import dev.simukraft.SimUKraft;
import dev.simukraft.entities.block.ConstructorTileEntity;
import dev.simukraft.net.ModPackets;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.UUID;
import java.util.function.Supplier;

public class SetTileDataC2SPacket {

    private ArrayList<UUID> workers;
    private BlockPos entityPos;
    private UUID player;

    public SetTileDataC2SPacket(BlockPos blockPos, ArrayList<UUID> selectedWorkers, UUID playerId) {
        entityPos = blockPos;
        workers = selectedWorkers;
        this.player = playerId;
    }


    public SetTileDataC2SPacket(FriendlyByteBuf buf) {
        SimUKraft.LOGGER.debug("SetTileData - Reading from buf");
        player = buf.readUUID();
        entityPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        int size = buf.readInt();
        workers = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            workers.add(buf.readUUID());
        }
        SimUKraft.LOGGER.debug("SetTileData - Done reading....");
    }


    public void toBytes(FriendlyByteBuf buf) {
        SimUKraft.LOGGER.debug("SetTileData - Writing to buf");
        buf.writeUUID(player);
        buf.writeInt(entityPos.getX());
        buf.writeInt(entityPos.getY());
        buf.writeInt(entityPos.getZ());
        buf.writeInt(workers.size());
        for (UUID id : workers) {
            buf.writeUUID(id);
        }
        SimUKraft.LOGGER.debug("SetTileData - Done writing....");
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            SimUKraft.LOGGER.debug("SetTileData - Got request from client.");

            ServerPlayer serverPlayer = context.getSender();
            ServerLevel level = serverPlayer.getLevel();
            ConstructorTileEntity entity = (ConstructorTileEntity) level.getBlockEntity(entityPos);

            entity.setChanged();
            entity.setWorkers(workers);

            ModPackets.sendToPlayer(new UpdateWorkersPacketS2C(entityPos, workers), serverPlayer);
        });
        return true;
    }
}
