package dev.simukraft.entities.folk.ai.behavior;

import com.google.common.collect.ImmutableMap;
import dev.simukraft.entities.folk.EntityFolk;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.schedule.Activity;
import org.jetbrains.annotations.NotNull;

public class FolkPanicTrigger extends Behavior<EntityFolk> {

    public FolkPanicTrigger() {
        super(ImmutableMap.of());
    }

    @Override
    protected boolean canStillUse(@NotNull ServerLevel pLevel, @NotNull EntityFolk pEntity, long pGameTime) {
        return isHurt(pEntity) || hasHostile(pEntity);
    }

    @Override
    protected void start(@NotNull ServerLevel pLevel, @NotNull EntityFolk pEntity, long pGameTime) {
        if (isHurt(pEntity) || hasHostile(pEntity)) {
            Brain<?> brain = pEntity.getBrain();
            if (!brain.isActive(Activity.PANIC)) {
                brain.eraseMemory(MemoryModuleType.PATH);
                brain.eraseMemory(MemoryModuleType.WALK_TARGET);
                brain.eraseMemory(MemoryModuleType.LOOK_TARGET);
                brain.eraseMemory(MemoryModuleType.BREED_TARGET);
                brain.eraseMemory(MemoryModuleType.INTERACTION_TARGET);
            }
            brain.setActiveActivityIfPossible(Activity.PANIC);
        }

    }

    @Override
    protected void tick(@NotNull ServerLevel pLevel, @NotNull EntityFolk pOwner, long pGameTime) {
        if (pGameTime % 100L == 0L) {
            // TODO: We have the chance to try to let a guard know a folk is in danger here
            // pOwner.spawnGolemIfNeeded(pLevel, pGameTime, 3);
        }
    }

    public static boolean hasHostile(LivingEntity pEntity) {
        return pEntity.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_HOSTILE);
    }

    public static boolean isHurt(LivingEntity pEntity) {
        return pEntity.getBrain().hasMemoryValue(MemoryModuleType.HURT_BY);
    }
}
