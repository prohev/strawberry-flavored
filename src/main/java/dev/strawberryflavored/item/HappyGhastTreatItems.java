package dev.strawberryflavored.item;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

public final class HappyGhastTreatItems {
    public static final Item HAPPY_GHAST_TREAT = Registry.register(
            BuiltInRegistries.ITEM,
            HappyGhastTreatItem.ID,
            new HappyGhastTreatItem(HappyGhastTreatItem.createProperties())
    );

    private HappyGhastTreatItems() {
    }

    public static void initialize() {
        // Loads the class and initializes the item.
    }
}
