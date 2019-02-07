package com.github.bartimaeusnek.bettervoidworlds.common.world.dimension;

import com.github.bartimaeusnek.bettervoidworlds.OUW;
import com.github.bartimaeusnek.bettervoidworlds.common.config.ConfigHandler;
import com.github.bartimaeusnek.bettervoidworlds.common.world.provider.*;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.DimensionManager;

public enum DimensionTypeManager {
    VOID("VOID", VoidWorldProvider.class, false),
    GARDEN("GARDEN", GardenWorldProvider.class, false),
    MINING("MINING", MiningWorldProvider.class, false),
    WINTER("WINTER", WinterWorldProvider.class, false),
    DUNES("DUNES", DuneWorldProvider.class, false),
    ;


    public static final PropertyEnum<PortalTypes> PROPERTY_ENUM = PropertyEnum.create("type", PortalTypes.class);
    private DimensionType dimensionType;

    private DimensionTypeManager(String name, Class<? extends WorldProvider> provider, boolean keepLoaded) {
        dimensionType = DimensionType.register(name, OUW.MODID, DimensionType.values().length, provider, keepLoaded);
    }

    public static int registerAndHotloadNewDim(DimensionTypeManager dimensionType) {
        final int ID = ConfigHandler.dimoffset + DimensionManager.getNextFreeDimId();
        DimensionManager.registerDimension(ID, dimensionType.dimensionType);
        DimensionManager.initDimension(ID);
        return ID;
    }

    public static void registerAndHotloadNewDim(DimensionTypeManager dimensionType, final int ID) {
        DimensionManager.registerDimension(ID, dimensionType.dimensionType);
        DimensionManager.initDimension(ID);
    }

    public static int registerNewDimWithoutLoad(DimensionTypeManager dimensionType) {
        final int ID = ConfigHandler.dimoffset + DimensionManager.getNextFreeDimId();
        DimensionManager.registerDimension(ID, dimensionType.dimensionType);
        return ID;
    }


    public static DimensionTypeManager getDimTypeFromMeta(int meta) {
        DimensionTypeManager ret;
        switch (meta) {
            case 2:
                ret = DimensionTypeManager.MINING;
                break;
            case 4:
                ret = DimensionTypeManager.GARDEN;
                break;
            case 5:
                ret = DimensionTypeManager.DUNES;
                break;
            case 6:
                ret = DimensionTypeManager.WINTER;
                break;
            default:
                ret = DimensionTypeManager.VOID;
                break;
        }
        return ret;
    }

    public DimensionType getDimensionType() {
        return dimensionType;
    }

    public static enum PortalTypes implements Comparable<PortalTypes>, IStringSerializable {

        RETURN(0), MINING(2), VOID(1), SHARED(3), GARDEN(4), DUNES(5), WINTER(6);

        int meta;

        private PortalTypes(int meta) {
            this.meta = meta;
        }

        public static PortalTypes getTypeFromMeta(int meta) {
            for (PortalTypes pt : PortalTypes.values()) {
                if (pt.meta == meta)
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

        public int getMeta() {
            return meta;
        }
    }


}
