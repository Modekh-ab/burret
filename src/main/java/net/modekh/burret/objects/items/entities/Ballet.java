//package net.modekh.burret.objects.items.entities;
//
//import net.minecraft.core.particles.ParticleTypes;
//import net.minecraft.network.syncher.EntityDataAccessor;
//import net.minecraft.network.syncher.EntityDataSerializers;
//import net.minecraft.network.syncher.SynchedEntityData;
//import net.minecraft.sounds.SoundEvents;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.phys.Vec3;
//import net.modekh.burret.registry.EntityTypesRegistry;
//import net.modekh.burret.registry.ItemRegistry;
//import net.neoforged.bus.api.SubscribeEvent;
//import org.jetbrains.annotations.NotNull;
//
//public class Ballet extends ThrowableItemProjectile {
//    private static final EntityDataAccessor<Integer> BOUNCES =
//            SynchedEntityData.defineId(Ballet.class, EntityDataSerializers.INT);
//    private static final double BOUNCE_DAMPENING = 0.7;
//    private static final double FRICTION = 0.98;
//    private static final int MAX_BOUNCES = 5;
//    private static final float DAMAGE = 4.0f;
//
//    public Ballet(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
//        super(entityType, level);
//    }
//
//    @Override
//    protected @NotNull Item getDefaultItem() {
//        return ItemRegistry.BALLET.get().asItem();
//    }
//
//    @Override
//    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
//        super.defineSynchedData(builder);
//        builder.define(BOUNCES, 0);
//    }
//
//    @Override
//    public void tick() {
//        super.tick();
//
//        Vec3 motion = this.getDeltaMovement();
//
//        if (this.onGround()) {
//            int bounces = this.entityData.get(BOUNCES);
//
//            if (bounces < MAX_BOUNCES) {
//                this.discard();
//                return;
//            }
//
//            this.entityData.set(BOUNCES, bounces + 1);
//            this.setDeltaMovement(motion.x * FRICTION, -motion.y * BOUNCE_DAMPENING, motion.z * FRICTION);
//
//            this.playSound(SoundEvents.SLIME_BLOCK_FALL, 1.0F, 1.0F);
//        } else {
//            this.setDeltaMovement(motion.x * FRICTION, motion.y - 0.04F, motion.z * FRICTION);
//        }
//
//        if (!this.level().isClientSide) {
//            for (LivingEntity entity
//                    : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0.3))) {
//                if (entity == this.getOwner()) {
//                    return;
//                }
//
//                entity.hurt(this.damageSources().thrown(this, this.getOwner()), DAMAGE);
//
//                this.discard();
//                return;
//            }
//        }
//
//        this.level().addParticle(ParticleTypes.CLOUD,
//                this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F, 0.0F);
//    }
//}
