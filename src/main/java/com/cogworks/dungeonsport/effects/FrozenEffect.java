package com.cogworks.dungeonsport.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class FrozenEffect extends MobEffect {

    public FrozenEffect() {
        super(MobEffectCategory.HARMFUL, 0x89E8FF);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level().isClientSide() || !entity.canFreeze()) {
            return true;
        }

        int required = entity.getTicksRequiredToFreeze();
        int targetFrozen = Math.max(entity.getTicksFrozen(), required + 5);
        int cap = required + 40 + (amplifier * 20);
        entity.setTicksFrozen(Math.min(cap, targetFrozen));

        return true;
    }
}