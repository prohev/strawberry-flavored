package dev.strawberryflavored.food.registry;

import dev.strawberryflavored.StrawberryFlavored;
import dev.strawberryflavored.food.block.StrawberryCakeBlock;
import dev.strawberryflavored.food.item.FoodPolymerBlockItem;
import dev.strawberryflavored.item.Signature;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

/** Registers decorative food blocks and their block items. */
public final class FoodDecorativeBlocks {
    public static final String STRAWBERRY_CAKE_ID = "strawberry_cake";
    public static final ResourceKey<Block> STRAWBERRY_CAKE_KEY =
            ResourceKey.create(Registries.BLOCK, StrawberryFlavored.id(STRAWBERRY_CAKE_ID));
    public static final Block STRAWBERRY_CAKE = Registry.register(BuiltInRegistries.BLOCK, STRAWBERRY_CAKE_KEY,
            new StrawberryCakeBlock(Block.Properties.ofFullCopy(Blocks.CAKE).setId(STRAWBERRY_CAKE_KEY), "strawberry_cake_variant"));
    public static final Block CARROT_CAKE = registerCake("carrot_cake");
    public static final Block PREMIUM_CAKE = registerCake("premium_cake");
    public static final Block COOKIE_CAKE = registerCake("cookie_cake");
    public static final Block PANCAKE = registerCake("pancake");

    private static Block registerCake(String id) {
        ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, StrawberryFlavored.id(id));
        Block block = Registry.register(BuiltInRegistries.BLOCK, key,
                new StrawberryCakeBlock(Block.Properties.ofFullCopy(Blocks.CAKE).setId(key), id));
        ResourceKey<net.minecraft.world.item.Item> itemKey = ResourceKey.create(Registries.ITEM, StrawberryFlavored.id(id));
        Registry.register(BuiltInRegistries.ITEM, itemKey,
                new FoodPolymerBlockItem(block, Signature.sign(new net.minecraft.world.item.Item.Properties().setId(itemKey)), Items.CAKE, id));
        return block;
    }

    static {
        ResourceKey<net.minecraft.world.item.Item> itemKey =
                ResourceKey.create(Registries.ITEM, StrawberryFlavored.id(STRAWBERRY_CAKE_ID));
        Registry.register(BuiltInRegistries.ITEM, itemKey,
                new FoodPolymerBlockItem(STRAWBERRY_CAKE,
                        Signature.sign(new net.minecraft.world.item.Item.Properties().setId(itemKey)),
                        Items.CAKE, STRAWBERRY_CAKE_ID));
    }

    private FoodDecorativeBlocks() {}
    public static void initialize() {}
}
