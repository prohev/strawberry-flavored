package dev.strawberryflavored.processing.registry;

import dev.strawberryflavored.StrawberryFlavored;
import dev.strawberryflavored.food.registry.FoodBlocks;
import dev.strawberryflavored.processing.block.FoodMachineBlockEntity;
import dev.strawberryflavored.processing.block.FoodMachineBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.BlockEntityType;
import eu.pb4.polymer.core.api.block.PolymerBlockUtils;

import java.util.Set;

public final class FoodBlockEntities {
    private static final ResourceKey<BlockEntityType<?>> OVEN_KEY =
            ResourceKey.create(Registries.BLOCK_ENTITY_TYPE, StrawberryFlavored.id("oven"));
    private static final ResourceKey<BlockEntityType<?>> MILLSTONE_KEY =
            ResourceKey.create(Registries.BLOCK_ENTITY_TYPE, StrawberryFlavored.id("millstone"));

    public static final BlockEntityType<FoodMachineBlockEntity> OVEN = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE, OVEN_KEY,
            new BlockEntityType<>((pos, state) -> new FoodMachineBlockEntity(FoodBlockEntities.OVEN, pos, state, FoodMachineBlock.Station.OVEN), Set.of(FoodBlocks.OVEN)));
    public static final BlockEntityType<FoodMachineBlockEntity> MILLSTONE = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE, MILLSTONE_KEY,
            new BlockEntityType<>((pos, state) -> new FoodMachineBlockEntity(FoodBlockEntities.MILLSTONE, pos, state, FoodMachineBlock.Station.MILLSTONE), Set.of(FoodBlocks.MILLSTONE)));
    public static final BlockEntityType<FoodMachineBlockEntity> POT = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE, ResourceKey.create(Registries.BLOCK_ENTITY_TYPE, StrawberryFlavored.id("pot")),
            new BlockEntityType<>((pos, state) -> new FoodMachineBlockEntity(FoodBlockEntities.POT, pos, state, FoodMachineBlock.Station.POT), Set.of(FoodBlocks.POT)));

    public static BlockEntityType<FoodMachineBlockEntity> typeFor(FoodMachineBlock.Station station) {
        return switch (station) { case OVEN -> OVEN; case MILLSTONE -> MILLSTONE; case POT -> POT; };
    }

    private FoodBlockEntities() {}

    public static void initialize() {
        // These types exist only on the server. Polymer synchronizes the
        // represented blocks and menus, while registry-sync must not require
        // an identically modded client to know these implementation types.
        PolymerBlockUtils.registerBlockEntity(OVEN, MILLSTONE, POT);
    }
}
