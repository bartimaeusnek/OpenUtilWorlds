package com.github.bartimaeusnek.openutilityworlds.util;

import com.github.bartimaeusnek.openutilityworlds.OUW;
import com.github.bartimaeusnek.openutilityworlds.common.world.dimension.DimensionTypeManager;
import com.github.bartimaeusnek.openutilityworlds.loader.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = OUW.MODID)
public class StupidNewRegisterer {

    @SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> onBlockRegister) {
        onBlockRegister.getRegistry().registerAll(ItemRegistry.blocks.toArray(new Block[0]));
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(ItemRegistry.items.toArray(new Item[0]));
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent event) {
        for (Item I : ItemRegistry.items) {
            for (DimensionTypeManager.PortalTypes i : DimensionTypeManager.PortalTypes.values()) {
                ModelLoader.setCustomModelResourceLocation(I, i.getMeta(), new ModelResourceLocation(I.getRegistryName().toString(), "inventory"));
            }
        }
        for (Block B : ItemRegistry.blocks) {
            for (DimensionTypeManager.PortalTypes i : DimensionTypeManager.PortalTypes.values()) {
                ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(B), i.getMeta(), new ModelResourceLocation(B.getRegistryName().toString(), "type=" + i.getName()));
            }
        }
    }
}
