package dev.simukraft.event;

import dev.simukraft.SimUKraft;
import dev.simukraft.client.renderer.EntityFolkRenderer;
import dev.simukraft.data.PlayerDataProvider;
import dev.simukraft.data.sided.ClientRuntime;
import dev.simukraft.init.ModEntities;
import dev.simukraft.net.ModPackets;
import dev.simukraft.net.packet.ReqDataC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ClientEvents {

    @Mod.EventBusSubscriber(modid = SimUKraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void renderOverlay(RenderGuiOverlayEvent event) {
            Minecraft.getInstance().player.getCapability(PlayerDataProvider.PLAYER_DATA).ifPresent(data -> {
                if (data.isInGroup()) {
                    if (!ClientRuntime.hasInfo()) {
                        if ((ClientRuntime.getLastRequestAt() + 1000 <= System.currentTimeMillis()) && ClientRuntime.isAllowRequest()) {
                            SimUKraft.LOGGER.debug("Render Overlay - Requesting info after timeout");
                            ModPackets.sendToServer(new ReqDataC2SPacket(data.getGroupID()));
                            ClientRuntime.setLastRequestAt(System.currentTimeMillis());
                        }
                    } else {
                        ClientRuntime.setAllowRequest(false);
                        Minecraft.getInstance().font.draw(event.getPoseStack(), ClientRuntime.getName() + "(sun) - Population: " + ClientRuntime.getSims() + " Sim-U-Credits: " + ClientRuntime.getMoney(), 2, 2, 0xFFFFFF);
                    }
                }
            });
        }
    }

    @Mod.EventBusSubscriber(modid = SimUKraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void entityRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(ModEntities.ENTITY_FOLK.get(), EntityFolkRenderer::new);
        }
    }
}
