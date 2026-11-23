package com.cogworks.dungeonsport.client;

import com.cogworks.dungeonsport.DungeonsPort;
import com.cogworks.dungeonsport.registry.DPItems;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@SuppressWarnings("removal")
@EventBusSubscriber(modid = DungeonsPort.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class SawbladeClientModelHandler {

    private SawbladeClientModelHandler() {
    }

    @SubscribeEvent
    public static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(
            new IClientItemExtensions() {
                @Override
                public HumanoidModel.ArmPose getArmPose(
                    LivingEntity entity,
                    InteractionHand hand,
                    ItemStack stack
                ) {
                    return entity.isUsingItem()
                        && entity.getUsedItemHand() == hand
                        && entity.getUseItem().getItem() == stack.getItem()
                        ? HumanoidModel.ArmPose.BOW_AND_ARROW
                        : null;
                }
            },
            DPItems.BROKEN_SAWBLADE,
            DPItems.MECHANIZED_SAWBLADE
        );
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ResourceLocation usingSawblade = ResourceLocation.fromNamespaceAndPath(
                DungeonsPort.MODID,
                "using_sawblade"
            );
            ItemProperties.register(
                DPItems.BROKEN_SAWBLADE,
                usingSawblade,
                (stack, level, entity, seed) -> entity != null
                    && entity.isUsingItem()
                    && entity.getUseItem().getItem() == stack.getItem() ? 1.0f : 0.0f
            );
            ItemProperties.register(
                DPItems.MECHANIZED_SAWBLADE,
                usingSawblade,
                (stack, level, entity, seed) -> entity != null
                    && entity.isUsingItem()
                    && entity.getUseItem().getItem() == stack.getItem() ? 1.0f : 0.0f
            );
        });
    }
}
