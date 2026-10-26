package dev.strawberryflavored.food.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/** A drinkable milk serving that clears effects like a vanilla milk bucket. */
public final class MilkBottleItem extends FoodPolymerItem {
    public MilkBottleItem(Properties properties, net.minecraft.world.item.Item vanilla, String modelName) {
        super(properties, vanilla, modelName);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
        ItemStack result = super.finishUsingItem(stack, level, user);
        user.removeAllEffects();
        return giveGlassBottle(result, level, user);
    }
}
