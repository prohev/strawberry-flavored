package dev.strawberryflavored.food.crop;

import dev.strawberryflavored.food.registry.FoodBlocks;
import dev.strawberryflavored.StrawberryFlavored;
import eu.pb4.factorytools.api.block.FactoryBlock;
import eu.pb4.factorytools.api.virtualentity.ItemDisplayElementUtil;
import eu.pb4.factorytools.api.virtualentity.BlockModel;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.core.api.block.PolymerBlock;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.attachment.BlockBoundAttachment;
import eu.pb4.polymer.virtualentity.api.attachment.HolderAttachment;
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;
import java.util.ArrayList;

/** Crop whose lower and upper blocks are always placed and kept together. */
public final class CornCropBlock extends CropBlock implements PolymerBlock, PolymerTexturedBlock, FactoryBlock {
    private static final BlockState TRANSPARENT_FALLBACK =
            PolymerBlockResourceUtils.requestEmpty(BlockModelType.BIOME_PLANT);
    public CornCropBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter world, BlockPos pos) {
        return state.is(Blocks.FARMLAND);
    }

    @Override
    public ElementHolder createElementHolder(ServerLevel world, BlockPos pos, BlockState initialBlockState) {
        return new Model(initialBlockState);
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext context) {
        return TRANSPARENT_FALLBACK;
    }

    @Override
    public BlockState getPolymerBreakEventBlockState(BlockState state, PacketContext context) {
        return Blocks.WHEAT.defaultBlockState();
    }

    @Override
    public BlockState playerWillDestroy(net.minecraft.world.level.Level level, BlockPos pos,
                                        BlockState state, net.minecraft.world.entity.player.Player player) {
        if (!level.isClientSide()) {
            int age = state.getValue(AGE);
            if (age == getMaxAge()) {
                popResource(level, pos, new ItemStack(dev.strawberryflavored.food.item.FoodItems.CORN,
                        2 + level.getRandom().nextInt(3)));
            } else if (age == getMaxAge() - 1) {
                popResource(level, pos, new ItemStack(dev.strawberryflavored.food.item.FoodItems.CORN));
            } else {
                popResource(level, pos, new ItemStack(FoodBlocks.CORN_KERNELS));
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void playerDestroy(ServerLevel level, net.minecraft.server.level.ServerPlayer player, BlockPos pos,
                              BlockState state, net.minecraft.world.level.block.entity.BlockEntity blockEntity,
                              ItemStack tool) {
    }

    @Override
    protected net.minecraft.world.level.ItemLike getBaseSeedId() {
        return FoodBlocks.CORN_KERNELS;
    }

    public static final class Model extends BlockModel {
        private static final ArrayList<ItemStack> MODELS = new ArrayList<>();
        static {
            // Match Borukva's five visual bands: 0, 1-2, 3-4, 5-6, and 7.
            for (int i = 0; i <= 4; i++) {
                MODELS.add(ItemDisplayElementUtil.getModelCopy(
                        StrawberryFlavored.id("block/corn_stage" + i)));
            }
        }
        private final ItemDisplayElement main;

        public Model(BlockState state) {
            main = ItemDisplayElementUtil.createSimple();
            updateItem(state);
            main.setScale(new Vector3f(1f, 2f, 1f));
            main.setTranslation(new Vector3f(0f, 0.5f, 0f));
            addElement(main);
        }

        private void updateItem(BlockState state) {
            int age = state.getValue(AGE);
            main.setItem(MODELS.get(age == 7 ? 4 : age >= 5 ? 3 : age >= 3 ? 2 : age >= 1 ? 1 : 0));
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
