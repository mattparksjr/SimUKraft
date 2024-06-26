package dev.simukraft.data;

import dev.simukraft.SimUKraft;
import dev.simukraft.data.group.SimGroup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.UUID;

public class SimSavedData extends SavedData {

    // Global data saved on the overworld
    private final ArrayList<SimGroup> groups;

    // used for first load
    private boolean requiresSetup;

    public SimSavedData() {
        groups = new ArrayList<>();
        requiresSetup = true;
    }

    public static SimSavedData create() {
        return new SimSavedData();
    }

    public static SimSavedData load(CompoundTag tag) {
        SimSavedData data = create();
        data.setRequiresSetup(false);

        SimUKraft.LOGGER.debug("Sim Saved Data - Got request to load groups.. loading now...");

        for (int i = 1; i <= tag.getInt("groups"); i++) {
            SimUKraft.LOGGER.debug("Sim Saved Data - Loading a group:  " + "group." + i);
            SimGroup group = SimGroup.load(tag.getCompound("group." + i));
            data.groups.add(group);
        }

        return data;
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag tag) {
        tag.putInt("groups", groups.size());

        int id = 1;
        for (SimGroup group : groups) {
            tag.put("group." + id, group.save());
            id++;
        }

        return tag;
    }

    public boolean requiresSetup() {
        return requiresSetup;
    }

    public void setRequiresSetup(boolean requiresSetup) {
        this.requiresSetup = requiresSetup;
    }

    public ArrayList<SimGroup> getGroups() {
        return groups;
    }

    public void addGroup(SimGroup group) {
        SimUKraft.LOGGER.debug("SimSavedData - Added a group with {} players", group.getPlayerIDS());
        groups.add(group);
        setDirty();
    }

    public SimGroup getGroupByID(UUID id) {
        SimGroup group = null;
        for (SimGroup data : groups) {
            if (data.getUuid().equals(id)) {
                group = data;
            }
        }
        return group;
    }
}
