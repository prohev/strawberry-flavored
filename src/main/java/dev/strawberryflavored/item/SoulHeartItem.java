package dev.strawberryflavored.item;

import dev.strawberryflavored.states.SoulHeartState;
import eu.pb4.polymer.core.api.item.SimplePolymerItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class SoulHeartItem extends SimplePolymerItem {
    public SoulHeartItem(Item.Properties properties) { super(properties, Items.GOLDEN_APPLE, true); }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (SoulHeartState.isProtected(player)) {
            if (player instanceof ServerPlayer sp) {
                sp.sendSystemMessage(Component.literal("Your soul is already protected.").withStyle(ChatFormatting.GRAY), true);
            }
            return InteractionResult.FAIL;
        }
        return super.use(level, player, hand);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack result = super.finishUsingItem(stack, level, entity);
        if (entity instanceof ServerPlayer player) {
            SoulHeartState.setProtected(player, true);
            player.sendSystemMessage(Component.literal("Your soul is now protected").withStyle(ChatFormatting.BLUE), false);
            var serverLevel = player.level();
            serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 1.0f, 0.8f);
            serverLevel.sendParticles(ParticleTypes.SCULK_SOUL, player.getX(), player.getY() + 1.0, player.getZ(), 20, 0.4, 0.6, 0.4, 0.02);
        }
        return result;
    }
}
