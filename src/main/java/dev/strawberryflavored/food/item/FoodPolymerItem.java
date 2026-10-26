package dev.strawberryflavored.food.item;

import dev.strawberryflavored.StrawberryFlavored;
import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.core.api.item.SimplePolymerItem;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/** A food item whose client-side representation uses this mod's item model. */
public class FoodPolymerItem extends SimplePolymerItem implements PolymerItem {
    private final String modelName;

    public FoodPolymerItem(Item.Properties properties, Item vanilla, String modelName) {
        super(properties, vanilla);
        this.modelName = modelName;
    }

    @Override
    public Identifier getPolymerItemModel(ItemStack stack, PacketContext context, HolderLookup.Provider lookup) {
        return StrawberryFlavored.id("food/" + modelName);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
        ItemStack result = super.finishUsingItem(stack, level, user);
        if (modelName.equals("strawberry_milk")) {
            return giveGlassBottle(result, level, user);
        }
        return result;
    }

    protected static ItemStack giveGlassBottle(ItemStack result, Level level, LivingEntity user) {
        ItemStack bottle = new ItemStack(net.minecraft.world.item.Items.GLASS_BOTTLE);
        if (result.isEmpty()) return bottle;
        if (user instanceof Player player && !level.isClientSide()) {
            if (!player.getInventory().add(bottle)) {
                level.addFreshEntity(new ItemEntity(level, player.getX(), player.getY(), player.getZ(), bottle));
            }
        }
        return result;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return modelName.equals("strawberry_milk") || modelName.equals("milk_bottle")
                ? ItemUseAnimation.DRINK : super.getUseAnimation(stack);
    }

}
