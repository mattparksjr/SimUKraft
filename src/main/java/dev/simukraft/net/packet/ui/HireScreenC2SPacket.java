package dev.simukraft.net.packet.ui;

import dev.simukraft.SimUKraft;
import dev.simukraft.data.SimSavedData;
import dev.simukraft.entities.block.SimTileEntity;
import dev.simukraft.entities.folk.EntityFolk;
import dev.simukraft.net.ModPackets;
import dev.simukraft.net.packet.folk.SyncFolkDataS2CPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class HireScreenC2SPacket {

    private final BlockPos entityPos;
    private final UUID groupID;


    public HireScreenC2SPacket(BlockPos pos, UUID groupID) {
        this.entityPos = pos;
        this.groupID = groupID;
    }

    public HireScreenC2SPacket(FriendlyByteBuf buf) {
        entityPos = buf.readBlockPos();
        groupID = buf.readUUID();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(entityPos);
        buf.writeUUID(groupID);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            SimUKraft.LOGGER.debug(getClass().getName() + " - Got a packet, returning");
            ServerPlayer player = context.getSender();
            ServerLevel level = player.getLevel();
            SimTileEntity entity = (SimTileEntity) level.getBlockEntity(entityPos);
            SimSavedData data = level.getDataStorage().computeIfAbsent(SimSavedData::load, SimSavedData::create, "simukraft");


            if (entity != null && level.hasChunkAt(entityPos)) {
                SimUKraft.LOGGER.debug(getClass().getName() + " - entity not null, and chunk exists");

                for (UUID id : data.getGroupByID(groupID).getSimIDS()) {
                    EntityFolk folk = (EntityFolk) level.getEntity(id);

                    if (folk == null) {
                        SimUKraft.LOGGER.error(getClass().getName() + " - Error, group contains invalid folk ids");
                        return;
                    }

                    ModPackets.sendToPlayer(new SyncFolkDataS2CPacket(id, folk.getFolkData()), player);
                    SimUKraft.LOGGER.debug(getClass().getName() + " - Sent data for ID: " + folk.getUUID());
                }
            }

            SimUKraft.LOGGER.debug(getClass().getName() + " - Sent all the folk data, telling client ready to open!");
            ModPackets.sendToPlayer(new OpenHireScreenS2CPacket(entityPos), player);
        });
    }
}
