package com.github.bartimaeusnek.bettervoidworlds.common.handlers;

import com.github.bartimaeusnek.bettervoidworlds.common.world.dimension.DimensionTypeManager;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

public class SaveEventHandler {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onWorldLoad(WorldEvent.Load onSave) {
        boolean ret = false;
        for (DimensionTypeManager DTM : DimensionTypeManager.values())
            if (DTM.getDimensionType().getId() == onSave.getWorld().provider.getDimensionType().getId())
                ret = true;
        if (!ret)
            return;
        String OUWWorldSave = DimensionManager.getWorld(0).getSaveHandler().getWorldDirectory().toString() + File.separatorChar + "ouwData.dat";
        IOHandler ioh = new IOHandler(OUWWorldSave);
        ioh.addToAndSave(onSave.getWorld().provider.getDimension(), onSave.getWorld().provider.getDimensionType().getId());
    }
}
