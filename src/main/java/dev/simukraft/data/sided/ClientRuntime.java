package dev.simukraft.data.sided;

import dev.simukraft.SimUKraft;
import dev.simukraft.client.menu.ConstructorScreen;
import dev.simukraft.client.menu.employ.FireWorkerScreen;
import dev.simukraft.client.menu.employ.HireWorkerScreen;
import dev.simukraft.client.menu.employ.HireWorkerScreenType;
import dev.simukraft.entities.block.SimTileEntity;
import dev.simukraft.entities.folk.FolkData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClientRuntime {

    private static String name;
    private static double money;
    private static int sims;
    private static boolean hasInfo = false;
    private static boolean hasRequestedInfo = false;
    private static long lastRequestAt = 0L;
    private static boolean allowRequest = true;
    private static Map<UUID, FolkData> folkData = new HashMap<>();
    private static UUID groupID;


    public static void openConstructorScreen(BlockEntity entity) {
        openScreen(new ConstructorScreen(entity));
    }

    public static void openFireScreen(BlockPos pos) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            SimUKraft.LOGGER.error("Tried to open fire screen for block at {} but client level was null!", pos.toShortString());
            return;
        }

        SimTileEntity entity = (SimTileEntity) level.getBlockEntity(pos);
        if (entity != null) {
            openScreen(new FireWorkerScreen(Component.translatable("simukraft.gui.general.fire"), null, entity));
        } else {
            SimUKraft.LOGGER.error("Failed to find block entity at {} while trying to open fire screen", pos.toShortString());
        }
    }

    public static void openHireScreen(BlockPos pos, HireWorkerScreenType type) {
        ClientLevel level = Minecraft.getInstance().level;

        if (level == null) {
            SimUKraft.LOGGER.error("Tried to open hire screen for block at {} but client level was null!", pos.toShortString());
            return;
        }

        SimTileEntity entity = (SimTileEntity) level.getBlockEntity(pos);
        if (entity != null) {
            openScreen(new HireWorkerScreen(Component.translatable("simukraft.gui.general.hire"), null, entity, type));
        } else {
            SimUKraft.LOGGER.error("Failed to find block entity at {} while trying to open hire screen", pos.toShortString());
        }
    }

    public static void openScreen(Screen screen) {
        Minecraft.getInstance().setScreen(null);
        Minecraft.getInstance().setScreen(screen);
    }

    public static Map<UUID, FolkData> getFolkDataMap() {
        return folkData;
    }

    public static void addDataToMap(UUID id, FolkData data) {
        folkData.put(id, data);
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        ClientRuntime.name = name;
    }

    public static double getMoney() {
        return money;
    }

    public static void setMoney(double money) {
        ClientRuntime.money = money;
    }

    public static int getSims() {
        return sims;
    }

    public static void setSims(int sims) {
        ClientRuntime.sims = sims;
    }

    public static boolean hasInfo() {
        return hasInfo;
    }

    public static void setHasInfo(boolean hasInfo) {
        ClientRuntime.hasInfo = hasInfo;
    }

    public static void setHasRequestedInfo(boolean hasRequstedInfo) {
        ClientRuntime.hasRequestedInfo = hasRequstedInfo;
    }

    public static boolean hasRequestedInfo() {
        return hasRequestedInfo;
    }

    public static long getLastRequestAt() {
        return lastRequestAt;
    }

    public static void setLastRequestAt(long lastRequestAt) {
        ClientRuntime.lastRequestAt = lastRequestAt;
    }

    public static boolean isAllowRequest() {
        return allowRequest;
    }

    public static void setAllowRequest(boolean allowRequest) {
        ClientRuntime.allowRequest = allowRequest;
    }

    public static UUID getGroupID() {
        return groupID;
    }

    public static void setGroupID(UUID groupID) {
        ClientRuntime.groupID = groupID;
    }
}
