package dev.simukraft.event;

import dev.simukraft.SimUKraft;
import dev.simukraft.entities.EntityFolk;
import dev.simukraft.init.ModEntities;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SimUKraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonEvents {

    @SubscribeEvent
    public static void entityAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.ENTITY_FOLK.get(), EntityFolk.getFolkAttributes().build());
    }
}
