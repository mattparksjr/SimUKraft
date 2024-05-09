package dev.simukraft.net.packet.ui;

import dev.simukraft.client.menu.employ.HireWorkerScreenType;
import dev.simukraft.data.sided.ClientRuntime;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class OpenHireScreenS2CPacket {

    private final BlockPos entityPos;
    private final HireWorkerScreenType type;


    public OpenHireScreenS2CPacket(BlockPos entityPos, HireWorkerScreenType type) {
        this.entityPos = entityPos;
        this.type = type;
    }

    public OpenHireScreenS2CPacket(FriendlyByteBuf buf) {
        entityPos = buf.readBlockPos();
        this.type = buf.readEnum(HireWorkerScreenType.class);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(entityPos);
        buf.writeEnum(type);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientRuntime.openHireScreen(entityPos, type);
        });
        return true;
    }
}
