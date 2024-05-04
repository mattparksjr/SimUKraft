package dev.simukraft.entities.folk.ai.behavior;

import com.google.common.collect.ImmutableMap;
import dev.simukraft.entities.folk.EntityFolk;
import dev.simukraft.init.ModEntities;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class FolkSocialize extends Behavior<EntityFolk> {
    private static final float SPEED_MODIFIER = 0.3F;

    public FolkSocialize() {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.MEETING_POINT, MemoryStatus.VALUE_PRESENT, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT, MemoryModuleType.INTERACTION_TARGET, MemoryStatus.VALUE_ABSENT));
    }

    protected boolean checkExtraStartConditions(ServerLevel pLevel, EntityFolk pOwner) {
        Brain<?> brain = pOwner.getBrain();
        Optional<GlobalPos> optional = brain.getMemory(MemoryModuleType.MEETING_POINT);
        return pLevel.getRandom().nextInt(100) == 0 &&
                optional.isPresent() && pLevel.dimension() == optional.get().dimension() &&
                optional.get().pos().closerToCenterThan(pOwner.position(), 4.0D) &&
                brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).get().contains((otherFolk) -> ModEntities.ENTITY_FOLK.get().equals(otherFolk.getType()));
    }

    protected void start(@NotNull ServerLevel pLevel, EntityFolk pEntity, long pGameTime) {
        Brain<?> brain = pEntity.getBrain();
        brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).flatMap((p_186067_) ->
                p_186067_.findClosest((closeEntity) ->
                        EntityType.VILLAGER.equals(closeEntity.getType()) && closeEntity.distanceToSqr(pEntity) <= 32.0D)).ifPresent((entity) -> {
            brain.setMemory(MemoryModuleType.INTERACTION_TARGET, entity);
            brain.setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(entity, true));
            brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new EntityTracker(entity, false), 0.3F, 1));
        });
    }
}
