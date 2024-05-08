package dev.simukraft;

import com.mojang.logging.LogUtils;
import dev.simukraft.client.ClientConfig;
import dev.simukraft.entities.folk.FolkDataSerializer;
import dev.simukraft.entities.folk.ai.FolkSchedule;
import dev.simukraft.init.*;
import dev.simukraft.net.ModPackets;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(SimUKraft.MOD_ID)
public class SimUKraft {

    public static final String MOD_ID = "simukraft";
    public static final Logger LOGGER = LogUtils.getLogger();

    public SimUKraft() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);

        ModTileEntities.BLOCK_ENTITIES.register(modEventBus);
        ModEntities.ENTITIES.register(modEventBus);
        FolkSchedule.FOLK_SCHEDULES.register(modEventBus);
        FolkDataSerializer.DATA_SERIALIZERS.register(modEventBus);

        ModSounds.register(modEventBus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC, MOD_ID + "-server.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC, MOD_ID + "-client.toml");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModPackets.register();
        });
    }
}
