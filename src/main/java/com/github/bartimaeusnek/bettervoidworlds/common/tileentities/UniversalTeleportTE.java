package com.github.bartimaeusnek.bettervoidworlds.common.tileentities;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ITeleporter;

public class UniversalTeleportTE extends TileEntity implements ITeleporter {

    public UniversalTeleportTE() {

    }

    @Override
    public void placeEntity(World world, Entity entity, float yaw) {
        if (world.isRemote)
            return;

        int id = this.getTileData().getInteger("DIM_ID");
        int[] pos = this.getTileData().getIntArray("RETURNPOS");
        Pteleporter tteleporter = new Pteleporter(DimensionManager.getWorld(id, true), pos);
        tteleporter.placeEntity(world, entity, yaw);
    }

    @Override
    public boolean isVanilla() {
        return false;
    }

    class Pteleporter extends Teleporter {
        int[] pos;

        public Pteleporter(WorldServer worldIn, int[] pos) {
            super(worldIn);
            this.pos = pos;
        }

        @Override
        public boolean makePortal(Entity par1Entity) {
            return true;
        }

        @Override
        public void placeInPortal(Entity entityIn, float rotationYaw) {
            if (pos.length == 3) {
                entityIn.setPositionAndUpdate(pos[0] + 1, pos[1], pos[2]);
            } else {
                entityIn.setPositionAndUpdate(world.getSpawnPoint().getX() + 1, world.getSpawnPoint().getY(), world.getSpawnPoint().getZ());
            }
        }
    }
}
