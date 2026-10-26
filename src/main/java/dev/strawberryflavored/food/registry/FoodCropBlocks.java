package dev.strawberryflavored.food.registry;

import dev.strawberryflavored.StrawberryFlavored;
import dev.strawberryflavored.food.crop.BerryBushBlock;
import dev.strawberryflavored.food.crop.CornCropBlock;
import dev.strawberryflavored.food.crop.RiceCropBlock;
import dev.strawberryflavored.food.crop.RicePaniclesBlock;
import dev.strawberryflavored.food.item.FoodPolymerBlockItem;
import dev.strawberryflavored.food.item.RiceItem;
import dev.strawberryflavored.item.Signature;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

/** Registers crops, bushes, and their corresponding items. */
public final class FoodCropBlocks {
    public static final ResourceKey<Block> CORN_KEY = key("corn");
    public static final Block CORN = registerBlock(CORN_KEY,
            new CornCropBlock(Block.Properties.ofFullCopy(Blocks.WHEAT).setId(CORN_KEY)));
    public static final ResourceKey<Block> RICE_KEY = key("rice");
    public static final Block RICE = registerBlock(RICE_KEY,
            new RiceCropBlock(Block.Properties.ofFullCopy(Blocks.TALL_GRASS).randomTicks().setId(RICE_KEY)));
    public static final Block RICE_PANICLES = registerBlock(key("rice_panicles"),
            new RicePaniclesBlock(Block.Properties.ofFullCopy(Blocks.TALL_GRASS).noCollision().randomTicks()
                    .setId(key("rice_panicles"))));
    public static final Block STRAWBERRY_BUSH = registerBerryBush("strawberry_bush", "strawberry");
    public static final Block BLUEBERRY_BUSH = registerBerryBush("blueberry_bush", "blueberry");
    public static final Item CORN_KERNELS = registerBlockItem("corn_kernels", CORN, Items.WHEAT_SEEDS);
    public static final Item STRAWBERRY = registerBerryItem("strawberry", STRAWBERRY_BUSH);
    public static final Item BLUEBERRY = registerBerryItem("blueberry", BLUEBERRY_BUSH);
    public static final Item RICE_ITEM = Registry.register(BuiltInRegistries.ITEM, itemKey("rice"),
            new RiceItem(Signature.sign(new Item.Properties().setId(itemKey("rice")))));

    private FoodCropBlocks() {}
    public static void initialize() {}

    private static ResourceKey<Block> key(String name) {
        return ResourceKey.create(Registries.BLOCK, StrawberryFlavored.id(name));
    }
    private static ResourceKey<Item> itemKey(String name) {
        return ResourceKey.create(Registries.ITEM, StrawberryFlavored.id(name));
    }
    private static Block registerBlock(ResourceKey<Block> key, Block block) {
        return Registry.register(BuiltInRegistries.BLOCK, key, block);
    }
    private static Block registerBerryBush(String name, String fruit) {
        ResourceKey<Block> blockKey = key(name);
        return registerBlock(blockKey, new BerryBushBlock(
                Block.Properties.ofFullCopy(Blocks.SWEET_BERRY_BUSH).setId(blockKey),
                () -> fruit.equals("strawberry") ? STRAWBERRY : BLUEBERRY, fruit));
    }
    private static Item registerBlockItem(String name, Block block, Item fallback) {
        ResourceKey<Item> key = itemKey(name);
        return Registry.register(BuiltInRegistries.ITEM, key,
                new FoodPolymerBlockItem(block, Signature.sign(new Item.Properties().setId(key)), fallback, name));
    }
    private static Item registerBerryItem(String name, Block block) {
        ResourceKey<Item> key = itemKey(name);
        return Registry.register(BuiltInRegistries.ITEM, key,
                new FoodPolymerBlockItem(block, Signature.sign(new Item.Properties().setId(key)), Items.SWEET_BERRIES, name));
    }
}
