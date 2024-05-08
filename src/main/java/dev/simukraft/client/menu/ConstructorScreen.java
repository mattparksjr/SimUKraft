package dev.simukraft.client.menu;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.simukraft.client.menu.constructor.ConstructorScreenBase;
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

    public ConstructorScreen(BlockEntity entity) {
        super((ConstructorTileEntity) entity);
    }

    @Override
    protected void init() {
        Minecraft.getInstance().player.getCapability(PlayerDataProvider.PLAYER_DATA).ifPresent(data -> {
            HIRE_BUILDER = new Button((this.width / 2) - 60, 150, BUTTON_WIDTH, BUTTON_HEIGHT, Component.literal("Hire Builder"), pButton -> {
                ModPackets.sendToServer(new HireScreenC2SPacket(getEntity().getBlockPos(), data.getGroupID()));
            });
        });

        FIRE_WORKER = new Button((this.width / 2) + 60, 150, BUTTON_WIDTH, BUTTON_HEIGHT, Component.literal("Fire worker"), pButton -> {
            ModPackets.sendToServer(new FireScreenC2SPacket(getEntity().getBlockPos()));
        });


        addRenderableWidget(HIRE_BUILDER);
        addRenderableWidget(FIRE_WORKER);
        addRenderableWidget(new Button((this.width / 2) - 180, 150, BUTTON_WIDTH, BUTTON_HEIGHT, Component.literal("Choose Building"), pButton -> {

        }));

    }

    @Override
    public void render(@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        drawCenteredString(pPoseStack, this.font, "Please choose a task for this building constructor", width / 2, 100, 0xffffaa);

        if (getEntity().getWorkers().isEmpty()) {
            FIRE_WORKER.active = false;
        }
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }

}
