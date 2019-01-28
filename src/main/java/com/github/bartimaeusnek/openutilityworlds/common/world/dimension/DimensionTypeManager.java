package com.github.bartimaeusnek.openutilityworlds.common.world.dimension;

import com.github.bartimaeusnek.openutilityworlds.OUW;
import com.github.bartimaeusnek.openutilityworlds.common.config.ConfigHandler;
import com.github.bartimaeusnek.openutilityworlds.common.world.provider.VoidWorldProvider;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.DimensionManager;

public enum DimensionTypeManager {
    VOID("VOID", VoidWorldProvider.class,false);

    public DimensionType getDimensionType() {
        return dimensionType;
    }

    private DimensionType dimensionType;

    private DimensionTypeManager(String name, Class<? extends WorldProvider> provider, boolean keepLoaded){
        dimensionType=DimensionType.register(name, OUW.MODID, DimensionType.values().length, provider, keepLoaded);
    }

    public static int registerAndHotloadNewDim(DimensionTypeManager dimensionType){
        final int ID = ConfigHandler.dimoffset+DimensionManager.getNextFreeDimId();
        DimensionManager.registerDimension(ID, dimensionType.dimensionType);
        DimensionManager.initDimension(ID);
        return ID;
    }

    public static void registerAndHotloadNewDim(DimensionTypeManager dimensionType,final int ID){
        DimensionManager.registerDimension(ID, dimensionType.dimensionType);
        DimensionManager.initDimension(ID);
    }

    public static int registerNewDimWithoutLoad(DimensionTypeManager dimensionType){
        final int ID = ConfigHandler.dimoffset+DimensionManager.getNextFreeDimId();
        DimensionManager.registerDimension(ID, dimensionType.dimensionType);
        return ID;
    }

    public static final PropertyEnum<PortalTypes> PROPERTY_ENUM = PropertyEnum.create("type",PortalTypes.class);
    public static enum PortalTypes implements Comparable<PortalTypes>, IStringSerializable {

        RETURN(0),MINING(2),VOID(1),SHARED(3),GARDEN(4);

        private PortalTypes(int meta){
            this.meta=meta;
        }

        public static PortalTypes getTypeFromMeta(int meta){
            for (PortalTypes pt : PortalTypes.values()){
                if(pt.meta == meta)
                    return pt;
            }
            return VOID;
        }

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }

        @Override
        public String getName() {
            return this.toString().toLowerCase();
        }

        int meta;

        public int getMeta() {
            return meta;
        }
    }


}
