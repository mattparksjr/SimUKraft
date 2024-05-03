package dev.simukraft.data;

import net.minecraft.nbt.CompoundTag;

import java.util.UUID;

public class PlayerData {

    private UUID groupID;
    private boolean inGroup;

    public void copyFrom(PlayerData data) {
        this.groupID = data.getGroupID();
    }

    public void saveNBTData(CompoundTag tag) {
        if(getGroupID() != null) {
            tag.putBoolean("ingroup", true);
            tag.putUUID("groupid", getGroupID());
        } else {
            tag.putInt("ingroup", 0);
        }
    }

    public void loadNBTData(CompoundTag tag) {
        if(tag.getBoolean("ingroup")) {
            setInGroup(true);
            setGroupID(tag.getUUID("groupid"));
        } else {
            setInGroup(false);
        }
    }

    public UUID getGroupID() {
        return groupID;
    }

    public void setGroupID(UUID groupID) {
        this.groupID = groupID;
    }

    public void setInGroup(boolean inGroup) {
        this.inGroup = inGroup;
    }

    public boolean isInGroup() {
        return inGroup;
    }
}
