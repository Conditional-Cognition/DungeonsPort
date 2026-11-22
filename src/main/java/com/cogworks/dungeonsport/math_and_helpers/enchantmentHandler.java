package com.cogworks.dungeonsport.math_and_helpers;

import com.cogworks.dungeonsport.DungeonsPort;
import com.cogworks.dungeonsport.registry.DPEffects;
import com.cogworks.dungeonsport.registry.DPItems;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

@EventBusSubscriber(modid = DungeonsPort.MODID)
public class enchantmentHandler {

    public static final ResourceKey<Enchantment> ECHO_KEY = ResourceKey.create(
        Registries.ENCHANTMENT,
        ResourceLocation.fromNamespaceAndPath(DungeonsPort.MODID, "echo")
    );

    public static final ResourceKey<Enchantment> EXPLODING_KEY = ResourceKey.create(
        Registries.ENCHANTMENT,
        ResourceLocation.fromNamespaceAndPath(DungeonsPort.MODID, "exploding")
    );

    public static final ResourceKey<Enchantment> FREEZING_KEY = ResourceKey.create(
        Registries.ENCHANTMENT,
        ResourceLocation.fromNamespaceAndPath(DungeonsPort.MODID, "freezing")
    );

    private static final Map<UUID, Long> LAST_ECHO_TICK = new HashMap<>();

    public static long getCooldownTicks(int level) {
        int clampedLevel = Math.max(1, Math.min(3, level));
        int seconds = 6 - clampedLevel;
        return seconds * 20L;
    }

    public static int getEffectiveEchoLevel(ItemStack weapon, Holder<Enchantment> echoHolder) {
        int enchantedLevel = echoHolder != null ? weapon.getEnchantmentLevel(echoHolder) : 0;

        int innateLevel = 0;
        if (weapon.is(DPItems.SWIFT_STRIKER)) {
            innateLevel = 2;
        } else if (weapon.is(DPItems.BACKSTABBER)) {
            innateLevel = 1;
        }

        // Uses innate level if unenchanted, or the higher tier if enchanted with a stronger Echo level
        return Math.max(innateLevel, enchantedLevel);
    }

    public static int getEffectiveExplodingLevel(ItemStack weapon, Holder<Enchantment> explodingHolder) {
        int enchantedLevel = explodingHolder != null ? weapon.getEnchantmentLevel(explodingHolder) : 0;

        int innateLevel = 0;
        if (weapon.is(DPItems.BATTLESTAFF_OF_TERROR)) {
            innateLevel = 1;
        }

        return Math.max(innateLevel, enchantedLevel);
    }

    @SubscribeEvent
    public static void onLivingDamagePre(LivingDamageEvent.Pre event) {
        if (!(event.getSource().getEntity() instanceof LivingEntity attacker)) {
            return;
        }
        if (attacker.level().isClientSide()) {
            return;
        }

        ItemStack weapon = attacker.getMainHandItem();
        if (weapon.isEmpty()) {
            return;
        }

        Holder<Enchantment> echoHolder = attacker.level()
            .registryAccess()
            .registryOrThrow(Registries.ENCHANTMENT)
            .getHolder(ECHO_KEY)
            .orElse(null);

        int level = getEffectiveEchoLevel(weapon, echoHolder);
        if (level <= 0) {
            return;
        }

        long now = attacker.level().getGameTime();
        Long lastTick = LAST_ECHO_TICK.get(attacker.getUUID());
        long cooldownTicks = getCooldownTicks(level);

        if (lastTick == null || now - lastTick >= cooldownTicks) {
            LAST_ECHO_TICK.put(attacker.getUUID(), now);
            event.setNewDamage(event.getNewDamage() * 2.0f);

            LivingEntity target = event.getEntity();
            target.level().playSound(
                null,
                target.getX(),
                target.getY(),
                target.getZ(),
                SoundEvents.WARDEN_TENDRIL_CLICKS,
                SoundSource.PLAYERS,
                1.0f,
                1.0f
            );
        }
    }

    @SubscribeEvent
    public static void onLivingDamagePost(LivingDamageEvent.Post event) {
        if (!(event.getSource().getEntity() instanceof LivingEntity attacker)) {
            return;
        }
        if (attacker.level().isClientSide()) {
            return;
        }

        ItemStack weapon = attacker.getMainHandItem();
        if (weapon.isEmpty()) {
            return;
        }

        LivingEntity target = event.getEntity();
        Level world = target.level();

        // Freezing effect
        Holder<Enchantment> freezingHolder = world
            .registryAccess()
            .registryOrThrow(Registries.ENCHANTMENT)
            .getHolder(FREEZING_KEY)
            .orElse(null);

        int freezingLevel = freezingHolder != null ? weapon.getEnchantmentLevel(freezingHolder) : 0;
        if (freezingLevel > 0 && target.canFreeze()) {
            int durationTicks = 40 + (20 * freezingLevel);
            int amplifier = Math.max(0, freezingLevel - 1);
            target.addEffect(new MobEffectInstance(
                DPEffects.FREEZING,
                durationTicks,
                amplifier,
                false,
                true,
                true
            ));
        }

        // Exploding AoE
        if (event.getSource().is(DamageTypeTags.IS_EXPLOSION)) {
            return;
        }

        Holder<Enchantment> explodingHolder = world
            .registryAccess()
            .registryOrThrow(Registries.ENCHANTMENT)
            .getHolder(EXPLODING_KEY)
            .orElse(null);

        int explodingLevel = getEffectiveExplodingLevel(weapon, explodingHolder);
        if (explodingLevel <= 0) {
            return;
        }

        double radius = (double) explodingLevel;

        world.explode(
            attacker,
            target.getX(),
            target.getY() + target.getBbHeight() * 0.5,
            target.getZ(),
            0.0f,
            Level.ExplosionInteraction.NONE
        );

        AABB area = target.getBoundingBox().inflate(radius);
        List<LivingEntity> nearby = world.getEntitiesOfClass(
            LivingEntity.class,
            area,
            entity -> entity.isAlive()
                && entity != attacker
                && entity != target
                && entity.distanceTo(target) <= radius
        );

        float splashDamage = Math.max(2.0f, event.getNewDamage() * (0.4f + 0.2f * explodingLevel));
        for (LivingEntity mob : nearby) {
            mob.hurt(world.damageSources().explosion(attacker, attacker), splashDamage);
        }
    }
}