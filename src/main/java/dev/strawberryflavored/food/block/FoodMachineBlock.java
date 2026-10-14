package dev.strawberryflavored.food.block;

import dev.strawberryflavored.StrawberryFlavored;
import dev.strawberryflavored.food.registry.FoodBlockEntities;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import eu.pb4.polymer.core.api.block.PolymerBlock;
import eu.pb4.polymer.core.api.block.SimplePolymerBlock;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import dev.strawberryflavored.food.ui.FoodMachineGui;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
/** Polymer-backed processing station whose inventory lives in a block entity. */
public final class FoodMachineBlock extends SimplePolymerBlock implements PolymerBlock, PolymerTexturedBlock, EntityBlock {
    public enum Station { OVEN, MILLSTONE, POT }
    private final Station station;
    private final BlockState polymerBlockState;

    public FoodMachineBlock(Properties properties, Block vanillaRepresentation) {
        this(properties, vanillaRepresentation, vanillaRepresentation == Blocks.FURNACE ? Station.OVEN : Station.MILLSTONE);
    }

    public FoodMachineBlock(Properties properties, Block vanillaRepresentation, Station station) {
        super(properties, vanillaRepresentation);
        this.station = station;
        // Never allocate a Polymer virtual state here. Virtual states can produce
        // palette IDs that vanilla clients cannot decode in chunk packets.
        this.polymerBlockState = vanillaRepresentation.defaultBlockState();
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext context) {
        return polymerBlockState;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack held, BlockState state, Level level, BlockPos pos,
                                          Player player, InteractionHand hand, BlockHitResult hit) {
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
