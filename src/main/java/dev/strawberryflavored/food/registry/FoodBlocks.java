package dev.strawberryflavored.food.registry;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

/** Compatibility facade for the food block registries. */
public final class FoodBlocks {
    public static final ResourceKey<Block> CORN_KEY = FoodCropBlocks.CORN_KEY;
    public static final Block CORN = FoodCropBlocks.CORN;
    public static final ResourceKey<Block> RICE_KEY = FoodCropBlocks.RICE_KEY;
    public static final Block RICE = FoodCropBlocks.RICE;
    public static final Block RICE_PANICLES = FoodCropBlocks.RICE_PANICLES;
    public static final Block STRAWBERRY_BUSH = FoodCropBlocks.STRAWBERRY_BUSH;
    public static final Block BLUEBERRY_BUSH = FoodCropBlocks.BLUEBERRY_BUSH;
    public static final Item CORN_KERNELS = FoodCropBlocks.CORN_KERNELS;
    public static final Item STRAWBERRY = FoodCropBlocks.STRAWBERRY;
    public static final Item BLUEBERRY = FoodCropBlocks.BLUEBERRY;
    public static final Item RICE_ITEM = FoodCropBlocks.RICE_ITEM;

    public static final Block MILLSTONE = FoodMachineBlocks.MILLSTONE;
    public static final Block OVEN = FoodMachineBlocks.OVEN;
    public static final Block COOKING_POT = FoodMachineBlocks.COOKING_POT;

    public static final ResourceKey<Block> STRAWBERRY_CAKE_KEY = FoodDecorativeBlocks.STRAWBERRY_CAKE_KEY;
    public static final Block STRAWBERRY_CAKE = FoodDecorativeBlocks.STRAWBERRY_CAKE;

    private FoodBlocks() {}

    /** Forces all food block categories to register in a predictable order. */
    public static void initialize() {
        FoodCropBlocks.initialize();
        FoodMachineBlocks.initialize();
        FoodDecorativeBlocks.initialize();
    }

}
