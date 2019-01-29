package com.github.bartimaeusnek.openutilityworlds.common.world.provider;

import com.github.bartimaeusnek.openutilityworlds.common.world.chunkgenerator.VoidChunkProvider;
import com.github.bartimaeusnek.openutilityworlds.common.world.dimension.DimensionTypeManager;
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
