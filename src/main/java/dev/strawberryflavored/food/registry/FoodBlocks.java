package dev.strawberryflavored.food.registry;

import dev.strawberryflavored.StrawberryFlavored;
import dev.strawberryflavored.food.block.FoodMachineBlock;
import dev.strawberryflavored.food.crop.CornCropBlock;
import dev.strawberryflavored.food.crop.CornTopBlock;
import dev.strawberryflavored.food.crop.BerryBushBlock;
import dev.strawberryflavored.food.crop.RiceCropBlock;
import dev.strawberryflavored.food.item.RiceItem;
import dev.strawberryflavored.food.item.FoodPolymerBlockItem;
import dev.strawberryflavored.item.Signature;
import eu.pb4.polymer.core.api.item.PolymerBlockItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public final class FoodBlocks {
    public static final ResourceKey<Block> CORN_KEY = ResourceKey.create(Registries.BLOCK, StrawberryFlavored.id("corn"));
    public static final Block CORN = Registry.register(BuiltInRegistries.BLOCK, CORN_KEY,
            new CornCropBlock(Block.Properties.ofFullCopy(Blocks.WHEAT).setId(CORN_KEY)));
    public static final Block CORN_TOP = Registry.register(BuiltInRegistries.BLOCK,
            ResourceKey.create(Registries.BLOCK, StrawberryFlavored.id("corn_top")),
            new CornTopBlock(Block.Properties.ofFullCopy(Blocks.TALL_GRASS).noCollision().setId(ResourceKey.create(Registries.BLOCK, StrawberryFlavored.id("corn_top")))));
    public static final ResourceKey<Block> RICE_KEY = ResourceKey.create(Registries.BLOCK, StrawberryFlavored.id("rice"));
    public static final Block RICE = Registry.register(BuiltInRegistries.BLOCK, RICE_KEY,
            new RiceCropBlock(Block.Properties.ofFullCopy(Blocks.WHEAT).setId(RICE_KEY)));
    public static final Block STRAWBERRY_BUSH = registerBerryBush("strawberry_bush", FoodBlocks::strawberryFruit);
    public static final Block BLUEBERRY_BUSH = registerBerryBush("blueberry_bush", FoodBlocks::blueberryFruit);
    public static final Item CORN_KERNELS = registerBlockItem("corn_kernels", CORN, Items.WHEAT_SEEDS);
    public static final Item STRAWBERRY = registerBerryItem("strawberry", STRAWBERRY_BUSH);
    public static final Item BLUEBERRY = registerBerryItem("blueberry", BLUEBERRY_BUSH);
    public static final Item RICE_ITEM = Registry.register(BuiltInRegistries.ITEM,
            ResourceKey.create(Registries.ITEM, StrawberryFlavored.id("rice")),
            new RiceItem(Signature.sign(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, StrawberryFlavored.id("rice"))))));
    public static final Block MILLSTONE = registerBlock("millstone", Blocks.STONE);
    // An unlit campfire is an interactable, familiar vanilla representation.
    public static final Block OVEN = registerBlock("oven", Blocks.CAMPFIRE, FoodMachineBlock.Station.OVEN);
    public static final Block POT = registerBlock("pot", Blocks.CAULDRON, dev.strawberryflavored.food.block.FoodMachineBlock.Station.POT);

    private FoodBlocks() {}

    private static Block registerBlock(String name, Block vanilla) {
        return registerBlock(name, vanilla, vanilla == Blocks.FURNACE ? dev.strawberryflavored.food.block.FoodMachineBlock.Station.OVEN : dev.strawberryflavored.food.block.FoodMachineBlock.Station.MILLSTONE);
    }

    private static Block registerBlock(String name, Block vanilla, dev.strawberryflavored.food.block.FoodMachineBlock.Station station) {
        ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, StrawberryFlavored.id(name));
        Block block = Registry.register(BuiltInRegistries.BLOCK, blockKey,
                new FoodMachineBlock(machineProperties(vanilla, blockKey), vanilla, station));
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, StrawberryFlavored.id(name));
        Registry.register(BuiltInRegistries.ITEM, itemKey,
                new PolymerBlockItem(block, Signature.sign(new Item.Properties().setId(itemKey)), vanillaItem(vanilla), true));
        return block;
    }

    private static Item vanillaItem(Block block) {
        return block == Blocks.CAMPFIRE ? Items.CAMPFIRE : Items.STONE;
    }

    private static Block.Properties machineProperties(Block vanilla, ResourceKey<Block> key) {
        // Furnace properties contain a lit-dependent light callback. The
        // server-safe machine deliberately has no custom lit property, so
        // replace that callback before constructing the block state definition.
        return Block.Properties.ofFullCopy(vanilla).lightLevel(state -> 0).setId(key);
    }

    private static Item registerBlockItem(String name, Block block, Item vanilla) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, StrawberryFlavored.id(name));
        return Registry.register(BuiltInRegistries.ITEM, key,
                new FoodPolymerBlockItem(block, Signature.sign(new Item.Properties().setId(key)), vanilla, name));
    }

    private static Block registerBerryBush(String name, java.util.function.Supplier<Item> fruit) {
        ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, StrawberryFlavored.id(name));
        return Registry.register(BuiltInRegistries.BLOCK, key,
                new BerryBushBlock(Block.Properties.ofFullCopy(Blocks.SWEET_BERRY_BUSH).setId(key), fruit, name.replace("_bush", "")));
    }

    private static Item strawberryFruit() { return STRAWBERRY; }
    private static Item blueberryFruit() { return BLUEBERRY; }

    private static Item registerBerryItem(String name, Block block) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, StrawberryFlavored.id(name));
        Item.Properties properties = Signature.sign(new Item.Properties().setId(key).food(
                new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).build()));
        return Registry.register(BuiltInRegistries.ITEM, key,
                new FoodPolymerBlockItem(block, properties, Items.SWEET_BERRIES, name));
    }
}
