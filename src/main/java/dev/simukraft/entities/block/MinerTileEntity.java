package dev.simukraft.entities.block;

import dev.simukraft.init.ModTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MinerTileEntity extends BlockEntity {

    public MinerTileEntity(BlockPos pos, BlockState state) {
        super(ModTileEntities.MINER.get(), pos, state);
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T be) {
        MinerTileEntity tile = (MinerTileEntity) be;
        // 20 times per second.
    }
}
