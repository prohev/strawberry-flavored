package dev.strawberryflavored.item;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;

import java.util.EnumMap;
import java.util.Map;

public final class DyedBrushItems {
    public static final Map<DyeColor, Item> BRUSHES = new EnumMap<>(DyeColor.class);

    static {
        for (DyeColor color : DyeColor.values()) {
            BRUSHES.put(color, Registry.register(
                    BuiltInRegistries.ITEM,
                    DyedBrushItem.id(color),
                    new DyedBrushItem(color, DyedBrushItem.createProperties(color))
            ));
        }
    }

    private DyedBrushItems() {
    }

    public static void initialize() {
        // Loads the class and initializes the items.
    }
}
