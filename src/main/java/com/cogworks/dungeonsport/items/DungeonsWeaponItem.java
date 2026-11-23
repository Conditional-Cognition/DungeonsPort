package com.cogworks.dungeonsport.items;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;

public class DungeonsWeaponItem extends Item {
    public DungeonsWeaponItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return 15;
    }

}
