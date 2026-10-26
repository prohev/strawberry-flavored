package dev.strawberryflavored.food.crop;

import dev.strawberryflavored.food.registry.FoodBlocks;
import dev.strawberryflavored.StrawberryFlavored;
import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockModel;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import eu.pb4.polymer.core.api.block.PolymerBlock;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public final class RicePaniclesBlock extends Block implements PolymerBlock, PolymerTexturedBlock, net.minecraft.world.level.block.BonemealableBlock {
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 1);
    private static final BlockState[] POLYMER_STATES = new BlockState[] {
            PolymerBlockResourceUtils.requestBlock(BlockModelType.PLANT,
                    PolymerBlockModel.of(StrawberryFlavored.id("block/rice_panicles_stage0"))),
            PolymerBlockResourceUtils.requestBlock(BlockModelType.PLANT,
                    PolymerBlockModel.of(StrawberryFlavored.id("block/rice_panicles_stage1")))
    };

    public RicePaniclesBlock(Properties properties) { super(properties); }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        growPanicle(level, pos, state);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state,
                                         net.minecraft.world.level.block.BonemealSource source) {
        return state.getValue(AGE) == 0;
    }

    @Override
    public boolean isBonemealSuccess(net.minecraft.world.level.Level level, RandomSource random,
                                    BlockPos pos, BlockState state,
                                    net.minecraft.world.level.block.BonemealSource source) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state,
                                net.minecraft.world.level.block.BonemealSource source) {
        growPanicle(level, pos, state);
    }

    private static void growPanicle(ServerLevel level, BlockPos pos, BlockState state) {
        BlockPos basePos = pos.below();
        BlockState baseState = level.getBlockState(basePos);
        if (state.getValue(AGE) == 0 && baseState.is(FoodBlocks.RICE)) {
            level.setBlock(pos, state.setValue(AGE, 1), Block.UPDATE_CLIENTS);
            if (!level.getBlockState(basePos).is(FoodBlocks.RICE)) {
                level.setBlock(basePos, baseState, Block.UPDATE_CLIENTS);
            }
        }
    }

    @Override
    public BlockState playerWillDestroy(net.minecraft.world.level.Level level, BlockPos pos,
                                        BlockState state, Player player) {
        BlockPos basePos = pos.below();
        BlockState base = level.getBlockState(basePos);
        if (base.is(FoodBlocks.RICE)) {
            level.setBlock(basePos, base.setValue(RiceCropBlock.AGE, 1), Block.UPDATE_ALL);
        }
        if (!level.isClientSide() && state.getValue(AGE) == 1) {
            popResource(level, pos, new ItemStack(FoodBlocks.RICE_ITEM,
                    1 + level.getRandom().nextInt(3)));
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState base = level.getBlockState(pos.below());
        // Neighbor updates can observe the crop while its age is changing. The
        // panicle should remain attached as long as the rice plant still exists.
        return base.is(FoodBlocks.RICE);
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext context) {
        return POLYMER_STATES[state.getValue(AGE)];
    }

    @Override
    public BlockState getPolymerBreakEventBlockState(BlockState state, PacketContext context) {
        return Blocks.WHEAT.defaultBlockState();
    }
}
