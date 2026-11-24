package com.cogworks.dungeonsport.math_and_helpers;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;


import com.cogworks.dungeonsport.DungeonsPort;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = DungeonsPort.MODID)
public class MobScalingHandler {
        private static final ResourceLocation XP_HEALTH_MODIFIER_ID =
        ResourceLocation.fromNamespaceAndPath("dungeonsport", "xp_mob_health");
    private static final ResourceLocation XP_DAMAGE_MODIFIER_ID =
        ResourceLocation.fromNamespaceAndPath("dungeonsport", "xp_mob_damage");

    @SubscribeEvent
    public static void onMobSpawn(FinalizeSpawnEvent event) {

        Player nearestPlayer = event.getLevel().getNearestPlayer(event.getEntity(), 96.0);
        if (nearestPlayer == null || nearestPlayer.experienceLevel <= 0) {
            return;
        }

        int level = nearestPlayer.experienceLevel;

        double healthBonus = Math.pow(event.getEntity().getMaxHealth(), level)*level;

        AttributeInstance maxHealth = event.getEntity().getAttribute(Attributes.MAX_HEALTH);
        if (maxHealth != null && !maxHealth.hasModifier(XP_HEALTH_MODIFIER_ID)) {
            maxHealth.addPermanentModifier(new AttributeModifier(
                XP_HEALTH_MODIFIER_ID,
                healthBonus,
                AttributeModifier.Operation.ADD_MULTIPLIED_BASE
            ));
            event.getEntity().setHealth(event.getEntity().getMaxHealth());
        }

        AttributeInstance attackDamage = event.getEntity().getAttribute(Attributes.ATTACK_DAMAGE);
        if (attackDamage != null && !attackDamage.hasModifier(XP_DAMAGE_MODIFIER_ID)) {
            attackDamage.addPermanentModifier(new AttributeModifier(
                XP_DAMAGE_MODIFIER_ID,
                level/2 * event.getEntity().getMaxHealth(),
                AttributeModifier.Operation.ADD_MULTIPLIED_BASE
            ));
        }
    }
}