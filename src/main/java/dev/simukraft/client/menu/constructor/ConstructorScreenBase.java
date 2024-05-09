package dev.simukraft.client.menu.constructor;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.simukraft.client.menu.base.ScreenBase;
import dev.simukraft.entities.block.ConstructorTileEntity;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class ConstructorScreenBase extends ScreenBase {

    private ConstructorTileEntity entity;

    public ConstructorScreenBase(ConstructorTileEntity entity) {
        super(Component.translatable("simukraft.container.constructor"));
        this.entity = entity;
    }

    @Override
    protected void init() {
        super.init();
        addRenderableWidget(new Button(0, 2, 12, 50, Component.translatable("simukraft.gui.general.done"), button -> {

        }));
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {

        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        drawCenteredString(pPoseStack, Component.translatable("simukraft.gui.constructor.title"), width / 2, 17, 0xffffff);

        String status = Component.translatable("simukraft.gui.general.idle").append(" ").getString();
        String building = Component.translatable("simukraft.gui.constructor.not_chosen").append(" ").getString();

        if(!entity.getWorkers().isEmpty()) {
            status = " the stage";
            building = " building display name";
        }

        drawCenteredString(pPoseStack, Component.translatable("simukraft.gui.constructor.status").append(status), width / 2, 30, 0xaaffff);
        drawCenteredString(pPoseStack, Component.translatable("simukraft.gui.constructor.building").append(building), width / 2, 40, 0xaaffff);


    }

    public ConstructorTileEntity getEntity() {
        return entity;
    }
}
