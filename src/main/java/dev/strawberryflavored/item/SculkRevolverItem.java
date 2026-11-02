package dev.strawberryflavored.item;

import eu.pb4.polymer.core.api.item.SimplePolymerItem;
import eu.pb4.polymer.core.api.item.PolymerItem;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import dev.strawberryflavored.StrawberryFlavored;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class SculkRevolverItem extends CrossbowItem implements PolymerItem {
    private static final double RANGE = 32.0;
    private static final int SHOOT_COOLDOWN_TICKS = 10;
    private static final Map<UUID, Integer> RELOADING = new HashMap<>();
    private static final Map<UUID, Integer> RELOAD_COOLDOWNS = new HashMap<>();
    public SculkRevolverItem(Item.Properties properties) { super(properties); }

    @Override
    public Item getPolymerItem(ItemStack stack, PacketContext context) {
        return Items.CROSSBOW;
    }
    @Override public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack revolver = player.getItemInHand(hand);
        if (player.isShiftKeyDown()) { if (!level.isClientSide()) reload(player, revolver); return InteractionResult.SUCCESS; }
        return InteractionResult.PASS;
    }
    @Override public ItemUseAnimation getUseAnimation(ItemStack stack) { return ItemUseAnimation.CROSSBOW; }
    @Override public int getUseDuration(ItemStack stack, net.minecraft.world.entity.LivingEntity entity) { return 72000; }
    @Override public Identifier getPolymerItemModel(ItemStack stack, PacketContext context, HolderLookup.Provider lookup) {
        return StrawberryFlavored.id("sculk_revolver");
    }
    public static void shoot(ServerPlayer player, ItemStack revolver) {
        if (RELOADING.containsKey(player.getUUID())) return;
        if (player.getCooldowns().isOnCooldown(revolver)) return;
        if (revolver.getDamageValue() >= revolver.getMaxDamage()) return;
        ServerLevel level = (ServerLevel) player.level(); Vec3 start = player.getEyePosition(); Vec3 end = start.add(player.getLookAngle().scale(RANGE));
        BlockHitResult blockHit = level.clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
        Vec3 target = blockHit.getType() == HitResult.Type.MISS ? end : blockHit.getLocation();
        EntityHitResult entityHit = findEntityHit(player, start, target);
        if (entityHit != null && entityHit.getEntity() instanceof LivingEntity living) {
            float damage = 8.0f;
            if (living instanceof Player && entityHit.getLocation().y >= living.getBoundingBox().maxY - 0.15) damage = 16.0f;
            living.hurtServer(level, level.damageSources().magic(), damage);
        }
        Vec3 trailEnd = entityHit == null ? target : entityHit.getLocation();
        double distance = start.distanceTo(trailEnd);
        for (int i = 1; i <= Math.max(1, (int) (distance / 1.5)); i++) {
            Vec3 point = start.lerp(trailEnd, i / Math.max(1.0, distance / 1.5));
            level.sendParticles(ParticleTypes.SCULK_SOUL, point.x, point.y, point.z, 1, 0, 0, 0, 0.0);
        }
        // End the shot at the entity intersection, rather than continuing to
        // the block/range target when an entity was hit.
        Vec3 impact = entityHit == null ? target : entityHit.getLocation();
        level.sendParticles(ParticleTypes.SONIC_BOOM, impact.x, impact.y, impact.z, 1, 0, 0, 0, 0.0);
        level.playSound(null, player.blockPosition(), SoundEvents.WARDEN_SONIC_BOOM, SoundSource.PLAYERS, 3.0f, 1.5f);
        // The revolver is an ammo container, not a breakable tool. Keep it at
        // the empty state when the last shot is fired so it can be reloaded.
        revolver.setDamageValue(Math.min(revolver.getMaxDamage(), revolver.getDamageValue() + 1));
        player.getCooldowns().addCooldown(revolver, SHOOT_COOLDOWN_TICKS);
    }
    private static EntityHitResult findEntityHit(ServerPlayer player, Vec3 start, Vec3 end) {
        Entity best = null; Vec3 bestHit = null; double bestDistance = Double.MAX_VALUE;
        for (Entity entity : ((ServerLevel) player.level()).getEntities(player, new AABB(start, end).inflate(1.0), e -> e instanceof LivingEntity && e != player && e.isPickable())) {
            var clip = entity.getBoundingBox().inflate(0.3).clip(start, end);
            if (clip.isPresent() && start.distanceToSqr(clip.get()) < bestDistance) {
                bestDistance = start.distanceToSqr(clip.get());
                best = entity;
                bestHit = clip.get();
            }
        }
        return best == null ? null : new EntityHitResult(best, bestHit);
    }
    public static void reload(Player player, ItemStack revolver) {
        if (!(player instanceof ServerPlayer serverPlayer) || RELOADING.containsKey(player.getUUID())) return;
        int loaded = revolver.getMaxDamage() - revolver.getDamageValue();
        if (loaded >= revolver.getMaxDamage()) return;
        RELOADING.put(player.getUUID(), loaded);
        RELOAD_COOLDOWNS.put(player.getUUID(), 0);
        serverPlayer.sendOverlayMessage(Component.literal("Reloading! " + loaded + "/" + revolver.getMaxDamage()));
    }

    public static void tickReloads(net.minecraft.server.MinecraftServer server) {
        RELOADING.entrySet().removeIf(entry -> {
            ServerPlayer player = server.getPlayerList().getPlayer(entry.getKey());
            if (player == null) { RELOAD_COOLDOWNS.remove(entry.getKey()); return true; }
            if (!player.isShiftKeyDown()) {
                player.sendOverlayMessage(Component.empty());
                RELOAD_COOLDOWNS.remove(entry.getKey());
                return true;
            }
            int cooldown = RELOAD_COOLDOWNS.getOrDefault(entry.getKey(), 0);
            if (cooldown > 0) { RELOAD_COOLDOWNS.put(entry.getKey(), cooldown - 1); return false; }
            ItemStack revolver = player.getMainHandItem();
            if (!(revolver.getItem() instanceof SculkRevolverItem)) { RELOAD_COOLDOWNS.remove(entry.getKey()); return true; }
            int loaded = entry.getValue();
            if (loaded >= revolver.getMaxDamage() || revolver.getDamageValue() <= 0) {
                if (loaded > 0) player.sendOverlayMessage(Component.literal("Reloaded!"));
                RELOAD_COOLDOWNS.remove(entry.getKey());
                return true;
            }
            for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
                ItemStack ammo = player.getInventory().getItem(slot);
                if (!ammo.is(SoulHeartItems.SCULK_BULLET) || ammo.isEmpty()) continue;
                ammo.shrink(1);
                revolver.setDamageValue(revolver.getDamageValue() - 1);
                loaded++;
                entry.setValue(loaded);
                RELOAD_COOLDOWNS.put(entry.getKey(), 20);
                player.sendOverlayMessage(Component.literal("Reloading! " + loaded + "/" + revolver.getMaxDamage()));
                return false;
            }
            if (loaded > 0) player.sendOverlayMessage(Component.literal("Reloaded!"));
            RELOAD_COOLDOWNS.remove(entry.getKey());
            return true;
        });
    }
}
