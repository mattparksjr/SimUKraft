package dev.simukraft.net.packet.ui;

import dev.simukraft.SimUKraft;
import dev.simukraft.entities.block.SimTileEntity;
import dev.simukraft.entities.folk.EntityFolk;
import dev.simukraft.net.ModPackets;
import dev.simukraft.net.packet.folk.SyncFolkDataS2CPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class FireScreenC2SPacket {

    private final BlockPos entityPos;

    public FireScreenC2SPacket(BlockPos pos) {
        this.entityPos = pos;
    }

    public FireScreenC2SPacket(FriendlyByteBuf buf) {
        entityPos = buf.readBlockPos();
    }


    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(entityPos);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            SimUKraft.LOGGER.debug(getClass().getName() + " - Got a packet, returning");
            ServerPlayer player = context.getSender();
            ServerLevel level = player.getLevel();
            SimTileEntity entity = (SimTileEntity) level.getBlockEntity(entityPos);

            if(entity != null && level.hasChunkAt(entityPos)) {
                for(UUID id : entity.getWorkers()) {
                    EntityFolk folk = (EntityFolk) level.getEntity(id);
                    ModPackets.sendToPlayer(new SyncFolkDataS2CPacket(id, folk.getFolkData()), player);
                    SimUKraft.LOGGER.debug(getClass().getName() + " - Sent data for ID: " + folk.getUUID());
                }
            }

            ModPackets.sendToPlayer(new OpenFireScreenS2CPacket(entityPos), player);
        });
    }
}
