package dev.strawberryflavored.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.storage.TagValueInput;

final class DyedBrushRecolor {
    // Keep beds/shulkers from popping items while swapping the colored variant.
    private static final int SWAP_FLAGS = Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_SUPPRESS_DROPS;

    private DyedBrushRecolor() {
    }

    static boolean apply(ServerLevel level, BlockPos pos, BlockState state, DyeColor color) {
        Block replacement = DyedBrushPalettes.recolor(state.getBlock(), color);
        if (replacement == null || replacement == state.getBlock()) {
            return false;
        }

        if (state.getBlock() instanceof BedBlock) {
            return recolorBed(level, pos, state, color, replacement);
        }

        BlockState newState = replacement.withPropertiesOf(state);
        CompoundTag blockEntityTag = saveBlockEntity(level, pos);
        level.setBlock(pos, newState, SWAP_FLAGS);
        loadBlockEntity(level, pos, blockEntityTag);
        playSound(level, pos);
        return true;
    }

    private static boolean recolorBed(
            ServerLevel level,
            BlockPos pos,
            BlockState state,
            DyeColor color,
            Block replacement
    ) {
        BedPart part = state.getValue(BedBlock.PART);
        Direction facing = state.getValue(BedBlock.FACING);
        BlockPos otherPos = part == BedPart.FOOT ? pos.relative(facing) : pos.relative(facing.getOpposite());
        BlockState otherState = level.getBlockState(otherPos);
        Block otherReplacement = DyedBrushPalettes.recolor(otherState.getBlock(), color);
        if (otherReplacement == null) {
            return false;
        }

        level.setBlock(pos, replacement.withPropertiesOf(state), SWAP_FLAGS);
        level.setBlock(otherPos, otherReplacement.withPropertiesOf(otherState), SWAP_FLAGS);
        playSound(level, pos);
        return true;
    }

    private static CompoundTag saveBlockEntity(ServerLevel level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity == null) {
            return null;
        }
        return blockEntity.saveWithoutMetadata(level.registryAccess());
    }

    private static void loadBlockEntity(ServerLevel level, BlockPos pos, CompoundTag tag) {
        if (tag == null) {
            return;
        }
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity == null) {
            return;
        }
        blockEntity.loadWithComponents(TagValueInput.create(
                ProblemReporter.DISCARDING,
                level.registryAccess(),
                tag
        ));
        blockEntity.setChanged();
    }

    private static void playSound(ServerLevel level, BlockPos pos) {
        level.playSound(null, pos, SoundEvents.BRUSH_GENERIC, SoundSource.BLOCKS, 1.0F, 1.0F);
    }
}
