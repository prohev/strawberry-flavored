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
    public static final ResourceKey<Block> SOUL_PEPPER_KEY = FoodCropBlocks.SOUL_PEPPER_KEY;
    public static final Block SOUL_PEPPER_CROP = FoodCropBlocks.SOUL_PEPPER_CROP;
    public static final Item SOUL_PEPPER = FoodCropBlocks.SOUL_PEPPER;
    public static final ResourceKey<Block> LETTUCE_KEY = FoodCropBlocks.LETTUCE_KEY;
    public static final Block LETTUCE_CROP = FoodCropBlocks.LETTUCE_CROP;
    public static final Item LETTUCE_SEEDS = FoodCropBlocks.LETTUCE_SEEDS;
    public static final Item LETTUCE = FoodCropBlocks.LETTUCE;
    public static final Block WILD_SOUL_PEPPER = FoodCropBlocks.WILD_SOUL_PEPPER;
    public static final Block WILD_LETTUCE = FoodCropBlocks.WILD_LETTUCE;

    public static final Block MILLSTONE = FoodMachineBlocks.MILLSTONE;
    public static final Block OVEN = FoodMachineBlocks.OVEN;
    public static final Block COOKING_POT = FoodMachineBlocks.COOKING_POT;

    public static final ResourceKey<Block> STRAWBERRY_CAKE_KEY = FoodDecorativeBlocks.STRAWBERRY_CAKE_KEY;
    public static final Block STRAWBERRY_CAKE = FoodDecorativeBlocks.STRAWBERRY_CAKE;
    public static final Block CARROT_CAKE = FoodDecorativeBlocks.CARROT_CAKE;
    public static final Block PREMIUM_CAKE = FoodDecorativeBlocks.PREMIUM_CAKE;
    public static final Block COOKIE_CAKE = FoodDecorativeBlocks.COOKIE_CAKE;
    public static final Block PANCAKE = FoodDecorativeBlocks.PANCAKE;

    private FoodBlocks() {}

    /** Forces all food block categories to register in a predictable order. */
    public static void initialize() {
        FoodCropBlocks.initialize();
        FoodMachineBlocks.initialize();
        FoodDecorativeBlocks.initialize();
    }

}
