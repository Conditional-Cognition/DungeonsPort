package com.cogworks.dungeonsport.math_and_helpers;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public final class scaledWeaponAttributes {

    public static final float DAMAGE_DIVISOR = 20.0f;
    public static final float KNOCKBACK_DIVISOR = 2.0f;

    public static ItemAttributeModifiers create(float baseDamage, float swingDurationSeconds, float knockback) {
        float totalDamage = Math.round(baseDamage / DAMAGE_DIVISOR);
        float damageBonus = Math.max(0.0f, totalDamage - 1.0f);

        float attacksPerSecond = 1.0f / swingDurationSeconds;
        float speedModifier = (attacksPerSecond - 4.0f);

        float knockbackValue = knockback / KNOCKBACK_DIVISOR;

        return ItemAttributeModifiers.builder()
            .add(
                Attributes.ATTACK_DAMAGE,
                new AttributeModifier(
                    ResourceLocation.fromNamespaceAndPath("dungeonsport", "base_attack_damage"),
                    damageBonus,
                    AttributeModifier.Operation.ADD_VALUE
                ),
                EquipmentSlotGroup.MAINHAND
            )
            .add(
                Attributes.ATTACK_SPEED,
                new AttributeModifier(
                    ResourceLocation.fromNamespaceAndPath("dungeonsport", "base_attack_speed"),
                    speedModifier,
                    AttributeModifier.Operation.ADD_VALUE
                ),
                EquipmentSlotGroup.MAINHAND
            )
            .add(
                Attributes.ATTACK_KNOCKBACK,
                new AttributeModifier(
                    ResourceLocation.fromNamespaceAndPath("dungeonsport", "base_attack_knockback"),
                    knockbackValue,
                    AttributeModifier.Operation.ADD_VALUE
                ),
                EquipmentSlotGroup.MAINHAND
            )
            .build();
    }
}