package dev.strawberryflavored.item;

import dev.strawberryflavored.StrawberryFlavored;
import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.core.api.item.SimplePolymerItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.equipment.Equippable;
import xyz.nucleoid.packettweaker.PacketContext;

public class FlowerCrownItem extends SimplePolymerItem implements PolymerItem {
    public static final ResourceKey<Item> ID = ResourceKey.create(
            Registries.ITEM,
            StrawberryFlavored.id("flower_crown")
    );

    public static final ResourceKey<net.minecraft.world.item.equipment.EquipmentAsset> EQUIPMENT_ASSET =
            ResourceKey.create(
                    net.minecraft.world.item.equipment.EquipmentAssets.ROOT_ID,
                    StrawberryFlavored.id("flower_crown")
            );

    public FlowerCrownItem(Item.Properties settings) {
        super(settings, Items.PAPER);
    }

    @Override
    public Identifier getPolymerItemModel(ItemStack stack, PacketContext context) {
        return StrawberryFlavored.id("item/flower_crown");
    }

    public static Item.Properties createProperties() {
        return new Item.Properties()
                .setId(ID)
                .stacksTo(1)
                .component(
                        net.minecraft.core.component.DataComponents.EQUIPPABLE,
                        Equippable.builder(EquipmentSlot.HEAD)
                                .setEquipSound(SoundEvents.ARMOR_EQUIP_LEATHER)
                                .setAsset(EQUIPMENT_ASSET)
                                .setDispensable(true)
                                .setSwappable(true)
                                .build()
                );
    }
}
