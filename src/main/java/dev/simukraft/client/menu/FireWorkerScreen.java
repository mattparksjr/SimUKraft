package dev.simukraft.client.menu;

import dev.simukraft.client.menu.base.ScreenBase;
import dev.simukraft.data.sided.ClientRuntime;
import dev.simukraft.entities.block.SimTileEntity;
import dev.simukraft.net.ModPackets;
import dev.simukraft.net.packet.block.SetTileDataC2SPacket;
import dev.simukraft.net.packet.folk.RequestFolkDataC2SPacket;
import dev.simukraft.net.packet.folk.UpdateFolkJobC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FireWorkerScreen extends ScreenBase {

    private Map<UUID, String> selectedWorkers = new HashMap<>();
    private SimTileEntity entity;
    private final Screen returnTo;

    public FireWorkerScreen(Component component, ScreenBase returnTo, SimTileEntity entity) {
        super(component);
        this.returnTo = returnTo;
        this.entity = entity;
    }

    @Override
    protected void init() {
        super.init();

        addRenderableWidget(new Button((width / 2) - 200, height - 30, BUTTON_WIDTH, BUTTON_HEIGHT, Component.translatable("simukraft.gui.general.cancel"), pButton -> {
            Minecraft.getInstance().setScreen(null);
        }));

        addRenderableWidget(new Button((width / 2), height - 30, BUTTON_WIDTH, BUTTON_HEIGHT, Component.translatable("simukraft.gui.general.ok"), pButton -> {

            for(UUID id : selectedWorkers.keySet()) {
                ModPackets.sendToServer(new UpdateFolkJobC2SPacket(entity.getGroupID(), id, new BlockPos(-999, -999, -999)));
            }

            ModPackets.sendToServer(new SetTileDataC2SPacket(entity.getBlockPos(), new ArrayList<>(selectedWorkers.keySet()), Minecraft.getInstance().player.getUUID()));
            ClientRuntime.openScreen(returnTo);
        }));


        int x = 10, y = 65, i = 0;

        for(UUID key : entity.getWorkers()) {
            System.out.println(key);
            addRenderableWidget(new Button(x, y, BUTTON_WIDTH, BUTTON_HEIGHT, Component.translatable("simukraft.gui.general.fire").append(" ").append(ClientRuntime.getFolkDataMap().get(key).getFullname()), pButton -> {
                selectedWorkers.put(key, " ");
                pButton.active = false;
            }));
            i++;
        }

    }
}
