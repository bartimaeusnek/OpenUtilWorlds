package com.github.bartimaeusnek.bettervoidworlds.client.plugins;

import com.github.bartimaeusnek.bettervoidworlds.OUW;
import com.github.bartimaeusnek.bettervoidworlds.common.tileentities.UniversalTeleportTE;
import mcjty.theoneprobe.api.*;

import com.google.common.base.Function;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import javax.annotation.Nullable;
import java.util.Arrays;

public class TOPProvider implements Function<ITheOneProbe, Void> {
    @Override
    public Void apply(@Nullable ITheOneProbe iTheOneProbe) {
        assert iTheOneProbe != null;
        iTheOneProbe.registerProvider(new IProbeInfoProvider()
        {
            @Override
            public String getID()
            {
                return OUW.MODID + "_TOP";
            }

            @Override
            public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data)
            {
                TileEntity te = world.getTileEntity(data.getPos());

                if (te instanceof UniversalTeleportTE)
                    handlePortal(probeInfo, te, data.getPos());
            }
        });

        return null;
    }

    private static void handlePortal(IProbeInfo info, TileEntity te, BlockPos pos) {
        String[] tooltip = {"Portal to " + (te.getTileData().getInteger("DIM_ID") == OUW.sharedVoid ? I18n.format("SharedVoid") : te.getTileData().getString("DIMSTRING").isEmpty() ? DimensionManager.getProviderType(te.getTileData().getInteger("DIM_ID")).getName() : te.getTileData().getString("DIMSTRING")),
                te.getTileData().getBoolean("NEW_DIM") ? "Creates a new Personal Dim" : "",
                !te.getTileData().getBoolean("ReturnHasBeenSet") ? "Not activated yet" : "Has been activated",
                "Teleports to " + (!Arrays.equals(new int[]{pos.getX(), pos.getY(), pos.getZ()}, te.getTileData().getIntArray("RETURNPOS")) && te.getTileData().getIntArray("RETURNPOS").length == 3 ? new BlockPos(te.getTileData().getIntArray("RETURNPOS")[0], te.getTileData().getIntArray("RETURNPOS")[1], te.getTileData().getIntArray("RETURNPOS")[2]).toString() : pos.toString())};
        for (int i = 0; i < tooltip.length; i++) {
            if (!tooltip[i].isEmpty())
                info.vertical().text(tooltip[i]);
        }
    }

}
