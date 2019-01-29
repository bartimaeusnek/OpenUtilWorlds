package com.github.bartimaeusnek.openutilityworlds.common.world.provider;

import com.github.bartimaeusnek.openutilityworlds.common.world.chunkgenerator.MiningWorldChunkGenerator;
import com.github.bartimaeusnek.openutilityworlds.common.world.dimension.DimensionTypeManager;
import net.minecraft.world.DimensionType;
import net.minecraft.world.gen.IChunkGenerator;

public class MiningWorldProvider extends UniversalWorldProvider {
    @Override
    public IChunkGenerator createChunkGenerator() {
        return new MiningWorldChunkGenerator(world);
    }

    @Override
    public DimensionType getDimensionType() {
        return DimensionTypeManager.MINING.getDimensionType();
    }
}
