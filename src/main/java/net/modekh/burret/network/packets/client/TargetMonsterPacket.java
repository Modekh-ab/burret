package net.modekh.burret.network.packets.client;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.modekh.burret.utils.Reference;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public class TargetMonsterPacket implements CustomPacketPayload {
    private final int projectileId;
    private final int monsterId;

    public TargetMonsterPacket(int projectileId, int monsterId) {
        this.projectileId = projectileId;
        this.monsterId = monsterId;
    }

    public static final CustomPacketPayload.Type<TargetMonsterPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "entity_target"));

    public static final StreamCodec<ByteBuf, TargetMonsterPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, TargetMonsterPacket::getMonsterId,
            ByteBufCodecs.INT, TargetMonsterPacket::getProjectileId,
            TargetMonsterPacket::new
    );

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Level level = ctx.player().getCommandSenderWorld();

            if (level.getEntity(projectileId) instanceof Projectile projectile
                    && level.getEntity(monsterId) instanceof Monster entity) {
                Vec3 motion = entity.position().subtract(projectile.position());

                projectile.setPos(projectile.position().add(0.0F, 1.4F, 0.0F));
                projectile.setDeltaMovement(projectile.getDeltaMovement()
                        .add(motion.x(), -0.5F, motion.z()));
            }
        });
    }

    @Override
    public CustomPacketPayload.@NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public int getProjectileId() {
        return projectileId;
    }

    public int getMonsterId() {
        return monsterId;
    }
}
