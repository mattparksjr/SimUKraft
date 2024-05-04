package dev.simukraft.init;

import dev.simukraft.SimUKraft;
import dev.simukraft.items.DevStick;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SimUKraft.MOD_ID);
    public static final RegistryObject<Item> DEV_STICK = ITEMS.register("devstick", () -> new DevStick(new Item.Properties().tab(SimCreativeTab.instance)));
    private static final FoodProperties FOOD_PROPERTIES = new FoodProperties.Builder().saturationMod(0.6f).nutrition(6).build();
    public static final RegistryObject<Item> FRIES = ITEMS.register("fries", () -> new Item(new Item.Properties().food(FOOD_PROPERTIES).tab(SimCreativeTab.instance)));
    public static final RegistryObject<Item> CHEESE = ITEMS.register("cheese", () -> new Item(new Item.Properties().food(FOOD_PROPERTIES).tab(SimCreativeTab.instance)));
    public static final RegistryObject<Item> CHEESE_BURGER = ITEMS.register("cheese_burger", () -> new Item(new Item.Properties().food(FOOD_PROPERTIES).tab(SimCreativeTab.instance)));
    public static final RegistryObject<Item> BURGER = ITEMS.register("burger", () -> new Item(new Item.Properties().food(FOOD_PROPERTIES).tab(SimCreativeTab.instance)));

    public static class SimCreativeTab extends CreativeModeTab {

        public static final SimCreativeTab instance = new SimCreativeTab(CreativeModeTab.TABS.length, "Sim-U-Kraft");

        private SimCreativeTab(int index, String label) {
            super(index, label);
        }

        @Override
        public ItemStack makeIcon() {
            return new ItemStack(DEV_STICK.get());
        }
    }
}
