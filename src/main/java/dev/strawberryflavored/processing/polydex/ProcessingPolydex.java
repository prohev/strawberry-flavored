package dev.strawberryflavored.processing.polydex;

import dev.strawberryflavored.StrawberryFlavored;
import dev.strawberryflavored.food.registry.FoodBlocks;
import dev.strawberryflavored.processing.recipe.FoodProcessingRecipes;
import dev.strawberryflavored.processing.recipe.ProcessingRecipe;
import eu.pb4.polydex.api.v1.recipe.PageBuilder;
import eu.pb4.polydex.api.v1.recipe.PolydexCategory;
import eu.pb4.polydex.api.v1.recipe.PolydexEntry;
import eu.pb4.polydex.api.v1.recipe.PolydexIngredient;
import eu.pb4.polydex.api.v1.recipe.PolydexPage;
import eu.pb4.polydex.api.v1.recipe.PolydexPageUtils;
import eu.pb4.polydex.api.v1.recipe.PolydexStack;
import eu.pb4.sgui.api.elements.GuiElement;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.ArrayList;
import java.util.function.Consumer;

/** Polydex categories, pages, and machine-screen buttons for processing recipes. */
public final class ProcessingPolydex {
    private static final Identifier OVEN_ID = StrawberryFlavored.id("oven_processing");
    private static final Identifier COOKING_POT_ID = StrawberryFlavored.id("cooking_pot_processing");
    private static final Identifier MILLSTONE_ID = StrawberryFlavored.id("millstone_processing");
    private static final PolydexCategory OVEN = PolydexCategory.of(OVEN_ID);
    private static final PolydexCategory COOKING_POT = PolydexCategory.of(COOKING_POT_ID);
    private static final PolydexCategory MILLSTONE = PolydexCategory.of(MILLSTONE_ID);

    private ProcessingPolydex() {}

    public static void register() {
        if (!FabricLoader.getInstance().isModLoaded("polydex")) return;
        PolydexPage.register(ProcessingPolydex::createPages);
    }

    private static void createPages(MinecraftServer server, Consumer<PolydexPage> pages) {
        FoodProcessingRecipes.OVEN.forEach(recipe -> pages.accept(new ProcessingPage(recipe, OVEN, FoodBlocks.OVEN.asItem())));
        FoodProcessingRecipes.COOKING_POT.forEach(recipe -> pages.accept(new ProcessingPage(recipe, COOKING_POT, FoodBlocks.COOKING_POT.asItem())));
        FoodProcessingRecipes.MILLSTONE.forEach(recipe -> pages.accept(new ProcessingPage(recipe, MILLSTONE, FoodBlocks.MILLSTONE.asItem())));
    }

    public static GuiElement button(PolydexCategory category, Component name, Runnable reopen, ServerPlayer player) {
        return new GuiElementBuilder(Items.KNOWLEDGE_BOOK)
                .setName(name)
                .setCallback((index, type, action, gui) -> {
                    PolydexPageUtils.openCategoryUi(player, category, reopen);
                })
                .build();
    }

    public static GuiElement ovenButton(ServerPlayer player, Runnable reopen) {
        return button(OVEN, Component.translatable("gui.strawberry-flavored.oven_recipes"), reopen, player);
    }

    public static GuiElement cookingPotButton(ServerPlayer player, Runnable reopen) {
        return button(COOKING_POT, Component.translatable("gui.strawberry-flavored.cooking_pot_recipes"), reopen, player);
    }

    public static GuiElement millstoneButton(ServerPlayer player, Runnable reopen) {
        return button(MILLSTONE, Component.translatable("gui.strawberry-flavored.millstone_recipes"), reopen, player);
    }

    private record ProcessingPage(ProcessingRecipe recipe, PolydexCategory category, net.minecraft.world.item.Item station)
            implements PolydexPage {
        @Override public Identifier identifier() {
            String inputs = recipe.inputs().stream()
                    .map(item -> item.toString().replace(':', '_'))
                    .reduce((left, right) -> left + "_" + right)
                    .orElse("empty");
            return StrawberryFlavored.id("processing/" + recipe.station() + "/" + recipe.output().toString().replace(':', '_') + "_from_" + inputs);
        }

        @Override public ItemStack typeIcon(ServerPlayer player) { return station.getDefaultInstance(); }
        @Override public ItemStack entryIcon(PolydexEntry entry, ServerPlayer player) { return recipe.output().getDefaultInstance(); }
        @Override public Component texture(ServerPlayer player) {
            // Use the custom recipe overlay so its machine layout stays aligned
            // with the station screen for every processing block.
            return ProcessingPolydexTextures.background("g");
        }
        @Override public List<PolydexIngredient<?>> ingredients() {
            List<PolydexIngredient<?>> result = new ArrayList<>();
            for (var input : recipe.inputs()) result.add(PolydexStack.of(input));
            return result;
        }
        @Override public List<PolydexCategory> categories() { return List.of(category); }
        @Override public boolean isOwner(MinecraftServer server, PolydexEntry entry) {
            return entry.isPartOf(PolydexStack.of(recipe.output()));
        }
        @Override public void createPage(@Nullable PolydexEntry entry, ServerPlayer player, PageBuilder builder) {
            int i = 0;
            for (var input : recipe.inputs()) {
                builder.setIngredient(2 + (i % 3), 1 + (i / 3), input.getDefaultInstance());
                i++;
            }
            builder.setOutput(7, 1, new net.minecraft.world.item.ItemStack(recipe.output(), recipe.outputCount()));
        }
    }
}
