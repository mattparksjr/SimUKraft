package dev.simukraft.init;

import dev.simukraft.SimUKraft;
import dev.simukraft.entities.block.ConstructorTileEntity;
import dev.simukraft.entities.block.ControllerTileEntity;
import dev.simukraft.entities.block.FarmerTileEntity;
import dev.simukraft.entities.block.MinerTileEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModTileEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, SimUKraft.MOD_ID);

    public static final RegistryObject<BlockEntityType<ConstructorTileEntity>> CONSTRUCTOR =
            BLOCK_ENTITIES.register("constructor", () -> BlockEntityType.Builder.of(ConstructorTileEntity::new, ModBlocks.CONSTRUCTOR_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<ControllerTileEntity>> CONTROLLER =
            BLOCK_ENTITIES.register("controller", () -> BlockEntityType.Builder.of(ControllerTileEntity::new, ModBlocks.CONTROLLER_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<FarmerTileEntity>> FARMER =
            BLOCK_ENTITIES.register("farmer", () -> BlockEntityType.Builder.of(FarmerTileEntity::new, ModBlocks.FARMER_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<MinerTileEntity>> MINER =
            BLOCK_ENTITIES.register("miner", () -> BlockEntityType.Builder.of(MinerTileEntity::new, ModBlocks.MINER_BLOCK.get()).build(null));
}