package net.modekh.burret;

import net.minecraft.server.Services;
import net.minecraft.world.entity.Entity;
import net.modekh.burret.registry.*;
import net.modekh.burret.utils.Reference;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Reference.MOD_ID)
public class Burret {
//    private static final Logger LOGGER = LogUtils.getLogger();

    public Burret(IEventBus bus) {
        bus.addListener(CreativeTabRegistry::addCreative);

        // registry
        AttachmentRegistry.register(bus);
        BlockRegistry.register(bus);
        ItemRegistry.register(bus);
//        EntityTypesRegistry.register(bus);
        CreativeTabRegistry.register(bus);
        ParticleRegistry.register(bus);

        // config
//        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}
