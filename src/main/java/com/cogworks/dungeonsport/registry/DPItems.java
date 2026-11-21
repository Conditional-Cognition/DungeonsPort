package com.cogworks.dungeonsport.registry;

import com.cogworks.dungeonsport.DungeonsPort;
import com.cogworks.dungeonsport.math_and_helpers.scaledWeaponAttributes;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.RegisterEvent;

public final class DPItems {
    public static final BlockItem EXAMPLE_BLOCK_ITEM = new BlockItem(DPBlocks.EXAMPLE_BLOCK, new Item.Properties());
    public static final Item EXAMPLE_ITEM = new Item(new Item.Properties()) {
        @Override
        public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
            if (!level.isClientSide) {
                player.addItem(new ItemStack(EXAMPLE_BLOCK_ITEM));
            }
            return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide);
        }
    };
    public static final Item ANCHOR = new Item(
        new Item.Properties()
            .durability(1200)
            .attributes(scaledWeaponAttributes.create(	238f, 2.5f, 5f))
    );
    public static final Item ENCRUSTED_ANCHOR = new Item(
        new Item.Properties()
            .durability(1200)
            .attributes(scaledWeaponAttributes.create(	238f, 2.5f, 5f))
    );
    public static final Item BLACKSMITH_UPGRADE = new Item(new Item.Properties());

    private DPItems() {
    }

    public static void register(RegisterEvent event) {
        event.register(Registries.ITEM, registry -> {
            registry.register(
                    ResourceLocation.fromNamespaceAndPath(DungeonsPort.MODID, "example_block"),
                    EXAMPLE_BLOCK_ITEM);
            registry.register(
                    ResourceLocation.fromNamespaceAndPath(DungeonsPort.MODID, "example_item"),
                    EXAMPLE_ITEM);
            registry.register(
                    ResourceLocation.fromNamespaceAndPath(DungeonsPort.MODID, "anchor"),
                    ANCHOR);
            registry.register(
                    ResourceLocation.fromNamespaceAndPath(DungeonsPort.MODID, "encrusted_anchor"),
                    ENCRUSTED_ANCHOR);
            registry.register(
                    ResourceLocation.fromNamespaceAndPath(DungeonsPort.MODID, "blacksmith_upgrade"),
                    BLACKSMITH_UPGRADE
            );
        });
    }
}
