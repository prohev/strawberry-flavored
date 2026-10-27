package dev.strawberryflavored.world;

import dev.strawberryflavored.StrawberryFlavored;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

/** Adds the food crops to a small, explicit set of vanilla biomes. */
public final class FoodWorldGeneration {
    private static final ResourceKey<PlacedFeature> WILD_SOUL_PEPPER = key("wild_soul_pepper");
    private static final ResourceKey<PlacedFeature> WILD_LETTUCE = key("wild_lettuce");

    private FoodWorldGeneration() {}

    public static void register() {
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(Biomes.SOUL_SAND_VALLEY),
                GenerationStep.Decoration.VEGETAL_DECORATION, WILD_SOUL_PEPPER);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(
                        Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS, Biomes.FOREST, Biomes.FLOWER_FOREST),
                GenerationStep.Decoration.VEGETAL_DECORATION, WILD_LETTUCE);
    }

    private static ResourceKey<PlacedFeature> key(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, StrawberryFlavored.id(name));
    }
}
