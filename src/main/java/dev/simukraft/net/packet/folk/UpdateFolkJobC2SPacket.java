package dev.simukraft.net.packet.folk;

import dev.simukraft.SimUKraft;
import dev.simukraft.data.SimSavedData;
import dev.simukraft.entities.folk.EntityFolk;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class UpdateFolkJobC2SPacket {

    private final UUID folkID;
    private final UUID groupID;
    private final BlockPos pos;

    public UpdateFolkJobC2SPacket(UUID groupID, UUID id, BlockPos pos) {
        this.folkID = id;
        this.groupID = groupID;
        this.pos = pos;
    }

    public UpdateFolkJobC2SPacket(FriendlyByteBuf buf) {
        folkID = buf.readUUID();
        groupID = buf.readUUID();
        pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(folkID);
        buf.writeUUID(groupID);
        buf.writeBlockPos(pos);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            ServerLevel level = player.getLevel();
            SimSavedData data = level.getDataStorage().computeIfAbsent(SimSavedData::load, SimSavedData::create, "simukraft");
            EntityFolk folk = (EntityFolk) level.getEntity(folkID);

            if(pos.getY() <= -999) {
                folk.setFired();
                return;
            }

            if(!level.hasChunkAt(pos)) return;
            if(!data.getGroupByID(groupID).getPlayerIDS().contains(player.getUUID())) return;


            folk.getFolkData().setJobSite(pos);
            SimUKraft.LOGGER.debug(getClass().getName() + " - Updated a folks job.");
        });
    }
}
