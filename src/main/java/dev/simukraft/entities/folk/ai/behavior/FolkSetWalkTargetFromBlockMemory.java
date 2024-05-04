package dev.simukraft.entities.folk.ai.behavior;

import com.google.common.collect.ImmutableMap;
import dev.simukraft.entities.folk.EntityFolk;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class FolkSetWalkTargetFromBlockMemory extends Behavior<EntityFolk> {
    private final MemoryModuleType<GlobalPos> memoryType;
    private final float speedModifier;
    private final int closeEnoughDist;
    private final int tooFarDistance;
    private final int tooLongUnreachableDuration;

    public FolkSetWalkTargetFromBlockMemory(MemoryModuleType<GlobalPos> memoryType, float speedModifier, int closeEnoughDist, int tooFarDistance, int tooLongUnreachableDuration) {
        super(ImmutableMap.of(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryStatus.REGISTERED, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, memoryType, MemoryStatus.VALUE_PRESENT));
        this.memoryType = memoryType;
        this.speedModifier = speedModifier;
        this.closeEnoughDist = closeEnoughDist;
        this.tooFarDistance = tooFarDistance;
        this.tooLongUnreachableDuration = tooLongUnreachableDuration;
    }

    private void dropPOI(EntityFolk folk, long pTime) {
        Brain<?> brain = folk.getBrain();
        folk.releasePoi(this.memoryType);
        brain.eraseMemory(this.memoryType);
        brain.setMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, pTime);
    }

    protected void start(ServerLevel level, EntityFolk folk, long pGameTime) {
        Brain<?> brain = folk.getBrain();
        brain.getMemory(this.memoryType).ifPresent((poi) -> {
            if (!this.wrongDimension(level, poi) && !this.tiredOfTryingToFindTarget(level, folk)) {
                if (this.tooFar(folk, poi)) {
                    Vec3 vec3 = null;
                    int i = 0;

                    for (int j = 1000; i < 1000 && (vec3 == null || this.tooFar(folk, GlobalPos.of(level.dimension(), new BlockPos(vec3)))); ++i) {
                        vec3 = DefaultRandomPos.getPosTowards(folk, 15, 7, Vec3.atBottomCenterOf(poi.pos()), (double) ((float) Math.PI / 2F));
                    }

                    if (i == 1000) {
                        this.dropPOI(folk, pGameTime);
                        return;
                    }

                    brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(vec3, this.speedModifier, this.closeEnoughDist));
                } else if (!this.closeEnough(level, folk, poi)) {
                    brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(poi.pos(), this.speedModifier, this.closeEnoughDist));
                }
            } else {
                this.dropPOI(folk, pGameTime);
            }
        });
    }

    private boolean tiredOfTryingToFindTarget(ServerLevel level, EntityFolk folk) {
        Optional<Long> optional = folk.getBrain().getMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
        return optional.filter(aLong -> level.getGameTime() - aLong > (long) this.tooLongUnreachableDuration).isPresent();
    }

    private boolean tooFar(EntityFolk folk, GlobalPos memory) {
        return memory.pos().distManhattan(folk.blockPosition()) > this.tooFarDistance;
    }

    private boolean wrongDimension(ServerLevel pLevel, GlobalPos pMemoryPos) {
        return pMemoryPos.dimension() != pLevel.dimension();
    }

    private boolean closeEnough(ServerLevel pLevel, EntityFolk pVillager, GlobalPos pMemoryPos) {
        return pMemoryPos.dimension() == pLevel.dimension() && pMemoryPos.pos().distManhattan(pVillager.blockPosition()) <= this.closeEnoughDist;
    }
}
