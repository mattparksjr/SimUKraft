package dev.simukraft.net.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SimPacket {

    private NetworkDirection direction;


    public SimPacket(NetworkDirection direction) {
        this.direction = direction;
    }

    public SimPacket(FriendlyByteBuf buf) {

    }

   public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        return false;
    }

    public NetworkDirection getDirection() {
        return direction;
    }
}
