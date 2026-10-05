package dev.strawberryflavored.item;

import dev.strawberryflavored.StrawberryFlavored;
import dev.strawberryflavored.effect.HappyGhastTreatTick;
import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.core.api.item.SimplePolymerItem;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.happyghast.HappyGhast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class HappyGhastTreatItem extends SimplePolymerItem implements PolymerItem {

    public static final ResourceKey<Item> ID = ResourceKey.create(
            Registries.ITEM,
            StrawberryFlavored.id("happy_ghast_treat")
    );

    public HappyGhastTreatItem(Item.Properties settings) {
        super(settings, Items.ECHO_SHARD);
    }

    @Override
    public Identifier getPolymerItemModel(
            ItemStack stack,
            PacketContext context,
            HolderLookup.Provider lookup
    ) {
        return StrawberryFlavored.id("happy_ghast_treat");
    }

    @Override
    public InteractionResult interactLivingEntity(
            ItemStack stack,
            Player player,
            LivingEntity target,
            InteractionHand hand
    ) {
        if (!(target instanceof HappyGhast happyGhast)) {
            return InteractionResult.PASS;
        }

        if (!player.level().isClientSide()) {
            HappyGhastTreatTick.applySpeed(happyGhast);

            stack.shrink(1);
        }

        return InteractionResult.SUCCESS;
    }

    public static Item.Properties createProperties() {
        return Signature.sign(new Item.Properties()
                .setId(ID)
                .stacksTo(16));
    }
}