package dev.simukraft.entities.folk.ai;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import dev.simukraft.entities.folk.EntityFolk;
import dev.simukraft.entities.folk.ai.behavior.FolkCalmDown;
import dev.simukraft.entities.folk.ai.behavior.FolkPanicTrigger;
import dev.simukraft.entities.folk.ai.behavior.FolkSetWalkTargetFromBlockMemory;
import dev.simukraft.entities.folk.ai.behavior.FolkSocialize;
import dev.simukraft.init.ModEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.npc.VillagerProfession;

import java.util.Optional;

/**
 * Goal packages are like the way to define what a activity really does.
 * The brain will try to set an activity, then go to these "goal packages"
 * and try to execute the tasks.
 */
public class FolkGoalPackages {


    // TODO: They do need their job lol this is FOR SURE going to take ALOT of work...
    // public static ImmutableList<Pair<Integer, ? extends Behavior<? super EntityFolk>>> getWorkPackage(VillagerProfession pProfession, float pSpeedModifier) {
    //     WorkAtPoi workatpoi;
    //    if (pProfession == VillagerProfession.FARMER) {
    //       workatpoi = new WorkAtComposter();
    //   } else {
    //       workatpoi = new WorkAtPoi();
    //   }
//
    //    return ImmutableList.of(getMinimalLookBehavior(), Pair.of(5, new RunOne<>(ImmutableList.of(Pair.of(workatpoi, 7), Pair.of(new StrollAroundPoi(MemoryModuleType.JOB_SITE, 0.4F, 4), 2), Pair.of(new StrollToPoi(MemoryModuleType.JOB_SITE, 0.4F, 1, 10), 5), Pair.of(new StrollToPoiList(MemoryModuleType.SECONDARY_JOB_SITE, pSpeedModifier, 1, 6, MemoryModuleType.JOB_SITE), 5), Pair.of(new HarvestFarmland(), pProfession == VillagerProfession.FARMER ? 2 : 5), Pair.of(new UseBonemeal(), pProfession == VillagerProfession.FARMER ? 4 : 7)))), Pair.of(10, new ShowTradesToPlayer(400, 1600)), Pair.of(10, new SetLookAndInteract(EntityType.PLAYER, 4)), Pair.of(2, new SetWalkTargetFromBlockMemory(MemoryModuleType.JOB_SITE, pSpeedModifier, 9, 100, 1200)), Pair.of(3, new GiveGiftToHero(100)), Pair.of(99, new UpdateActivityFromSchedule()));
    //}


    // TODO: Add their job here lol
    public static ImmutableList<Pair<Integer, ? extends Behavior<? super EntityFolk>>> getCorePackage(float pSpeedModifier) {
        return ImmutableList.of(
                Pair.of(0, new Swim(0.8F)),
                Pair.of(0, new InteractWithDoor()),
                Pair.of(0, new LookAtTargetSink(45, 90)),
                Pair.of(0, new FolkPanicTrigger()),
                Pair.of(0, new WakeUp()),
                Pair.of(0, new ReactToBell()),
                Pair.of(0, new SetRaidStatus()),
                // TODO: How tf do wanted items works?
                Pair.of(5, new GoToWantedItem(pSpeedModifier, false, 4)),
                Pair.of(10, new AcquirePoi((type) -> type.is(PoiTypes.HOME), MemoryModuleType.HOME, false, Optional.of((byte) 14))),
                Pair.of(10, new AcquirePoi((type) -> type.is(PoiTypes.MEETING), MemoryModuleType.MEETING_POINT, true, Optional.of((byte) 14))));
    }

    public static ImmutableList<Pair<Integer, ? extends Behavior<? super EntityFolk>>> getIdlePackage(float pSpeedModifier) {
        return ImmutableList.of(
                Pair.of(2, new RunOne<>(ImmutableList.of(
                        Pair.of(InteractWith.of(ModEntities.ENTITY_FOLK.get(), 8, MemoryModuleType.INTERACTION_TARGET, pSpeedModifier, 2), 2),
                        Pair.of(InteractWith.of(EntityType.CAT, 8, MemoryModuleType.INTERACTION_TARGET, pSpeedModifier, 2), 1),
                        // TODO: VillageBoundRandomStroll will need to be rewritten
                        Pair.of(new VillageBoundRandomStroll(pSpeedModifier), 1),
                        Pair.of(new SetWalkTargetFromLookTarget(pSpeedModifier, 2), 1),
                        Pair.of(new JumpOnBed(pSpeedModifier), 1),
                        Pair.of(new DoNothing(30, 60), 1)
                ))),
                Pair.of(3, new SetLookAndInteract(EntityType.PLAYER, 4)),
                getFullLookBehavior(),
                Pair.of(99, new UpdateActivityFromSchedule())
        );
    }

    public static ImmutableList<Pair<Integer, ? extends Behavior<? super EntityFolk>>> getPanicPackage(float pSpeedModifier) {
        float f = pSpeedModifier * 1.5F;
        return ImmutableList.of(
                Pair.of(0, new FolkCalmDown()),
                Pair.of(1, SetWalkTargetAwayFrom.entity(MemoryModuleType.NEAREST_HOSTILE, f, 6, false)),
                Pair.of(1, SetWalkTargetAwayFrom.entity(MemoryModuleType.HURT_BY_ENTITY, f, 6, false)),
                // TODO: VillageBoundRandomStroll will need to be rewritten
                Pair.of(3, new VillageBoundRandomStroll(f, 2, 2)),
                getMinimalLookBehavior()
        );
    }

    public static ImmutableList<Pair<Integer, ? extends Behavior<? super EntityFolk>>> getMeetPackage(float pSpeedModifier) {
        return ImmutableList.of(
                Pair.of(2, new RunOne<>(ImmutableList.of(
                        Pair.of(new StrollAroundPoi(MemoryModuleType.MEETING_POINT, 0.4F, 40), 2),
                        Pair.of(new FolkSocialize(), 2)))),
                Pair.of(10, new SetLookAndInteract(EntityType.PLAYER, 4)),
                Pair.of(2, new FolkSetWalkTargetFromBlockMemory(MemoryModuleType.MEETING_POINT, pSpeedModifier, 6, 100, 200)),
                Pair.of(3, new ValidateNearbyPoi((poi) -> poi.is(PoiTypes.MEETING), MemoryModuleType.MEETING_POINT)),
                getFullLookBehavior(),
                Pair.of(99, new UpdateActivityFromSchedule()));
    }


    private static Pair<Integer, Behavior<LivingEntity>> getFullLookBehavior() {
        return Pair.of(5, new RunOne<>(ImmutableList.of(
                Pair.of(new SetEntityLookTarget(EntityType.CAT, 8.0F), 8),
                Pair.of(new SetEntityLookTarget(EntityType.VILLAGER, 8.0F), 2),
                Pair.of(new SetEntityLookTarget(EntityType.PLAYER, 8.0F), 2),
                Pair.of(new SetEntityLookTarget(MobCategory.CREATURE, 8.0F), 1),
                Pair.of(new SetEntityLookTarget(MobCategory.WATER_CREATURE, 8.0F), 1),
                Pair.of(new SetEntityLookTarget(MobCategory.AXOLOTLS, 8.0F), 1),
                Pair.of(new SetEntityLookTarget(MobCategory.UNDERGROUND_WATER_CREATURE, 8.0F), 1),
                Pair.of(new SetEntityLookTarget(MobCategory.WATER_AMBIENT, 8.0F), 1),
                Pair.of(new SetEntityLookTarget(MobCategory.MONSTER, 8.0F), 1),
                Pair.of(new SetEntityLookTarget(ModEntities.ENTITY_FOLK.get(), 8.0F), 1),
                Pair.of(new DoNothing(30, 60), 2))));
    }

    private static Pair<Integer, Behavior<LivingEntity>> getMinimalLookBehavior() {
        return Pair.of(5, new RunOne<>(ImmutableList.of(
                Pair.of(new SetEntityLookTarget(ModEntities.ENTITY_FOLK.get(), 8.0F), 2),
                Pair.of(new SetEntityLookTarget(EntityType.PLAYER, 8.0F), 2),
                Pair.of(new DoNothing(30, 60), 8))));
    }
}
