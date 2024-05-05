package dev.simukraft.entities.folk;

import dev.simukraft.SimUKraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class FolkDataSerializer {

    public static final DeferredRegister<EntityDataSerializer<?>> DATA_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, SimUKraft.MOD_ID);

    public static final RegistryObject<EntityDataSerializer<FolkData>> FOLK_DATA = DATA_SERIALIZERS.register("folk_data_serializer", () -> new EntityDataSerializer<>() {
        public void write(@NotNull FriendlyByteBuf buf, @NotNull FolkData data1) {
            data1.write(buf);
        }

        public @NotNull FolkData read(@NotNull FriendlyByteBuf buf) {
            return FolkData.read(buf);
        }

        @Override
        public @NotNull FolkData copy(@NotNull FolkData pValue) {
            return FolkData.copyFrom(pValue);
        }
    });

}
