//package net.modekh.burret.client.screen;
//
//import com.mojang.blaze3d.systems.RenderSystem;
//import net.minecraft.client.gui.GuiGraphics;
//import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
//import net.minecraft.client.renderer.GameRenderer;
//import net.minecraft.network.chat.Component;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.entity.player.Inventory;
//import net.modekh.burret.utils.Reference;
//import org.jetbrains.annotations.NotNull;
//
//public class BurretScreen extends AbstractContainerScreen<BurretMenu> {
//    private static final ResourceLocation TEXTURE =
//            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "textures/gui/burret.png");
//
//    public BurretScreen(BurretMenu menu, Inventory playerInventory, Component title) {
//        super(menu, playerInventory, title);
//    }
//
//    @Override
//    protected void init() {
//        super.init();
//
//        this.titleLabelY = (this.height - 96) / 2;
//        this.inventoryLabelY = (this.height - 96) / 2;
//    }
//
//    @Override
//    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
//        RenderSystem.setShader(GameRenderer::getPositionTexShader);
//        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//        RenderSystem.setShaderTexture(0, TEXTURE);
//
//        int x = (width - imageWidth) / 2;
//        int y = (height - imageHeight) / 2;
//
//        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
//    }
//
//    @Override
//    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
//        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
//
//        super.render(guiGraphics, mouseX, mouseY, partialTick);
//
//        renderTooltip(guiGraphics, mouseX, mouseY);
//    }
//}
