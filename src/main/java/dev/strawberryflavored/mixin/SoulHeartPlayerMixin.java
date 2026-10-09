package dev.strawberryflavored.mixin;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.strawberryflavored.states.SoulHeartState;
@Mixin(Player.class)
public abstract class SoulHeartPlayerMixin {
    @Inject(method = "dropEquipment", at = @At("HEAD"), cancellable = true)
    private void strawberry$keepInventory(ServerLevel level, CallbackInfo ci) { Player self = (Player) (Object) this; if (self instanceof ServerPlayer && SoulHeartState.isProtected(self)) ci.cancel(); }
}
