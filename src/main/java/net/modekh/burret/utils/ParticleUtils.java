package net.modekh.burret.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.modekh.burret.client.particles.SparkParticle;

import java.awt.*;

public class ParticleUtils {
    private final static Color BURRET_COLOR = new Color(0,0, 0);

    public static void drawCircle(Level level, ParticleOptions spark, Vec3 center, float radius, float gap) {
        int sparksNum = (int) Math.ceil((2 * Math.PI * radius) / gap);
        double angleIncrement = (2 * Math.PI) / sparksNum;

        for (int i = 0; i < sparksNum; i++) {
            double angle = i * angleIncrement;

            double x = center.x() + radius * Math.cos(angle);
            double y = center.y();
            double z = center.z() + radius * Math.sin(angle);

            int counter, counterMax = 16;
            boolean foundBlock = false;

            for (counter = 0; counter < counterMax; counter++) {
                BlockPos pos = new BlockPos(Mth.floor(x), Mth.floor(y), Mth.floor(z));
                BlockState state = level.getBlockState(pos);
                VoxelShape shape = state.getCollisionShape(level, pos);

                if (state.getBlock() instanceof LiquidBlock) {
                    shape = Shapes.block();
                }

                if (shape.isEmpty()) {
                    if (!foundBlock) {
                        y--;
                        continue;
                    } else {
                        break;
                    }
                } else {
                    foundBlock = true;
                }

                AABB border = shape.bounds();

                if (!border.move(pos).contains(new Vec3(x, y, z))) {
                    if (border.maxY >= 1.0) {
                        y++;
                        continue;
                    } else {
                        break;
                    }
                }

                y += gap;
            }

            if (counter < counterMax) {
                level.addParticle(spark, x, y + 0.1, z, 0, 0, 0);
            }
        }
    }

    public static ParticleOptions drawSpark(Color color, float diameter, int lifetime, float scaleModifier) {
        return new SparkParticle.Options(SparkParticle.Constructor.builder()
                .color(color.getRGB())
                .diameter(diameter)
                .lifetime(lifetime)
                .scaleModifier(scaleModifier)
                .roll(0.5F)
                .build());
    }
}
