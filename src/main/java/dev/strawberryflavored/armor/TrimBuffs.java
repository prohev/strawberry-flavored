package dev.strawberryflavored.armor;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimMaterials;

public final class TrimBuffs {
    private TrimBuffs() {
    }

    public static Set<Holder<MobEffect>> getEffects(Holder<TrimMaterial> material) {
        if (material == null) {
            return Collections.emptySet();
        }

        Set<Holder<MobEffect>> effects = new HashSet<>();

        if (material.is(TrimMaterials.REDSTONE)) {
            effects.add(MobEffects.REGENERATION);
        } else if (material.is(TrimMaterials.LAPIS)
                || material.is(TrimMaterials.QUARTZ)) {
            effects.add(MobEffects.SPEED);
        }

        return effects;
    }
}
