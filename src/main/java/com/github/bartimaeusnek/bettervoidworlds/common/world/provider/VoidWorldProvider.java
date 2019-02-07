package com.github.bartimaeusnek.bettervoidworlds.common.world.provider;

import com.github.bartimaeusnek.bettervoidworlds.common.world.chunkgenerator.VoidChunkProvider;
import com.github.bartimaeusnek.bettervoidworlds.common.world.dimension.DimensionTypeManager;
import net.minecraft.world.DimensionType;
import net.minecraft.world.gen.IChunkGenerator;

public class VoidWorldProvider extends UniversalWorldProvider {

    @Override
    public DimensionType getDimensionType() {
        return DimensionTypeManager.VOID.getDimensionType();
    }

    @Override
    public IChunkGenerator createChunkGenerator() {
        return new VoidChunkProvider(this.world);
    }

}
