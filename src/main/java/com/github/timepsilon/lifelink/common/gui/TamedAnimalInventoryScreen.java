package com.github.timepsilon.lifelink.common.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Inventory;

public class TamedAnimalInventoryScreen extends AbstractContainerScreen<TamedAnimalInventoryMenu> {

    private static final ResourceLocation TAMED_INVENTORY_TEXTURE = new ResourceLocation("");
    private final LivingEntity animal;
    private float xMouse;
    private float yMouse;

    public TamedAnimalInventoryScreen(TamedAnimalInventoryMenu pMenu, Inventory pPlayerInventory, TamableAnimal animal) {
        super(pMenu, pPlayerInventory, animal.getDisplayName());
        this.animal = animal;
        this.passEvents = false;
    }

    public TamedAnimalInventoryScreen(TamedAnimalInventoryMenu pMenu, Inventory pPlayerInventory, Component title) {
        super(pMenu, pPlayerInventory, title);
        this.animal = pPlayerInventory.player;
        this.passEvents = false;
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1,1,1,1);
        RenderSystem.setShaderTexture(0,TAMED_INVENTORY_TEXTURE);

        // The coordinates of the top left corner on screen
        int xOrg = (width - imageWidth) / 2;
        int yOrg = (height - imageHeight) / 2;

        blit(pPoseStack, xOrg, yOrg, 0, 0, imageWidth, imageHeight);

        InventoryScreen.renderEntityInInventory(xOrg+51, yOrg + 60, 17, (float)(xOrg + 51)-xMouse, (float)(yOrg + 25) - yMouse, animal);
    }

    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pPoseStack);
        xMouse = (float) pMouseX;
        yMouse = (float) pMouseY;
        super.render(pPoseStack,pMouseX,pMouseY,pPartialTick);
        renderTooltip(pPoseStack,pMouseX,pMouseY);
    }
}
