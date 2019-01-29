package com.github.bartimaeusnek.openutilityworlds.common.world.provider;

import com.github.bartimaeusnek.openutilityworlds.common.world.chunkgenerator.WinterChunkGenerator;
import com.github.bartimaeusnek.openutilityworlds.common.world.dimension.DimensionTypeManager;
import net.minecraft.world.DimensionType;
import net.minecraft.world.gen.IChunkGenerator;

public class WinterWorldProvider extends UniversalWorldProvider {
    @Override
    public IChunkGenerator createChunkGenerator() {
        return new WinterChunkGenerator(world);
    }

    @Override
    public DimensionType getDimensionType() {
        return DimensionTypeManager.WINTER.getDimensionType();
    }


}
