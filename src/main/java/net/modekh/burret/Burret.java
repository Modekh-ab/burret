package net.modekh.burret;

import net.modekh.burret.registry.*;
import net.modekh.burret.utils.Reference;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Reference.MOD_ID)
public class Burret {
//    private static final Logger LOGGER = LogUtils.getLogger();

    public Burret(IEventBus bus) {
        bus.addListener(CreativeTabRegistry::addCreative);

//        NeoForge.EVENT_BUS.register(this);

        // registry
        AttachmentRegistry.register(bus);
        BlockRegistry.register(bus);
        ItemRegistry.register(bus);
        CreativeTabRegistry.register(bus);
        ParticleRegistry.register(bus);

        // config
//        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}
