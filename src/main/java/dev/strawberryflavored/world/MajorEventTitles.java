package dev.strawberryflavored.world;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
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
        ServerTickEvents.END_SERVER_TICK.register(MajorEventTitles::tick);
    }

    private static void tick(MinecraftServer server) {
        for (ServerLevel level : server.getAllLevels()) {
            for (WitherBoss wither : level.getEntitiesOfClass(WitherBoss.class, entitySearchBounds(level), entity -> true)) {
                if (REVEALED_WITHERS.add(wither.getUUID())) {
                    showTitleToAllPlayers(server,
                            Component.literal("THE WITHER HAS AWAKENED").withStyle(ChatFormatting.DARK_RED, ChatFormatting.BOLD, ChatFormatting.ITALIC),
                            Component.literal("The world trembles").withStyle(ChatFormatting.GOLD, ChatFormatting.ITALIC));
                }
            }
        }

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            if (player.level().dimension() == Level.END) {
                if (hasDragon(player.level()) && SHOWN_END_DRAGON_ALERTS.add(player.getUUID())) {
                    showTitle(player,
                            Component.literal("THE END").withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.BOLD, ChatFormatting.ITALIC),
                            Component.literal("The dragon watches").withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.ITALIC));
                }
            } else {
                SHOWN_END_DRAGON_ALERTS.remove(player.getUUID());
            }
        }
    }

    private static boolean hasDragon(ServerLevel level) {
        return !level.getEntitiesOfClass(EnderDragon.class, entitySearchBounds(level), entity -> true).isEmpty();
    }

    /**
     * EntitySectionStorage requires finite coordinates when converting an AABB
     * to section bounds. The world border is the finite horizontal extent in
     * which entities can normally exist; using the dimension's build height
     * keeps the vertical extent finite as well.
     */
    private static AABB entitySearchBounds(ServerLevel level) {
        var border = level.getWorldBorder();
        var dimensionType = level.dimensionType();
        return new AABB(
                border.getMinX(),
                dimensionType.minY(),
                border.getMinZ(),
                border.getMaxX(),
                dimensionType.minY() + dimensionType.height(),
                border.getMaxZ());
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
