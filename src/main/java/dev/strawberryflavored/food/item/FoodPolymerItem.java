package dev.strawberryflavored.food.item;

import dev.strawberryflavored.StrawberryFlavored;
import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.core.api.item.SimplePolymerItem;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/** A food item whose client-side representation uses this mod's item model. */
public final class FoodPolymerItem extends SimplePolymerItem implements PolymerItem {
    private final String modelName;

    public FoodPolymerItem(Item.Properties properties, Item vanilla, String modelName) {
        super(properties, vanilla);
        this.modelName = modelName;
    }

    @Override
    public Identifier getPolymerItemModel(ItemStack stack, PacketContext context, HolderLookup.Provider lookup) {
        return StrawberryFlavored.id("food/" + modelName);
    }
}
