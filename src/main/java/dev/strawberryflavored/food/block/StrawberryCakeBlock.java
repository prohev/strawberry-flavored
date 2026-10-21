package dev.strawberryflavored.food.block;

import eu.pb4.polymer.core.api.block.PolymerBlock;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

/** Vanilla cake mechanics represented to vanilla clients by a campfire. */
public final class StrawberryCakeBlock extends CakeBlock implements PolymerBlock {
    public StrawberryCakeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext context) {
        return Blocks.CAMPFIRE.defaultBlockState().setValue(BlockStateProperties.LIT, false);
    }
}
