package dev.strawberryflavored.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.strawberryflavored.world.CropProtection;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FarmlandBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FarmlandBlock.class)
public abstract class FarmlandBlockMixin {
    @WrapOperation(
            method = "fallOn",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/FarmlandBlock;turnToDirt(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V"
            )
    )
    private static void strawberryFlavored$protectCrops(
            BlockState state,
            Level level,
            BlockPos pos,
            Operation<Void> original,
            Level fallLevel,
            BlockState fallState,
            BlockPos fallPos,
            Entity entity,
            float fallDistance
    ) {
        if (entity instanceof net.minecraft.world.entity.LivingEntity living
                && CropProtection.preventsTrampling(living)) {
            return;
        }

        original.call(state, level, pos);
    }
}
