package dev.strawberryflavored.processing.ui;

import dev.strawberryflavored.StrawberryFlavored;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.Component;

/** Bitmap-font GUI backgrounds adapted from Borukva-Food's SGUI resource pattern. */
final class FoodMachineGuiTextures {
    private static final Style GUI_STYLE = Style.EMPTY.withColor(0xFFFFFF).withFont(
            new FontDescription.Resource(StrawberryFlavored.id("gui")));
    private static final Style SIGNATURE_STYLE = Style.EMPTY.withFont(
            new FontDescription.Resource(StrawberryFlavored.id("signature")))
            .withColor(0xFFFFFF).withItalic(false);
    // Match the reference generator's ordinary generated glyph characters.
    private static final String COOKING_POT_GLYPH = "b";
    private static final String COOKING_POT_LEFT_SPACE = "a";
    private static final String COOKING_POT_RIGHT_SPACE = "c";

    private FoodMachineGuiTextures() {}

    static Component cookingPotTitle(Component title) {
        return machineTitle(title, "b");
    }

    static Component machineTitle(Component title, String glyph) {
        Component background = Component.literal(COOKING_POT_LEFT_SPACE + COOKING_POT_GLYPH + COOKING_POT_RIGHT_SPACE)
                .withStyle(GUI_STYLE);
        if (!glyph.equals(COOKING_POT_GLYPH)) {
            background = Component.literal(COOKING_POT_LEFT_SPACE + glyph + COOKING_POT_RIGHT_SPACE)
                .withStyle(GUI_STYLE);
        }
        Component strawberry = Component.literal("\uE000").withStyle(SIGNATURE_STYLE);
        return Component.empty().append(background).append(strawberry)
                .append(Component.literal(" ")).append(title.copy().setStyle(Style.EMPTY));
    }
}
