package com.cogworks.dungeonsport.client;

import com.cogworks.dungeonsport.DungeonsPort;
import com.cogworks.dungeonsport.math_and_helpers.enchantmentHandler;
import com.cogworks.dungeonsport.registry.DPAttachments;
import com.cogworks.dungeonsport.registry.DPItems;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

@EventBusSubscriber(modid = DungeonsPort.MODID, value = Dist.CLIENT)
public final class SoulTooltipHandler {

    private SoulTooltipHandler() {
    }

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        Player player = event.getEntity();
        Holder<Enchantment> enigmaResonatorHolder = player == null
            ? null
            : player.level()
                .registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolder(enchantmentHandler.ENIGMA_RESONATOR_KEY)
                .orElse(null);

        float soulGathering = enchantmentHandler.getSoulGatheringAmount(stack, enigmaResonatorHolder);
        if (soulGathering <= 0.0f) {
            return;
        }

        event.getToolTip().add(Component.translatable(
            "item.dungeonsport.description.soulgathering",
            formatSoulGathering(soulGathering)
        ));

        if (!stack.is(DPItems.MOON_DAGGER) || player == null) {
            return;
        }

        int enigmaLevel = enchantmentHandler.getEffectiveEnigmaResonatorLevel(
            stack,
            enigmaResonatorHolder
        );
        float chance = enchantmentHandler.getEnigmaResonatorChance(
            DPAttachments.getSouls(player),
            DPAttachments.getMaxSouls(player),
            enigmaLevel
        );
        event.getToolTip().add(Component.translatable(
            "item.dungeonsport.moon_dagger.enigma_chance",
            Math.round(chance * 100.0f) + "%"
        ));
    }

    private static String formatSoulGathering(float amount) {
        return amount == (int) amount ? Integer.toString((int) amount) : Float.toString(amount);
    }
}
