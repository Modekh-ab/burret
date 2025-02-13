//package net.modekh.burret.client.models.entities;
//
//import com.mojang.blaze3d.vertex.PoseStack;
//import com.mojang.blaze3d.vertex.VertexConsumer;
//import net.minecraft.client.model.EntityModel;
//import net.minecraft.client.model.geom.ModelPart;
//import net.minecraft.client.model.geom.PartPose;
//import net.minecraft.client.model.geom.builders.*;
//import net.minecraft.world.entity.Entity;
//
//public class BalletModel<T extends Entity> extends EntityModel<T> {
//    private final ModelPart ballet;
//
//    public BalletModel() {
//        MeshDefinition meshdefinition = new MeshDefinition();
//        PartDefinition partdefinition = meshdefinition.getRoot();
//
//        PartDefinition ballet = partdefinition.addOrReplaceChild("snowball", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -15.0F, -7.0F, 14.0F, 14.0F, 14.0F, new CubeDeformation(0.0F))
//                .texOffs(22, 40).addBox(7.0F, -13.0F, -5.0F, 1.0F, 10.0F, 10.0F, new CubeDeformation(0.0F))
//                .texOffs(0, 39).addBox(-8.0F, -13.0F, -5.0F, 1.0F, 10.0F, 10.0F, new CubeDeformation(0.0F))
//                .texOffs(44, 40).addBox(-5.0F, -13.0F, -8.0F, 10.0F, 10.0F, 1.0F, new CubeDeformation(0.0F))
//                .texOffs(42, 0).addBox(-5.0F, -13.0F, 7.0F, 10.0F, 10.0F, 1.0F, new CubeDeformation(0.0F))
//                .texOffs(30, 29).addBox(-5.0F, -1.0F, -5.0F, 10.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
//                .texOffs(0, 28).addBox(-5.0F, -16.0F, -5.0F, 10.0F, 1.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 8.0F, 0.0F));
//
//        this.ballet = LayerDefinition.create(meshdefinition, 128, 128).bakeRoot().getChild("snowball");
//    }
//
//    @Override
//    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
//    }
//
//    @Override
//    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
//        ballet.render(poseStack, buffer, packedLight, packedOverlay, color);
//    }
//}
