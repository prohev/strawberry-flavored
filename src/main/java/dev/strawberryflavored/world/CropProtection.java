package dev.strawberryflavored.world;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

public final class CropProtection {
    private CropProtection() {
    }

    public static boolean preventsTrampling(LivingEntity entity) {
        ItemStack boots = entity.getItemBySlot(EquipmentSlot.FEET);

        // Leather boots inherently protect crops from trampling.
        if (boots.is(Items.LEATHER_BOOTS)) {
            return true;
        }

        // Any level of Feather Falling on the boots provides the same protection.
        Holder<Enchantment> featherFalling = entity.level()
                .registryAccess()
                .lookupOrThrow(Registries.ENCHANTMENT)
                .getOrThrow(Enchantments.FEATHER_FALLING);

        return EnchantmentHelper.getItemEnchantmentLevel(featherFalling, boots) > 0;
    }
}
