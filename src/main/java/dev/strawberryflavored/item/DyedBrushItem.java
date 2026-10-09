package dev.strawberryflavored.item;

import dev.strawberryflavored.StrawberryFlavored;
import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.core.api.item.SimplePolymerItem;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class DyedBrushItem extends SimplePolymerItem implements PolymerItem {
    public static final int MAX_USES = 64;
    private static final int USE_DURATION = 200;
    private static final int BRUSH_INTERVAL = 10;
    private static final int BRUSH_OFFSET = 5;

    private final DyeColor color;

    public DyedBrushItem(DyeColor color, Item.Properties settings) {
        super(settings, Items.BRUSH);
        this.color = color;
    }

    public DyeColor color() {
        return color;
    }

    @Override
    public Identifier getPolymerItemModel(
            ItemStack stack,
            PacketContext context,
            HolderLookup.Provider lookup
    ) {
        return StrawberryFlavored.id("brushes/" + color.getName() + "_brush");
    }

    public static ResourceKey<Item> id(DyeColor color) {
        return ResourceKey.create(Registries.ITEM, StrawberryFlavored.id(color.getName() + "_brush"));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player != null && calculateHitResult(player).getType() == HitResult.Type.BLOCK) {
            player.startUsingItem(context.getHand());
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.BRUSH;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return USE_DURATION;
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingUseDuration) {
        if (remainingUseDuration < 0 || !(entity instanceof Player player)) {
            entity.releaseUsingItem();
            return;
        }

        HitResult hitResult = calculateHitResult(player);
        if (!(hitResult instanceof BlockHitResult blockHitResult)
                || hitResult.getType() != HitResult.Type.BLOCK) {
            entity.releaseUsingItem();
            return;
        }

        int elapsedUseDuration = getUseDuration(stack, entity) - remainingUseDuration + 1;
        if (elapsedUseDuration % BRUSH_INTERVAL != BRUSH_OFFSET
                || !(level instanceof ServerLevel serverLevel)) {
            return;
        }

        BlockPos pos = blockHitResult.getBlockPos();
        BlockState state = serverLevel.getBlockState(pos);
        if (!player.mayInteract(serverLevel, pos)
                || !DyedBrushRecolor.apply(serverLevel, pos, state, color)) {
            return;
        }

        if (!player.hasInfiniteMaterials()) {
            InteractionHand hand = entity.getUsedItemHand();
            ServerPlayer serverPlayer = player instanceof ServerPlayer currentPlayer ? currentPlayer : null;
            stack.hurtAndBreak(1, serverLevel, serverPlayer, item -> player.onEquippedItemBroken(
                    item,
                    hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND
            ));
        }
    }

    private static HitResult calculateHitResult(Player player) {
        return ProjectileUtil.getHitResultOnViewVector(
                player,
                EntitySelector.CAN_BE_PICKED,
                player.blockInteractionRange()
        );
    }

    public static Item.Properties createProperties(DyeColor color) {
        return Signature.sign(new Item.Properties()
                .setId(id(color))
                .durability(MAX_USES));
    }
}
