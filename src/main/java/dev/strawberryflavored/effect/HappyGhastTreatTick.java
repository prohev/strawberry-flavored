package dev.strawberryflavored.effect;

import dev.strawberryflavored.StrawberryFlavored;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.happyghast.HappyGhast;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public final class HappyGhastTreatTick {

    private static final int DURATION_TICKS = 5 * 60 * 20;
    private static final double SPEED_INCREASE = 0.03;

    private static final Identifier SPEED_MODIFIER_ID =
            StrawberryFlavored.id("happy_ghast_treat_speed");

    private static final Map<UUID, HappyGhast> ACTIVE_GHASTS = new HashMap<>();
    private static final Map<UUID, Integer> REMAINING_TICKS = new HashMap<>();

    static {
        ServerTickEvents.END_SERVER_TICK.register(server -> tick());
    }

    private HappyGhastTreatTick() {
    }

    public static void applySpeed(HappyGhast happyGhast) {
        AttributeInstance flyingSpeed =
                happyGhast.getAttribute(Attributes.FLYING_SPEED);

        if (flyingSpeed == null) {
            return;
        }

        // Remove the old modifier if the ghast was already treated.
        flyingSpeed.removeModifier(SPEED_MODIFIER_ID);

        // Happy Ghast normally has 0.05 flying speed.
        // Adding SPEED_INCREASE makes it 0.08
        flyingSpeed.addTransientModifier(
                new AttributeModifier(
                        SPEED_MODIFIER_ID,
                        SPEED_INCREASE,
                        AttributeModifier.Operation.ADD_VALUE
                )
        );

        UUID uuid = happyGhast.getUUID();

        ACTIVE_GHASTS.put(uuid, happyGhast);
        REMAINING_TICKS.put(uuid, DURATION_TICKS);
    }

    private static void tick() {
        Iterator<Map.Entry<UUID, Integer>> iterator =
                REMAINING_TICKS.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<UUID, Integer> entry = iterator.next();

            UUID uuid = entry.getKey();
            int remaining = entry.getValue() - 1;

            HappyGhast happyGhast = ACTIVE_GHASTS.get(uuid);

            if (happyGhast == null || happyGhast.isRemoved()) {
                ACTIVE_GHASTS.remove(uuid);
                iterator.remove();
                continue;
            }

            if (remaining <= 0) {
                AttributeInstance flyingSpeed =
                        happyGhast.getAttribute(Attributes.FLYING_SPEED);

                if (flyingSpeed != null) {
                    flyingSpeed.removeModifier(SPEED_MODIFIER_ID);
                }

                ACTIVE_GHASTS.remove(uuid);
                iterator.remove();
            } else {
                entry.setValue(remaining);
            }
        }
    }
}