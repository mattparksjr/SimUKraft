package dev.simukraft.client.menu.base;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ScreenBase extends Screen {

    public int BUTTON_WIDTH = 120;
    public int BUTTON_HEIGHT = 20;


    public ScreenBase(Component title) {
        super(title);
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }

    public void drawCenteredString(PoseStack stack, Component txt, int x, int y, int color) {
        drawCenteredString(stack, this.font, txt, x, y, color);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
