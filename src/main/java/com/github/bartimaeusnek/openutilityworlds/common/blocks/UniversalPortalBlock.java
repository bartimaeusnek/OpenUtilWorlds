package com.github.bartimaeusnek.openutilityworlds.common.blocks;

import com.github.bartimaeusnek.openutilityworlds.OUW;
import com.github.bartimaeusnek.openutilityworlds.common.config.ConfigHandler;
import com.github.bartimaeusnek.openutilityworlds.common.items.UniversalPortalItemBlockItem;
import com.github.bartimaeusnek.openutilityworlds.common.tileentities.UniversalTeleportTE;
import com.github.bartimaeusnek.openutilityworlds.common.world.dimension.DimensionTypeManager;
import com.github.bartimaeusnek.openutilityworlds.loader.ItemRegistry;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

import static com.github.bartimaeusnek.openutilityworlds.common.world.dimension.DimensionTypeManager.PROPERTY_ENUM;

public class UniversalPortalBlock extends BlockContainer {

    public UniversalPortalBlock() {
        super(Material.IRON,MapColor.PURPLE);
        this.setCreativeTab(OUW.tab);
        this.blockResistance=100f;
        this.blockHardness=100f;
        this.setTranslationKey("universalportalblock");
        this.setSoundType(SoundType.METAL);
        this.setRegistryName(OUW.MODID,"universalportalblock");
        //this.setDefaultState(this.createBlockState().getBaseState().withProperty(PROPERTY_ENUM, DimensionTypeManager.PortalTypes.RETURN));
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    final static AxisAlignedBB box = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D);

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return box;
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return box;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos){
        return box;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public BlockStateContainer getBlockState() {
        return this.blockState;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(PROPERTY_ENUM).getMeta();
    }


    @Deprecated
    public IBlockState getStateFromMeta(int meta) {
        return createBlockState().getBaseState().withProperty(DimensionTypeManager.PROPERTY_ENUM, DimensionTypeManager.PortalTypes.getTypeFromMeta(meta));
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getStateFromMeta(meta);
    }

    @Deprecated
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getStateFromMeta(meta);
    }

    protected BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, PROPERTY_ENUM);
    }

    protected DimensionTypeManager getPortalType(){
        return DimensionTypeManager.VOID;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote || worldIn.getBlockState(pos).getBlock() != this || !(worldIn.getTileEntity(pos) instanceof UniversalTeleportTE))
            return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
        TileEntity universalTeleportTE = worldIn.getTileEntity(pos); // cant be null due to the instanceof check before
        NBTTagCompound teData = universalTeleportTE.getTileData(); //therefore this can not crash
        BlockPos toSpawn;
        boolean newDim = teData.getBoolean("NEW_DIM");
        int toPort = teData.getInteger("DIM_ID");
        if (newDim) {
            toPort=DimensionTypeManager.registerAndHotloadNewDim(getPortalType());
            teData.setBoolean("NEW_DIM",false);
            teData.setInteger("DIM_ID",toPort);
        }
        if (worldIn.provider.getDimension() != toPort) {
            World worldOut = DimensionManager.getWorld(toPort, true);
            if (worldOut == null) {
                if (!DimensionManager.isDimensionRegistered(toPort))
                    DimensionTypeManager.registerAndHotloadNewDim(getPortalType(),toPort);
                DimensionManager.initDimension(toPort);
                worldOut = DimensionManager.getWorld(toPort, true);
            }
            if (ConfigHandler.alwaysToSpawn) {
                teData.setIntArray("RETURNPOS", new int[]{pos.getX(), pos.getY(), pos.getZ()});
                toSpawn = worldOut.provider.getSpawnPoint();
            } else
                toSpawn = pos;
            if (!teData.getBoolean("ReturnHasBeenSet") && !(worldOut.getTileEntity(toSpawn) instanceof UniversalTeleportTE)) {

                Chunk initial = worldOut.getChunk(toSpawn);
                Chunk[] chunks = new Chunk[]{initial};
                if (pos.getX() % 16 == 15) {
                    chunks = new Chunk[]{initial, worldOut.getChunk(toSpawn.add(1, 0, 0))};
                }
                for (Chunk worldOutChunk : chunks) {
                    if (!worldOut.isChunkGeneratedAt(pos.getX() >> 4, pos.getZ() >> 4)) {
                        if (worldOut.provider.createChunkGenerator() == null) {
                            OUW.logger.error("Could not create Chunk for Dim " + worldOut.provider.getDimension() + ", please contact the Mod Auther for that Dim to fix his ChunkGenerator!");
                            return false;
                        }
                        worldOut.getChunkProvider().provideChunk(worldOutChunk.x, worldOutChunk.z);
                    }
                }

                for (int x = -1; x <= 1; x++) {
                    for (int z = -1; z <= 1; z++) {
                        worldOut.setBlockState(toSpawn.add(x, -1, z), Blocks.STONE.getBlockState().getBaseState());
                        for (int y = 0; y < 2; y++) {
                            worldOut.setBlockState(toSpawn.add(x, y, z), Blocks.AIR.getBlockState().getBaseState());
                        }
                    }
                }

                ItemStack stackOut = new ItemStack(ItemRegistry.portalBlock);

                NBTTagCompound tagCompound = stackOut.getTagCompound();
                if (tagCompound == null) {
                    stackOut.setTagCompound(new NBTTagCompound());
                    tagCompound = stackOut.getTagCompound();
                }
                tagCompound.setInteger("DIM_ID", worldIn.provider.getDimension());
                tagCompound.setIntArray("RETURNPOS", new int[]{toSpawn.getX(), toSpawn.getY(), toSpawn.getZ()});
                tagCompound.setBoolean("ReturnHasBeenSet", true);
                teData.setBoolean("ReturnHasBeenSet", true);
                teData.setIntArray("RETURNPOS", new int[]{toSpawn.getX(), toSpawn.getY(), toSpawn.getZ()});
                teData.setString("DIMSTRING",playerIn.getName()+"'s "+this.getPortalType().getDimensionType().getName()+" Dim");
                worldOut.setBlockState(toSpawn, ItemRegistry.portalBlock.getBlockState().getBaseState());
                UniversalPortalItemBlockItem.setBlockNbt(worldOut, null, toSpawn, stackOut);
                initial.markDirty();
                for (Chunk worldOutChunk : chunks)
                    ((EntityPlayerMP) playerIn).connection.sendPacket(new SPacketChunkData(worldOutChunk, 65535));
                ((EntityPlayerMP) playerIn).connection.sendPacket(new SPacketChunkData(worldIn.getChunk(pos), 65535));
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().transferPlayerToDimension((EntityPlayerMP) playerIn, toPort, (ITeleporter) universalTeleportTE);
        }

        return true;
    }


    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        if (itemIn.equals(OUW.tab))
            for (DimensionTypeManager.PortalTypes Type : DimensionTypeManager.PortalTypes.values()){
                ItemStack teleporter = new ItemStack(this, 1, Type.getMeta());
                NBTTagCompound blocktag = teleporter.getTagCompound();
                if (blocktag == null)
                    blocktag=new NBTTagCompound();
                blocktag.setBoolean("NEW_DIM",true);
                teleporter.setTagCompound(blocktag);
                items.add(teleporter);
            }
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new UniversalTeleportTE();
    }
}
