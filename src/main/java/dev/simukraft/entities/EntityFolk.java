package dev.simukraft.entities;

import dev.simukraft.init.ModEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EntityFolk extends AgeableMob {


    private static final EntityDataAccessor<Integer> SKIN_ID = SynchedEntityData.defineId(EntityFolk.class, EntityDataSerializers.INT);

    public EntityFolk(EntityType<EntityFolk> entityEntityType, Level level) {
        super(entityEntityType, level);
        ((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(true);
        this.getNavigation().setCanFloat(true);
        this.setCanPickUpLoot(true);
    }

    @Override
    public void die(DamageSource pDamageSource) {
        // TODO
        super.die(pDamageSource);
    }

    @Override
    public void onItemPickup(@NotNull ItemEntity itemEntity) {
        super.onItemPickup(itemEntity);
        setItemInHand(InteractionHand.MAIN_HAND, itemEntity.getItem());
    }

    @Override
    public boolean shouldShowName() {
        return super.shouldShowName();
    }

    public static AttributeSupplier.Builder getFolkAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 40.0D).add(ForgeMod.NAMETAG_DISTANCE.get(), 20.0D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.05D));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0F));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.entityData.set(SKIN_ID, pCompound.getInt("Skin"));
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("Skin", this.getSkin());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SKIN_ID, 1);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return ModEntities.ENTITY_FOLK.get().create(pLevel);
    }

    @Override
    public void tick() {
        super.tick();
    }

    public int getSkin() {
        return this.entityData.get(SKIN_ID);
    }

    public void setSkin(int skin) {
        this.entityData.set(SKIN_ID, skin);
    }
}

