package dev.simukraft.net.packet.ui;

import dev.simukraft.SimUKraft;
import dev.simukraft.data.sided.ClientRuntime;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class OpenFireScreenS2CPacket {

    private final BlockPos entityPos;

    public OpenFireScreenS2CPacket(BlockPos entityPos) {
        this.entityPos = entityPos;
    }

    public OpenFireScreenS2CPacket(FriendlyByteBuf buf) {
        entityPos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(entityPos);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            SimUKraft.LOGGER.debug(getClass().getName() + " - Got request from server to open a fire screen at {}", entityPos.toShortString());
            ClientRuntime.openFireScreen(entityPos);
        });
    }
}
