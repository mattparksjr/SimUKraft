package dev.simukraft.data;

import dev.simukraft.data.group.SimGroup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SimSavedData extends SavedData {

    private ArrayList<SimGroup> groups;

    public SimSavedData() {
        groups = new ArrayList<>();

        // DEBUG
        SimGroup test = new SimGroup();
        test.setMoney(1203123);
        test.setUuid(UUID.randomUUID());
        test.setOwner(UUID.randomUUID());
        groups.add(test);
    }


    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putInt("groups", groups.size());

        int id = 1;
        for(SimGroup group : groups) {
            tag.put("group." + id, group.save());
            id++;
        }

        return tag;
    }

    public static SimSavedData create() {
        return new SimSavedData();
    }

    public static SimSavedData load(CompoundTag tag) {
        SimSavedData data = create();
        return data;
    }

}
