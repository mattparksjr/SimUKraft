package dev.simukraft.client.menu;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.simukraft.client.menu.constructor.ConstructorScreenBase;
import dev.simukraft.client.menu.employ.HireWorkerScreenType;
import dev.simukraft.data.PlayerDataProvider;
import dev.simukraft.entities.block.ConstructorTileEntity;
import dev.simukraft.net.ModPackets;
import dev.simukraft.net.packet.ui.FireScreenC2SPacket;
import dev.simukraft.net.packet.ui.HireScreenC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class ConstructorScreen extends ConstructorScreenBase {

    private Button HIRE_BUILDER;
    private Button FIRE_WORKER;
    private Button CHOOSE_BUILDING;

    public ConstructorScreen(BlockEntity entity) {
        super((ConstructorTileEntity) entity);
    }

    @Override
    protected void init() {
        Minecraft.getInstance().player.getCapability(PlayerDataProvider.PLAYER_DATA).ifPresent(data -> {
            HIRE_BUILDER = new Button((this.width / 2) - 60, 150, BUTTON_WIDTH, BUTTON_HEIGHT, Component.literal("Hire Builder"), pButton -> {
                ModPackets.sendToServer(new HireScreenC2SPacket(getEntity().getBlockPos(), data.getGroupID(), HireWorkerScreenType.BUILDER));
            });
        });

        FIRE_WORKER = new Button((this.width / 2) + 60, 150, BUTTON_WIDTH, BUTTON_HEIGHT, Component.literal("Fire worker"), pButton -> {
            ModPackets.sendToServer(new FireScreenC2SPacket(getEntity().getBlockPos()));
        });

        CHOOSE_BUILDING = new Button((this.width / 2) - 180, 150, BUTTON_WIDTH, BUTTON_HEIGHT, Component.literal("Choose Building"), pButton -> {

        });


        addRenderableWidget(HIRE_BUILDER);
        addRenderableWidget(FIRE_WORKER);
        addRenderableWidget(CHOOSE_BUILDING);
    }

    @Override
    public void render(@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);

        drawCenteredString(pPoseStack, Component.translatable("simukraft.gui.constructor.choose_task"), width / 2, 100, 0xffffaa);

        if (getEntity().getWorkers().isEmpty()) {
            FIRE_WORKER.active = false;
        } else {
            HIRE_BUILDER.active = false;
        }
    }

}
