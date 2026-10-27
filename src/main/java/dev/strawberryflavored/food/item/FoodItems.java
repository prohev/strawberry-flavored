package dev.strawberryflavored.food.item;

import dev.strawberryflavored.StrawberryFlavored;
import dev.strawberryflavored.food.registry.FoodBlocks;
import dev.strawberryflavored.item.Signature;
import eu.pb4.polymer.core.api.item.SimplePolymerItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.sounds.SoundEvents;

/** All food-module items live here; recipes and future blocks can reference this registry. */
public final class FoodItems {
    public static final Item STRAWBERRY = FoodBlocks.STRAWBERRY;
    public static final Item BLUEBERRY = FoodBlocks.BLUEBERRY;
    public static final Item COOKED_RICE = register("cooked_rice", Items.ECHO_SHARD, 5, 0.6f);
    public static final Item ONIGIRI = register("onigiri", Items.ECHO_SHARD, 6, 0.8f);
    public static final Item FLOUR = register("flour", Items.ECHO_SHARD, 0, 0);
    public static final Item DOUGH = register("dough", Items.ECHO_SHARD, 0, 0);
    public static final Item STRAWBERRY_JAM = register("strawberry_jam", Items.ECHO_SHARD, 4, 0.5f);
    public static final Item BLUEBERRY_JAM = register("blueberry_jam", Items.ECHO_SHARD, 4, 0.5f);
    public static final Item SWEET_BERRY_JAM = register("sweet_berry_jam", Items.ECHO_SHARD, 4, 0.5f);
    public static final Item APPLE_JAM = register("apple_jam", Items.ECHO_SHARD, 4, 0.5f);
    public static final Item BAKED_APPLE = register("baked_apple", Items.ECHO_SHARD, 5, 0.6f);
    public static final Item GOLDEN_STRAWBERRY = register("golden_strawberry", Items.ECHO_SHARD, 6, 1.2f);
    public static final Item APPLE_PIE = register("apple_pie", Items.ECHO_SHARD, 6, 0.6f);
    public static final Item BLUEBERRY_PIE = register("blueberry_pie", Items.ECHO_SHARD, 6, 0.6f);
    public static final Item STRAWBERRY_PIE = register("strawberry_pie", Items.ECHO_SHARD, 6, 0.6f);
    public static final Item STRAWBERRY_CAKE = FoodBlocks.STRAWBERRY_CAKE.asItem();
    public static final Item CARROT_CAKE = FoodBlocks.CARROT_CAKE.asItem();
    public static final Item PREMIUM_CAKE = FoodBlocks.PREMIUM_CAKE.asItem();
    public static final Item COOKIE_CAKE = FoodBlocks.COOKIE_CAKE.asItem();
    public static final Item PANCAKE = FoodBlocks.PANCAKE.asItem();
    public static final Item CORN = register("corn", Items.ECHO_SHARD, 3, 0.3f);
    public static final Item POPCORN = register("popcorn", Items.ECHO_SHARD, 4, 0.4f);
    public static final Item MASA = register("masa", Items.ECHO_SHARD, 0, 0);
    public static final Item TORTILLA = register("tortilla", Items.ECHO_SHARD, 3, 0.4f);
    public static final Item PORK_TACO = register("pork_taco", Items.ECHO_SHARD, 8, 0.8f);
    public static final Item STEAK_TACO = register("steak_taco", Items.ECHO_SHARD, 8, 0.8f);
    public static final Item APPLE_FRITTER = register("apple_fritter", Items.ECHO_SHARD, 7, 0.8f);
    public static final Item STRAWBERRY_DANISH = register("strawberry_danish", Items.ECHO_SHARD, 7, 0.8f);
    public static final Item FRIED_EGG = register("fried_egg", Items.COOKED_BEEF, 5, 0.6f);
    public static final Item MILK_BOTTLE = register("milk_bottle", Items.POTION, 0, 0);
    public static final Item STRAWBERRY_MILK = registerMilk("strawberry_milk", 6, 0.8f);
    public static final Item LETTUCE_WRAP = register("lettuce_wrap", Items.COOKED_BEEF, 7, 0.8f);
    public static final Item BERRY_FILLED_CREPES = register("berry_filled_crepes", Items.PUMPKIN_PIE, 8, 0.8f);

    private FoodItems() {}

    private static Item register(String name, Item vanilla, int nutrition, float saturation) {
        return register(name, vanilla, nutrition, saturation, true);
    }

    private static Item register(String name, Item vanilla, int nutrition, float saturation, boolean signed) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, StrawberryFlavored.id(name));
        Item.Properties properties = new Item.Properties().setId(key).stacksTo(64);
        if (signed) properties = Signature.sign(properties);
        if (nutrition > 0) properties = properties.food(new FoodProperties.Builder().nutrition(nutrition).saturationModifier(saturation).build());
        if (name.equals("milk_bottle")) {
            properties = properties.food(new FoodProperties.Builder().nutrition(0).saturationModifier(0).build(),
                    Consumable.builder().consumeSeconds(1.6f).animation(ItemUseAnimation.DRINK)
                            .sound(SoundEvents.GENERIC_DRINK).build());
            return Registry.register(BuiltInRegistries.ITEM, key, new MilkBottleItem(properties, vanilla, name));
        }
        return Registry.register(BuiltInRegistries.ITEM, key, new FoodPolymerItem(properties, vanilla, name));
    }

    private static Item registerMilk(String name, int nutrition, float saturation) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, StrawberryFlavored.id(name));
        Item.Properties properties = Signature.sign(new Item.Properties().setId(key).stacksTo(16)
                .food(new FoodProperties.Builder().nutrition(nutrition).saturationModifier(saturation).build(),
                Consumable.builder().consumeSeconds(1.6f).animation(ItemUseAnimation.DRINK)
                        .sound(SoundEvents.GENERIC_DRINK).build()));
        return Registry.register(BuiltInRegistries.ITEM, key,
                new FoodPolymerItem(properties, Items.POTION, name));
    }

    public static void initialize() { }
}
