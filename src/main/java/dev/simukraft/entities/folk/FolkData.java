package dev.simukraft.entities.folk;

import dev.simukraft.data.pack.NameReloadListener;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Random;
import java.util.UUID;

public class FolkData {

    private String firstName;
    private String lastName;
    private int age;
    private UUID groupID;
    private int gender;
    private int skinID;

    private BlockPos jobSite;

    private boolean hasJob;

    public FolkData(CompoundTag tag) {
        this.firstName = tag.getString("firstName");
        this.lastName = tag.getString("lastName");
        this.age = tag.getInt("age");
        // Note, for UUID they are never written as null, you must test when pulling back from source.
        if (tag.contains("groupId")) {
            this.groupID = tag.getUUID("groupId");
        }
        this.gender = tag.getInt("gender");
        this.skinID = tag.getInt("skinId");

        if (tag.contains("jobSite")) {
            hasJob = true;
            int[] coords = tag.getIntArray("jobSite");
            this.jobSite = new BlockPos(coords[0], coords[1], coords[2]);
        }
    }

    public FolkData(String firstName, String lastName, int age, UUID groupID, int gender, int skinID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.groupID = groupID;
        this.gender = gender;
        this.skinID = skinID;
    }

    public FolkData() {
    }

    public static FolkData generateNewFolk(UUID groupID) {
        Random ran = new Random();

        int gender = ran.nextInt(2);
        int skinId = 1; // TODO: TEMP, use rand when skins updated
        String name;

        if (gender == 1) {
            name = NameReloadListener.getRandomFemaleName();
        } else {
            name = NameReloadListener.getRandomMaleName();
        }

        return new FolkData(name, NameReloadListener.getRandomLastName(), 18, groupID, gender, skinId);
    }

    public static FolkData copyFrom(FolkData pValue) {
        return new FolkData(pValue.getFirstName(), pValue.getLastName(), pValue.getAge(), pValue.getGroupID(), pValue.getGender(), pValue.getSkinID());
    }

    public void writeToCompound(CompoundTag pCompound) {
        pCompound.putString("firstName", getFirstName());
        pCompound.putString("lastName", getLastName());
        pCompound.putInt("age", getAge());
        pCompound.putUUID("groupId", getGroupID());
        pCompound.putInt("gender", getGender());
        pCompound.putInt("skinId", getSkinID());

        if (jobSite != null) {
            int[] coords = new int[3];
            coords[0] = jobSite.getX();
            coords[1] = jobSite.getY();
            coords[2] = jobSite.getZ();
            pCompound.putIntArray("jobSite", coords);
        }
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(getFirstName());
        buf.writeUtf(getLastName());
        buf.writeInt(getAge());
        buf.writeUUID(getGroupID());
        buf.writeInt(getGender());
        buf.writeInt(getSkinID());
        buf.writeBoolean(hasJob);
        if(jobSite != null) {
            buf.writeBlockPos(jobSite);
        }
    }

    public static FolkData read(FriendlyByteBuf buf) {
        FolkData data = new FolkData(buf.readUtf(), buf.readUtf(), buf.readInt(), buf.readUUID(), buf.readInt(), buf.readInt());

        if(buf.readBoolean()) {
            data.setJobSite(buf.readBlockPos());
        }

        return data;
    }

    public String getFullname() {
        return this.getFirstName() + " " + getLastName();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public UUID getGroupID() {
        return groupID;
    }

    public void setGroupID(UUID groupID) {
        this.groupID = groupID;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getSkinID() {
        return skinID;
    }

    public void setSkinID(int skinID) {
        this.skinID = skinID;
    }

    public boolean hasJob() {
        return hasJob;
    }

    public void setJobSite(BlockPos jobSite) {
        this.jobSite = jobSite;
    }
}
