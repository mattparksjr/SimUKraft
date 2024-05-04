package dev.simukraft.entities.folk.ai.behavior;

import com.google.common.collect.ImmutableMap;
import dev.simukraft.entities.folk.EntityFolk;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

public class FolkCalmDown extends Behavior<EntityFolk> {

    // TODO: see below, funny that vanilla has this, but never uses it
    private static final int SAFE_DISTANCE_FROM_DANGER = 36;

    public FolkCalmDown() {
        super(ImmutableMap.of());
    }

    @Override
    protected void start(ServerLevel level, EntityFolk folk, long gameTime) {
        boolean flag = FolkPanicTrigger.isHurt(folk) || FolkPanicTrigger.hasHostile(folk) || isCloseToEntityThatHurtMe(folk);
        if (!flag) {
            folk.getBrain().eraseMemory(MemoryModuleType.HURT_BY);
            folk.getBrain().eraseMemory(MemoryModuleType.HURT_BY_ENTITY);
            folk.getBrain().updateActivityFromSchedule(level.getDayTime(), level.getGameTime());
        }

    }

    private static boolean isCloseToEntityThatHurtMe(EntityFolk folk) {
        // TODO: Add the dist to config or something, not just a random value here....
        return folk.getBrain().getMemory(MemoryModuleType.HURT_BY_ENTITY).filter((p_24581_) -> p_24581_.distanceToSqr(folk) <= 36.0D).isPresent();
    }
}
