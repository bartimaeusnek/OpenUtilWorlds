package com.github.bartimaeusnek.bettervoidworlds.common.world.provider;

import com.github.bartimaeusnek.bettervoidworlds.common.config.ConfigHandler;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.IChunkGenerator;

import javax.annotation.Nullable;

public abstract class UniversalWorldProvider extends WorldProvider {

    public UniversalWorldProvider() {
        this.nether = false;
        this.doesWaterVaporize = false;
        this.hasSkyLight = true;
    }

    @Override
    public boolean canRespawnHere() {
        return ConfigHandler.canRespawn;
    }

    abstract public IChunkGenerator createChunkGenerator();

    @Nullable
    @Override
    public String getSaveFolder() {
        return "OUW_UNIVERSAL_DIM_" + this.getDimension();
    }

    @Override
    public long getWorldTime() {
        return ConfigHandler.alwaysDay ? 6000 : ConfigHandler.alwaysNight ? 18000 : super.getWorldTime();
    }

    @Override
    public boolean isDaytime() {
        return !ConfigHandler.alwaysNight && (ConfigHandler.alwaysDay || super.isDaytime());
    }

    @Override
    public int getRespawnDimension(net.minecraft.entity.player.EntityPlayerMP player) {
        return ConfigHandler.canRespawn ? this.getDimension() : 0;
    }

    @Override
    public boolean isSurfaceWorld() {
        return true;
    }

    @Override
    public boolean canDoRainSnowIce(Chunk chunk) {
        return ConfigHandler.rainAndSnow;
    }

    @Override
    public boolean canDoLightning(Chunk chunk) {
        return ConfigHandler.thunder;
    }


}
