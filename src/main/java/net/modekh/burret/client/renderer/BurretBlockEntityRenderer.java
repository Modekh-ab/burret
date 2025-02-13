package net.modekh.burret.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.modekh.burret.objects.blocks.entities.BurretBlockEntity;
import org.jetbrains.annotations.NotNull;

public class BurretBlockEntityRenderer implements BlockEntityRenderer<BurretBlockEntity> {
    public BurretBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
    }

    @Override
    public void render(BurretBlockEntity blockEntity, float partialTick, PoseStack poseStack,
                       @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        Level level = blockEntity.getLevel();
        ItemStack itemStack = blockEntity.getBurretStack();

        if (level == null) {
            return;
        }

        poseStack.pushPose();

        Direction facing = blockEntity.getBlockState().getValue(HorizontalDirectionalBlock.FACING);

        float translateX = 0.4225f, translateZ = 0.5F; // defaults for NORTH
        float rotationYP = 180.0F; // default for NORTH

        switch (facing) {
            case SOUTH -> {
                translateX = 0.5625F;
                rotationYP = 0.0F;
            }
            case WEST -> {
                translateX = 0.5F;
                translateZ = 0.425f;
                rotationYP = 90.0F;
            }
            case EAST -> {
                translateX = 0.5F;
                translateZ = 0.5625f;
                rotationYP = -90.0F;
            }
        }

        poseStack.translate(translateX, 1.0F, translateZ);
        poseStack.mulPose(Axis.YP.rotationDegrees(rotationYP));
        poseStack.rotateAround(Axis.ZP.rotation((float) Math.PI / 4), 0.0F, 0.0F, 0.0F);
        poseStack.scale(0.86f, 0.86f, 0.86f);

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        itemRenderer.render(itemStack, ItemDisplayContext.GROUND,
                true, poseStack, bufferSource, packedLight, packedOverlay,
                itemRenderer.getModel(itemStack,blockEntity.getLevel(), null, 0));

        poseStack.popPose();
    }

    private static int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}
