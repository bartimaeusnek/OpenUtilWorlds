package com.github.bartimaeusnek.openutilityworlds.common.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ConfigHandler {
    public static volatile int dimoffset;
    public static volatile boolean canRespawn;
    public static volatile boolean alwaysDay;
    public static volatile boolean rainAndSnow;
    public static volatile boolean thunder;
    public static volatile boolean creatureSpawn;
    public static volatile boolean alwaysToSpawn;

    private static Configuration configuration;


    public ConfigHandler(FMLPreInitializationEvent event) {
        configuration = new Configuration(event.getSuggestedConfigurationFile());
        canRespawn=configuration.get("Default Dim Logic","canRespawn",true,"If you respawn in the Dimension ur in").getBoolean(true);
        alwaysDay=configuration.get("Default Dim Logic","alwaysDay",false,"If it is always Day in your Dimension").getBoolean(false);
        rainAndSnow=configuration.get("Default Dim Logic","rainAndSnow",false,"If it can rain/snow in the Dimension ur in").getBoolean(false);
        thunder=configuration.get("Default Dim Logic","thunder",false,"If it can thunder in the Dimension ur in").getBoolean(false);
        creatureSpawn=configuration.get("Default Dim Logic","creatureSpawn",false,"If creatures can spawn in the Dimension ur in").getBoolean(false);
        dimoffset=configuration.get("System","DimensionOffset",0,"A Dimension Offset added if you run into trubles with registering the Dims.").getInt(0);
        alwaysToSpawn=configuration.get("Default Dim Logic","alwaysToSpawn",false,"if this is enabled, your portals will always point to the spawn of the dim you port to.").getBoolean(false);
        if (configuration.hasChanged())
            configuration.save();
    }


}
