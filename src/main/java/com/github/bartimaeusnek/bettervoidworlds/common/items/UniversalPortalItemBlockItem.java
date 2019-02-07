package com.github.bartimaeusnek.bettervoidworlds.common.items;

import com.github.bartimaeusnek.bettervoidworlds.OUW;
import com.github.bartimaeusnek.bettervoidworlds.common.world.dimension.DimensionTypeManager;
import com.github.bartimaeusnek.bettervoidworlds.loader.ItemRegistry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class UniversalPortalItemBlockItem extends ItemBlock {

    public UniversalPortalItemBlockItem() {
        super(ItemRegistry.portalBlock);
        this.maxStackSize = 1;
        this.hasSubtypes = true;
        this.setCreativeTab(OUW.tab);
        this.setRegistryName(OUW.MODID, "universalportalitemblockitem");
    }

    public static boolean setBlockNbt(World world, @Nullable EntityPlayer entityPlayer, BlockPos pos, ItemStack stack) {
        if (world.isRemote)
            return false;

        IBlockState state = world.getBlockState(pos);
        if (state.getBlock().equals(ItemRegistry.portalBlock)) {
            MinecraftServer minecraftserver = world.getMinecraftServer();

            if (minecraftserver != null) {
                NBTTagCompound tag = stack.getTagCompound();
                if (tag == null)
                    return false;
                TileEntity te = world.getTileEntity(pos);
                if (te == null)
                    return false;
                boolean newWorld = tag.getBoolean("NEW_DIM");
                NBTTagCompound blocktag = te.getTileData();
                if (!newWorld) {
                    int id = tag.getInteger("DIM_ID");
                    blocktag.setInteger("DIM_ID", id);
                } else
                    blocktag.setBoolean("NEW_DIM", true);
                blocktag.setBoolean("ReturnHasBeenSet", tag.getBoolean("ReturnHasBeenSet"));
                blocktag.setIntArray("RETURNPOS", tag.getIntArray("RETURNPOS"));
                if (!tag.getString("DIMSTRING").isEmpty())
                    blocktag.setString("DIMSTRING", tag.getString("DIMSTRING"));
                return true;
            }
        }
        return false;

    }

    public int getMetadata(int damage) {
        return damage % 15;
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
        return super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState) && setBlockNbt(world, player, pos, stack);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            if (stack.getTagCompound() == null)
                return I18n.format("ouw.portalblock.broken");
            NBTTagCompound tag = stack.getTagCompound();
            int dimid = tag.getInteger("DIM_ID");
            final DimensionType T = DimensionManager.isDimensionRegistered(dimid) ? DimensionManager.getProviderType(dimid) : DimensionTypeManager.getDimTypeFromMeta(stack.getItemDamage()).getDimensionType();
            return I18n.format("ouw.portalblock.portalto") + " " + (tag.getBoolean("NEW_DIM") ? I18n.format("ouw.PrivateVoid") : dimid == OUW.sharedVoid ? I18n.format("SharedVoid") : I18n.format(T.getName()));
        }
        return stack.toString();
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) {
            super.addInformation(stack, worldIn, tooltip, flagIn);
            return;
        }
        int dimid = tag.getInteger("DIM_ID");
        final DimensionType T = DimensionManager.isDimensionRegistered(dimid) ? DimensionManager.getProviderType(dimid) : DimensionTypeManager.getDimTypeFromMeta(stack.getItemDamage()).getDimensionType();
        tooltip.add(I18n.format("ouw.portalblock.portalto") + " " + (tag.getBoolean("NEW_DIM") ? I18n.format("ouw.PrivateVoid") : dimid == OUW.sharedVoid ? I18n.format("SharedVoid") : I18n.format(T.getName())));
        super.addInformation(stack, worldIn, tooltip, flagIn);
        this.block.addInformation(stack, worldIn, tooltip, flagIn);
    }
}