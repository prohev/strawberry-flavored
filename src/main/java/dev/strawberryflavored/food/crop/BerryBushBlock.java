package dev.strawberryflavored.food.crop;

import eu.pb4.polymer.core.api.block.PolymerBlock;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.Level;
import java.util.function.Supplier;

/** Berry bush that uses the vanilla sweet berry bush appearance for Polymer clients. */
public final class BerryBushBlock extends SweetBerryBushBlock implements PolymerBlock, PolymerTexturedBlock {
    private final Supplier<Item> fruit;

    public BerryBushBlock(Properties properties, Supplier<Item> fruit, String name) {
        super(properties);
        this.fruit = fruit;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
                                               BlockHitResult hit) {
        int age = state.getValue(AGE);
        if (age < MAX_AGE) return InteractionResult.PASS;
        if (!level.isClientSide()) {
            int count = 1 + level.getRandom().nextInt(2);
            popResource(level, pos, new ItemStack(fruit.get(), count));
            level.setBlock(pos, state.setValue(AGE, 1), Block.UPDATE_ALL);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext context) {
        return Blocks.SWEET_BERRY_BUSH.defaultBlockState().setValue(AGE, state.getValue(AGE));
    }
}
