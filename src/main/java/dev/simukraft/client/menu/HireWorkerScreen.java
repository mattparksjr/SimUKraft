package dev.simukraft.client.menu;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.simukraft.SimUKraft;
import dev.simukraft.client.menu.base.ScreenBase;
import dev.simukraft.data.sided.ClientRuntime;
import dev.simukraft.entities.block.SimTileEntity;
import dev.simukraft.net.ModPackets;
import dev.simukraft.net.packet.block.SetTileDataC2SPacket;
import dev.simukraft.net.packet.folk.UpdateFolkJobC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.UUID;

public class HireWorkerScreen extends ScreenBase {

    private ArrayList<UUID> selectedWorkers;
    private SimTileEntity entity;
    private final Screen returnTo;

    public HireWorkerScreen(Component component, ScreenBase returnTo, SimTileEntity entity) {
        super(component);
        this.returnTo = returnTo;
        this.entity = entity;
        selectedWorkers = new ArrayList<>();
    }

    @Override
    protected void init() {
        super.init();

        addRenderableWidget(new Button((width / 2) - 200, height - 30, (width / 2), BUTTON_HEIGHT, Component.translatable("simukraft.gui.general.cancel"), pButton -> {
            Minecraft.getInstance().setScreen(null);
        }));

        addRenderableWidget(new Button((width / 2), height - 30, (width / 2), BUTTON_HEIGHT, Component.translatable("simukraft.gui.general.ok"), pButton -> {

            if (ClientRuntime.getMoney() <= 0.0D) {
                Minecraft.getInstance().player.sendSystemMessage(Component.translatable("simukraft.message.need_money_hire"));
                Minecraft.getInstance().setScreen(null);
                return;
            }

            System.out.println(entity.getGroupID());
            for (UUID id : selectedWorkers) {
                ModPackets.sendToServer(new UpdateFolkJobC2SPacket(ClientRuntime.getGroupID(), id, entity.getBlockPos()));
            }

            SimUKraft.LOGGER.debug("HireWorkerScreen - about to send data");
            ModPackets.sendToServer(new SetTileDataC2SPacket(entity.getBlockPos(), selectedWorkers, Minecraft.getInstance().player.getUUID()));

            ClientRuntime.openScreen(returnTo);
        }));

        int x = 10, y = 40;

        for (UUID folkUUID : ClientRuntime.getFolkDataMap().keySet()) {
            String xp = "3";

            addRenderableWidget(new Button(x, y, BUTTON_WIDTH, BUTTON_HEIGHT, Component.literal(ClientRuntime.getFolkDataMap().get(folkUUID).getFullname() + " (" + xp + ")"), pButton -> {
                selectedWorkers.add(folkUUID);
                pButton.active = false;
            }));

            x += BUTTON_WIDTH;

            if ((x + BUTTON_WIDTH) > width) {
                x = 10;
                y += 20;
            }

            // TODO: lets just make a scroll list instead of this...
            if ((y + 20) > (height - 50)) {
                break;
            }
        }
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }

}
