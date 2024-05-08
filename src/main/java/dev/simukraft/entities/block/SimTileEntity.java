package dev.simukraft.entities.block;

import dev.simukraft.SimUKraft;
import dev.simukraft.entities.folk.EntityFolk;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.UUID;

public class SimTileEntity extends BlockEntity {

    private ArrayList<UUID> workers = new ArrayList<>();

    private UUID groupID;

    public SimTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        CompoundTag tag = pTag.getCompound("workers");

        for (int i = 0; i < pTag.getInt("numWorkers"); i++) {
            workers.add(tag.getUUID(String.valueOf(i)));
        }
        groupID = pTag.getUUID("groupID");
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        super.saveAdditional(pTag);
        CompoundTag tag = new CompoundTag();

        for (int i = 0; i < workers.size(); i++) {
            tag.putUUID(String.valueOf(i), workers.get(i));
        }

        pTag.putInt("numWorkers", workers.size());
        pTag.put("workers", tag);
        pTag.putUUID("groupID", groupID);
    }

    public void setWorkers(ArrayList<UUID> workers) {
        SimUKraft.LOGGER.debug("SimTileEntity - Setting workers...");
        this.workers = workers;
    }

    public UUID getGroupID() {
        return groupID;
    }

    public void setGroupID(UUID groupID) {
        this.groupID = groupID;
    }

    public ArrayList<UUID> getWorkers() {
        return workers;
    }
}
