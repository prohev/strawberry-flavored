package dev.strawberryflavored.processing.block;

import dev.strawberryflavored.processing.registry.FoodBlockEntities;
import eu.pb4.factorytools.api.block.FactoryBlock;
import eu.pb4.factorytools.api.block.model.generic.BSMMParticleBlock;
import eu.pb4.factorytools.api.block.model.generic.BlockStateModel;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.core.api.block.SimplePolymerBlock;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import dev.strawberryflavored.processing.ui.FoodMachineGui;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.item.context.BlockPlaceContext;
/** Polymer-backed processing station whose inventory lives in a block entity. */
public final class FoodMachineBlock extends SimplePolymerBlock implements FactoryBlock, BSMMParticleBlock, EntityBlock {
    public enum Station { OVEN, MILLSTONE, COOKING_POT }
    private final Station station;
    private static final VoxelShape COOKING_POT_SHAPE = Block.box(0, 0, 0, 16, 12, 16);

    public FoodMachineBlock(Properties properties, Block vanillaRepresentation) {
        this(properties, vanillaRepresentation, vanillaRepresentation == Blocks.FURNACE ? Station.OVEN : Station.MILLSTONE);
    }

    public FoodMachineBlock(Properties properties, Block vanillaRepresentation, Station station) {
        super(properties, vanillaRepresentation);
        this.station = station;
        this.registerDefaultState(this.stateDefinition.any().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.LIT);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING,
                context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos,
                                           CollisionContext context) {
        return station == Station.COOKING_POT ? COOKING_POT_SHAPE : super.getCollisionShape(state, level, pos, context);
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext context) {
        // The model is rendered by the FactoryTools virtual entity. An
        // invisible full block prevents the vanilla fallback from clipping
        // through custom machine geometry while keeping the block targetable.
        return Blocks.BARRIER.defaultBlockState();
    }

    @Override
    public ElementHolder createElementHolder(ServerLevel world, BlockPos pos, BlockState initialBlockState) {
        return BlockStateModel.longRange(initialBlockState, pos);
    }

    @Override
    public boolean tickElementHolder(ServerLevel world, BlockPos pos, BlockState initialBlockState) {
        return false;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack held, BlockState state, Level level, BlockPos pos,
                                          Player player, InteractionHand hand, BlockHitResult hit) {
        if (station == Station.OVEN && held.is(Items.FLINT_AND_STEEL) && !state.getValue(BlockStateProperties.LIT)) {
            level.setBlock(pos, state.setValue(BlockStateProperties.LIT, true), Block.UPDATE_ALL);
            level.levelEvent(1009, pos, 0);
            held.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND
                    ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
            return InteractionResult.SUCCESS;
        }
        return openMachine(level, pos, player);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
                                               BlockHitResult hit) {
        return openMachine(level, pos, player);
    }

    private InteractionResult openMachine(Level level, BlockPos pos, Player player) {
        if (!level.isClientSide()) {
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof FoodMachineBlockEntity machine && player instanceof ServerPlayer serverPlayer) {
                new FoodMachineGui(serverPlayer, machine).open();
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FoodMachineBlockEntity(FoodBlockEntities.typeFor(station), pos, state, station);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        BlockEntityType<?> expected = FoodBlockEntities.typeFor(station);
        return type == expected ? (level1, pos1, state1, entity) ->
                FoodMachineBlockEntity.serverTick(level1, pos1, state1, (FoodMachineBlockEntity) entity) : null;
    }
}
