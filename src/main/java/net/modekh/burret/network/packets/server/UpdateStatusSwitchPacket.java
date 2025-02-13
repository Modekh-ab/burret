package net.modekh.burret.network.packets.server;

import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.modekh.burret.registry.AttachmentRegistry;
import net.modekh.burret.utils.Reference;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record UpdateStatusSwitchPacket(boolean status) implements CustomPacketPayload {
    public static final StreamCodec<ByteBuf, UpdateStatusSwitchPacket> STREAM_CODEC =
            StreamCodec.ofMember(UpdateStatusSwitchPacket::write, UpdateStatusSwitchPacket::decode);

    public static final CustomPacketPayload.Type<UpdateStatusSwitchPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "status"));

    @Override
    public CustomPacketPayload.@NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    private void write(ByteBuf data) {
        data.writeBoolean(status);
    }

    private static UpdateStatusSwitchPacket decode(ByteBuf buf) {
        var keyDown = buf.readBoolean();
        return new UpdateStatusSwitchPacket(keyDown);
    }

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (!(ctx.player() instanceof ServerPlayer serverPlayer)) {
                return;
            }

            serverPlayer.setData(AttachmentRegistry.STATUS, status);

            serverPlayer.displayClientMessage(Component.translatable("message.burret.status",
                    status ? "on" : "off").withStyle(ChatFormatting.GOLD), true);
        });

    }
}
