package dev.strawberryflavored.world;

import dev.strawberryflavored.StrawberryFlavored;
import dev.strawberryflavored.food.registry.FoodBlocks;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import java.util.Set;

/** Adds the farm items to vanilla exploration loot without replacing vanilla tables. */
public final class FoodLootAndTradeIntegration {
    private static final Set<Identifier> CHEST_TABLES = Set.of(
            Identifier.withDefaultNamespace("chests/simple_dungeon"),
            Identifier.withDefaultNamespace("chests/village/village_plains_house"),
            Identifier.withDefaultNamespace("chests/village/village_desert_house"),
            Identifier.withDefaultNamespace("chests/village/village_savanna_house"),
            Identifier.withDefaultNamespace("chests/village/village_snowy_house"),
            Identifier.withDefaultNamespace("chests/village/village_taiga_house"),
            Identifier.withDefaultNamespace("chests/abandoned_camp_common_chest"));

    private FoodLootAndTradeIntegration() {}

    public static void register() {
        LootTableEvents.MODIFY.register((key, table, source, registries) -> {
            if (!CHEST_TABLES.contains(key.identifier())) return;
            table.withPool(pool(FoodBlocks.CORN_KERNELS));
            table.withPool(pool(FoodBlocks.RICE_ITEM));
            table.withPool(pool(FoodBlocks.STRAWBERRY));
            table.withPool(pool(FoodBlocks.BLUEBERRY));
        });
    }

    private static LootPool.Builder pool(net.minecraft.world.level.ItemLike item) {
        return LootPool.lootPool()
                .when(LootItemRandomChanceCondition.randomChance(0.25f))
                .add(LootItem.lootTableItem(item).setWeight(1));
    }
}
