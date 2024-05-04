package dev.simukraft.net.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CannotFindGroupS2CPacket {

    public CannotFindGroupS2CPacket() {
    }

    public CannotFindGroupS2CPacket(FriendlyByteBuf buf) {
    }

    public void toBytes(FriendlyByteBuf buf) {
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Minecraft.getInstance().level.playSound(context.getSender(), context.getSender().blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.MASTER, 1.0F, 1.0F);
        });
        return true;
    }
}
