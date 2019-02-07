package com.github.bartimaeusnek.bettervoidworlds.loader;

import com.github.bartimaeusnek.bettervoidworlds.common.blocks.UniversalPortalBlock;
import com.github.bartimaeusnek.bettervoidworlds.common.items.UniversalPortalItemBlockItem;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

import java.util.ArrayList;

public class ItemRegistry {
    public static final ArrayList<Block> blocks = new ArrayList<Block>();
    public static final Block portalBlock = new UniversalPortalBlock();
    public static final ArrayList<Item> items = new ArrayList<Item>();
    public static final Item portalItemBlock = new UniversalPortalItemBlockItem();

    public ItemRegistry() {
        blocks.add(portalBlock);
        items.add(portalItemBlock);
    }
}
