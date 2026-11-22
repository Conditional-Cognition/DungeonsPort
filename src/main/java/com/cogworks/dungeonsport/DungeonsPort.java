package com.cogworks.dungeonsport;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.cogworks.dungeonsport.registry.*;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;

@Mod(DungeonsPort.MODID)
public class DungeonsPort {
    public static final String MODID = "dungeonsport";
    public static final Logger LOGGER = LogUtils.getLogger();

    public DungeonsPort(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(DPBlocks::register);
        modEventBus.addListener(DPItems::register);
        modEventBus.addListener(DPTabs::register);

        DPEffects.register(modEventBus);
    }
}