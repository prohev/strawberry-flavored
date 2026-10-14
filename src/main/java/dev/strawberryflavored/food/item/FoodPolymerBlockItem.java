package dev.strawberryflavored.food.item;

import dev.strawberryflavored.StrawberryFlavored;
import eu.pb4.polymer.core.api.item.PolymerBlockItem;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

/** Polymer block item with an explicit hosted-resource-pack model. */
public final class FoodPolymerBlockItem extends PolymerBlockItem {
    private final String modelName;

    public FoodPolymerBlockItem(Block block, Item.Properties properties, Item vanilla, String modelName) {
        super(block, properties, vanilla, true);
        this.modelName = modelName;
    }

    @Override
    public Identifier getPolymerItemModel(ItemStack stack, PacketContext context, HolderLookup.Provider lookup) {
        return StrawberryFlavored.id("food/" + modelName);
    }
}
