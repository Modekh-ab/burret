package net.modekh.burret.events;

import net.modekh.burret.client.renderer.BurretBlockEntityRenderer;
import net.modekh.burret.registry.BlockRegistry;
import net.modekh.burret.utils.Reference;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public class ModClientEvents {
    @EventBusSubscriber(modid = Reference.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class BusClientEvents {
        @SubscribeEvent
        public static void onEntitiesRender(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(BlockRegistry.BURRET_ENTITY.get(), BurretBlockEntityRenderer::new);
        }
    }
}
