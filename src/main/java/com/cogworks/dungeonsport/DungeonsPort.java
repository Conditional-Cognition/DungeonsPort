package com.cogworks.dungeonsport;

import com.cogworks.dungeonsport.math_and_helpers.ItemObliteratorCompat;
import com.cogworks.dungeonsport.math_and_helpers.enchantmentHandler;
import com.cogworks.dungeonsport.registry.*;
import com.mojang.logging.LogUtils;
import net.combat_roll.api.event.ServerSideRollEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;

import org.slf4j.Logger;

@Mod(DungeonsPort.MODID)
public class DungeonsPort {
    public static final String MODID = "dungeonsport";
    public static final Logger LOGGER = LogUtils.getLogger();

    public DungeonsPort(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::onLoadComplete);

        modEventBus.addListener(DPBlocks::register);
        modEventBus.addListener(DPItems::register);
        modEventBus.addListener(DPTabs::register);

        DPAttachments.register(modEventBus);
        DPEffects.register(modEventBus);

        ServerSideRollEvents.PLAYER_START_ROLLING.register((ServerPlayer player, Vec3 velocity) -> {
            var enchantments = player.level().registryAccess().registryOrThrow(Registries.ENCHANTMENT);
            Holder<Enchantment> dynamoHolder = enchantments.getHolder(enchantmentHandler.DYNAMO_KEY).orElse(null);

            boolean hasDynamoWeapon = false;
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack stack = player.getInventory().getItem(i);
                if (!stack.isEmpty() && enchantmentHandler.getEffectiveDynamoLevel(stack, dynamoHolder) > 0) {
                    hasDynamoWeapon = true;
                    break;
                }
            }

            if (hasDynamoWeapon) {
                DPAttachments.addDynamoCache(player, 1);
                int stacks = DPAttachments.getDynamoCache(player);
                if (stacks > 0 && player.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(
                        ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER,
                        player.getX(),
                        player.getY() + player.getBbHeight() * 0.5,
                        player.getZ(),
                        stacks,
                        0.35,
                        0.35,
                        0.35,
                        0.02
                    );
                }
            }
        });
    }

    private void onLoadComplete(final FMLLoadCompleteEvent event) {
        event.enqueueWork(() -> {
            if (ItemObliteratorCompat.injectBlacklistEntries()) {
                DungeonsPort.LOGGER.warn("======================================================================");
                DungeonsPort.LOGGER.warn("DungeonsPort updated item_obliterator.json5 on first launch.");
                DungeonsPort.LOGGER.warn("Please restart Minecraft or your the server to apply weapon changes.");
                DungeonsPort.LOGGER.warn("======================================================================");
                System.exit(0);
            }
        });
    }
}