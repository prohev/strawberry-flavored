package dev.strawberryflavored.mixin;

import dev.strawberryflavored.world.ItemFrameInvisibility;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.projectile.throwableitemprojectile.AbstractThrownPotion;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownSplashPotion;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractThrownPotion.class)
public abstract class ThrownSplashPotionMixin {
    @Inject(method = "onHit", at = @At("HEAD"))
    private void strawberryFlavored$changeItemFrameVisibility(
            HitResult hitResult,
            CallbackInfo callbackInfo
    ) {
        AbstractThrownPotion potion = (AbstractThrownPotion) (Object) this;
        if (potion instanceof ThrownSplashPotion splashPotion && potion.level() instanceof ServerLevel level) {
            ItemFrameInvisibility.apply(splashPotion, level, potion.getItem(), hitResult.getLocation());
        }
    }
}
