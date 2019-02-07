package com.github.bartimaeusnek.bettervoidworlds.client.creativetabs;

import com.github.bartimaeusnek.bettervoidworlds.loader.ItemRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreativeTab extends CreativeTabs {

    public CreativeTab() {
        super("bettervoidworlds");
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(ItemRegistry.portalBlock);
    }
}
