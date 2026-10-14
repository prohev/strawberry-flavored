package dev.strawberryflavored.food.crop;

import dev.strawberryflavored.food.registry.FoodBlocks;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import eu.pb4.polymer.core.api.block.PolymerBlock;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/** Crop whose lower and upper blocks are always placed and kept together. */
public final class CornCropBlock extends CropBlock implements PolymerBlock, PolymerTexturedBlock {
    public CornCropBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter world, BlockPos pos) {
        return state.is(Blocks.FARMLAND);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return context.getLevel().isEmptyBlock(context.getClickedPos().above())
                ? super.getStateForPlacement(context) : null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide() && state.getValue(AGE) >= getMaxAge() && level.isEmptyBlock(pos.above())) {
            level.setBlock(pos.above(), FoodBlocks.CORN_TOP.defaultBlockState(), Block.UPDATE_ALL);
        }
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        // CropBlock advances AGE one stage at a time.  The top half is purely
        // visual and must not replace the lower crop state or its age.
        super.randomTick(state, world, pos, random);
        BlockState grown = world.getBlockState(pos);
        if (grown.getValue(AGE) >= getMaxAge()
                && !world.getBlockState(pos.above()).is(FoodBlocks.CORN_TOP) && world.isEmptyBlock(pos.above())) {
            world.setBlockAndUpdate(pos.above(), FoodBlocks.CORN_TOP.defaultBlockState());
        }
    }

    @Override
    protected net.minecraft.world.level.ItemLike getBaseSeedId() {
        return FoodBlocks.CORN_KERNELS;
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext context) {
        return Blocks.WHEAT.defaultBlockState().setValue(AGE, state.getValue(AGE));
    }
}
