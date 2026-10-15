package dev.strawberryflavored.processing.recipe;

import net.minecraft.world.item.Item;
import java.util.List;

/** Immutable description shared by every food-processing station. */
public record ProcessingRecipe(String station, List<Item> inputs, Item output, int duration, Item containerReturn) {
    public ProcessingRecipe(String station, Item input, Item output, int duration, Item containerReturn) {
        this(station, List.of(input), output, duration, containerReturn);
    }

    public ProcessingRecipe(Item input, Item output, int duration) {
        this("", input, output, duration, null);
    }

    public Item input() { return inputs.getFirst(); }
}
