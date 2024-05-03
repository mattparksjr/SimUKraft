package dev.simukraft.data.pack;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class StructureBlock {

    private BlockPos pos;
    private BlockState state;

    public StructureBlock(BlockPos blockPos, BlockState state) {
        this.pos = blockPos;
        this.state = state;
    }

    public BlockPos getPos() {
        return pos;
    }

    public void setPos(BlockPos pos) {
        this.pos = pos;
    }

    public BlockState getState() {
        return state;
    }

    public void setState(BlockState state) {
        this.state = state;
    }
}
