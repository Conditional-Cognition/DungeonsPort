package com.cogworks.dungeonsport.items;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class SawbladeItem extends Item {

    private static final double MELEE_REACH = 3.0;
    private static final int SHREDDING_SOUND_INTERVAL = 10;
    private final int enchantmentValue;

    public SawbladeItem(Properties properties, int enchantmentValue) {
        super(properties);
        this.enchantmentValue = enchantmentValue;
    }

    @Override
    public int getEnchantmentValue() {
        return enchantmentValue;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE;
    }

    @Override
    public void onUseTick(Level level, LivingEntity user, ItemStack stack, int remainingUseDuration) {
        if (level.isClientSide() || !(user instanceof Player player) || !(level instanceof ServerLevel serverLevel)) {
            return;
        }
        if (remainingUseDuration % SHREDDING_SOUND_INTERVAL == 0) {
            level.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.MINECART_RIDING,
                SoundSource.PLAYERS,
                0.35f,
                1.0f
            );
        }
        if ((remainingUseDuration & 1) != 0) {
            return;
        }

        LivingEntity target = findTarget(player);
        if (target == null) {
            return;
        }

        var source = player.damageSources().playerAttack(player);
        float damage = EnchantmentHelper.modifyDamage(serverLevel, stack, target, source, 0.5f);
        target.invulnerableTime = 0;
        if (target.hurt(source, damage)) {
            level.playSound(
                null,
                target.getX(),
                target.getY(),
                target.getZ(),
                SoundEvents.GRINDSTONE_USE,
                SoundSource.PLAYERS,
                1.0f,
                1.0f
            );
            float yaw = player.getYRot() * ((float) Math.PI / 180.0f);
            target.knockback(
                0.2f,
                Mth.sin(yaw),
                -Mth.cos(yaw)
            );
        }
    }

    private static LivingEntity findTarget(Player player) {
        Vec3 start = player.getEyePosition();
        Vec3 look = player.getLookAngle();
        Vec3 end = start.add(look.scale(MELEE_REACH));
        AABB searchArea = player.getBoundingBox()
            .expandTowards(look.scale(MELEE_REACH))
            .inflate(1.0);

        LivingEntity closestTarget = null;
        double closestDistance = Double.MAX_VALUE;
        for (LivingEntity candidate : player.level().getEntitiesOfClass(
            LivingEntity.class,
            searchArea,
            entity -> entity != player
                && entity.isAlive()
                && !player.isAlliedTo(entity)
                && player.hasLineOfSight(entity)
        )) {
            var hitPosition = candidate.getBoundingBox().inflate(0.3).clip(start, end);
            if (hitPosition.isPresent()) {
                double distance = start.distanceToSqr(hitPosition.get());
                if (distance < closestDistance) {
                    closestDistance = distance;
                    closestTarget = candidate;
                }
            }
        }
        return closestTarget;
    }
}
