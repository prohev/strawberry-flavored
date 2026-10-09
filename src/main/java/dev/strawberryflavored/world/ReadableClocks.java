package dev.strawberryflavored.world;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class ReadableClocks {
    private static final Map<UUID, ClockReading> LAST_READINGS = new HashMap<>();

    private ReadableClocks() {
    }

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                update(player);
            }
        });
    }

    private static void update(ServerPlayer player) {
        if (!player.getMainHandItem().is(Items.CLOCK) && !player.getOffhandItem().is(Items.CLOCK)) {
            if (LAST_READINGS.remove(player.getUUID()) != null) {
                player.sendSystemMessage(Component.empty(), true);
            }
            return;
        }

        long timeOfDay = player.level().getOverworldClockTime();
        int day = (int) Math.floorDiv(timeOfDay, 24_000L) + 1;
        int ticksIntoDay = (int) Math.floorMod(timeOfDay, 24_000L);
        int minute = Math.floorDiv(ticksIntoDay + 250, 500);
        if (minute >= 1_440) {
            minute = 0;
            day++;
        }

        ClockReading reading = new ClockReading(day, minute);
        if (reading.equals(LAST_READINGS.get(player.getUUID()))) {
            return;
        }

        LAST_READINGS.put(player.getUUID(), reading);
        int hour24 = minute / 60;
        int hour12 = hour24 % 12;
        if (hour12 == 0) {
            hour12 = 12;
        }
        String period = hour24 < 12 ? "AM" : "PM";
        player.sendSystemMessage(Component.literal("Day " + day + " — " + hour12 + ":" + String.format("%02d", minute % 60) + " " + period), true);
    }

    private record ClockReading(int day, int minute) {
    }
}
