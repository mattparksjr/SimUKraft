package dev.simukraft.net;

import dev.simukraft.SimUKraft;
import dev.simukraft.net.packet.*;
import dev.simukraft.net.packet.block.SetTileDataC2SPacket;
import dev.simukraft.net.packet.block.UpdateWorkersPacketS2C;
import dev.simukraft.net.packet.folk.RequestFolkDataC2SPacket;
import dev.simukraft.net.packet.folk.SyncFolkDataS2CPacket;
import dev.simukraft.net.packet.folk.UpdateFolkJobC2SPacket;
import dev.simukraft.net.packet.ui.FireScreenC2SPacket;
import dev.simukraft.net.packet.ui.HireScreenC2SPacket;
import dev.simukraft.net.packet.ui.OpenFireScreenS2CPacket;
import dev.simukraft.net.packet.ui.OpenHireScreenS2CPacket;
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
        net.messageBuilder(SetTileDataC2SPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(SetTileDataC2SPacket::new)
                .encoder(SetTileDataC2SPacket::toBytes)
                .consumerMainThread(SetTileDataC2SPacket::handle)
                .add();
        net.messageBuilder(UpdateWorkersPacketS2C.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(UpdateWorkersPacketS2C::new)
                .encoder(UpdateWorkersPacketS2C::toBytes)
                .consumerMainThread(UpdateWorkersPacketS2C::handle)
                .add();
        net.messageBuilder(RequestFolkDataC2SPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(RequestFolkDataC2SPacket::new)
                .encoder(RequestFolkDataC2SPacket::toBytes)
                .consumerMainThread(RequestFolkDataC2SPacket::handle)
                .add();
        net.messageBuilder(SyncFolkDataS2CPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncFolkDataS2CPacket::new)
                .encoder(SyncFolkDataS2CPacket::toBytes)
                .consumerMainThread(SyncFolkDataS2CPacket::handle)
                .add();
        net.messageBuilder(FireScreenC2SPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(FireScreenC2SPacket::new)
                .encoder(FireScreenC2SPacket::toBytes)
                .consumerMainThread(FireScreenC2SPacket::handle)
                .add();
        net.messageBuilder(OpenFireScreenS2CPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(OpenFireScreenS2CPacket::new)
                .encoder(OpenFireScreenS2CPacket::toBytes)
                .consumerMainThread(OpenFireScreenS2CPacket::handle)
                .add();
        net.messageBuilder(UpdateFolkJobC2SPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(UpdateFolkJobC2SPacket::new)
                .encoder(UpdateFolkJobC2SPacket::toBytes)
                .consumerMainThread(UpdateFolkJobC2SPacket::handle)
                .add();
        net.messageBuilder(HireScreenC2SPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(HireScreenC2SPacket::new)
                .encoder(HireScreenC2SPacket::toBytes)
                .consumerMainThread(HireScreenC2SPacket::handle)
                .add();
        net.messageBuilder(OpenHireScreenS2CPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(OpenHireScreenS2CPacket::new)
                .encoder(OpenHireScreenS2CPacket::toBytes)
                .consumerMainThread(OpenHireScreenS2CPacket::handle)
                .add();
    }

    private void register(SimPacket packet) {

    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToAll(MSG msg, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> player), msg);
    }

}
