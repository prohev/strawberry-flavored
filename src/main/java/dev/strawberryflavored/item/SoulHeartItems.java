package dev.strawberryflavored.item;

import dev.strawberryflavored.states.SoulHeartState;
import eu.pb4.polymer.core.api.item.SimplePolymerItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.food.FoodProperties;

public final class SoulHeartItems {
    public static Item SOUL_SHARD;
    public static Item SOUL_HEART;
    private SoulHeartItems() {}

    public static void initialize() {
        ResourceKey<Item> shardKey = ResourceKey.create(Registries.ITEM, SoulHeartState.id("soul_shard"));
        SOUL_SHARD = Registry.register(BuiltInRegistries.ITEM, shardKey, new SimplePolymerItem(Signature.sign(new Item.Properties().setId(shardKey).rarity(Rarity.RARE).component(DataComponents.ITEM_NAME, Component.literal("Soul Shard").withStyle(ChatFormatting.AQUA))), Items.ECHO_SHARD, true));
        ResourceKey<Item> heartKey = ResourceKey.create(Registries.ITEM, SoulHeartState.id("soul_heart"));
        SOUL_HEART = Registry.register(BuiltInRegistries.ITEM, heartKey, new SoulHeartItem(Signature.sign(new Item.Properties().setId(heartKey).stacksTo(16).rarity(Rarity.EPIC).food(new FoodProperties.Builder().alwaysEdible().build()).component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true).component(DataComponents.ITEM_NAME, Component.literal("Soul Heart").withStyle(ChatFormatting.BLUE)))));
    }
}
