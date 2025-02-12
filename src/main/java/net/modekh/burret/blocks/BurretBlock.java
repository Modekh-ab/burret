package net.modekh.burret.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.modekh.burret.blocks.entities.BurretBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BurretBlock extends HorizontalDirectionalBlock implements EntityBlock {
    private static final VoxelShape SHAPE = Block.box(3, 0, 3, 13, 13, 13);

    public BurretBlock(Properties properties) {
        super(properties);
    }

    private static long tick = 0;
    private static final int DELAY = 20;

//    @Override
//    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
//        super.tick(state, level, pos, random);
//
//        float radius = 4.0F;
//
//        ParticleUtils.drawCircle(level,
//                ParticleUtils.drawSpark(new Color(26, 21, 63, 193), 0.24F, 10, 0.76F),
//                pos.getBottomCenter(), radius, 0.56F
//        );
//
//        // shooting logic
//
//        tick++;
//
//        ItemStack burretStack = BurretBlockEntity.burretStack;
//
//        if (tick == 0 || tick % DELAY != 0 || burretStack == null
//                || burretStack.isEmpty() || !(burretStack.getItem() instanceof ProjectileItem)) {
//            return;
//        }
//
//        System.out.println(tick);
//
//        AABB sphereArea = new AABB(pos).inflate(radius);
//        List<Entity> entities = level.getEntities(null, sphereArea);
//
//        if (entities.isEmpty()) {
//            return;
//        }
//
//        var projectile = new Arrow(EntityType.ARROW, level);
//
//        for (Entity entity : entities) {
//            if (entity instanceof ServerPlayer player && !player.getData(AttachmentRegistry.STATUS)) {
//                break;
//            }
//
//            if (entity.distanceTo(entity) <= radius) {
//                if (entity instanceof Player player) {
//                    projectile.setOwner(player);
//                }
//
//                // shooting
//                if (entity instanceof Monster && level instanceof ServerLevel) {
//                    Vec3 motion = entity.position().subtract(projectile.position())
//                            .normalize().scale(0.1F);
//
//                    projectile.setPos(pos.getX(), pos.getY(), pos.getZ());
//                    projectile.setDeltaMovement(
//                            projectile.getDeltaMovement().add(motion.x(), 1.0F, motion.z()));
//
//                    level.addFreshEntity(projectile);
//                    burretStack.shrink(1);
//                }
//            }
//        }
//    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new BurretBlockEntity(pos, state);
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level,
                                           @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);

        builder.add(FACING);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
//        return EntityBlock.super.getTicker(level, state, blockEntityType);
        return (levelIn, pos, blockState, entity) -> BurretBlockEntity.tick(levelIn, pos);
    }

    @Override
    protected @NotNull MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return simpleCodec(BurretBlock::new);
    }

    @Override
    protected void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos,
                            @NotNull BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())
                && level.getBlockEntity(pos) instanceof BurretBlockEntity tile) {
            ItemStack stack = tile.getBurretStack();

            if (stack != null && !stack.isEmpty()) {
                level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), stack));
            }
        }

        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}
