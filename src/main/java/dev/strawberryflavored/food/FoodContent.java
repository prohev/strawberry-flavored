package dev.strawberryflavored.food;

import dev.strawberryflavored.food.item.FoodItems;
import dev.strawberryflavored.food.registry.FoodBlocks;
import dev.strawberryflavored.food.registry.FoodBlockEntities;
import dev.strawberryflavored.food.recipe.FoodProcessingRecipes;

/** Entry point for the food module.  Keeping this call in one place makes the
 * module easy to extend without growing the root initializer. */
public final class FoodContent {
    private FoodContent() {}

    public static void initialize() {
        FoodItems.initialize();
        FoodBlockEntities.initialize();
        FoodBlocks.MILLSTONE.toString();
        FoodProcessingRecipes.MILLSTONE.size();
        FoodProcessingRecipes.OVEN.size();
        FoodProcessingRecipes.POT.size();
        FoodProcessingRecipes.registerReload();
    }
}
