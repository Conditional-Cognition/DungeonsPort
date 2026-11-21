package com.cogworks.dungeonsport.registry;

import com.cogworks.dungeonsport.DungeonsPort;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.RegisterEvent;

public final class DPTabs {
    public static final CreativeModeTab DUNGEONS_PORT_TAB = CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.dungeonsport"))
            .icon(() -> new ItemStack(DPItems.EXAMPLE_ITEM))
            .displayItems((parameters, output) -> {
                output.accept(DPItems.EXAMPLE_BLOCK_ITEM);
                output.accept(DPItems.EXAMPLE_ITEM);
                output.accept(DPItems.ANCHOR);
                output.accept(DPItems.BLACKSMITH_UPGRADE);
            })
            .build();

    private DPTabs() {
    }

    public static void register(RegisterEvent event) {
        event.register(Registries.CREATIVE_MODE_TAB, registry -> registry.register(
                ResourceLocation.fromNamespaceAndPath(DungeonsPort.MODID, "dungeons_port_tab"),
                DUNGEONS_PORT_TAB));
    }
}
