package com.github.bartimaeusnek.openutilityworlds.client.plugins;

import com.github.bartimaeusnek.openutilityworlds.OUW;
import com.github.bartimaeusnek.openutilityworlds.common.blocks.UniversalPortalBlock;
import mcp.mobius.waila.api.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;


@WailaPlugin
public class WailaProvider implements IWailaPlugin {

    @Override
    public void register(IWailaRegistrar registrar) {
        PortalTooltipProvider instance = new PortalTooltipProvider();
        registrar.registerBodyProvider(instance, UniversalPortalBlock.class);
        registrar.registerNBTProvider(instance, UniversalPortalBlock.class);
    }

    public static class PortalTooltipProvider implements IWailaDataProvider
    {
        @Nonnull
        @Override
        public List<String> getWailaBody(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
            tooltip.addAll(Arrays.asList(
                    "Portal to "+ (accessor.getNBTData().getInteger("DIM_ID") == OUW.sharedVoid ? I18n.format("SharedVoid") : accessor.getNBTData().getString("DIMSTRING").isEmpty() ? DimensionManager.getProviderType(accessor.getNBTData().getInteger("DIM_ID")).getName() : accessor.getNBTData().getString("DIMSTRING")),
                    accessor.getNBTData().getBoolean("NEW_DIM") ? "Creates a new Personal Dim" : "",
                    !accessor.getNBTData().getBoolean("ReturnHasBeenSet") ? "Not activated yet" : "Has been activated",
                    "Teleports to "+(!Arrays.equals(new int[]{accessor.getPosition().getX(),accessor.getPosition().getY(),accessor.getPosition().getZ()}, accessor.getNBTData().getIntArray("RETURNPOS")) && accessor.getNBTData().getIntArray("RETURNPOS").length == 3  ? new BlockPos(accessor.getNBTData().getIntArray("RETURNPOS")[0],accessor.getNBTData().getIntArray("RETURNPOS")[1],accessor.getNBTData().getIntArray("RETURNPOS")[2]).toString() : accessor.getPosition().toString())
            ));
            return tooltip;
        }

        @Nonnull
        public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
            tag.merge(te.getTileData());
            return tag;
        }
    }
}
