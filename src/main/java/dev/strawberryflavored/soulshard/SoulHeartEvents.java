package dev.strawberryflavored.soulshard;

import dev.strawberryflavored.soulshard.SoulHeartState;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import java.util.Set;

public final class SoulHeartEvents {
    private static final Set<Identifier> SHARD_LOOT = Set.of(Identifier.withDefaultNamespace("chests/ancient_city"), Identifier.withDefaultNamespace("chests/ancient_city_ice_box"));
    private SoulHeartEvents() {}
    public static void register() {
        LootTableEvents.MODIFY.register((key, builder, source, registries) -> {
            if (source.isBuiltin() && SHARD_LOOT.contains(key.identifier())) {
                builder.withPool(LootPool.lootPool().when(LootItemRandomChanceCondition.randomChance(0.10f)).add(LootItem.lootTableItem(SoulHeartItems.SOUL_SHARD)));
            }
        });
        ServerLivingEntityEvents.AFTER_DEATH.register((entity, source) -> {
            if (entity instanceof ServerPlayer player && SoulHeartState.isProtected(player)) {
                player.setAttached(SoulHeartState.RESTORE, true);
                SoulHeartState.setProtected(player, false);
                var level = player.level();
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 1.0f, 1.0f);
                level.sendParticles(ParticleTypes.SCULK_SOUL, player.getX(), player.getY() + 1.0, player.getZ(), 30, 0.4, 0.6, 0.4, 0.02);
                level.getServer().getPlayerList().broadcastSystemMessage(Component.literal(player.getName().getString() + "'s soul was protected").withStyle(ChatFormatting.AQUA), false);
            }
        });
    }
}
