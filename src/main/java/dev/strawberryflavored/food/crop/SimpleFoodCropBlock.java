package dev.strawberryflavored.food.crop;

import dev.strawberryflavored.StrawberryFlavored;
import eu.pb4.factorytools.api.block.FactoryBlock;
import eu.pb4.factorytools.api.virtualentity.BlockModel;
import eu.pb4.factorytools.api.virtualentity.ItemDisplayElementUtil;
import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import eu.pb4.polymer.core.api.block.PolymerBlock;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.attachment.BlockBoundAttachment;
import eu.pb4.polymer.virtualentity.api.attachment.HolderAttachment;
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3f;
import java.util.ArrayList;
import java.util.function.Supplier;

/** Standard age-seven food crop with a hosted resource-pack model. */
public final class SimpleFoodCropBlock extends CropBlock implements PolymerBlock, PolymerTexturedBlock, FactoryBlock {
    private static final BlockState FALLBACK = PolymerBlockResourceUtils.requestEmpty(BlockModelType.BIOME_PLANT);
    private final Supplier<Item> produce;
    private final Supplier<Item> seed;
    private final Block soil;
    private final boolean lowLightGrowth;

    public SimpleFoodCropBlock(Properties properties, Supplier<Item> produce, Supplier<Item> seed, Block soil) {
        super(properties);
        this.produce = produce;
        this.seed = seed;
        this.soil = soil;
        this.lowLightGrowth = soil == Blocks.SOUL_SAND;
    }

    @Override protected boolean mayPlaceOn(BlockState state, BlockGetter world, BlockPos pos) {
        return state.is(soil) || (soil == Blocks.FARMLAND && (state.is(Blocks.DIRT) || state.is(Blocks.GRASS_BLOCK)));
    }
    @Override protected net.minecraft.world.level.ItemLike getBaseSeedId() { return seed.get(); }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, net.minecraft.util.RandomSource random) {
        if (!lowLightGrowth) {
            super.randomTick(state, level, pos, random);
            return;
        }
        int age = state.getValue(AGE);
        if (age < getMaxAge() && random.nextInt(3) == 0) {
            level.setBlock(pos, state.setValue(AGE, age + 1), Block.UPDATE_CLIENTS);
        }
    }
    @Override public BlockState getPolymerBlockState(BlockState state, PacketContext context) { return FALLBACK; }
    @Override public BlockState getPolymerBreakEventBlockState(BlockState state, PacketContext context) { return Blocks.WHEAT.defaultBlockState(); }
    @Override public ElementHolder createElementHolder(ServerLevel level, BlockPos pos, BlockState state) {
        return new Model(state, soil == Blocks.SOUL_SAND);
    }

    @Override public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, net.minecraft.world.entity.player.Player player) {
        if (!level.isClientSide()) {
            if (state.getValue(AGE) == getMaxAge()) {
                popResource(level, pos, new ItemStack(produce.get(), 1 + level.getRandom().nextInt(2)));
            } else {
                popResource(level, pos, new ItemStack(seed.get()));
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    public static final class Model extends BlockModel {
        private final ArrayList<ItemStack> stages = new ArrayList<>();
        private final ItemDisplayElement main = ItemDisplayElementUtil.createSimple();
        private final boolean soul;
        Model(BlockState state, boolean soul) {
            this.soul = soul;
            String name = soul ? "soul_pepper" : "lettuce";
            for (int i = 0; i < 4; i++) stages.add(ItemDisplayElementUtil.getModelCopy(StrawberryFlavored.id("block/" + name + "_stage" + i)));
            updateItem(state);
            main.setScale(new Vector3f(1f, 1f, 1f));
            addElement(main);
        }
        private void updateItem(BlockState state) { main.setItem(stages.get(Math.min(3, state.getValue(AGE) / 2))); }
        @Override public void notifyUpdate(HolderAttachment.UpdateType type) {
            if (type == BlockBoundAttachment.BLOCK_STATE_UPDATE) { updateItem(blockState()); tick(); }
            super.notifyUpdate(type);
        }
    }
}
