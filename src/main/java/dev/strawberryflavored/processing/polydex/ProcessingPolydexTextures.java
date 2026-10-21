package dev.strawberryflavored.processing.polydex;

import dev.strawberryflavored.StrawberryFlavored;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.Style;

/** Backgrounds matching the reference Polydex page layout. */
final class ProcessingPolydexTextures {
    private static final Style STYLE = Style.EMPTY.withColor(0xFFFFFF)
            .withFont(new FontDescription.Resource(StrawberryFlavored.id("gui")));

    private ProcessingPolydexTextures() {}

    static Component background(String glyph) {
        return Component.literal("h" + glyph).withStyle(STYLE);
    }
}
