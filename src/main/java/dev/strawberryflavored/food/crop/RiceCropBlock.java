package dev.strawberryflavored.food.crop;

import dev.strawberryflavored.food.registry.FoodBlocks;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.core.api.block.PolymerBlock;
import eu.pb4.factorytools.api.block.FactoryBlock;
import eu.pb4.factorytools.api.virtualentity.BlockModel;
import eu.pb4.factorytools.api.virtualentity.ItemDisplayElementUtil;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.attachment.BlockBoundAttachment;
import eu.pb4.polymer.virtualentity.api.attachment.HolderAttachment;
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import dev.strawberryflavored.StrawberryFlavored;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;
import java.util.ArrayList;

public final class RiceCropBlock extends CropBlock implements PolymerBlock, PolymerTexturedBlock,
        SimpleWaterloggedBlock, FactoryBlock {
    public static final int MAX_AGE = 3;
    private static final BlockState TRANSPARENT_WATERLOGGED_FALLBACK =
            PolymerBlockResourceUtils.requestEmpty(eu.pb4.polymer.blocks.api.BlockModelType.KELP);
    public RiceCropBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BlockStateProperties.WATERLOGGED);
    }

    @Override
    public net.minecraft.world.level.material.FluidState getFluidState(BlockState state) {
        return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public int getMaxAge() { return MAX_AGE; }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter world, BlockPos pos) {
        return state.is(BlockTags.DIRT) || state.is(Blocks.MUD);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        FluidState fluid = level.getFluidState(pos);
        return fluid.is(Fluids.WATER) && fluid.isSource()
                && super.canSurvive(state, level, pos);
    }

    @Override
    public BlockState getStateForPlacement(net.minecraft.world.item.context.BlockPlaceContext context) {
        if (!context.getLevel().getFluidState(context.getClickedPos()).is(Fluids.WATER)
                || !context.getLevel().getFluidState(context.getClickedPos()).isSource()) {
            return null;
        }
        return defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, true);
    }

    @Override
    public ElementHolder createElementHolder(ServerLevel level, BlockPos pos, BlockState state) {
        return new Model(state);
    }

    private static boolean hasRiceLight(ServerLevel level, BlockPos pos) {
        BlockPos lightPos = pos.above();
        if (level.getMaxLocalRawBrightness(lightPos) < 10) return false;
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (level.getMaxLocalRawBrightness(lightPos.relative(direction)) < 8) return false;
        }
        return true;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!hasRiceLight(level, pos)) return;
        super.randomTick(state, level, pos, random);
        BlockState current = level.getBlockState(pos);
        // The vanilla crop tick can remove or replace the block when it is no
        // longer valid. Do not read AGE from the resulting air/other state.
        if (!current.is(this)) return;
        int age = current.getValue(AGE);
        if (age >= getMaxAge()) createPanicles(level, pos);
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state,
                                net.minecraft.world.level.block.BonemealSource source) {
        BlockState current = level.getBlockState(pos);
        if (!current.is(this)) return;
        int age = current.getValue(AGE);
        if (age < getMaxAge()) {
            level.setBlockAndUpdate(pos, current.setValue(AGE, Math.min(getMaxAge(), age + 1)));
        }
        if (level.getBlockState(pos).getValue(AGE) >= getMaxAge()) createPanicles(level, pos);
    }

    private static void createPanicles(ServerLevel level, BlockPos pos) {
        if (level.isEmptyBlock(pos.above())
                && FoodBlocks.RICE_PANICLES.defaultBlockState().canSurvive(level, pos.above())) {
            level.setBlockAndUpdate(pos.above(), FoodBlocks.RICE_PANICLES.defaultBlockState()
                    .setValue(RicePaniclesBlock.AGE, 0));
        }
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
    public BlockState playerWillDestroy(net.minecraft.world.level.Level level, BlockPos pos,
                                        BlockState state, net.minecraft.world.entity.player.Player player) {
        if (!level.isClientSide()) {
            popResource(level, pos, new ItemStack(FoodBlocks.RICE_ITEM));
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext context) {
        return TRANSPARENT_WATERLOGGED_FALLBACK;
    }

    public static final class Model extends BlockModel {
        private static final ArrayList<ItemStack> MODELS = new ArrayList<>();
        static {
            for (int i = 0; i <= MAX_AGE; i++) {
                MODELS.add(ItemDisplayElementUtil.getModelCopy(
                        StrawberryFlavored.id("block/rice_stage" + i)));
            }
        }

        private final ItemDisplayElement main;

        public Model(BlockState state) {
            main = ItemDisplayElementUtil.createSimple();
            updateItem(state);
            main.setScale(new Vector3f(1f, 1f, 1f));
            main.setTranslation(new Vector3f(0f, 0f, 0f));
            addElement(main);
        }

        private void updateItem(BlockState state) {
            main.setItem(MODELS.get(Math.min(state.getValue(AGE), MAX_AGE)));
        }

        @Override
        public void notifyUpdate(HolderAttachment.UpdateType updateType) {
            if (updateType == BlockBoundAttachment.BLOCK_STATE_UPDATE) {
                updateItem(blockState());
                tick();
            }
            super.notifyUpdate(updateType);
        }
    }
}
