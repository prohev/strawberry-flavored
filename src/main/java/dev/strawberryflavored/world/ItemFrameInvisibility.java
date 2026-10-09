package dev.strawberryflavored.world;

import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownSplashPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class ItemFrameInvisibility {
    private static final double SPLASH_RANGE = 4.0;
    private static final double SPLASH_RANGE_SQUARED = SPLASH_RANGE * SPLASH_RANGE;

    private ItemFrameInvisibility() {
    }

    public static void apply(ThrownSplashPotion potion, ServerLevel level, ItemStack stack, Vec3 impact) {
        PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
        if (contents == null) {
            return;
        }

        boolean invisible = contents.is(Potions.INVISIBILITY) || contents.is(Potions.LONG_INVISIBILITY);
        boolean visible = contents.is(Potions.WATER);
        if (!invisible && !visible) {
            return;
        }

        AABB affectedArea = potion.getBoundingBox().inflate(SPLASH_RANGE, 2.0, SPLASH_RANGE);
        for (ItemFrame frame : level.getEntitiesOfClass(ItemFrame.class, affectedArea)) {
            if (frame.distanceToSqr(impact) < SPLASH_RANGE_SQUARED && frame.isInvisible() != invisible) {
                frame.setInvisible(invisible);
            }
        }
    }
}
