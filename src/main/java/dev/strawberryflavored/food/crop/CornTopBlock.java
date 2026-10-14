package dev.strawberryflavored.food.crop;

import dev.strawberryflavored.food.registry.FoodBlocks;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import eu.pb4.polymer.core.api.block.PolymerBlock;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;

public final class CornTopBlock extends Block implements PolymerBlock, PolymerTexturedBlock {
    public CornTopBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).is(FoodBlocks.CORN);
    }

    @Override
    protected BlockState updateShape(BlockState state, net.minecraft.world.level.LevelReader level,
                                     net.minecraft.world.level.ScheduledTickAccess ticks, BlockPos pos,
                                     net.minecraft.core.Direction direction, BlockPos neighborPos,
                                     BlockState neighborState, net.minecraft.util.RandomSource random) {
        return direction == net.minecraft.core.Direction.DOWN && !canSurvive(state, level, pos)
                ? net.minecraft.world.level.block.Blocks.AIR.defaultBlockState()
                : super.updateShape(state, level, ticks, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    public BlockState playerWillDestroy(net.minecraft.world.level.Level level, BlockPos pos,
                                       BlockState state, Player player) {
        BlockPos basePos = pos.below();
        if (level.getBlockState(basePos).is(FoodBlocks.CORN)) {
            level.destroyBlock(basePos, true, player);
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext context) {
        return Blocks.TALL_GRASS.defaultBlockState();
    }
}
