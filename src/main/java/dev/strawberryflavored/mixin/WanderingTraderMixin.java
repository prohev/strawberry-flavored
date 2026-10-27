package dev.strawberryflavored.mixin;

import dev.strawberryflavored.food.registry.FoodBlocks;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WanderingTrader.class)
public abstract class WanderingTraderMixin {
    @Inject(method = "updateTrades", at = @At("TAIL"))
    private void strawberryFlavored$addFarmSeeds(net.minecraft.server.level.ServerLevel level, CallbackInfo info) {
        WanderingTrader trader = (WanderingTrader) (Object) this;
        trader.getOffers().add(new MerchantOffer(new ItemCost(Items.EMERALD, 1),
                new ItemStack(FoodBlocks.CORN_KERNELS, 2), 12, 1, 0.05f));
        trader.getOffers().add(new MerchantOffer(new ItemCost(Items.EMERALD, 1),
                new ItemStack(FoodBlocks.RICE_ITEM, 1), 12, 1, 0.05f));
        trader.getOffers().add(new MerchantOffer(new ItemCost(Items.EMERALD, 1),
                new ItemStack(FoodBlocks.STRAWBERRY, 2), 12, 1, 0.05f));
        trader.getOffers().add(new MerchantOffer(new ItemCost(Items.EMERALD, 1),
                new ItemStack(FoodBlocks.BLUEBERRY, 2), 12, 1, 0.05f));
    }
}
