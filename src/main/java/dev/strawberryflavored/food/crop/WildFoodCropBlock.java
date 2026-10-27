package dev.strawberryflavored.food.crop;

import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import eu.pb4.polymer.core.api.block.PolymerBlock;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import java.util.function.Supplier;

/** A naturally generated, non-plantable crop that behaves like a flower. */
public final class WildFoodCropBlock extends BushBlock implements PolymerBlock, PolymerTexturedBlock {
    private final Supplier<net.minecraft.world.item.Item> produce;
    private final Supplier<net.minecraft.world.item.Item> seed;
    private final Block soil;
    private final BlockState polymerState;

    public WildFoodCropBlock(Properties properties, String modelName,
                             Supplier<net.minecraft.world.item.Item> produce,
                             Supplier<net.minecraft.world.item.Item> seed, Block soil) {
        super(properties);
        this.produce = produce;
        this.seed = seed;
        this.soil = soil;
        this.polymerState = PolymerBlockResourceUtils.requestEmpty(BlockModelType.PLANT);
    }

    @Override protected boolean mayPlaceOn(BlockState state, BlockGetter world, BlockPos pos) {
        return state.is(soil) || (soil == Blocks.FARMLAND && (state.is(Blocks.DIRT) || state.is(Blocks.GRASS_BLOCK)));
    }

    @Override public BlockState getPolymerBlockState(BlockState state, PacketContext context) {
        return polymerState;
    }

    @Override public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state,
                                                   net.minecraft.world.entity.player.Player player) {
        if (!level.isClientSide()) {
            popResource(level, pos, new ItemStack(produce.get()));
            popResource(level, pos, new ItemStack(seed.get()));
        }
        return super.playerWillDestroy(level, pos, state, player);
    }
}
