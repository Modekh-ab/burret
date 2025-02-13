//package net.modekh.burret.client.renderer;
//
//import com.mojang.blaze3d.vertex.PoseStack;
//import com.mojang.math.Axis;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.LightTexture;
//import net.minecraft.client.renderer.MultiBufferSource;
//import net.minecraft.client.renderer.RenderType;
//import net.minecraft.client.renderer.entity.EntityRenderer;
//import net.minecraft.client.renderer.entity.EntityRendererProvider;
//import net.minecraft.client.renderer.texture.OverlayTexture;
//import net.minecraft.resources.ResourceLocation;
//import net.modekh.burret.client.models.entities.BalletModel;
//import net.modekh.burret.objects.items.entities.Ballet;
//import net.modekh.burret.utils.Reference;
//import net.neoforged.api.distmarker.Dist;
//import net.neoforged.api.distmarker.OnlyIn;
//import org.jetbrains.annotations.NotNull;
//
//@OnlyIn(Dist.CLIENT)
//public class BalletRenderer extends EntityRenderer<Ballet> {
//    protected BalletRenderer(EntityRendererProvider.Context context) {
//        super(context);
//    }
//
//    @Override
//    public @NotNull ResourceLocation getTextureLocation(@NotNull Ballet entity) {
//        return ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "textures/items/ballet.png");
//    }
//
//    @Override
//    public void render(Ballet ballet, float entityYaw, float partialTick,
//                       PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
//        float time = ballet.tickCount + (Minecraft.getInstance().isPaused() ? 0 : partialTick);
//
//        poseStack.pushPose();
//
//        poseStack.translate(0F, 0.25F, 0F);
//
//        float speed = 20F;
//
//        poseStack.mulPose(Axis.YP.rotationDegrees(time * speed));
//        poseStack.mulPose(Axis.XN.rotationDegrees(time * speed));
//
//        float scale = 0.1F + 0.009F;
//
//        poseStack.scale(scale, scale, scale);
//
//        new BalletModel<>().renderToBuffer(poseStack,
//                bufferSource.getBuffer(RenderType.entityCutout(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID,
//                "textures/items/ballet.png"))), packedLight, OverlayTexture.NO_OVERLAY);
//
//        poseStack.popPose();
//    }
//}
