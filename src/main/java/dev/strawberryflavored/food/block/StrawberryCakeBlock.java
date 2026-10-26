package dev.strawberryflavored.food.block;

import eu.pb4.polymer.core.api.block.PolymerBlock;
import eu.pb4.factorytools.api.block.FactoryBlock;
import eu.pb4.factorytools.api.virtualentity.BlockModel;
import eu.pb4.factorytools.api.virtualentity.ItemDisplayElementUtil;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.attachment.HolderAttachment;
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.joml.Vector3f;

import java.util.ArrayList;

/** Vanilla cake mechanics represented to vanilla clients by a vanilla cake. */
public final class StrawberryCakeBlock extends CakeBlock implements PolymerBlock, FactoryBlock {
    public StrawberryCakeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext context) {
        return Blocks.BARRIER.defaultBlockState();
    }

    @Override
    public BlockState getPolymerBreakEventBlockState(BlockState state, PacketContext context) {
        return Blocks.CAKE.defaultBlockState();
    }

    @Override
    public ElementHolder createElementHolder(ServerLevel level, BlockPos pos, BlockState state) {
        return new Model(state);
    }

    public static final class Model extends BlockModel {
        private static final ArrayList<ItemStack> MODELS = new ArrayList<>();
        private final ItemDisplayElement main;

        static {
            for (int bites = 0; bites <= 6; bites++) {
                MODELS.add(ItemDisplayElementUtil.getModelCopy(
                        Identifier.fromNamespaceAndPath("strawberry-flavored",
                                "block/strawberry_cake_slice" + bites)));
            }
        }

        public Model(BlockState state) {
            this.main = ItemDisplayElementUtil.createSimple();
            updateItem(state);
            this.main.setScale(new Vector3f(1f));
            addElement(this.main);
        }

        private void updateItem(BlockState state) {
            this.main.setItem(MODELS.get(state.getValue(BlockStateProperties.BITES)));
        }

        @Override
        public void notifyUpdate(HolderAttachment.UpdateType updateType) {
            updateItem(blockState());
            tick();
            super.notifyUpdate(updateType);
        }
    }
}
