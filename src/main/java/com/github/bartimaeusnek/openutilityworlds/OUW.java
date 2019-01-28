package com.github.bartimaeusnek.openutilityworlds;

import com.github.bartimaeusnek.openutilityworlds.client.creativetabs.CreativeTab;
import com.github.bartimaeusnek.openutilityworlds.common.tileentities.UniversalTeleportTE;
import com.github.bartimaeusnek.openutilityworlds.common.world.dimension.DimensionTypeManager;
import com.github.bartimaeusnek.openutilityworlds.loader.ItemRegistry;
import com.github.bartimaeusnek.openutilityworlds.util.CraftingRecipeHandler;
import it.unimi.dsi.fastutil.ints.IntSortedSet;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Map;

@Mod(modid = OUW.MODID, name = OUW.NAME, version = OUW.VERSION)
public class OUW
{
    public static final String MODID = "openutilworlds";
    public static final String NAME = "@modname@";
    public static final String VERSION = "@version@";
    public static CreativeTabs tab = new CreativeTab();
    public static HashSet<Integer> dimIDs = new HashSet<>();
    public static Logger logger = LogManager.getLogger(MODID);
    public static int sharedVoid;


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent preInit) {
        sharedVoid=DimensionTypeManager.registerNewDimWithoutLoad(DimensionTypeManager.VOID);
        DimensionTypeManager.VOID.getDimensionType();
        for (Map.Entry<DimensionType, IntSortedSet> map : DimensionManager.getRegisteredDimensions().entrySet() ) {
            dimIDs.addAll(map.getValue());
        }
        GameRegistry.registerTileEntity(UniversalTeleportTE.class,new ResourceLocation(MODID+":UniversalTeleportTE"));
        new ItemRegistry();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent init) {
        NBTTagCompound voidNBT = new NBTTagCompound();
        voidNBT.setInteger("DIM_ID",sharedVoid);
        ItemStack voidPortal = new ItemStack(ItemRegistry.portalItemBlock,1,DimensionTypeManager.PortalTypes.SHARED.getMeta());
        voidPortal.setTagCompound(voidNBT);
        CraftingRecipeHandler.addShapedRecipe(
                voidPortal,
                "DDD",
                "DPD",
                "DDD",
                'P', new ItemStack(Items.DIAMOND_PICKAXE),
                'D', new ItemStack(Blocks.OBSIDIAN)
        );
        voidNBT = new NBTTagCompound();
        voidNBT.setBoolean("NEW_DIM",true);
        voidPortal = new ItemStack(ItemRegistry.portalItemBlock,1,DimensionTypeManager.PortalTypes.VOID.getMeta());
        voidPortal.setTagCompound(voidNBT);
        CraftingRecipeHandler.addShapedOreRecipe(
                voidPortal,
                "DHD",
                "DPD",
                "DDD",
                'H', "blockLapis",
                'P', new ItemStack(Items.DIAMOND_PICKAXE),
                'D', new ItemStack(Blocks.OBSIDIAN)
        );
    }

    @Mod.EventHandler
    public void postinit(FMLInitializationEvent postinit){
    }

}
