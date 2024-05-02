package dev.simukraft.init;

import dev.simukraft.SimUKraft;
import dev.simukraft.entities.block.ConstructorTileEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModTileEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, SimUKraft.MOD_ID);

    public static final RegistryObject<BlockEntityType<ConstructorTileEntity>> CONSTRUCTOR =
            BLOCK_ENTITIES.register("constructor", () -> BlockEntityType.Builder.of(ConstructorTileEntity::new, ModBlocks.CONSTRUCTOR_BLOCK.get()).build(null));
}