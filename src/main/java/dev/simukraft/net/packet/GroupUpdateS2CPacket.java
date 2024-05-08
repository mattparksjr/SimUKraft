package dev.simukraft.net.packet;

import dev.simukraft.SimUKraft;
import dev.simukraft.data.sided.ClientRuntime;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class GroupUpdateS2CPacket {

    private final double money;
    private final String name;
    private final int numSims;
    private final UUID groupID;


    public GroupUpdateS2CPacket(String name, double money, int sims, UUID groupID) {
        this.name = name;
        this.money = money;
        this.numSims = sims;
        this.groupID = groupID;
    }

    public GroupUpdateS2CPacket(FriendlyByteBuf buf) {
        this.name = buf.readUtf();
        this.money = buf.readDouble();
        this.numSims = buf.readInt();
        this.groupID = buf.readUUID();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(name);
        buf.writeDouble(money);
        buf.writeInt(numSims);
        buf.writeUUID(groupID);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            SimUKraft.LOGGER.debug("GroupUpdatePacket - Got info, setting...");
            // CLIENT SIDE!!!!!!
            ClientRuntime.setHasInfo(true);
            ClientRuntime.setName(name);
            ClientRuntime.setMoney(money);
            ClientRuntime.setSims(numSims);
            ClientRuntime.setGroupID(groupID);
        });
        return true;
    }
}
