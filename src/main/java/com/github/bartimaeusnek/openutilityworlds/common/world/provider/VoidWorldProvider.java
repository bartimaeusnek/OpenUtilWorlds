package com.github.bartimaeusnek.openutilityworlds.common.world.provider;

import com.github.bartimaeusnek.openutilityworlds.common.world.dimension.DimensionTypeManager;
import net.minecraft.world.DimensionType;

public class VoidWorldProvider extends  UniversalWorldProvider {
    @Override
    public DimensionType getDimensionType() {
        return DimensionTypeManager.VOID.getDimensionType();
    }
}
