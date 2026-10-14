package dev.strawberryflavored.food.crop;

import dev.strawberryflavored.food.registry.FoodBlocks;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import eu.pb4.polymer.core.api.block.PolymerBlock;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public final class RiceCropBlock extends CropBlock implements PolymerBlock, PolymerTexturedBlock {
    public RiceCropBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter world, BlockPos pos) {
        FluidState fluid = state.getFluidState();
        return state.is(Blocks.WATER) && fluid.isSource();
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, net.minecraft.world.level.ScheduledTickAccess ticks,
                                     BlockPos pos, Direction direction, BlockPos neighborPos,
                                     BlockState neighborState, RandomSource random) {
        return super.updateShape(state, level, ticks, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    protected net.minecraft.world.level.ItemLike getBaseSeedId() {
        return FoodBlocks.RICE_ITEM;
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext context) {
        return Blocks.WHEAT.defaultBlockState().setValue(AGE, Math.min(7, state.getValue(AGE)));
    }
}
