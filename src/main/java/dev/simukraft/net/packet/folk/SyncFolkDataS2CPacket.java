package dev.simukraft.net.packet.folk;

import dev.simukraft.data.sided.ClientRuntime;
import dev.simukraft.entities.folk.FolkData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class SyncFolkDataS2CPacket {

    private final UUID id;
    private final FolkData data;

    public SyncFolkDataS2CPacket(UUID id, FolkData data) {
        this.id = id;
        this.data = data;
    }

    public SyncFolkDataS2CPacket(FriendlyByteBuf buf) {
        id = buf.readUUID();
        data = FolkData.read(buf);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(id);
        data.write(buf);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientRuntime.addDataToMap(id, data);
        });
    }
}
