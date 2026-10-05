package dev.strawberryflavored.item;

import dev.strawberryflavored.StrawberryFlavored;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemLore;

import java.util.List;

public final class Signature {
    // \uE000 is the strawberry icon in font/signature.json.
    // Color must stay white: the gradient is baked into the font pixels,
    // and any other color would tint it.
    private static final ItemLore LORE = new ItemLore(List.of(
            Component.literal("\uE000 STRAWBERRY FLAVORED")
                    .withStyle(style -> style
                            .withFont(new FontDescription.Resource(StrawberryFlavored.id("signature")))
                            .withColor(0xFFFFFF)
                            .withItalic(false))
    ));

    private Signature() {
    }

    public static Item.Properties sign(Item.Properties properties) {
        return properties.component(DataComponents.LORE, LORE);
    }
}
