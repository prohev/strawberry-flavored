package dev.strawberryflavored.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Items;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.server.level.ServerLevel;

@Mixin(LivingEntity.class)
public abstract class VoidTotemMixin {
    @org.spongepowered.asm.mixin.Unique
    private int strawberryFlavored$voidImmunityTicks;

    @org.spongepowered.asm.mixin.Shadow
    abstract boolean checkTotemDeathProtection(DamageSource source);

    @Inject(method = "hurtServer", at = @At("HEAD"), cancellable = true)
    private void strawberryFlavored$useTotemOnVoidTick(
            ServerLevel level, DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfo) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (source.is(DamageTypes.FELL_OUT_OF_WORLD) && this.strawberryFlavored$voidImmunityTicks > 0) {
            callbackInfo.setReturnValue(true);
        } else if (source.is(DamageTypes.FELL_OUT_OF_WORLD)
                && (self.getItemInHand(InteractionHand.MAIN_HAND).is(Items.TOTEM_OF_UNDYING)
                || self.getItemInHand(InteractionHand.OFF_HAND).is(Items.TOTEM_OF_UNDYING))
                && this.checkTotemDeathProtection(source)) {
            this.strawberryFlavored$voidImmunityTicks = 60;
            callbackInfo.setReturnValue(true);
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void strawberryFlavored$tickVoidImmunity(org.spongepowered.asm.mixin.injection.callback.CallbackInfo callbackInfo) {
        if (this.strawberryFlavored$voidImmunityTicks > 0) {
            this.strawberryFlavored$voidImmunityTicks--;
        }
    }

    @Redirect(
            method = "checkTotemDeathProtection",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/DamageSource;is(Lnet/minecraft/tags/TagKey;)Z")
    )
    private boolean strawberryFlavored$allowVoidTotem(DamageSource source, TagKey<DamageType> tag) {
        return !source.is(DamageTypes.FELL_OUT_OF_WORLD) && source.is(tag);
    }

    @Inject(method = "checkTotemDeathProtection", at = @At("RETURN"))
    private void strawberryFlavored$applyVoidEffects(
            DamageSource source, CallbackInfoReturnable<Boolean> callbackInfo) {
        if (callbackInfo.getReturnValue() && source.is(DamageTypes.FELL_OUT_OF_WORLD)) {
            LivingEntity self = (LivingEntity) (Object) this;
            self.setHealth(self.getMaxHealth());
            self.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 200, 15));
            self.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 200, 0));
        }
    }
}
