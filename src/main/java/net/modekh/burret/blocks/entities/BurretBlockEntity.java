package net.modekh.burret.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.modekh.burret.registry.AttachmentRegistry;
import net.modekh.burret.registry.BlockRegistry;
import net.modekh.burret.utils.ParticleUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.text.html.Option;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class BurretBlockEntity extends BlockEntity {
    private static final String STACK_TAG = "burretStack";

    public static ItemStack burretStack = ItemStack.EMPTY;

    public BurretBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockRegistry.BURRET_ENTITY.get(), pos, blockState);
    }

//    private static long tick = 0;
    private static final int DELAY = 20;

    public static void tick(Level level, BlockPos pos) {
        float radius = 8.0F;

        ParticleUtils.drawCircle(level,
                ParticleUtils.drawSpark(new Color(0, 0, 0), 0.24F, 10000, 0.86F),
                pos.getBottomCenter(), radius, 0.15F
        );

//        tick++;

        // tick == 0 || tick % DELAY != 0 ||
        if (burretStack == null || burretStack.isEmpty()) {
            return;
        }

        AABB sphereArea = new AABB(pos).inflate(radius);
        List<Entity> entities = level.getEntities(null, sphereArea);

        if (entities.isEmpty()) {
            return;
        }

        var projectile = getProjectile(level);

        if (projectile == null) {
            return;
        }

        for (Entity entity : entities) {
            if (entity instanceof Player player && !player.getData(AttachmentRegistry.STATUS)) {
                break;
            }

            if (entity.distanceTo(entity) <= radius * radius) {
                System.out.println("herr");

                if (entity instanceof Player player) {
                    projectile.setOwner(player);
                }

                // shooting
                if (entity instanceof Monster && level instanceof ServerLevel) {
                    Vec3 motion = entity.position().subtract(pos.getBottomCenter()).scale(0.1F);

                    projectile.setPos(pos.getX(), pos.getY() + 0.4F, pos.getZ());
                    projectile.setDeltaMovement(
                            projectile.getDeltaMovement().add(motion.x(), 0.0F, motion.z()));

                    level.addFreshEntity(projectile);
                    burretStack.shrink(1);

                    // also draw some particles
                }
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        if (burretStack != null && !burretStack.isEmpty()) {
            tag.put(STACK_TAG, burretStack.save(registries, new CompoundTag()));
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        burretStack = ItemStack.parseOptional(registries, tag.getCompound(STACK_TAG));
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);

        this.saveAdditional(tag, registries);

        return super.getUpdateTag(registries);
    }

    public ItemStack getBurretStack() {
        return burretStack;
    }

    public void setBurretStack(ItemStack burretStack) {
        BurretBlockEntity.burretStack = burretStack;
    }

    private static Projectile getProjectile(Level level) {
        Map<Item, Projectile> projectilesMap = Map.of(
                Items.ARROW, new Arrow(EntityType.ARROW, level),
                Items.SPECTRAL_ARROW, new SpectralArrow(EntityType.SPECTRAL_ARROW, level),
                Items.EGG, new ThrownEgg(EntityType.EGG, level),
                Items.EXPERIENCE_BOTTLE, new ThrownExperienceBottle(EntityType.EXPERIENCE_BOTTLE, level),
                Items.FIRE_CHARGE, new SmallFireball(EntityType.SMALL_FIREBALL, level),
                Items.FIREWORK_ROCKET, new FireworkRocketEntity(EntityType.FIREWORK_ROCKET, level),
                Items.SPLASH_POTION, new ThrownPotion(EntityType.POTION, level),
                Items.LINGERING_POTION, new ThrownPotion(EntityType.POTION, level),
                Items.SNOWBALL, new Snowball(EntityType.SNOWBALL, level),
                Items.TRIDENT, new ThrownTrident(EntityType.TRIDENT, level)
        );

        return projectilesMap.get(burretStack.getItem());
    }
}
