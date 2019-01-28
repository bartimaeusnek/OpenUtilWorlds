package com.github.bartimaeusnek.openutilityworlds.common.world.provider;

import com.github.bartimaeusnek.openutilityworlds.common.config.ConfigHandler;
import com.github.bartimaeusnek.openutilityworlds.common.world.chunkgenerator.UniversalChunkGenerator;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.IChunkGenerator;

import javax.annotation.Nullable;

public abstract class UniversalWorldProvider extends WorldProvider {

    public UniversalWorldProvider(){
        this.nether = false;
        this.doesWaterVaporize = false;
        this.hasSkyLight = true;
    }
    @Override
    public boolean canRespawnHere() {
        return ConfigHandler.canRespawn;
    }

    @Override
    public IChunkGenerator createChunkGenerator() {
        return new UniversalChunkGenerator(this.world);
    }

    @Nullable
    @Override
    public String getSaveFolder() {
        return "OUW_UNIVERSAL_DIM_"+this.getDimension();
    }

    @Override
    public long getWorldTime() {
        return ConfigHandler.alwaysDay ? 6000 : super.getWorldTime();
    }

    @Override
    public boolean isDaytime() {
        return ConfigHandler.alwaysDay || super.isDaytime();
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
