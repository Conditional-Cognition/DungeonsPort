package com.cogworks.dungeonsport.registry;

import com.cogworks.dungeonsport.DungeonsPort;
import com.cogworks.dungeonsport.items.SawbladeItem;
import com.cogworks.dungeonsport.math_and_helpers.scaledWeaponAttributes;

import java.util.List;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
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
    public static final Item BLACKSMITH_UPGRADE = new Item(new Item.Properties());
    public static final Item ANCHOR = new Item(
        new Item.Properties()
            .durability(1200)
            .attributes(scaledWeaponAttributes.create(	238f, 2.5f, 5f))
    ) {
        @Override
        public void appendHoverText(
            ItemStack stack,
            Item.TooltipContext context,
            List<Component> tooltip,
            TooltipFlag flag
        ) {
            super.appendHoverText(stack, context, tooltip, flag);
            tooltip.add(Component.translatable("item.dungeonsport.anchor.description"));
        }
    };
    public static final Item ENCRUSTED_ANCHOR = new Item(
        new Item.Properties()
            .durability(1200)
            .attributes(scaledWeaponAttributes.create(	238f, 2.5f, 5f))
    ) {
        @Override
        public void appendHoverText(
            ItemStack stack,
            Item.TooltipContext context,
            List<Component> tooltip,
            TooltipFlag flag
        ) {
            super.appendHoverText(stack, context, tooltip, flag);
            tooltip.add(Component.translatable("item.dungeonsport.encrusted_anchor.description"));
        }
    };
    public static final Item FIREBRAND = new Item(
        new Item.Properties()
            .durability(1200)
            .attributes(scaledWeaponAttributes.create(	150f, 1.0f, 1f))
    ) {
        @Override
        public void appendHoverText(
            ItemStack stack,
            Item.TooltipContext context,
            List<Component> tooltip,
            TooltipFlag flag
        ) {
            super.appendHoverText(stack, context, tooltip, flag);
            tooltip.add(Component.translatable("item.dungeonsport.firebrand.description"));
        }
    };
    public static final Item HIGHLAND_AXE = new Item(
        new Item.Properties()
            .durability(1200)
            .attributes(scaledWeaponAttributes.create(  160f, 1.15f, 1f))
    ) {
        @Override
        public void appendHoverText(
            ItemStack stack,
            Item.TooltipContext context,
            List<Component> tooltip,
            TooltipFlag flag
        ) {
            super.appendHoverText(stack, context, tooltip, flag);
            tooltip.add(Component.translatable("item.dungeonsport.highland_axe.description"));
        }
    };
    public static final Item BACKSTABBER = new Item(
        new Item.Properties()
            .durability(1200)
            .attributes(scaledWeaponAttributes.create(	80f, 0.5f, 1f))
    ) {
        @Override
        public void appendHoverText(
            ItemStack stack,
            Item.TooltipContext context,
            List<Component> tooltip,
            TooltipFlag flag
        ) {
            super.appendHoverText(stack, context, tooltip, flag);
            tooltip.add(Component.translatable("item.dungeonsport.backstabber.description"));
        }
    };
    public static final Item SWIFT_STRIKER = new Item(
        new Item.Properties()
            .durability(1200)
            .attributes(scaledWeaponAttributes.create(	80f, 0.5f, 1f))
    ) {
        @Override
        public void appendHoverText(
            ItemStack stack,
            Item.TooltipContext context,
            List<Component> tooltip,
            TooltipFlag flag
        ) {
            super.appendHoverText(stack, context, tooltip, flag);
            tooltip.add(Component.translatable("item.dungeonsport.swift_striker.description"));
        }
    };
    public static final Item BATTLESTAFF = new Item(
        new Item.Properties()
            .durability(1200)
            .attributes(scaledWeaponAttributes.create(	70f, 0.3f, 0.5f))
    ) {
        @Override
        public void appendHoverText(
            ItemStack stack,
            Item.TooltipContext context,
            List<Component> tooltip,
            TooltipFlag flag
        ) {
            super.appendHoverText(stack, context, tooltip, flag);
            tooltip.add(Component.translatable("item.dungeonsport.battlestaff.description"));
        }
    };
    public static final Item BATTLESTAFF_OF_TERROR = new Item(
        new Item.Properties()
            .durability(1200)
            .attributes(scaledWeaponAttributes.create(	70f, 0.3f, 0.5f))
    ) {
        @Override
        public void appendHoverText(
            ItemStack stack,
            Item.TooltipContext context,
            List<Component> tooltip,
            TooltipFlag flag
        ) {
            super.appendHoverText(stack, context, tooltip, flag);
            tooltip.add(Component.translatable("item.dungeonsport.battlestaff_of_terror.description"));
        }
    };
    public static final Item BONECLUB = new Item(
        new Item.Properties()
            .durability(1200)
            .attributes(scaledWeaponAttributes.create(162f, 1.1f, 5f))
    ) {
        @Override
        public void appendHoverText(
            ItemStack stack,
            Item.TooltipContext context,
            List<Component> tooltip,
            TooltipFlag flag
        ) {
            super.appendHoverText(stack, context, tooltip, flag);
            tooltip.add(Component.translatable("item.dungeonsport.boneclub.description"));
        }
    };
    public static final Item BONE_CUDGEL = new Item(
        new Item.Properties()
            .durability(1200)
            .attributes(scaledWeaponAttributes.create(162f, 1.1f, 5f))
    ) {
        @Override
        public void appendHoverText(
            ItemStack stack,
            Item.TooltipContext context,
            List<Component> tooltip,
            TooltipFlag flag
        ) {
            super.appendHoverText(stack, context, tooltip, flag);
            tooltip.add(Component.translatable("item.dungeonsport.bone_cudgel.description"));
        }
    };
    public static final Item BROKEN_SAWBLADE = new SawbladeItem(
        new Item.Properties()
            .durability(250)
            .attributes(scaledWeaponAttributes.create(120f, 0.625f, 0f)),
        14
    );
    public static final Item MECHANIZED_SAWBLADE = new SawbladeItem(
        new Item.Properties()
            .durability(2031)
            .attributes(scaledWeaponAttributes.create(160f, 0.625f, 0f)),
        15
    );
    public static final Item DAGGER = new Item(
        new Item.Properties()
            .durability(1200)
            .attributes(scaledWeaponAttributes.create(	25f, 0.2f, 0.5f))
    ) {
        @Override
        public void appendHoverText(
            ItemStack stack,
            Item.TooltipContext context,
            List<Component> tooltip,
            TooltipFlag flag
        ) {
            super.appendHoverText(stack, context, tooltip, flag);
            tooltip.add(Component.translatable("item.dungeonsport.dagger.description"));
        }
    };
    public static final Item FANGS_OF_FROST = new Item(
        new Item.Properties()
            .durability(1200)
            .attributes(scaledWeaponAttributes.create(	25f, 0.2f, 0.5f))
    ) {
        @Override
        public void appendHoverText(
            ItemStack stack,
            Item.TooltipContext context,
            List<Component> tooltip,
            TooltipFlag flag
        ) {
            super.appendHoverText(stack, context, tooltip, flag);
            tooltip.add(Component.translatable("item.dungeonsport.fangs_of_frost.description"));
        }
    };
    public static final Item MOON_DAGGER = new Item(
        new Item.Properties()
            .durability(1200)
            .attributes(scaledWeaponAttributes.create(	25f, 0.2f, 0.5f))
    ) {
        @Override
        public void appendHoverText(
            ItemStack stack,
            Item.TooltipContext context,
            List<Component> tooltip,
            TooltipFlag flag
        ) {
            super.appendHoverText(stack, context, tooltip, flag);
            tooltip.add(Component.translatable("item.dungeonsport.moon_dagger.description"));
        }
    };
    public static final Item SHEER_DAGGER = new Item(
        new Item.Properties()
            .durability(1200)
            .attributes(scaledWeaponAttributes.create(	25f, 0.2f, 0.5f))
    ) {
        @Override
        public void appendHoverText(
            ItemStack stack,
            Item.TooltipContext context,
            List<Component> tooltip,
            TooltipFlag flag
        ) {
            super.appendHoverText(stack, context, tooltip, flag);
            tooltip.add(Component.translatable("item.dungeonsport.sheer_dagger.description"));
        }
    };
    
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
                ResourceLocation.fromNamespaceAndPath(DungeonsPort.MODID, "blacksmith_upgrade"),
                BLACKSMITH_UPGRADE
            );
            registry.register(
                ResourceLocation.fromNamespaceAndPath(DungeonsPort.MODID, "anchor"),
                ANCHOR);
            registry.register(
                ResourceLocation.fromNamespaceAndPath(DungeonsPort.MODID, "encrusted_anchor"),
                ENCRUSTED_ANCHOR);
            registry.register(
                ResourceLocation.fromNamespaceAndPath(DungeonsPort.MODID, "firebrand"),
                FIREBRAND);
            registry.register(
                ResourceLocation.fromNamespaceAndPath(DungeonsPort.MODID, "highland_axe"),
                HIGHLAND_AXE);
            registry.register(
                ResourceLocation.fromNamespaceAndPath(DungeonsPort.MODID, "backstabber"),
                BACKSTABBER);
            registry.register(
                ResourceLocation.fromNamespaceAndPath(DungeonsPort.MODID, "swift_striker"),
                SWIFT_STRIKER);
            registry.register(
                ResourceLocation.fromNamespaceAndPath(DungeonsPort.MODID, "battlestaff"),
                BATTLESTAFF);
            registry.register(
                ResourceLocation.fromNamespaceAndPath(DungeonsPort.MODID, "battlestaff_of_terror"),
                BATTLESTAFF_OF_TERROR);
            registry.register(
                ResourceLocation.fromNamespaceAndPath(DungeonsPort.MODID, "boneclub"),
                BONECLUB
            );
            registry.register(
                ResourceLocation.fromNamespaceAndPath(DungeonsPort.MODID, "bone_cudgel"),
                BONE_CUDGEL
            );
            registry.register(
                ResourceLocation.fromNamespaceAndPath(DungeonsPort.MODID, "broken_sawblade"),
                BROKEN_SAWBLADE
            );
            registry.register(
                ResourceLocation.fromNamespaceAndPath(DungeonsPort.MODID, "mechanized_sawblade"),
                MECHANIZED_SAWBLADE
            );
            registry.register(
                ResourceLocation.fromNamespaceAndPath(DungeonsPort.MODID, "dagger"),
                DAGGER);
            registry.register(
                ResourceLocation.fromNamespaceAndPath(DungeonsPort.MODID, "fangs_of_frost"),
                FANGS_OF_FROST);
            registry.register(
                ResourceLocation.fromNamespaceAndPath(DungeonsPort.MODID, "moon_dagger"),
                MOON_DAGGER);
            registry.register(
                ResourceLocation.fromNamespaceAndPath(DungeonsPort.MODID, "sheer_dagger"),
                SHEER_DAGGER);
        });
    }
}
