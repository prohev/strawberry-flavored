package dev.strawberryflavored.world;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityLevelChangeEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public final class MajorEventTitles {
    private static final int TITLE_FADE_IN = 10;
    private static final int TITLE_STAY = 75;
    private static final int TITLE_FADE_OUT = 30;
    private static final Set<UUID> REVEALED_WITHERS = new HashSet<>();
    private static final Set<UUID> SHOWN_END_DRAGON_ALERTS = new HashSet<>();

    private MajorEventTitles() {
    }

    public static void register() {
        ServerEntityEvents.ENTITY_LOAD.register((entity, level) -> {
            if (entity instanceof WitherBoss wither && REVEALED_WITHERS.add(wither.getUUID())) {
                showTitleToAllPlayers(level.getServer(),
                        Component.literal("THE WITHER HAS AWAKENED").withStyle(ChatFormatting.DARK_RED, ChatFormatting.BOLD, ChatFormatting.ITALIC),
                        Component.literal("The world trembles").withStyle(ChatFormatting.GOLD, ChatFormatting.ITALIC));
            }

            if (entity instanceof EnderDragon dragon && level.dimension() == Level.END) {
                for (ServerPlayer player : level.players()) {
                    showDragonAlert(player);
                }
            }
        });

        ServerEntityLevelChangeEvents.AFTER_PLAYER_CHANGE_LEVEL.register((player, origin, destination) -> {
            if (destination.dimension() == Level.END) {
                if (hasDragon(destination)) {
                    showDragonAlert(player);
                }
            } else {
                SHOWN_END_DRAGON_ALERTS.remove(player.getUUID());
            }
        });
    }

    private static void showDragonAlert(ServerPlayer player) {
        if (SHOWN_END_DRAGON_ALERTS.add(player.getUUID())) {
            showTitle(player,
                    Component.literal("THE END").withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.BOLD, ChatFormatting.ITALIC),
                    Component.literal("The dragon watches").withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.ITALIC));
        }
    }

    private static boolean hasDragon(ServerLevel level) {
        var dimensionType = level.dimensionType();
        return !level.getEntitiesOfClass(EnderDragon.class,
                new AABB(-256, dimensionType.minY(), -256, 256,
                        dimensionType.minY() + dimensionType.height(), 256),
                entity -> true).isEmpty();
    }

    private static void showTitleToAllPlayers(MinecraftServer server, Component title, Component subtitle) {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            showTitle(player, title, subtitle);
        }
    }

    private static void showTitle(ServerPlayer player, Component title, Component subtitle) {
        player.connection.send(new ClientboundSetTitlesAnimationPacket(TITLE_FADE_IN, TITLE_STAY, TITLE_FADE_OUT));
        player.connection.send(new ClientboundSetTitleTextPacket(title));
        player.connection.send(new ClientboundSetSubtitleTextPacket(subtitle));
    }
}
