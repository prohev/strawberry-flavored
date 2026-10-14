package dev.strawberryflavored.food.item;

import dev.strawberryflavored.StrawberryFlavored;
import dev.strawberryflavored.food.registry.FoodBlocks;
import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.core.api.item.SimplePolymerItem;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public final class RiceItem extends SimplePolymerItem implements PolymerItem {
    public RiceItem(Item.Properties properties) { super(properties, Items.WHEAT_SEEDS); }

    @Override
    public Identifier getPolymerItemModel(ItemStack stack, PacketContext context, HolderLookup.Provider lookup) {
        return StrawberryFlavored.id("food/rice");
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos waterPos = context.getClickedPos();
        BlockPos cropPos = waterPos.above();
        var cropState = FoodBlocks.RICE.defaultBlockState();
        if (!level.getBlockState(waterPos).is(Blocks.WATER)
                || !level.getFluidState(waterPos).isSource()
                || !level.getBlockState(cropPos).canBeReplaced()
                || !cropState.canSurvive(level, cropPos)) return InteractionResult.PASS;
        if (level.isClientSide()) return InteractionResult.SUCCESS;
        if (!level.setBlock(cropPos, cropState, 3)) return InteractionResult.PASS;
        context.getItemInHand().shrink(1);
        return InteractionResult.SUCCESS;
    }
}
