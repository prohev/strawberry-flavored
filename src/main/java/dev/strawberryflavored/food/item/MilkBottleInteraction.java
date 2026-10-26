package dev.strawberryflavored.food.item;

import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.animal.cow.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.item.ItemEntity;

/** Collects a small serving of milk without changing vanilla bucket milking. */
public final class MilkBottleInteraction {
    private MilkBottleInteraction() {}

    public static void register() {
        UseEntityCallback.EVENT.register((player, level, hand, entity, hitResult) -> {
            if (!(entity instanceof Cow) || player.isSpectator()) return InteractionResult.PASS;
            ItemStack held = player.getItemInHand(hand);
            if (!held.is(Items.GLASS_BOTTLE)) return InteractionResult.PASS;
            if (level.isClientSide()) return InteractionResult.SUCCESS;

            ItemStack result = new ItemStack(FoodItems.MILK_BOTTLE);
            held.shrink(1);
            if (!player.getInventory().add(result)) {
                level.addFreshEntity(new ItemEntity(level, player.getX(), player.getY(), player.getZ(), result));
            }
            return InteractionResult.SUCCESS;
        });
    }
}
