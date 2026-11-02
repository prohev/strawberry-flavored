package dev.strawberryflavored.mixin;

import dev.strawberryflavored.item.SculkRevolverItem;
import net.minecraft.network.protocol.game.ServerboundAttackPacket;
import net.minecraft.network.protocol.game.ServerboundPunchPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.InteractionHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class SculkRevolverSwingMixin {
    @Inject(method = "handleAttack", at = @At("HEAD"), cancellable = true)
    private void strawberryFlavored$fireRevolver(ServerboundAttackPacket packet, CallbackInfo info) {
        ServerPlayer player = ((ServerGamePacketListenerImpl) (Object) this).player;
        if (player.getMainHandItem().getItem() instanceof SculkRevolverItem
            && !player.isUsingItem() && !player.isShiftKeyDown()) { player.level().getServer().execute(() -> SculkRevolverItem.shoot(player, player.getMainHandItem())); info.cancel(); }
    }

    @Inject(method = "handlePunch", at = @At("HEAD"), cancellable = true)
    private void strawberryFlavored$fireRevolverInAir(ServerboundPunchPacket packet, CallbackInfo info) {
        ServerPlayer player = ((ServerGamePacketListenerImpl) (Object) this).player;
        if (player.getMainHandItem().getItem() instanceof SculkRevolverItem
                && !player.isUsingItem() && !player.isShiftKeyDown()) { player.level().getServer().execute(() -> SculkRevolverItem.shoot(player, player.getMainHandItem())); info.cancel(); }
    }

}
