package dev.strawberryflavored.food.item;

import dev.strawberryflavored.StrawberryFlavored;
import dev.strawberryflavored.food.registry.FoodBlocks;
import dev.strawberryflavored.food.crop.RiceCropBlock;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;

public final class RiceItem extends SimplePolymerItem implements PolymerItem {
    // Kelp is the vanilla client fallback because it is a water-growing item;
    // using wheat seeds makes Polymer/client placement treat rice as farmland crop.
    public RiceItem(Item.Properties properties) { super(properties, Items.KELP); }

    @Override
    public Identifier getPolymerItemModel(ItemStack stack, PacketContext context, HolderLookup.Provider lookup) {
        return StrawberryFlavored.id("food/rice");
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos waterPos = context.getClickedPos();
        if (!level.getFluidState(waterPos).is(Fluids.WATER)) {
            waterPos = waterPos.relative(context.getClickedFace());
        }
        BlockPos cropPos = waterPos;
        var cropState = FoodBlocks.RICE.defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, true);
        if (!level.getFluidState(waterPos).is(Fluids.WATER)
                || !level.getFluidState(waterPos).isSource()
                || !level.getBlockState(waterPos).canBeReplaced()
                || !RiceCropBlock.isRiceSoil(level.getBlockState(waterPos.below()))) return InteractionResult.PASS;
        if (!cropState.canSurvive(level, cropPos)) return InteractionResult.PASS;
        if (level.isClientSide()) return InteractionResult.SUCCESS;
        if (!level.setBlockAndUpdate(cropPos, cropState)) return InteractionResult.PASS;
        context.getItemInHand().shrink(1);
        return InteractionResult.SUCCESS;
    }
}
