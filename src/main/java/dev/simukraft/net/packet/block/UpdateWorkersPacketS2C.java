package dev.simukraft.net.packet.block;

import dev.simukraft.SimUKraft;
import dev.simukraft.entities.block.ConstructorTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.UUID;
import java.util.function.Supplier;

public class UpdateWorkersPacketS2C {

    private BlockPos pos;

    private ArrayList<UUID> workers;

    public UpdateWorkersPacketS2C(BlockPos pos, ArrayList<UUID> workers) {
        this.pos = pos;
        this.workers = workers;
    }

    public UpdateWorkersPacketS2C(FriendlyByteBuf buf) {
        workers = new ArrayList<>();
        pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        int count = buf.readInt();

        for (int i = 0; i < count; i++) {
            workers.add(buf.readUUID());
        }
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
        buf.writeInt(workers.size());
        for (UUID id : workers) {
            buf.writeUUID(id);
        }
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            SimUKraft.LOGGER.debug("Got a packet to update workers!");

            if(!Minecraft.getInstance().level.hasChunkAt(pos)) {
                return;
            }

            ConstructorTileEntity entity = (ConstructorTileEntity) Minecraft.getInstance().level.getBlockEntity(pos);

            if(entity == null) {
                return;
            }

            entity.setWorkers(workers);
        });
        return true;
    }
}
