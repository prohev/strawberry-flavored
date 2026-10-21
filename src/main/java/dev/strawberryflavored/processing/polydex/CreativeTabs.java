package dev.strawberryflavored.processing.polydex;

import dev.strawberryflavored.StrawberryFlavored;
import dev.strawberryflavored.food.item.FoodItems;
import dev.strawberryflavored.food.registry.FoodBlocks;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/** Registers the food and general Strawberry Flavored creative tabs. */
public final class CreativeTabs {
    private static final ResourceKey<CreativeModeTab> FOOD_KEY = ResourceKey.create(
            Registries.CREATIVE_MODE_TAB, StrawberryFlavored.id("food"));
    private static final ResourceKey<CreativeModeTab> GENERAL_KEY = ResourceKey.create(
            Registries.CREATIVE_MODE_TAB, StrawberryFlavored.id("general"));

    private CreativeTabs() {}

    public static void initialize() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, FOOD_KEY,
                CreativeModeTab.builder(CreativeModeTab.Row.TOP, 7)
                        .title(Component.translatable("itemGroup.strawberry-flavored.food"))
                        .icon(() -> new ItemStack(Items.SWEET_BERRIES))
                        .displayItems((parameters, output) -> addFoodItems(output))
                        .build());

        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, GENERAL_KEY,
                CreativeModeTab.builder(CreativeModeTab.Row.TOP, 8)
                        .title(Component.translatable("itemGroup.strawberry-flavored.general"))
                        .icon(() -> new ItemStack(Items.KNOWLEDGE_BOOK))
                        .displayItems((parameters, output) -> BuiltInRegistries.ITEM.forEach(item -> {
                            if (BuiltInRegistries.ITEM.getKey(item).getNamespace().equals(StrawberryFlavored.MOD_ID)
                                    && !isFoodItem(item)) output.accept(item);
                        }))
                        .build());
    }

    private static void addFoodItems(CreativeModeTab.Output output) {
        output.accept(FoodItems.STRAWBERRY);
        output.accept(FoodItems.BLUEBERRY);
        output.accept(FoodItems.COOKED_RICE);
        output.accept(FoodItems.ONIGIRI);
        output.accept(FoodItems.FLOUR);
        output.accept(FoodItems.DOUGH);
        output.accept(FoodItems.STRAWBERRY_JAM);
        output.accept(FoodItems.BLUEBERRY_JAM);
        output.accept(FoodItems.SWEET_BERRY_JAM);
        output.accept(FoodItems.APPLE_JAM);
        output.accept(FoodItems.BAKED_APPLE);
        output.accept(FoodItems.GOLDEN_STRAWBERRY);
        output.accept(FoodItems.APPLE_PIE);
        output.accept(FoodItems.BLUEBERRY_PIE);
        output.accept(FoodItems.STRAWBERRY_PIE);
        output.accept(FoodItems.STRAWBERRY_CAKE);
        output.accept(FoodItems.CORN);
        output.accept(FoodItems.MASA);
        output.accept(FoodItems.TORTILLA);
        output.accept(FoodItems.PORK_TACO);
        output.accept(FoodItems.STEAK_TACO);
        output.accept(FoodItems.APPLE_FRITTER);
        output.accept(FoodItems.FRIED_EGG);
        output.accept(FoodBlocks.CORN_KERNELS);
        output.accept(FoodBlocks.RICE_ITEM);
    }

    private static boolean isFoodItem(Item item) {
        return item == FoodItems.STRAWBERRY || item == FoodItems.BLUEBERRY
                || item == FoodItems.COOKED_RICE || item == FoodItems.ONIGIRI
                || item == FoodItems.FLOUR || item == FoodItems.DOUGH
                || item == FoodItems.STRAWBERRY_JAM || item == FoodItems.BLUEBERRY_JAM
                || item == FoodItems.SWEET_BERRY_JAM || item == FoodItems.APPLE_JAM
                || item == FoodItems.BAKED_APPLE || item == FoodItems.GOLDEN_STRAWBERRY
                || item == FoodItems.APPLE_PIE || item == FoodItems.BLUEBERRY_PIE
                || item == FoodItems.STRAWBERRY_PIE || item == FoodItems.STRAWBERRY_CAKE
                || item == FoodItems.CORN || item == FoodItems.MASA || item == FoodItems.TORTILLA
                || item == FoodItems.PORK_TACO || item == FoodItems.STEAK_TACO
                || item == FoodItems.APPLE_FRITTER || item == FoodItems.FRIED_EGG
                || item == FoodBlocks.CORN_KERNELS || item == FoodBlocks.RICE_ITEM;
    }
}
