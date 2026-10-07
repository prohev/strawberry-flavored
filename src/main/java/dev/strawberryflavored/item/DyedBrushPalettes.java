package dev.strawberryflavored.item;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ColorCollection;

import java.util.IdentityHashMap;
import java.util.Map;

final class DyedBrushPalettes {
    private static final Map<Block, ColorCollection<Block>> FAMILIES = new IdentityHashMap<>();

    static {
        register(Blocks.WOOL);
        register(Blocks.CARPET);
        register(Blocks.BED);
        registerUndyed(Blocks.TERRACOTTA, Blocks.DYED_TERRACOTTA);
        register(Blocks.CONCRETE);
        register(Blocks.CONCRETE_POWDER);
        registerUndyed(Blocks.GLASS, Blocks.STAINED_GLASS);
        registerUndyed(Blocks.GLASS_PANE, Blocks.STAINED_GLASS_PANE);
        registerUndyed(Blocks.CANDLE, Blocks.DYED_CANDLE);
        registerUndyed(Blocks.CANDLE_CAKE, Blocks.DYED_CANDLE_CAKE);
        registerUndyed(Blocks.SHULKER_BOX, Blocks.DYED_SHULKER_BOX);
        register(Blocks.BANNER);
        register(Blocks.WALL_BANNER);
    }

    private DyedBrushPalettes() {
    }

    static Block recolor(Block block, DyeColor color) {
        ColorCollection<Block> family = FAMILIES.get(block);
        return family == null ? null : family.pick(color);
    }

    private static void register(ColorCollection<Block> colored) {
        colored.forEach(block -> FAMILIES.put(block, colored));
    }

    private static void registerUndyed(Block undyed, ColorCollection<Block> colored) {
        register(colored);
        FAMILIES.put(undyed, colored);
    }
}
