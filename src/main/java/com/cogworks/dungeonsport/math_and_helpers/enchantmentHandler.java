package com.cogworks.dungeonsport.math_and_helpers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

@EventBusSubscriber(modid = "dungeonsport")
public class enchantmentHandler {

    public static final ResourceKey<Enchantment> ECHO_KEY = ResourceKey.create(
        Registries.ENCHANTMENT,
        ResourceLocation.fromNamespaceAndPath("dungeonsport", "echo")
    );

    private static final Map<UUID, Long> LAST_ECHO_TICK = new HashMap<>();

    public static long getCooldownTicks(int level) {
        int clampedLevel = Math.max(1, Math.min(3, level));
        int seconds = 6 - clampedLevel;
        return seconds * 20L;
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

        if (echoHolder == null) {
            return;
        }

        int level = weapon.getEnchantmentLevel(echoHolder);
        if (level <= 0) {
            return;
        }

        long now = attacker.level().getGameTime();
        long lastTick = LAST_ECHO_TICK.getOrDefault(attacker.getUUID(), Long.MIN_VALUE);
        long cooldownTicks = getCooldownTicks(level);

        if (now - lastTick >= cooldownTicks) {
            LAST_ECHO_TICK.put(attacker.getUUID(), now);
            event.setNewDamage(event.getNewDamage() * 2.0f);
        }
    }
}