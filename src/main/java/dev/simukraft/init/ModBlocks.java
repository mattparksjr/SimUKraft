package dev.simukraft.init;

import dev.simukraft.SimUKraft;
import dev.simukraft.blocks.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SimUKraft.MOD_ID);

    public static final RegistryObject<Block> CONSTRUCTOR_BLOCK = registerBlock("constructor_block", ConstructorBlock::new, ModItems.SimCreativeTab.instance);
    public static final RegistryObject<Block> MINER_BLOCK = registerBlock("miner_block", MiningBlock::new, ModItems.SimCreativeTab.instance);
    public static final RegistryObject<Block> CONTROLLER_BLOCK = registerBlock("controller_block", ControllerBlock::new, ModItems.SimCreativeTab.instance);
    public static final RegistryObject<Block> MARKER_BLOCK = registerBlock("marker_block", MarkerBlock::new, ModItems.SimCreativeTab.instance);
    public static final RegistryObject<Block> FARMER_BLOCK = registerBlock("farmer_block", FarmingBlock::new, ModItems.SimCreativeTab.instance);

    public static final RegistryObject<Block> CHEESE_BLOCK = registerBlock("cheese_block", () ->
            new Block(BlockBehaviour.Properties.of(Material.CAKE).strength(0.1F, 0.5F)), ModItems.SimCreativeTab.instance);


    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab) {
        RegistryObject<T> ret = BLOCKS.register(name, block);
        registerBlockItem(name, ret, tab);
        return ret;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, CreativeModeTab tab) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }
}
