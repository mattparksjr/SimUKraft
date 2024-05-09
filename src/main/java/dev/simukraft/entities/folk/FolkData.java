package dev.simukraft.entities.folk;

import dev.simukraft.data.pack.NameReloadListener;
import dev.simukraft.entities.folk.job.Job;
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
    private float xpBuilder;
    private float xpMiner;
    private float  xpFighter;
    private Job job;

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

        if(tag.contains("job")) {
            setJob(Job.fromCompound(tag.getCompound("job")));
        }

        if(tag.contains("xpBuilder")) {
            xpBuilder = tag.getFloat("xpBuilder");
        }

        if(tag.contains("xpFighter")) {
            xpFighter = tag.getFloat("xpFighter");
        }

        if(tag.contains("xpMiner")) {
            xpMiner = tag.getFloat("xpMiner");
        }

        if(tag.getBoolean("employeed")) {
            job = Job.fromCompound(tag.getCompound("job"));
        }
    }

    public FolkData(String firstName, String lastName, int age, UUID groupID, int gender, int skinID, float xpBuilder, float xpFighter, float xpMiner) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.groupID = groupID;
        this.gender = gender;
        this.skinID = skinID;
        this.xpBuilder = xpBuilder;
        this.xpFighter = xpFighter;
        this.xpMiner = xpMiner;
    }

    public FolkData() {
    }

    public static FolkData generateNewFolk(UUID groupID) {
        Random ran = new Random();

        FolkData data = new FolkData();
        int gender = ran.nextInt(2);
        int skinId = 1; // TODO: TEMP, use rand when skins updated
        String name;

        if (gender == 1) {
            name = NameReloadListener.getRandomFemaleName();
        } else {
            name = NameReloadListener.getRandomMaleName();
        }

        data.setFirstName(name);
        data.setLastName(NameReloadListener.getRandomLastName());
        data.setAge(18);
        data.setGroupID(groupID);
        data.setGender(gender);
        data.setSkinID(skinId);
        data.setXpMiner(1.0F);
        data.setXpFighter(1.0F);
        data.setXpMiner(1.0F);
        data.setHasJob(false);

        return data;
    }

    public static FolkData copyFrom(FolkData pValue) {
        FolkData data = new FolkData();
        data.setFirstName(pValue.getFirstName());
        data.setLastName(pValue.getLastName());
        data.setAge(pValue.getAge());
        data.setGender(pValue.getGender());
        data.setGroupID(pValue.getGroupID());
        data.setSkinID(pValue.getSkinID());
        data.setXpBuilder(pValue.getXpBuilder());
        data.setXpFighter(pValue.getXpFighter());
        data.setXpMiner(pValue.getXpMiner());


        if(pValue.hasJob()) {
            data.setHasJob(true);
            data.setJob(pValue.getJob());
        } else {
            data.setHasJob(false);
        }

        return data;
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

        pCompound.putFloat("xpBuilder", xpBuilder);
        pCompound.putFloat("xpFighter", xpFighter);
        pCompound.putFloat("xpMiner", xpMiner);

        if(hasJob()) {
            pCompound.put("job", getJob().writeToCompound());
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
        buf.writeFloat(xpBuilder);
        buf.writeFloat(xpFighter);
        buf.writeFloat(xpMiner);
    }

    public static FolkData read(FriendlyByteBuf buf) {
        FolkData data = new FolkData(buf.readUtf(), buf.readUtf(), buf.readInt(), buf.readUUID(), buf.readInt(), buf.readInt(), buf.readFloat(), buf.readFloat(), buf.readFloat());

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

    public void setHasJob(boolean hasJob) {
        this.hasJob = hasJob;
    }

    public void setJobSite(BlockPos jobSite) {
        this.jobSite = jobSite;
    }

    public float getXpBuilder() {
        return xpBuilder;
    }

    public float getXpFighter() {
        return xpFighter;
    }

    public float getXpMiner() {
        return xpMiner;
    }

    public void setXpBuilder(float xpBuilder) {
        this.xpBuilder = xpBuilder;
    }

    public void setXpFighter(float xpFighter) {
        this.xpFighter = xpFighter;
    }

    public void setXpMiner(float xpMiner) {
        this.xpMiner = xpMiner;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }
}
