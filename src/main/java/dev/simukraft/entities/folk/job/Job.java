package dev.simukraft.entities.folk.job;

import dev.simukraft.entities.folk.EntityFolk;
import net.minecraft.nbt.CompoundTag;

public class Job {

    public String jobID;

    public void onArrive() {
    }

    public void tick(EntityFolk folk) {

    }

    public CompoundTag writeToCompound() {
        CompoundTag tag = new CompoundTag();

        tag.putString("id", getJobID());

        return tag;
    }

    public static Job fromCompound(CompoundTag tag) {

        // TODO: job will swap by id
        Job job = new Job();
        job.setJobID(tag.getString("id"));

        return job;
    }

    public String getJobID() {
        return jobID;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }
}

