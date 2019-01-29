package com.github.bartimaeusnek.openutilityworlds.common.world.provider;

import com.github.bartimaeusnek.openutilityworlds.common.world.chunkgenerator.GardenChunkGenerator;
import com.github.bartimaeusnek.openutilityworlds.common.world.dimension.DimensionTypeManager;
import net.minecraft.world.DimensionType;
import net.minecraft.world.gen.IChunkGenerator;

public class GardenWorldProvider extends UniversalWorldProvider {

    @Override
    public IChunkGenerator createChunkGenerator() {
        return new GardenChunkGenerator(world);
    }

    @Override
    public DimensionType getDimensionType() {
        return DimensionTypeManager.GARDEN.getDimensionType();
    }
}