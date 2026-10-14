package dev.strawberryflavored.food.recipe;

import dev.strawberryflavored.food.item.FoodItems;
import dev.strawberryflavored.food.registry.FoodBlocks;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.strawberryflavored.StrawberryFlavored;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleResourceReloadListener;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import java.util.List;

/** Central recipe table used by the future machine block entities and tests. */
public final class FoodProcessingRecipes {
    public static volatile List<ProcessingRecipe> MILLSTONE = List.of(
            new ProcessingRecipe(Items.WHEAT, FoodItems.FLOUR, 80),
            new ProcessingRecipe(FoodItems.CORN, FoodItems.MASA, 80)
    );

    public static volatile List<ProcessingRecipe> OVEN = List.of(
            new ProcessingRecipe(FoodItems.DOUGH, Items.BREAD, 200),
            new ProcessingRecipe(FoodBlocks.RICE_ITEM, FoodItems.COOKED_RICE, 100),
            new ProcessingRecipe(Items.APPLE, FoodItems.BAKED_APPLE, 160),
            new ProcessingRecipe(FoodItems.MASA, FoodItems.TORTILLA, 100),
            new ProcessingRecipe(Items.EGG, FoodItems.FRIED_EGG, 120)
    );
    public static volatile List<ProcessingRecipe> POT = List.of(
            new ProcessingRecipe(FoodBlocks.RICE_ITEM, FoodItems.COOKED_RICE, 100)
    );

    public static List<ProcessingRecipe> forStation(dev.strawberryflavored.food.block.FoodMachineBlock.Station station) {
        return switch (station) { case OVEN -> OVEN; case MILLSTONE -> MILLSTONE; case POT -> POT; };
    }

    private FoodProcessingRecipes() {}

    public static void registerReload() {
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new SimpleResourceReloadListener<List<ProcessingRecipe>>() {
            @Override
            public Identifier getFabricId() {
                return StrawberryFlavored.id("processing_recipes");
            }

            @Override
            public CompletableFuture<List<ProcessingRecipe>> load(ResourceManager manager, Executor executor) {
                return CompletableFuture.completedFuture(loadDatapackRecipes(manager));
            }

            @Override
            public CompletableFuture<Void> apply(List<ProcessingRecipe> recipes, ResourceManager manager, Executor executor) {
                return CompletableFuture.runAsync(() -> applyLoaded(recipes), executor);
            }
        });
    }

    private static List<ProcessingRecipe> loadDatapackRecipes(ResourceManager manager) {
        List<ProcessingRecipe> all = new ArrayList<>();
        Map<Identifier, Resource> resources = manager.listResources(
                "recipes/" + StrawberryFlavored.MOD_ID + "/processing", id -> id.getPath().endsWith(".json"));
        resources.forEach((id, resource) -> {
            try (var reader = resource.openAsReader()) {
                JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
                all.add(fromJson(json));
            } catch (IOException | RuntimeException exception) {
                StrawberryFlavored.LOGGER.error("Could not load processing recipe {}", id, exception);
            }
        });
        return all;
    }

    private static void applyLoaded(List<ProcessingRecipe> all) {
        List<ProcessingRecipe> oven = new ArrayList<>(), pot = new ArrayList<>(), millstone = new ArrayList<>();
        for (ProcessingRecipe recipe : all) switch (recipe.station()) {
            case "oven" -> oven.add(recipe);
            case "pot" -> pot.add(recipe);
            case "millstone" -> millstone.add(recipe);
            default -> StrawberryFlavored.LOGGER.warn("Unknown processing station {}", recipe.station());
        }
        if (!oven.isEmpty()) OVEN = List.copyOf(oven);
        if (!pot.isEmpty()) POT = List.copyOf(pot);
        if (!millstone.isEmpty()) MILLSTONE = List.copyOf(millstone);
        StrawberryFlavored.LOGGER.info("Loaded {} oven, {} pot, and {} millstone processing recipes", OVEN.size(), POT.size(), MILLSTONE.size());
    }

    private static ProcessingRecipe fromJson(JsonObject json) {
        List<Item> inputs = new ArrayList<>();
        if (json.has("inputs")) {
            json.getAsJsonArray("inputs").forEach(value -> inputs.add(item(value.getAsString())));
        } else {
            inputs.add(item(json.get("input").getAsString()));
        }
        Item output = item(json.get("output").getAsString());
        int duration = json.get("duration").getAsInt();
        Item container = json.has("container_return") ? item(json.get("container_return").getAsString()) : null;
        if (inputs.isEmpty() || inputs.stream().anyMatch(input -> input == Items.AIR)
                || output == Items.AIR || duration <= 0) throw new IllegalArgumentException("Invalid processing recipe");
        return new ProcessingRecipe(json.get("station").getAsString(), List.copyOf(inputs), output, duration, container == Items.AIR ? null : container);
    }

    private static Item item(String value) {
        return net.minecraft.core.registries.BuiltInRegistries.ITEM.getValue(Identifier.parse(value));
    }

    public static ProcessingRecipe find(List<ProcessingRecipe> recipes, Item input) {
        return recipes.stream().filter(recipe -> recipe.input() == input).findFirst().orElse(null);
    }
}
