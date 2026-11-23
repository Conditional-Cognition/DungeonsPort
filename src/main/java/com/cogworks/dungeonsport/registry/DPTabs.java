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
            .icon(() -> new ItemStack(DPItems.SWORD))
            .displayItems((parameters, output) -> {
                output.accept(DPItems.EXAMPLE_BLOCK_ITEM);
                output.accept(DPItems.EXAMPLE_ITEM);
                output.accept(DPItems.ANCHOR);
                output.accept(DPItems.ENCRUSTED_ANCHOR);
                output.accept(DPItems.BLACKSMITH_UPGRADE);
                output.accept(DPItems.FIREBRAND);
                output.accept(DPItems.HIGHLAND_AXE);
                output.accept(DPItems.BACKSTABBER);
                output.accept(DPItems.SWIFT_STRIKER);
                output.accept(DPItems.BATTLESTAFF);
                output.accept(DPItems.BATTLESTAFF_OF_TERROR);
                output.accept(DPItems.BONECLUB);
                output.accept(DPItems.BONE_CUDGEL);
                output.accept(DPItems.BROKEN_SAWBLADE);
                output.accept(DPItems.MECHANIZED_SAWBLADE);
                output.accept(DPItems.CLAYMORE);
                output.accept(DPItems.BROADSWORD);
                output.accept(DPItems.FROST_SLAYER);
                output.accept(DPItems.GREAT_AXEBLADE);
                output.accept(DPItems.HEARTSTEALER);
                output.accept(DPItems.CORAL_BLADE);
                output.accept(DPItems.SPONGE_STRIKER);
                output.accept(DPItems.CUTLASS);
                output.accept(DPItems.DANCERS_SWORD);
                output.accept(DPItems.NAMELESS_BLADE);
                output.accept(DPItems.SPARKLER);
                output.accept(DPItems.DAGGER);
                output.accept(DPItems.FANGS_OF_FROST);
                output.accept(DPItems.MOON_DAGGER);
                output.accept(DPItems.SHEER_DAGGER);
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
