package dev.strawberryflavored.food.crop;

import eu.pb4.polymer.core.api.block.PolymerBlock;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockModel;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import dev.strawberryflavored.StrawberryFlavored;
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
import net.minecraft.resources.Identifier;
import java.util.function.Supplier;

/** Berry bush that uses the vanilla sweet berry bush appearance for Polymer clients. */
public final class BerryBushBlock extends SweetBerryBushBlock implements PolymerBlock, PolymerTexturedBlock {
    private final Supplier<Item> fruit;
    private final BlockState[] polymerStates;

    public BerryBushBlock(Properties properties, Supplier<Item> fruit, String name) {
        super(properties);
        this.fruit = fruit;
        this.polymerStates = new BlockState[4];
        for (int age = 0; age <= MAX_AGE; age++) {
            Identifier model = StrawberryFlavored.id("block/" + name + "_bush_stage" + age);
            this.polymerStates[age] = PolymerBlockResourceUtils.requestBlock(
                    BlockModelType.PLANT, PolymerBlockModel.of(model));
        }
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
        BlockState polymerState = this.polymerStates[state.getValue(AGE)];
        return polymerState != null
                ? polymerState
                : Blocks.SWEET_BERRY_BUSH.defaultBlockState().setValue(AGE, state.getValue(AGE));
    }
}
