package com.github.bartimaeusnek.bettervoidworlds.common.world.provider;

import com.github.bartimaeusnek.bettervoidworlds.common.world.chunkgenerator.DuneChunkGenerator;
import com.github.bartimaeusnek.bettervoidworlds.common.world.dimension.DimensionTypeManager;
import net.minecraft.world.DimensionType;
import net.minecraft.world.gen.IChunkGenerator;

public class DuneWorldProvider extends UniversalWorldProvider {
    @Override
    public IChunkGenerator createChunkGenerator() {
        return new DuneChunkGenerator(world);
    }

    @Override
    public DimensionType getDimensionType() {
        return DimensionTypeManager.DUNES.getDimensionType();
    }
}
