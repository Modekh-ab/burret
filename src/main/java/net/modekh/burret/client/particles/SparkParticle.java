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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
public class SparkParticle extends TextureSheetParticle {
    private final float scaleModifier;

    public SparkParticle(ClientLevel level,
                         double x, double y, double z,
                         double xSpeed, double ySpeed, double zSpeed,
                         int red, int green, int blue, int alpha,
                         float diameter, int lifetime, float scaleModifier, float roll) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);

        this.xd = xSpeed; // xD
        this.yd = ySpeed;
        this.zd = zSpeed;

        this.quadSize = diameter;
        this.lifetime = lifetime;
        this.scaleModifier = scaleModifier;
        this.roll = roll;

        setSize(this.quadSize, this.quadSize);
        setColor(red / 255F, green / 255F, blue / 255F);
        setAlpha(alpha / 255F);
        setLifetime(this.lifetime);
    }

    @Override
    public void tick() {
        this.quadSize *= this.scaleModifier;

        xo = x;
        yo = y;
        zo = z;

        oRoll = roll;
        roll += this.roll;

        move(xd, yd, zd);

        if (this.age++ >= this.lifetime) {
            this.remove();
        }
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

    public static class Provider implements ParticleProvider<Options> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public @Nullable Particle createParticle(@NotNull Options type, @NotNull ClientLevel level,
                                                 double x, double y, double z,
                                                 double xSpeed, double ySpeed, double zSpeed) {
            Color color = new Color(type.color);

            SparkParticle spark = new SparkParticle(level, x, y, z, xSpeed, ySpeed, zSpeed,
                    color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha(),
                    type.diameter, type.lifetime, type.scaleModifier, type.roll);
            spark.pickSprite(spriteSet);

            return spark;
        }
    }

    public static class Options implements ParticleOptions {
        private final int color;
        private final float diameter;
        private final int lifetime;
        private final float roll;
        private final float scaleModifier;

        public Options(int color, float diameter, int lifetime, float roll, float scaleModifier) {
            this.color = color;
            this.diameter = diameter;
            this.lifetime = lifetime;
            this.roll = roll;
            this.scaleModifier = scaleModifier;
        }

        @Override
        public @NotNull ParticleType<Options> getType() {
            return ParticleRegistry.SPARK.get();
        }

        public static final MapCodec<Options> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        Codec.INT.fieldOf("color").forGetter(options -> options.color),
                        Codec.FLOAT.fieldOf("diameter").forGetter(options -> options.diameter),
                        Codec.INT.fieldOf("lifetime").forGetter(options -> options.lifetime),
                        Codec.FLOAT.fieldOf("roll").forGetter(options -> options.roll),
                        Codec.FLOAT.fieldOf("scaleModifier").forGetter(options -> options.scaleModifier)
                ).apply(instance, Options::new));

        public static final StreamCodec<ByteBuf, Options> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.INT, options -> options.color,
                ByteBufCodecs.FLOAT, options -> options.diameter,
                ByteBufCodecs.INT, options -> options.lifetime,
                ByteBufCodecs.FLOAT, options -> options.roll,
                ByteBufCodecs.FLOAT, options -> options.scaleModifier,
                Options::new
        );
    }
}
