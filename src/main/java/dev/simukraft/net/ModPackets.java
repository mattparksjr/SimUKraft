package dev.simukraft.net;

import dev.simukraft.SimUKraft;
import dev.simukraft.net.packet.CannotFindGroupS2CPacket;
import dev.simukraft.net.packet.GroupUpdateS2CPacket;
import dev.simukraft.net.packet.ReqDataC2SPacket;
import dev.simukraft.net.packet.SyncCapUpdateS2CPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModPackets {

    private static final String PROTOCOL_VERSION = "1";
    private static int id = 0;

    private static SimpleChannel INSTANCE;

    public static void register() {
        SimpleChannel net = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(SimUKraft.MOD_ID, "main"),
                () -> PROTOCOL_VERSION,
                PROTOCOL_VERSION::equals,
                PROTOCOL_VERSION::equals
        );
        INSTANCE = net;

        net.messageBuilder(ReqDataC2SPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(ReqDataC2SPacket::new)
                .encoder(ReqDataC2SPacket::toBytes)
                .consumerMainThread(ReqDataC2SPacket::handle)
                .add();
        net.messageBuilder(GroupUpdateS2CPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(GroupUpdateS2CPacket::new)
                .encoder(GroupUpdateS2CPacket::toBytes)
                .consumerMainThread(GroupUpdateS2CPacket::handle)
                .add();
        net.messageBuilder(CannotFindGroupS2CPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(CannotFindGroupS2CPacket::new)
                .encoder(CannotFindGroupS2CPacket::toBytes)
                .consumerMainThread(CannotFindGroupS2CPacket::handle)
                .add();
        net.messageBuilder(SyncCapUpdateS2CPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncCapUpdateS2CPacket::new)
                .encoder(SyncCapUpdateS2CPacket::toBytes)
                .consumerMainThread(SyncCapUpdateS2CPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }


}
