package dev.simukraft.data;

import net.minecraft.nbt.CompoundTag;

import java.util.UUID;

public class PlayerData {

    private UUID groupID;

    public UUID getGroupID() {
        return groupID;
    }

    public void setGroupID(UUID groupID) {
        this.groupID = groupID;
    }

    public void copyFrom(PlayerData data) {
        this.groupID = data.getGroupID();
    }

    public void saveNBTData(CompoundTag tag) {
        if(getGroupID() != null) {
            tag.putInt("ingroup", 1);
            tag.putUUID("groupid", getGroupID());
        } else {
            tag.putInt("ingroup", 0);
        }
    }

    public void loadNBTData(CompoundTag tag) {
        if(tag.getInt("ingroup") == 1) {
            setGroupID(tag.getUUID("groupid"));
        }
    }
}
