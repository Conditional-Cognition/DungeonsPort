package com.cogworks.dungeonsport.registry;

import com.cogworks.dungeonsport.DungeonsPort;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.RegisterEvent;

public final class DPBlocks {
    public static final Block EXAMPLE_BLOCK = new Block(BlockBehaviour.Properties.of().strength(2.0F));

    private DPBlocks() {
    }

    public static void register(RegisterEvent event) {
        event.register(Registries.BLOCK, registry -> registry.register(
                ResourceLocation.fromNamespaceAndPath(DungeonsPort.MODID, "example_block"),
                EXAMPLE_BLOCK));
    }
}
