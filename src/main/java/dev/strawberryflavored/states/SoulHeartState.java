package dev.strawberryflavored.states;

import com.mojang.serialization.Codec;

import dev.strawberryflavored.StrawberryFlavored;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Objects;

public final class SoulHeartState {
    public static final AttachmentType<Boolean> PROTECTED = Objects.requireNonNull(AttachmentRegistry.create(StrawberryFlavored.id("protected"), b -> b.persistent(Codec.BOOL)));
    public static final AttachmentType<Boolean> RESTORE = Objects.requireNonNull(AttachmentRegistry.create(StrawberryFlavored.id("restore_inventory"), b -> b.persistent(Codec.BOOL)));
    private SoulHeartState() {}
    public static boolean isProtected(Player player) { return player.getAttachedOrElse(PROTECTED, false); }
    public static void setProtected(ServerPlayer player, boolean value) { if (value) player.setAttached(PROTECTED, true); else player.removeAttached(PROTECTED); refreshTab(player); }
    public static void refreshTab(ServerPlayer player) { player.level().getServer().getPlayerList().broadcastAll(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME, player)); }
    public static Identifier id(String path) { return StrawberryFlavored.id(path); }
}
