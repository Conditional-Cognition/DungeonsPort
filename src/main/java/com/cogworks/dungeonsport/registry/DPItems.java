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
    public static final Item BLACKSMITH_UPGRADE = new Item(new Item.Properties());
    public static final Item ANCHOR = new Item(
        new Item.Properties()
            .durability(1200)
            .attributes(scaledWeaponAttributes.create(	238f, 2.5f, 5f))
    );
    public static final Item ENCRUSTED_ANCHOR = new Item(
        new Item.Properties()
            .durability(1200)
            .attributes(scaledWeaponAttributes.create(	238f, 2.5f, 5f))
    ); // + Poison 1 for 5 seconds on hit
    public static final Item FIREBRAND = new Item(
        new Item.Properties()
            .durability(1200)
            .attributes(scaledWeaponAttributes.create(	150f, 1.0f, 1f))
    ); // + Fire Ticks for 3 seconds on hit
    public static final Item HIGHLAND_AXE = new Item(
        new Item.Properties()
            .durability(1200)
            .attributes(scaledWeaponAttributes.create(  160f, 1.5f, 1f))
    ); // + Weakness 1 for 5 seconds on hit
    public static final Item BACKSTABBER = new Item(
        new Item.Properties()
            .durability(1200)
            .attributes(scaledWeaponAttributes.create(	80f, 0.5f, 1f))
    ); // + Extra Damage on mobs not targeting the user
    public static final Item SWIFT_STRIKER = new Item(
        new Item.Properties()
            .durability(1200)
            .attributes(scaledWeaponAttributes.create(	80f, 0.5f, 1f))
    ); // + Extra Damage on mobs not targeting the user, built in Echo Enchantment (will do double damage with 5-I/4-II/3-III second cooldown)
    public static final Item BATTLESTAFF = new Item(
        new Item.Properties()
            .durability(1200)
            .attributes(scaledWeaponAttributes.create(	70f, 0.3f, 0.5f))
    ); // + attack speezy
    
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
        });
    }
}
