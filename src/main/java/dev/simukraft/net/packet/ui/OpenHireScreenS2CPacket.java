package dev.simukraft.net.packet.ui;

import dev.simukraft.data.sided.ClientRuntime;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class OpenHireScreenS2CPacket {

    private final BlockPos entityPos;

    public OpenHireScreenS2CPacket(BlockPos entityPos) {
        this.entityPos = entityPos;
    }

    public OpenHireScreenS2CPacket(FriendlyByteBuf buf) {
        entityPos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(entityPos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientRuntime.openHireScreen(entityPos);
        });
        return true;
    }
}
