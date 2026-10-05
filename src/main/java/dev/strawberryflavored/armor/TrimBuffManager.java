package dev.strawberryflavored.armor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import net.minecraft.world.item.equipment.trim.TrimMaterial;

public final class TrimBuffManager {
    private static final int EFFECT_DURATION_TICKS = 60;
    private static final int REFRESH_THRESHOLD_TICKS = 20;

    // Tracks only effects that this mod actually supplied to a player.
    private static final Map<UUID, Set<Holder<MobEffect>>> OWNED_EFFECTS = new HashMap<>();

    private TrimBuffManager() {
    }

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(TrimBuffManager::tick);
    }

    private static void tick(MinecraftServer server) {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            updatePlayer(player);
        }

        OWNED_EFFECTS.keySet().removeIf(uuid -> server.getPlayerList().getPlayer(uuid) == null);
    }

    private static void updatePlayer(ServerPlayer player) {
        Holder<TrimMaterial> material = getFullSetMaterial(player);
        Set<Holder<MobEffect>> requiredEffects = TrimBuffs.getEffects(material);
        Set<Holder<MobEffect>> ownedEffects = OWNED_EFFECTS.computeIfAbsent(
                player.getUUID(),
                ignored -> new HashSet<>()
        );

        for (Holder<MobEffect> effect : requiredEffects) {
            MobEffectInstance current = player.getEffect(effect);

            if (current == null) {
                player.addEffect(new MobEffectInstance(
                        effect,
                        EFFECT_DURATION_TICKS,
                        0,
                        true,
                        false,
                        true
                ));
                ownedEffects.add(effect);
            } else if (ownedEffects.contains(effect) && current.getDuration() <= REFRESH_THRESHOLD_TICKS) {
                player.addEffect(new MobEffectInstance(
                        effect,
                        EFFECT_DURATION_TICKS,
                        0,
                        true,
                        false,
                        true
                ));
            }
        }

        ownedEffects.removeIf(effect -> {
            if (requiredEffects.contains(effect)) {
                return false;
            }

            MobEffectInstance current = player.getEffect(effect);
            if (current != null && current.getDuration() <= EFFECT_DURATION_TICKS) {
                player.removeEffect(effect);
            }

            return true;
        });

        if (ownedEffects.isEmpty()) {
            OWNED_EFFECTS.remove(player.getUUID());
        }
    }

    // full set detector
    private static Holder<TrimMaterial> getFullSetMaterial(ServerPlayer player) {
        Holder<TrimMaterial> helmet = getTrimMaterial(
                player.getItemBySlot(EquipmentSlot.HEAD)
        );
        Holder<TrimMaterial> chestplate = getTrimMaterial(
                player.getItemBySlot(EquipmentSlot.CHEST)
        );
        Holder<TrimMaterial> leggings = getTrimMaterial(
                player.getItemBySlot(EquipmentSlot.LEGS)
        );
        Holder<TrimMaterial> boots = getTrimMaterial(
                player.getItemBySlot(EquipmentSlot.FEET)
        );

        if (helmet == null || chestplate == null || leggings == null || boots == null) {
            return null;
        }

        if (!helmet.equals(chestplate)
                || !helmet.equals(leggings)
                || !helmet.equals(boots)) {
            return null;
        }

        return helmet;
    }

    private static Holder<TrimMaterial> getTrimMaterial(ItemStack stack) {
        ArmorTrim trim = stack.get(DataComponents.TRIM);
        return trim == null ? null : trim.material();
    }
}
