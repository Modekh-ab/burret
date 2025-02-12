package net.modekh.burret.client.particles;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.modekh.burret.registry.ParticleRegistry;
import net.modekh.burret.utils.Reference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * Based on particles implementation from <b>Relics</b> mod
 */
public class SparkParticle extends TextureSheetParticle {
    private final Constructor constructor;

    public SparkParticle(ClientLevel level,
                         double x, double y, double z,
                         double xSpeed, double ySpeed, double zSpeed, Constructor constructor) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);

        setColor(constructor.color.getRed() / 255F,
                constructor.color.getGreen() / 255F,
                constructor.color.getBlue() / 255F);
        setSize(constructor.diameter, constructor.diameter);
        setAlpha(constructor.color.getAlpha() / 255F);
        setLifetime(constructor.lifetime);

        this.constructor = constructor;
        this.quadSize = constructor.diameter;

        this.xd = xSpeed; // xD
        this.yd = ySpeed;
        this.zd = zSpeed;
    }

    @Override
    public void tick() {
        this.quadSize *= constructor.scaleModifier;

        xo = x;
        yo = y;
        zo = z;

        oRoll = roll;
        roll += constructor.roll;

        move(xd, yd, zd);

        if (this.age++ >= this.lifetime)
            this.remove();
    }

    @Override
    protected int getLightColor(float partialTick) {
        return LightTexture.FULL_BRIGHT;
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return PARTICLE_RENDERER;
    }

    private static final ParticleRenderType PARTICLE_RENDERER = new ParticleRenderType() {
        @Override
        public BufferBuilder begin(Tesselator tesselator, @NotNull TextureManager manager) {
            RenderSystem.setShader(GameRenderer::getParticleShader);
            RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);

            RenderSystem.enableBlend();

            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);

            RenderSystem.depthMask(false);

            return tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        }

        @Override
        public String toString() {
            return Reference.MOD_ID + ":spark";
        }
    };

    @lombok.Builder
    public static class Constructor {
        @lombok.Builder.Default private Color color = new Color(0xFFFFFFFF, true);
        @lombok.Builder.Default private float diameter = 1F;
        @lombok.Builder.Default private float roll = 0F;
        @lombok.Builder.Default private int lifetime = 20;
        @lombok.Builder.Default private float scaleModifier = 1F;

        public static class ConstructorBuilder {
            private Color color;

            public ConstructorBuilder color(int color) {
                this.color = new Color(color, true);

                return this;
            }
        }
    }

    public static class Options implements ParticleOptions {
        private final Constructor data;

        private Options(int color, float diameter, int lifetime, float roll, float scaleModifier) {
            this.data = Constructor.builder()
                    .color(color)
                    .diameter(diameter)
                    .lifetime(lifetime)
                    .roll(roll)
                    .scaleModifier(scaleModifier)
                    .build();
        }

        public Options(Constructor data) {
            this.data = data;
        }

        @Override
        public @NotNull ParticleType<Options> getType() {
            return ParticleRegistry.SPARK.get();
        }

        public static final MapCodec<Options> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        Codec.INT.fieldOf("color").forGetter(options -> options.data.color.getRGB()),
                        Codec.FLOAT.fieldOf("diameter").forGetter(options -> options.data.diameter),
                        Codec.INT.fieldOf("lifetime").forGetter(options -> options.data.lifetime),
                        Codec.FLOAT.fieldOf("roll").forGetter(options -> options.data.roll),
                        Codec.FLOAT.fieldOf("scaleModifier").forGetter(options -> options.data.scaleModifier)
                ).apply(instance, Options::new));

        public static final StreamCodec<ByteBuf, Options> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.INT, options -> options.data.color.getRGB(),
                ByteBufCodecs.FLOAT, options -> options.data.diameter,
                ByteBufCodecs.INT, options -> options.data.lifetime,
                ByteBufCodecs.FLOAT, options -> options.data.roll,
                ByteBufCodecs.FLOAT, options -> options.data.scaleModifier,
                Options::new
        );
    }

    public static class Provider implements ParticleProvider<Options> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public @Nullable Particle createParticle(Options type, @NotNull ClientLevel level,
                                                 double x, double y, double z,
                                                 double xSpeed, double ySpeed, double zSpeed) {
            SparkParticle spark = new SparkParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, type.data);
            spark.pickSprite(spriteSet);

            return spark;
        }
    }

    public static class Type extends ParticleType<Options> {
        public Type() {
            super(false);
        }

        @Override
        public @NotNull MapCodec<Options> codec() {
            return Options.CODEC;
        }

        @Override
        public @NotNull StreamCodec<? super RegistryFriendlyByteBuf, Options> streamCodec() {
            return Options.STREAM_CODEC;
        }
    }
}
