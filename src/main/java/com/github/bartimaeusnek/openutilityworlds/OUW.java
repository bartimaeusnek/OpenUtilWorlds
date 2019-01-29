package com.github.bartimaeusnek.openutilityworlds;

import com.github.bartimaeusnek.openutilityworlds.client.creativetabs.CreativeTab;
import com.github.bartimaeusnek.openutilityworlds.common.config.ConfigHandler;
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
public class OUW {
    public static final String MODID = "openutilworlds";
    public static final String NAME = "@modname@";
    public static final String VERSION = "@version@";
    public static CreativeTabs tab = new CreativeTab();
    public static HashSet<Integer> dimIDs = new HashSet<>();
    public static Logger logger = LogManager.getLogger(MODID);
    public static int sharedVoid;


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent preInit) {
        sharedVoid = DimensionTypeManager.registerNewDimWithoutLoad(DimensionTypeManager.VOID);
        for (Map.Entry<DimensionType, IntSortedSet> map : DimensionManager.getRegisteredDimensions().entrySet()) {
            dimIDs.addAll(map.getValue());
        }
        GameRegistry.registerTileEntity(UniversalTeleportTE.class, new ResourceLocation(MODID + ":UniversalTeleportTE"));
        new ItemRegistry();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent init) {
        if (ConfigHandler.createPortals) {
            makeRecipeFor(DimensionTypeManager.PortalTypes.SHARED, new ItemStack(Blocks.OBSIDIAN));
            makeRecipeFor(DimensionTypeManager.PortalTypes.MINING, "blockLapis");
            makeRecipeFor(DimensionTypeManager.PortalTypes.VOID, "blockIron");
            makeRecipeFor(DimensionTypeManager.PortalTypes.GARDEN, "treeSapling");
            makeRecipeFor(DimensionTypeManager.PortalTypes.WINTER, new ItemStack(Blocks.SNOW));
        }
    }

    private void makeRecipeFor(DimensionTypeManager.PortalTypes types, Object ingridient) {
        NBTTagCompound voidNBT = new NBTTagCompound();
        voidNBT.setInteger("DIM_ID", sharedVoid);
        ItemStack voidPortal = new ItemStack(ItemRegistry.portalItemBlock, 1, types.getMeta());
        voidPortal.setTagCompound(voidNBT);
        CraftingRecipeHandler.addShapedOreRecipe(
                voidPortal,
                "DHD",
                "DPD",
                "DDD",
                'H', ingridient,
                'P', new ItemStack(Items.DIAMOND_PICKAXE),
                'D', new ItemStack(Blocks.OBSIDIAN)
        );
    }

    @Mod.EventHandler
    public void postinit(FMLInitializationEvent postinit) {
    }

}
