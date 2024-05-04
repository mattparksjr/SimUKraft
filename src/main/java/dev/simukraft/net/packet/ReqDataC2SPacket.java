package dev.simukraft.net.packet;

import dev.simukraft.SimUKraft;
import dev.simukraft.data.PlayerDataProvider;
import dev.simukraft.data.SimSavedData;
import dev.simukraft.data.group.SimGroup;
import dev.simukraft.net.ModPackets;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class ReqDataC2SPacket {

    private final UUID groupID;

    public ReqDataC2SPacket(UUID groupID) {
        this.groupID = groupID;
    }

    public ReqDataC2SPacket(FriendlyByteBuf buf) {
        this.groupID = buf.readUUID();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(groupID);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            ServerLevel level = player.getLevel();
            SimSavedData data = level.getDataStorage().computeIfAbsent(SimSavedData::load, SimSavedData::create, "simukraft");
            SimGroup group = data.getGroupByID(groupID);

            SimUKraft.LOGGER.debug("Request Data Packet - Got group info, replying...");
            if (group != null) {
                SimUKraft.LOGGER.debug("Request Data Packet - sending group info now");
                ModPackets.sendToPlayer(new GroupUpdateS2CPacket(group.getName(), group.getMoney(), group.getNumSims()), player);
            } else {
                SimUKraft.LOGGER.debug("Request Data Packet - failed to get group data replying...");
                ModPackets.sendToPlayer(new CannotFindGroupS2CPacket(), player);
                player.getCapability(PlayerDataProvider.PLAYER_DATA).ifPresent(playerData -> {
                    playerData.setInGroup(false);
                });
            }
        });
        return true;
    }
}
