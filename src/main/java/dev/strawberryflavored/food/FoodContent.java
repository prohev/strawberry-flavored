package dev.strawberryflavored.food;

import dev.strawberryflavored.food.item.FoodItems;
import dev.strawberryflavored.food.item.MilkBottleInteraction;
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
        FoodBlocks.initialize();
        FoodItems.initialize();
        MilkBottleInteraction.register();
        FoodBlockEntities.initialize();
        FoodBlocks.MILLSTONE.toString();
        BlockStateModelManager.addBlock(StrawberryFlavored.id("millstone"), FoodBlocks.MILLSTONE);
        BlockStateModelManager.addBlock(StrawberryFlavored.id("oven"), FoodBlocks.OVEN);
        BlockStateModelManager.addBlock(StrawberryFlavored.id("cooking_pot"), FoodBlocks.COOKING_POT);
        BlockStateModelManager.addBlock(StrawberryFlavored.id("strawberry_cake"), FoodBlocks.STRAWBERRY_CAKE);
        BlockStateModelManager.addBlock(StrawberryFlavored.id("carrot_cake"), FoodBlocks.CARROT_CAKE);
        BlockStateModelManager.addBlock(StrawberryFlavored.id("premium_cake"), FoodBlocks.PREMIUM_CAKE);
        BlockStateModelManager.addBlock(StrawberryFlavored.id("cookie_cake"), FoodBlocks.COOKIE_CAKE);
        BlockStateModelManager.addBlock(StrawberryFlavored.id("pancake"), FoodBlocks.PANCAKE);
        verifyFactoryModel("millstone", FoodBlocks.MILLSTONE);
        verifyFactoryModel("oven", FoodBlocks.OVEN);
        verifyFactoryModel("cooking_pot", FoodBlocks.COOKING_POT);
        verifyFactoryModel("strawberry_cake", FoodBlocks.STRAWBERRY_CAKE);
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
