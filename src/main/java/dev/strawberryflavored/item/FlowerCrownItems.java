package dev.strawberryflavored.item;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;

public final class FlowerCrownItems {
    public static final Item FLOWER_CROWN = Registry.register(
            BuiltInRegistries.ITEM,
            FlowerCrownItem.ID,
            new FlowerCrownItem(FlowerCrownItem.createProperties())
    );

    private FlowerCrownItems() {
    }

    public static void initialize() {
        // Loads the class and initializes the item.
    }
}
