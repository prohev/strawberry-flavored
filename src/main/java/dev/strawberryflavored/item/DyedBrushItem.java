package dev.strawberryflavored.item;

import dev.strawberryflavored.StrawberryFlavored;
import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.core.api.item.SimplePolymerItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class DyedBrushItem extends SimplePolymerItem implements PolymerItem {
    public static final int MAX_USES = 64;

    private final DyeColor color;

    public DyedBrushItem(DyeColor color, Item.Properties settings) {
        super(settings, Items.BRUSH);
        this.color = color;
    }

    public DyeColor color() {
        return color;
    }

    public static ResourceKey<Item> id(DyeColor color) {
        return ResourceKey.create(Registries.ITEM, StrawberryFlavored.id(color.getName() + "_brush"));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        Player player = context.getPlayer();

        if (DyedBrushPalettes.recolor(state.getBlock(), color) == null) {
            return InteractionResult.PASS;
        }

        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        if (!(level instanceof ServerLevel serverLevel)) {
            return InteractionResult.PASS;
        }

        if (player != null && !player.mayInteract(serverLevel, pos)) {
            return InteractionResult.FAIL;
        }

        if (!DyedBrushRecolor.apply(serverLevel, pos, state, color)) {
            return InteractionResult.PASS;
        }

        ItemStack stack = context.getItemInHand();
        if (player == null || !player.hasInfiniteMaterials()) {
            stack.hurtAndBreak(1, serverLevel, player instanceof ServerPlayer serverPlayer ? serverPlayer : null, item -> {
                if (player != null) {
                    player.onEquippedItemBroken(item, context.getHand() == net.minecraft.world.InteractionHand.MAIN_HAND
                            ? EquipmentSlot.MAINHAND
                            : EquipmentSlot.OFFHAND);
                }
            });
        }

        return InteractionResult.SUCCESS;
    }

    public static Item.Properties createProperties(DyeColor color) {
        return Signature.sign(new Item.Properties()
                .setId(id(color))
                .durability(MAX_USES));
    }
}
