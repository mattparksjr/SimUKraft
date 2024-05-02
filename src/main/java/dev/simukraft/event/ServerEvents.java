package dev.simukraft.event;

import dev.simukraft.SimUKraft;
import dev.simukraft.data.SimSavedData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ServerEvents {

    public static class ForgeServerEvents {

        @SubscribeEvent
        public static void onJoin(EntityJoinLevelEvent event) {
            if(!(event.getEntity() instanceof Player)) return;
            if(event.getLevel().isClientSide) return;
            System.out.println("ENTITY JOIN LEVEL EVENT");
        }

        @SubscribeEvent
        public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
            if(event.getEntity().level.isClientSide) return;
            ServerLevel level = (ServerLevel) event.getEntity().getLevel();
            level.getDataStorage().computeIfAbsent(SimSavedData::load, SimSavedData::create, "simukraft");
        }
    }
}
