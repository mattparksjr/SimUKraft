package dev.simukraft.event;

import dev.simukraft.SimUKraft;
import dev.simukraft.data.PlayerData;
import dev.simukraft.data.PlayerDataProvider;
import dev.simukraft.data.SimSavedData;
import dev.simukraft.data.group.SimGroup;
import dev.simukraft.data.pack.StructureReloadListener;
import dev.simukraft.data.pack.NameReloadListener;
import dev.simukraft.net.ModPackets;
import dev.simukraft.net.packet.GroupUpdateS2CPacket;
import dev.simukraft.net.packet.SyncCapUpdateS2CPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SimUKraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {

    @SubscribeEvent
    public static void onAttach(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            if (!event.getObject().getCapability(PlayerDataProvider.PLAYER_DATA).isPresent()) {
                event.addCapability(new ResourceLocation(SimUKraft.MOD_ID, "properties"), new PlayerDataProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            event.getOriginal().getCapability(PlayerDataProvider.PLAYER_DATA).ifPresent(oldStore -> {
                event.getOriginal().getCapability(PlayerDataProvider.PLAYER_DATA).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
        }
    }

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity().level.isClientSide) return;
        if (event.getEntity().level != event.getEntity().getServer().overworld()) return;

        ServerLevel level = (ServerLevel) event.getEntity().getLevel();
        // This is the start of the world data collection, all the world's global data is stored here.
        SimSavedData data = level.getDataStorage().computeIfAbsent(SimSavedData::load, SimSavedData::create, "simukraft");

        if (data.requiresSetup()) {
            // This is the case of when we first load the mod for a world
            SimUKraft.LOGGER.debug("Player Join Event - First time setup");
            SimGroup group = new SimGroup(event.getEntity().getName().getString() + "'s town", event.getEntity().getUUID());
            data.addGroup(group);
            data.setDirty();
            event.getEntity().getCapability(PlayerDataProvider.PLAYER_DATA).ifPresent(playerData -> {
                playerData.setGroupID(group.getUuid());
                playerData.setInGroup(true);
            });
            ModPackets.sendToPlayer(new SyncCapUpdateS2CPacket(group.getUuid(), true), (ServerPlayer) event.getEntity());
        } else {
            event.getEntity().getCapability(PlayerDataProvider.PLAYER_DATA).ifPresent(playerData -> {
                SimUKraft.LOGGER.debug("Player Join Event - Sending data");
                ModPackets.sendToPlayer(new SyncCapUpdateS2CPacket(playerData.getGroupID(), playerData.isInGroup()), (ServerPlayer) event.getEntity());

                // Send the player their current group data, used for overlay etc...
                if (playerData.isInGroup()) {
                    SimGroup group = data.getGroupByID(playerData.getGroupID());
                    ModPackets.sendToPlayer(new GroupUpdateS2CPacket(group.getName(), group.getMoney(), group.getNumSims()), (ServerPlayer) event.getEntity());
                }
            });
        }


    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PlayerData.class);
    }


    @SubscribeEvent
    public static void onReload(AddReloadListenerEvent event) {
        event.addListener(new StructureReloadListener());
        event.addListener(new NameReloadListener());
    }
}
