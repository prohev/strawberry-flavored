package dev.strawberryflavored.processing.ui;

import eu.pb4.sgui.api.elements.GuiElement;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.polymer.resourcepack.extras.api.ResourcePackExtras;
import net.minecraft.network.chat.Component;
import net.minecraft.core.component.DataComponents;
import dev.strawberryflavored.StrawberryFlavored;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/** Shared SGUI display elements used by every food-processing station. */
final class FoodMachineGuiElements {
    private FoodMachineGuiElements() {}

    static GuiElement filler(Item item) {
        return new GuiElementBuilder(item)
                .setName(Component.empty())
                .hideTooltip()
                .build();
    }

    static GuiElement display(ItemStack stack) {
        return new GuiElementBuilder(stack.copy()).build();
    }

    /** Upstream-style SGUI element: a vanilla carrier with a virtual model. */
    static GuiElement progress(float fraction) {
        int stage = Math.max(0, Math.min(22, (int) Math.ceil(Math.max(0, Math.min(1, fraction)) * 22)));
        return virtualElement("progress_" + stage);
    }

    static GuiElement heat(boolean heated) {
        return virtualElement(heated ? "heated" : "empty");
    }

    private static GuiElement virtualElement(String name) {
        ItemStack stack = new ItemStack(Items.ALLIUM);
        stack.set(DataComponents.ITEM_MODEL,
                ResourcePackExtras.bridgeModel(StrawberryFlavored.id("sgui/elements/" + name)));
        return new GuiElementBuilder(stack).setName(Component.empty()).hideTooltip().build();
    }

}
