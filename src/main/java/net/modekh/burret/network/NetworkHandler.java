package net.modekh.burret.network;

import net.modekh.burret.network.packets.client.TargetMonsterPacket;
import net.modekh.burret.network.packets.server.UpdateStatusSwitchPacket;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class NetworkHandler {
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1").executesOn(HandlerThread.NETWORK);

        registrar.playToServer(UpdateStatusSwitchPacket.TYPE,
                UpdateStatusSwitchPacket.STREAM_CODEC, UpdateStatusSwitchPacket::handle);

//        registrar.playToClient(TargetMonsterPacket.TYPE,
//                TargetMonsterPacket.STREAM_CODEC, TargetMonsterPacket::handle);
    }
}
