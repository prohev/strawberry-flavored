package dev.strawberryflavored.food;

import dev.strawberryflavored.food.item.FoodItems;
import dev.strawberryflavored.food.registry.FoodBlocks;
import dev.strawberryflavored.processing.registry.FoodBlockEntities;
import dev.strawberryflavored.processing.recipe.FoodProcessingRecipes;
import dev.strawberryflavored.processing.polydex.CreativeTabs;
import dev.strawberryflavored.StrawberryFlavored;
import eu.pb4.factorytools.api.block.model.generic.BlockStateModelManager;

/** Entry point for the food module.  Keeping this call in one place makes the
 * module easy to extend without growing the root initializer. */
public final class FoodContent {
    private FoodContent() {}

    public static void initialize() {
        FoodItems.initialize();
        FoodBlockEntities.initialize();
        FoodBlocks.MILLSTONE.toString();
        BlockStateModelManager.addBlock(StrawberryFlavored.id("millstone"), FoodBlocks.MILLSTONE);
        BlockStateModelManager.addBlock(StrawberryFlavored.id("oven"), FoodBlocks.OVEN);
        BlockStateModelManager.addBlock(StrawberryFlavored.id("cooking_pot"), FoodBlocks.COOKING_POT);
        verifyFactoryModel("millstone", FoodBlocks.MILLSTONE);
        verifyFactoryModel("oven", FoodBlocks.OVEN);
        verifyFactoryModel("cooking_pot", FoodBlocks.COOKING_POT);
        CreativeTabs.initialize();
        FoodProcessingRecipes.MILLSTONE.size();
        FoodProcessingRecipes.OVEN.size();
        FoodProcessingRecipes.COOKING_POT.size();
        FoodProcessingRecipes.registerReload();
    }

    private static void verifyFactoryModel(String name, net.minecraft.world.level.block.Block block) {
        if (BlockStateModelManager.get(block.defaultBlockState()).isEmpty()) {
            throw new IllegalStateException("FactoryTools model was not registered for " + name);
        }
        StrawberryFlavored.LOGGER.info("{} FactoryTools model registered for {}", StrawberryFlavored.LOG_TAG, name);
    }
}
