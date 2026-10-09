package dev.strawberryflavored.mixin;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.PlayerTeam;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.strawberryflavored.states.SoulHeartState;
@Mixin(ServerPlayer.class)
public abstract class SoulHeartServerPlayerMixin {
    @Inject(method = "restoreFrom", at = @At("TAIL"))
    private void strawberry$restoreInventory(ServerPlayer oldPlayer, boolean alive, CallbackInfo ci) { if (!alive && oldPlayer.getAttachedOrElse(SoulHeartState.RESTORE, false)) ((ServerPlayer) (Object) this).getInventory().replaceWith(oldPlayer.getInventory()); }
    @Inject(method = "getTabListDisplayName", at = @At("RETURN"), cancellable = true)
    private void strawberry$tabHeart(CallbackInfoReturnable<Component> cir) { ServerPlayer self = (ServerPlayer) (Object) this; if (SoulHeartState.isProtected(self)) { Component base = cir.getReturnValue(); if (base == null) base = PlayerTeam.formatNameForTeam(self.getTeam(), self.getName()); cir.setReturnValue(Component.empty().append(Component.literal("\u2764 ").withStyle(ChatFormatting.BLUE)).append(base)); } }
}
