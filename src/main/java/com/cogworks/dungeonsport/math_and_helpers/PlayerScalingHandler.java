package com.cogworks.dungeonsport.math_and_helpers;

import com.cogworks.dungeonsport.DungeonsPort;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = DungeonsPort.MODID)
public class PlayerScalingHandler {

    private static final ResourceLocation ARMOR_XP_HEALTH_ID =
        ResourceLocation.fromNamespaceAndPath(DungeonsPort.MODID, "armor_xp_health");

    @SubscribeEvent
    public static void onLivingDamagePre(LivingDamageEvent.Pre event) {
        if (!(event.getSource().getEntity() instanceof LivingEntity attacker)) {
            return;
        }
        if (attacker.level().isClientSide()) {
            return;
        }
        if (attacker instanceof Player player && player.experienceLevel > 0 && !player.getMainHandItem().isEmpty()) {
            float xpMultiplier = 1.0f + (0.25f * player.experienceLevel);
            event.setNewDamage(event.getNewDamage() * xpMultiplier);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) {
            return;
        }

        AttributeInstance maxHealth = player.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealth == null) {
            return;
        }

        int armorPoints = player.getArmorValue();

        double bonusHealth = player.experienceLevel * armorPoints * 0.1;

        AttributeModifier existing = maxHealth.getModifier(ARMOR_XP_HEALTH_ID);
        if (bonusHealth <= 0.0) {
            if (existing != null) {
                maxHealth.removeModifier(ARMOR_XP_HEALTH_ID);
                if (player.getHealth() > player.getMaxHealth()) {
                    player.setHealth(player.getMaxHealth());
                }
            }
            return;
        }

        if (existing == null || existing.amount() != bonusHealth) {
            maxHealth.addOrReplacePermanentModifier(new AttributeModifier(
                ARMOR_XP_HEALTH_ID,
                bonusHealth,
                AttributeModifier.Operation.ADD_VALUE
            ));
        }
    }
}
