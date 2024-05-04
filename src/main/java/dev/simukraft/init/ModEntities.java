package dev.simukraft.init;

import dev.simukraft.SimUKraft;
import dev.simukraft.entities.folk.EntityFolk;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, SimUKraft.MOD_ID);

    public static final RegistryObject<EntityType<EntityFolk>> ENTITY_FOLK = ENTITIES.register("entity_folk",
            () -> EntityType.Builder.of(EntityFolk::new, MobCategory.CREATURE).build(new ResourceLocation(SimUKraft.MOD_ID, "entity_folk").toString()));
}
