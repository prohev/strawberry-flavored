package dev.strawberryflavored.food.registry;

import dev.strawberryflavored.StrawberryFlavored;
import dev.strawberryflavored.item.Signature;
import dev.strawberryflavored.processing.block.FoodMachineBlock;
import eu.pb4.polymer.core.api.item.PolymerBlockItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

/** Registers processing machines and coordinates their block items. */
public final class FoodMachineBlocks {
    public static final Block MILLSTONE = register("millstone", Blocks.BARRIER, Items.STONE, Blocks.STONE,
            FoodMachineBlock.Station.MILLSTONE);
    public static final Block OVEN = register("oven", Blocks.CAMPFIRE, Items.CAMPFIRE, Blocks.CAMPFIRE,
            FoodMachineBlock.Station.OVEN);
    public static final Block COOKING_POT = register("cooking_pot", Blocks.PLAYER_HEAD, Items.PLAYER_HEAD,
            Blocks.PLAYER_HEAD, FoodMachineBlock.Station.COOKING_POT);

    private FoodMachineBlocks() {}
    public static void initialize() {}

    private static Block register(String name, Block fallback, Item itemFallback, Block propertiesFrom,
                                  FoodMachineBlock.Station station) {
        ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, StrawberryFlavored.id(name));
        Block block = Registry.register(BuiltInRegistries.BLOCK, blockKey,
                station == FoodMachineBlock.Station.OVEN
                        ? new FoodMachineBlock.Oven(Block.Properties.ofFullCopy(propertiesFrom).lightLevel(state -> 15).setId(blockKey), fallback)
                        : new FoodMachineBlock(Block.Properties.ofFullCopy(propertiesFrom).setId(blockKey), fallback, station));
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, StrawberryFlavored.id(name));
        Registry.register(BuiltInRegistries.ITEM, itemKey,
                new PolymerBlockItem(block, Signature.sign(new Item.Properties().setId(itemKey)), itemFallback, true));
        return block;
    }
}
