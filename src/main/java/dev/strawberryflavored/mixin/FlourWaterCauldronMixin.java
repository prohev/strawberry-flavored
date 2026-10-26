package dev.strawberryflavored.mixin;

import dev.strawberryflavored.food.item.FoodItems;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class FlourWaterCauldronMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void strawberryFlavored$makeDoughInWaterCauldron(CallbackInfo callbackInfo) {
        ItemEntity itemEntity = (ItemEntity) (Object) this;
        if (itemEntity.level().isClientSide()
                || !itemEntity.getItem().is(FoodItems.FLOUR)
                || !itemEntity.level().getBlockState(itemEntity.blockPosition()).is(Blocks.WATER_CAULDRON)) {
            return;
        }

        itemEntity.setItem(itemEntity.getItem().transmuteCopy(FoodItems.DOUGH));
    }
}
