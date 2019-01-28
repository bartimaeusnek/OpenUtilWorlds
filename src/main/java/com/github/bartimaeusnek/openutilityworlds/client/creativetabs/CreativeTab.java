package com.github.bartimaeusnek.openutilityworlds.client.creativetabs;

import com.github.bartimaeusnek.openutilityworlds.loader.ItemRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreativeTab extends CreativeTabs {

    public CreativeTab() {
        super("Open Utility Worlds");
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(ItemRegistry.portalBlock);
    }
}
