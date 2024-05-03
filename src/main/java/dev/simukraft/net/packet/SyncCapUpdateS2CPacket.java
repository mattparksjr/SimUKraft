package dev.simukraft.net.packet;

import dev.simukraft.SimUKraft;
import dev.simukraft.data.PlayerDataProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class SyncCapUpdateS2CPacket {

    private UUID groupID;

    private boolean inGroup;

    public SyncCapUpdateS2CPacket(UUID groupID, boolean inGroup) {
        this.groupID = groupID;
        this.inGroup = inGroup;
    }

    public SyncCapUpdateS2CPacket(FriendlyByteBuf buf) {
        this.groupID = buf.readUUID();
        this.inGroup = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(this.groupID);
        buf.writeBoolean(this.inGroup);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            SimUKraft.LOGGER.debug("Sync Cap - setting");
            Minecraft.getInstance().player.getCapability(PlayerDataProvider.PLAYER_DATA).ifPresent(data -> {
                SimUKraft.LOGGER.debug("Sync Cap - " + inGroup);
                SimUKraft.LOGGER.debug("Sync Cap - " + groupID);
                data.setInGroup(inGroup);
                data.setGroupID(groupID);
            });
        });
        return true;
    }
}
