package dev.simukraft.entities.folk;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Dynamic;
import dev.simukraft.entities.folk.ai.FolkGoalPackages;
import dev.simukraft.entities.folk.ai.FolkSchedule;
import dev.simukraft.init.ModEntities;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.schedule.Schedule;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiPredicate;

public class EntityFolk extends AgeableMob {

    public static final Map<MemoryModuleType<GlobalPos>, BiPredicate<EntityFolk, Holder<PoiType>>> POI_MEMORIES = ImmutableMap.of(MemoryModuleType.HOME, (p_219625_, p_219626_) -> {
        return p_219626_.is(PoiTypes.HOME);
    }, MemoryModuleType.JOB_SITE, (p_219622_, p_219623_) -> {
        // TODO REMOVED FOR TESTING return p_219622_.getVillagerData().getProfession().heldJobSite().test(p_219623_);
        return true;
    }, MemoryModuleType.POTENTIAL_JOB_SITE, (p_219619_, p_219620_) -> {
        return VillagerProfession.ALL_ACQUIRABLE_JOBS.test(p_219620_);
    }, MemoryModuleType.MEETING_POINT, (p_219616_, p_219617_) -> {
        return p_219617_.is(PoiTypes.MEETING);
    });

    private static final EntityDataAccessor<FolkData> FOLK_DATA = SynchedEntityData.defineId(EntityFolk.class, FolkDataSerializer.FOLK_DATA.get());
    private static final Set<Item> WANTED_ITEMS = ImmutableSet.of(Items.BREAD, Items.POTATO, Items.CARROT, Items.WHEAT, Items.WHEAT_SEEDS, Items.BEETROOT, Items.BEETROOT_SEEDS);
    private static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(
            MemoryModuleType.JOB_SITE, MemoryModuleType.HOME, MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
            MemoryModuleType.NEAREST_PLAYERS, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.WALK_TARGET, MemoryModuleType.LOOK_TARGET,
            MemoryModuleType.INTERACTION_TARGET, MemoryModuleType.PATH, MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.MEETING_POINT

    );

    // TODO: we need to add an adult/child memory and sensor, this will allow children to run from mobs etc. We also need to make a list of things that will hurt the folks, as its more then just zombies
    private static final ImmutableList<SensorType<? extends Sensor<? super EntityFolk>>> SENSOR_TYPES = ImmutableList.of(
            SensorType.NEAREST_ITEMS, SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.HURT_BY
    );

    public EntityFolk(EntityType<EntityFolk> entityEntityType, Level level) {
        super(entityEntityType, level);
        ((GroundPathNavigation) this.getNavigation()).setCanOpenDoors(true);
        this.getNavigation().setCanFloat(true);
        this.setCanPickUpLoot(true);
    }

    public static AttributeSupplier.Builder getFolkAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 40.0D).add(ForgeMod.NAMETAG_DISTANCE.get(), 20.0D);
    }

    @Override
    protected void customServerAiStep() {
        // TODO Lots to do here...
        this.getBrain().tick((ServerLevel) level, this);
        super.customServerAiStep();
    }

    @Override
    public @NotNull Brain<EntityFolk> getBrain() {
        return (Brain<EntityFolk>) super.getBrain();
    }

    @Override
    protected Brain.@NotNull Provider<EntityFolk> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    @Override
    protected @NotNull Brain<?> makeBrain(Dynamic<?> pDynamic) {
        Brain<EntityFolk> brain = this.brainProvider().makeBrain(pDynamic);
        this.registerBrainGoals(brain);
        return brain;
    }

    public void refreshBrain(ServerLevel pServerLevel) {
        Brain<EntityFolk> brain = this.getBrain();
        brain.stopAll(pServerLevel, this);
        this.brain = brain.copyWithoutBehaviors();
        this.registerBrainGoals(this.getBrain());
    }

    private void registerBrainGoals(Brain<EntityFolk> brain) {
        if (isBaby()) {
            // No work, IMPORTANT: IF YOU ADD A NEW ACTIVTY TO THE SCHEDULE YOU NEED TO ADD IT HERE
            brain.setSchedule(Schedule.EMPTY);
        } else {
            brain.setSchedule(FolkSchedule.FOLK_DEFAULT.get());
            // TODO: Change to job once complete.
            brain.addActivity(Activity.WORK, FolkGoalPackages.getIdlePackage(0.5F));
        }


        brain.addActivity(Activity.CORE, FolkGoalPackages.getCorePackage(0.5F));
        brain.addActivity(Activity.REST, FolkGoalPackages.getIdlePackage(0.5F));
        brain.addActivity(Activity.PANIC, FolkGoalPackages.getPanicPackage(0.5F));
        brain.addActivity(Activity.MEET, FolkGoalPackages.getMeetPackage(0.5F));
        brain.addActivity(Activity.IDLE, FolkGoalPackages.getIdlePackage(0.5F));
        brain.setDefaultActivity(Activity.IDLE);
        brain.setActiveActivityIfPossible(Activity.IDLE);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.updateActivityFromSchedule(this.level.getDayTime(), this.level.getGameTime());
        // TODO: below, maybe they should react to raids?
        // pVillagerBrain.addActivity(Activity.PRE_RAID, VillagerGoalPackages.getPreRaidPackage(villagerprofession, 0.5F));
        //   pVillagerBrain.addActivity(Activity.RAID, VillagerGoalPackages.getRaidPackage(villagerprofession, 0.5F));
        //  pVillagerBrain.addActivity(Activity.HIDE, VillagerGoalPackages.getHidePackage(villagerprofession, 0.5F));
    }

    // TODO: fix when aging added...
    protected void ageBoundaryReached() {
        super.ageBoundaryReached();
        if (this.level instanceof ServerLevel) {
            this.refreshBrain((ServerLevel) this.level);
        }

    }

    // TODO: Need to change
    public void releasePoi(MemoryModuleType<GlobalPos> pModuleType) {
        if (this.level instanceof ServerLevel) {
            MinecraftServer minecraftserver = ((ServerLevel) this.level).getServer();
            this.brain.getMemory(pModuleType).ifPresent((p_186306_) -> {
                ServerLevel serverlevel = minecraftserver.getLevel(p_186306_.dimension());
                if (serverlevel != null) {
                    PoiManager poimanager = serverlevel.getPoiManager();
                    Optional<Holder<PoiType>> optional = poimanager.getType(p_186306_.pos());
                    BiPredicate<EntityFolk, Holder<PoiType>> bipredicate = POI_MEMORIES.get(pModuleType);
                    if (optional.isPresent() && bipredicate.test(this, optional.get())) {
                        poimanager.release(p_186306_.pos());
                        DebugPackets.sendPoiTicketCountPacket(serverlevel, p_186306_.pos());
                    }

                }
            });
        }
    }

    @Override
    public void die(DamageSource pDamageSource) {
        // TODO
        super.die(pDamageSource);
    }

    @Override
    public void onItemPickup(@NotNull ItemEntity itemEntity) {
        // TODO
        super.onItemPickup(itemEntity);
        setItemInHand(InteractionHand.MAIN_HAND, itemEntity.getItem());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.entityData.set(FOLK_DATA, new FolkData(pCompound));
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        // TODO: Group ID needs to be set by the spawn
        this.setFolkData(FolkData.generateNewFolk(UUID.randomUUID()));
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        this.getFolkData().writeToCompound(pCompound);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(FOLK_DATA, new FolkData());
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return ModEntities.ENTITY_FOLK.get().create(pLevel);
    }

    @Override
    public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
    }

    public FolkData getFolkData() {
        return this.entityData.get(FOLK_DATA);
    }

    public void setFolkData(FolkData data) {
        this.entityData.set(FOLK_DATA, data);
    }
}

