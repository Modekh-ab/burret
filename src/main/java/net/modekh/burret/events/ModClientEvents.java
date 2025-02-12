package net.modekh.burret.events;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.modekh.burret.client.renderer.BurretBlockEntityRenderer;
import net.modekh.burret.network.packets.server.UpdateStatusSwitchPacket;
import net.modekh.burret.registry.AttachmentRegistry;
import net.modekh.burret.registry.BlockRegistry;
import net.modekh.burret.registry.KeyRegistry;
import net.modekh.burret.utils.Reference;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public class ModClientEvents {
    @EventBusSubscriber(modid = Reference.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class BusClientEvents {
        @SubscribeEvent
        public static void onEntitiesRender(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(BlockRegistry.BURRET_ENTITY.get(), BurretBlockEntityRenderer::new);
        }
    }

    @EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
    public static class ClientEvents {
        @SubscribeEvent
        public static void onKeyUse(InputEvent.Key event) {
            Player player = Minecraft.getInstance().player;

            if (player == null) {
                return;
            }

            if (event.getAction() != InputConstants.PRESS
                    && event.getAction() != InputConstants.REPEAT) {
                return;
            }

            if (event.getKey() == KeyRegistry.KEY_STATUS_SWITCH.getKey().getValue()) {
                boolean newStatus = !player.getData(AttachmentRegistry.STATUS);

                player.setData(AttachmentRegistry.STATUS, newStatus);
                PacketDistributor.sendToServer(new UpdateStatusSwitchPacket(newStatus));

                player.displayClientMessage(
                        Component.translatable("message.burret.status",
                                newStatus ? "on" : "off"), true);
            }
        }
    }
}
