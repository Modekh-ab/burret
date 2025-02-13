package net.modekh.burret.objects.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.modekh.burret.network.packets.server.UpdateStatusSwitchPacket;
import net.modekh.burret.registry.*;
import net.modekh.burret.utils.ParticleUtils;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class BurretBlockEntity extends BlockEntity {
//    public BlockCapabilityCache<IItemHandler, @Nullable Direction> capCache;
//
//    private final ItemStackHandler stackHandler = new ItemStackHandler(2) {};
//
//    public Lazy<IItemHandler> lazyStackHandler = Lazy.of(() -> stackHandler);

    public static ItemStack burretStack = ItemStack.EMPTY;
    public static ItemStack catalystStack = ItemStack.EMPTY;

    public int ticks = 0;
    private int delay = 20;
    private float radius = 8.0F;

    private static final Color BURRET_COLOR = new Color(129, 66, 66, 111);
    private static final Color BURRET_AREA_COLOR = new Color(255, 205, 7);

    public BurretBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockRegistry.BURRET_ENTITY.get(), pos, blockState);
    }

    public static void tick(Level level, BlockPos pos, BurretBlockEntity burretEntity) {
        if (level == null) {
            return;
        }

        if (!catalystStack.isEmpty() && burretEntity.radius <= 16.0F) {
            burretEntity.radius += 0.5F;
            catalystStack.shrink(1);
        }

        // enable / disable the curret by the key
        if (KeyRegistry.KEY_STATUS_SWITCH.consumeClick()) {
            boolean newStatus = !burretEntity.getData(AttachmentRegistry.STATUS);

            burretEntity.setData(AttachmentRegistry.STATUS, newStatus);

            PacketDistributor.sendToServer(new UpdateStatusSwitchPacket(newStatus));
        }

        if (burretEntity.getData(AttachmentRegistry.STATUS)) {
            ParticleUtils.drawCircle(level,
                    ParticleUtils.drawSpark(BURRET_AREA_COLOR, 0.46F, 1000, 0.76F),
                    pos.getBottomCenter(), burretEntity.radius, 0.25F
            );
        }

        // shooting logic

        burretEntity.ticks++;

        ItemStack stack = burretEntity.getBurretStack();

        if (stack == null || stack.isEmpty()) {
            return;
        }

        if (burretEntity.ticks == 0 || burretEntity.ticks % burretEntity.getDelay() != 0) {
            level.addParticle(ParticleUtils.drawSpark(BURRET_COLOR, 0.56F, 500, 0.76F),
                    pos.getX() + 0.5F, pos.getY() + 1.1F, pos.getZ() + 0.5F,
                    0.0D, 0.1D, 0.0D);

            return;
        }

        AABB sphereArea = new AABB(pos).inflate(burretEntity.radius);
        List<Entity> entities = level.getEntities(null, sphereArea);

        if (entities.isEmpty()) {
            return;
        }

        var projectile = getProjectile(level);

        if (projectile == null) {
            return;
        }

        if (!burretEntity.getData(AttachmentRegistry.STATUS)) {
            return;
        }

        for (Entity entity : entities) {
//            if (entity instanceof Player player && !player.getData(AttachmentRegistry.STATUS)) {
//                break;
//            }

            if (entity.distanceTo(entity) <= Math.pow(burretEntity.radius, 2)) {
                if (entity instanceof Player player) {
                    projectile.setOwner(player);
                }

                // shooting
                if (entity instanceof Monster) {
                    if (((Monster) entity).isDeadOrDying()) {
                        return;
                    }

//                    if (level instanceof ServerLevel) {
//                        Vec3 motion = entity.position().subtract(pos.getCenter());
//
//                        projectile.setPos(pos.getCenter().add(0.0F, 1.4F, 0.0F));
//                        projectile.setDeltaMovement(projectile.getDeltaMovement()
//                                .add(motion.x(), (projectile instanceof Fireball) ? -1.0F : -0.4F, motion.z()));
//
////                        PacketDistributor.sendToPlayersTrackingEntity(projectile,
////                                new TargetMonsterPacket(projectile.getId(), entity.getId()));
//
//                        level.addFreshEntity(projectile);
//                        stack.shrink(1);
//                    }

                    Vec3 motion = entity.position().subtract(pos.getCenter());

                    projectile.setPos(pos.getCenter().add(0.0F, 1.4F, 0.0F));
                    projectile.setDeltaMovement(projectile.getDeltaMovement()
                            .add(motion.x(), (projectile instanceof Fireball) ? -1.0F : -0.4F, motion.z()));

                    level.addFreshEntity(projectile);
                    stack.shrink(1);
                }
            }
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);

//        if (stackHandler != null) {
//            tag.put("stack", stackHandler.serializeNBT(registries));
//        }

        tag.putInt("delay", this.getDelay());
        tag.putFloat("radius", this.radius);

        if (burretStack != null && !burretStack.isEmpty()) {
            tag.put("stack", burretStack.save(registries, new CompoundTag()));
        }
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);

//        stackHandler.deserializeNBT(registries, tag.getCompound("stack"));
        setDelay(tag.getInt("delay"));
        setRadius(tag.getFloat("radius"));

        burretStack = ItemStack.parseOptional(registries, tag.getCompound("stack"));
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);

        this.saveAdditional(tag, registries);

        return super.getUpdateTag(registries);
    }

    // container

//    @Override
//    public void onLoad() {
//        super.onLoad();
//
//        lazyStackHandler = Lazy.of(() -> stackHandler);
//
//        if (getLevel() instanceof ServerLevel serverLevel) {
//            this.capCache = BlockCapabilityCache.create(
//                    Capabilities.ItemHandler.BLOCK,
//                    serverLevel, this.getBlockPos(), Direction.NORTH
//            );
//        }
//    }
//
//    @Override
//    public void invalidateCapabilities() {
//        super.invalidateCapabilities();
//        lazyStackHandler.invalidate();
//    }
//
//    @Override
//    public @NotNull Component getDisplayName() {
//        return Component.translatable("block.burret.burret");
//    }
//
//    @Override
//    public @Nullable AbstractContainerMenu createMenu(int containerId,
//                                                      @NotNull Inventory playerInventory, @NotNull Player player) {
//        return new BurretMenu(containerId, playerInventory, this);
//    }

    // utils

    public ItemStack getBurretStack() {
        return burretStack;
    }

    public void setBurretStack(ItemStack burretStack) {
        BurretBlockEntity.burretStack = burretStack;
    }

    public ItemStack getCatalystStack() {
        return catalystStack;
    }

    public void setCatalystStack(ItemStack catalystStack) {
        BurretBlockEntity.catalystStack = catalystStack;
    }

    @Nullable
    private static Projectile getProjectile(Level level) {
        Map<Item, Projectile> projectilesMap = Map.of(
                Items.ARROW, new Arrow(EntityType.ARROW, level),
                Items.SPECTRAL_ARROW, new SpectralArrow(EntityType.SPECTRAL_ARROW, level),
                Items.TIPPED_ARROW, new Arrow(EntityType.ARROW, level),
                Items.EGG, new ThrownEgg(EntityType.EGG, level),
                Items.FIRE_CHARGE, new SmallFireball(EntityType.SMALL_FIREBALL, level),
                Items.FIREWORK_ROCKET, new FireworkRocketEntity(EntityType.FIREWORK_ROCKET, level),
                Items.SPLASH_POTION, new ThrownPotion(EntityType.POTION, level),
                Items.LINGERING_POTION, new ThrownPotion(EntityType.POTION, level),
                Items.SNOWBALL, new Snowball(EntityType.SNOWBALL, level),
                Items.TRIDENT, new ThrownTrident(EntityType.TRIDENT, level)
        );

        return projectilesMap.get(burretStack.getItem());
    }

    private int getDelay() {
        return delay;
    }

    public void setDelay(int newDelay) {
        this.delay = newDelay;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float newRadius) {
        this.radius = newRadius;
    }
}
