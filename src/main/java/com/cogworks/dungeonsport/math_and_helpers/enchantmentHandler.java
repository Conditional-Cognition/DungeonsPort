package com.cogworks.dungeonsport.math_and_helpers;

import com.cogworks.dungeonsport.DungeonsPort;
import com.cogworks.dungeonsport.registry.DPAttachments;
import com.cogworks.dungeonsport.registry.DPEffects;
import com.cogworks.dungeonsport.registry.DPItems;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
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

    public static final ResourceKey<Enchantment> AMBUSH_KEY = ResourceKey.create(
        Registries.ENCHANTMENT,
        ResourceLocation.fromNamespaceAndPath(DungeonsPort.MODID, "ambush")
    );

    public static final ResourceKey<Enchantment> POISON_ASPECT_KEY = ResourceKey.create(
        Registries.ENCHANTMENT,
        ResourceLocation.fromNamespaceAndPath(DungeonsPort.MODID, "poison_aspect")
    );

    public static final ResourceKey<Enchantment> ILLAGER_BANE_KEY = ResourceKey.create(
        Registries.ENCHANTMENT,
        ResourceLocation.fromNamespaceAndPath(DungeonsPort.MODID, "illager_bane")
    );

    public static final ResourceKey<Enchantment> ENIGMA_RESONATOR_KEY = ResourceKey.create(
        Registries.ENCHANTMENT,
        ResourceLocation.fromNamespaceAndPath(DungeonsPort.MODID, "enigma_resonator")
    );

    private static final Map<UUID, Long> LAST_ECHO_TICK = new HashMap<>();
    private static final Set<UUID> SWEEPING_ATTACKERS = new HashSet<>();

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

    public static int getEffectiveAmbushLevel(ItemStack weapon, Holder<Enchantment> ambushHolder) {
        int enchantedLevel = ambushHolder != null ? weapon.getEnchantmentLevel(ambushHolder) : 0;

        int innateLevel = 0;
        if (weapon.is(DPItems.SWIFT_STRIKER)) {
            innateLevel = 2;
        }

        return Math.max(innateLevel, enchantedLevel);
    }

    public static int getEffectivePoisonAspectLevel(ItemStack weapon, Holder<Enchantment> poisonAspectHolder) {
        int enchantedLevel = poisonAspectHolder != null ? weapon.getEnchantmentLevel(poisonAspectHolder) : 0;
        int innateLevel = weapon.is(DPItems.ENCRUSTED_ANCHOR) ? 1 : 0;
        return Math.max(innateLevel, enchantedLevel);
    }

    public static int getEffectiveEnigmaResonatorLevel(
        ItemStack weapon,
        Holder<Enchantment> enigmaResonatorHolder
    ) {
        int enchantedLevel = enigmaResonatorHolder != null
            ? weapon.getEnchantmentLevel(enigmaResonatorHolder)
            : 0;
        int innateLevel = weapon.is(DPItems.MOON_DAGGER) ? 1 : 0;
        return Math.max(innateLevel, enchantedLevel);
    }

    public static float getSoulGatheringAmount(
        ItemStack weapon,
        Holder<Enchantment> enigmaResonatorHolder
    ) {
        float soulGathering = weapon.is(DPItems.MOON_DAGGER) ? 2.0f : 0.0f;
        if (enigmaResonatorHolder != null && weapon.getEnchantmentLevel(enigmaResonatorHolder) > 0) {
            soulGathering += 1.0f;
        }
        return soulGathering;
    }

    public static float getEnigmaResonatorChance(float souls, int maxSouls, int level) {
        if (maxSouls <= 0 || level <= 0) {
            return 0.0f;
        }

        float soulMultiplier = 1.0f + 0.5f * (Math.min(level, 5) - 1);
        return Math.min(1.0f, Math.max(0.0f, souls * soulMultiplier / maxSouls));
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

        var enchantments = attacker.level().registryAccess().registryOrThrow(Registries.ENCHANTMENT);
        Holder<Enchantment> echoHolder = enchantments.getHolder(ECHO_KEY).orElse(null);
        int echoLevel = getEffectiveEchoLevel(weapon, echoHolder);
        if (echoLevel > 0) {
            long now = attacker.level().getGameTime();
            Long lastTick = LAST_ECHO_TICK.get(attacker.getUUID());
            long cooldownTicks = getCooldownTicks(echoLevel);

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

        Holder<Enchantment> enigmaResonatorHolder = enchantments
            .getHolder(ENIGMA_RESONATOR_KEY)
            .orElse(null);
        int enigmaResonatorLevel = getEffectiveEnigmaResonatorLevel(weapon, enigmaResonatorHolder);
        if (attacker instanceof Player player && enigmaResonatorLevel > 0) {
            float chance = getEnigmaResonatorChance(
                DPAttachments.getSouls(player),
                DPAttachments.getMaxSouls(player),
                enigmaResonatorLevel
            );
            if (player.getRandom().nextFloat() < chance) {
                event.setNewDamage(event.getNewDamage() * 3.0f);
            }
        }

        LivingEntity target = event.getEntity();
        if (target instanceof Mob mob && mob.getTarget() != attacker) {
            Holder<Enchantment> ambushHolder = enchantments.getHolder(AMBUSH_KEY).orElse(null);
            int ambushLevel = getEffectiveAmbushLevel(weapon, ambushHolder);
            if (ambushLevel > 0) {
                event.setNewDamage(event.getNewDamage() * (1.0f + 0.25f * ambushLevel));
            }
        }

        if (weapon.is(DPItems.MOON_DAGGER) && target.getType().is(EntityTypeTags.UNDEAD)) {
            event.setNewDamage(event.getNewDamage() + 2.5f);
        }

        if (weapon.is(DPItems.BONE_CUDGEL) && target.getType().is(EntityTypeTags.ILLAGER)) {
            Holder<Enchantment> illagerBaneHolder = enchantments.getHolder(ILLAGER_BANE_KEY).orElse(null);
            int enchantedLevel = illagerBaneHolder != null
                ? weapon.getEnchantmentLevel(illagerBaneHolder)
                : 0;
            if (enchantedLevel == 0) {
                event.setNewDamage(event.getNewDamage() + 2.5f);
            }
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
        if (SWEEPING_ATTACKERS.contains(attacker.getUUID())) {
            return;
        }

        LivingEntity target = event.getEntity();
        Level world = target.level();
        Holder<Enchantment> enigmaResonatorHolder = world
            .registryAccess()
            .registryOrThrow(Registries.ENCHANTMENT)
            .getHolder(ENIGMA_RESONATOR_KEY)
            .orElse(null);
        float soulGathering = getSoulGatheringAmount(weapon, enigmaResonatorHolder);
        if (
            target.isDeadOrDying()
                && attacker instanceof Player player
                && soulGathering > 0.0f
        ) {
            DPAttachments.addSouls(player, soulGathering);
            if (world instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(
                    ParticleTypes.SOUL,
                    target.getX(),
                    target.getY() + target.getBbHeight() * 0.5,
                    target.getZ(),
                    8,
                    0.25,
                    target.getBbHeight() * 0.25,
                    0.25,
                    0.02
                );
            }
        }

        if (weapon.is(DPItems.FIREBRAND)) {
            target.igniteForSeconds(3.0f);
        }
        if (weapon.is(DPItems.HIGHLAND_AXE)) {
            target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 0));
        }

        Holder<Enchantment> poisonAspectHolder = world
            .registryAccess()
            .registryOrThrow(Registries.ENCHANTMENT)
            .getHolder(POISON_ASPECT_KEY)
            .orElse(null);
        int poisonAspectLevel = getEffectivePoisonAspectLevel(weapon, poisonAspectHolder);
        if (poisonAspectLevel > 0) {
            target.addEffect(new MobEffectInstance(
                MobEffects.POISON,
                100 + (20 * (poisonAspectLevel - 1)),
                poisonAspectLevel - 1
            ));
        }

        // Freezing effect
        Holder<Enchantment> freezingHolder = world
            .registryAccess()
            .registryOrThrow(Registries.ENCHANTMENT)
            .getHolder(FREEZING_KEY)
            .orElse(null);

        int freezingLevel = freezingHolder != null ? weapon.getEnchantmentLevel(freezingHolder) : 0;
        if (weapon.is(DPItems.FANGS_OF_FROST)) {
            freezingLevel = Math.max(freezingLevel, 1);
        }
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
        if (!event.getSource().is(DamageTypeTags.IS_EXPLOSION)) {
            Holder<Enchantment> explodingHolder = world
                .registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolder(EXPLODING_KEY)
                .orElse(null);

            int explodingLevel = getEffectiveExplodingLevel(weapon, explodingHolder);
            if (explodingLevel > 0) {
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

        if (weapon.is(DPItems.SHEER_DAGGER) && target.isDeadOrDying()) {
            float sweepDamage = Math.max(1.0f, event.getNewDamage() * 0.2f);
            SWEEPING_ATTACKERS.add(attacker.getUUID());
            try {
                List<LivingEntity> sweepTargets = world.getEntitiesOfClass(
                    LivingEntity.class,
                    target.getBoundingBox().inflate(1.0),
                    entity -> entity.isAlive()
                        && entity != attacker
                        && entity != target
                        && entity.distanceTo(target) <= 1.0
                );
                var sweepSource = attacker instanceof Player player
                    ? world.damageSources().playerAttack(player)
                    : world.damageSources().mobAttack(attacker);
                for (LivingEntity mob : sweepTargets) {
                    mob.hurt(sweepSource, sweepDamage);
                }
            } finally {
                SWEEPING_ATTACKERS.remove(attacker.getUUID());
            }
        }

        if (weapon.is(DPItems.SHEER_DAGGER) && target instanceof Sheep sheep) {
            if (!sheep.isSheared() && !sheep.isBaby()) {
                sheep.setSheared(true);
                world.playSound(
                    null,
                    sheep.getX(),
                    sheep.getY(),
                    sheep.getZ(),
                    SoundEvents.SHEEP_SHEAR,
                    SoundSource.PLAYERS,
                    1.0f,
                    1.0f
                );
                if (!world.isClientSide()) {
                    Item woolItem = BuiltInRegistries.ITEM.get(
                        ResourceLocation.withDefaultNamespace(sheep.getColor().getName() + "_wool")
                    );

                    int count = 1 + sheep.getRandom().nextInt(3);
                    for (int i = 0; i < count; i++) {
                        ItemEntity drop = sheep.spawnAtLocation(woolItem, 1);
                        if (drop != null) {
                            drop.setDeltaMovement(drop.getDeltaMovement().add(
                                (sheep.getRandom().nextFloat() - sheep.getRandom().nextFloat()) * 0.1F,
                                sheep.getRandom().nextFloat() * 0.05F,
                                (sheep.getRandom().nextFloat() - sheep.getRandom().nextFloat()) * 0.1F
                            ));
                        }
                    }
                }
            }
        }
    }
}