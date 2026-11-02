package dev.strawberryflavored.material;

import dev.strawberryflavored.StrawberryFlavored;
import eu.pb4.polymer.core.api.block.SimplePolymerBlock;
import eu.pb4.polymer.core.api.item.PolymerBlockItem;
import eu.pb4.polymer.core.api.item.SimplePolymerItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

/** Rose gold material content. Vanilla clients see gold fallbacks. */
public final class RoseGoldContent {
    public static final ResourceKey<Item> INGOT_KEY = key(Registries.ITEM, "rose_gold");
    public static final ResourceKey<Block> BLOCK_KEY = key(Registries.BLOCK, "rose_gold_block");

    public static final Item ROSE_GOLD = Registry.register(BuiltInRegistries.ITEM, INGOT_KEY,
            new RoseGoldItem(new Item.Properties().setId(INGOT_KEY)));
    public static final Block ROSE_GOLD_BLOCK = Registry.register(BuiltInRegistries.BLOCK, BLOCK_KEY,
            new SimplePolymerBlock(Block.Properties.ofFullCopy(Blocks.GOLD_BLOCK).setId(BLOCK_KEY), Blocks.GOLD_BLOCK));

    static {
        ResourceKey<Item> blockItemKey = key(Registries.ITEM, "rose_gold_block");
        Registry.register(BuiltInRegistries.ITEM, blockItemKey,
                new PolymerBlockItem(ROSE_GOLD_BLOCK, new Item.Properties().setId(blockItemKey), Items.GOLD_BLOCK, true));
    }

    private RoseGoldContent() {}

    private static <T> ResourceKey<T> key(net.minecraft.resources.ResourceKey<net.minecraft.core.Registry<T>> registry, String name) {
        return ResourceKey.create(registry, StrawberryFlavored.id(name));
    }

    public static void initialize() {}
}
