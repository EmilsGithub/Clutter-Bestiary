package net.emilsg.clutterbestiary.entity.custom;
import java.util.function.Predicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.ValueInput;

import net.emilsg.clutterbestiary.block.ModBlocks;
import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.custom.goal.EmperorPenguinLayEggGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.EmperorPenguinMateGoal;
import net.emilsg.clutterbestiary.entity.custom.parent.IEggLayingAnimal;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentAnimalEntity;
import net.emilsg.clutterbestiary.sound.ModSoundEvents;
import net.emilsg.clutterbestiary.util.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.pathfinder.PathType;
import org.jetbrains.annotations.Nullable;

public class EmperorPenguinEntity extends ParentAnimalEntity implements IEggLayingAnimal {
    private static final int EGG_LAYING_DELAY_TICKS = 400;
    private static final Predicate<ItemStack> BREEDING_INGREDIENT = stack -> stack.is(ItemTags.FISHES);
    private static final EntityDataAccessor<Boolean> HAS_EGG = SynchedEntityData.defineId(EmperorPenguinEntity.class, EntityDataSerializers.BOOLEAN);

    public final AnimationState flapAnimationStateOne = new AnimationState();
    public final AnimationState flapAnimationStateTwo = new AnimationState();
    public final AnimationState preenAnimationState = new AnimationState();

    public int randomAnimationTimeout = 0;

    private int eggTimer;

    public EmperorPenguinEntity(EntityType<? extends ParentAnimalEntity> entityType, Level world) {
        super(entityType, world);
        this.setPathfindingMalus(PathType.WATER, 0.0F);
        this.setPathfindingMalus(PathType.FIRE_IN_NEIGHBOR, -1.0F);
        this.setPathfindingMalus(PathType.FIRE, -1.0F);
        this.setPathfindingMalus(PathType.COCOA, -1.0F);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(HAS_EGG, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25));
        this.goalSelector.addGoal(2, new EmperorPenguinMateGoal(this, 1));
        this.goalSelector.addGoal(3, new EmperorPenguinLayEggGoal(this, 1, ModBlocks.EMPEROR_PENGUIN_EGG.get().defaultBlockState()));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.1, BREEDING_INGREDIENT, false));
        this.goalSelector.addGoal(5, new FollowParentGoal(this, 1));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1f));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    public void readAdditionalSaveData(ValueInput nbt) {
        super.readAdditionalSaveData(nbt);
        this.setHasEgg(nbt.getBooleanOr("HasEgg", false));
        this.setEggTimer(nbt.getIntOr("EggTimer", 0));
    }

    public void addAdditionalSaveData(ValueOutput nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("HasEgg", this.hasEgg());
        nbt.putInt("EggTimer", this.getEggTimer());
    }

    public static AttributeSupplier.Builder setAttributes() {
        return ParentAnimalEntity.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 16.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.14f)
                .add(Attributes.ATTACK_SPEED, 0.5f)
                .add(Attributes.ATTACK_KNOCKBACK, 0.1f)
                .add(Attributes.ATTACK_DAMAGE, 4.0f)
                .add(Attributes.FOLLOW_RANGE, 16.0f);
    }

    public static boolean checkAnimalSpawnRules(EntityType<? extends Animal> type, LevelAccessor world, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
        return world.getBlockState(pos.below()).is(ModBlockTags.EMPEROR_PENGUINS_SPAWN_ON);
    }

    public boolean canFallInLove() {
        return super.canFallInLove() && !this.hasEgg();
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
        return ModEntityTypes.EMPEROR_PENGUIN.get().create(world, EntitySpawnReason.BREEDING);
    }

    public int getEggTimer() {
        return this.eggTimer;
    }

    public void setEggTimer(int time) {
        this.eggTimer = time;
    }

    @Override
    public int getAmbientSoundInterval() {
        return 240;
    }

    @Override
    public boolean hasEgg() {
        return this.entityData.get(HAS_EGG);
    }

    @Override
    public boolean isReadyToLayEgg() {
        return this.hasEgg() && this.eggTimer >= EGG_LAYING_DELAY_TICKS;
    }

    public void beginCarryingEgg() {
        this.eggTimer = 0;
        this.setHasEgg(true);
    }

    @Override
    public void finishLayingEgg() {
        this.setHasEgg(false);
        this.eggTimer = 0;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return BREEDING_INGREDIENT.test(stack);
    }

    @Override
    public void playAmbientSound() {
        SoundEvent soundEvent = this.getAmbientSound();
        if (soundEvent != null) {
            this.playSound(soundEvent, this.getSoundVolume(), this.getVoicePitch() + 0.3f);
        }

    }

    public void setHasEgg(boolean hasEgg) {
        this.entityData.set(HAS_EGG, hasEgg);
    }

    @Override
    public void tick() {
        super.tick();
        Level world = this.level();

        if (world.isClientSide()) {
            this.setupAnimationStates();
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (!this.level().isClientSide() && this.hasEgg() && this.eggTimer < EGG_LAYING_DELAY_TICKS) {
            this.eggTimer++;
        }
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return ModSoundEvents.ENTITY_EMPEROR_PENGUIN_AMBIENT.get();
    }

    protected void updateWalkAnimation(float v) {
        float f;
        if (this.getPose() == Pose.STANDING) {
            f = Math.min(v * 6.0f, 1.0f);
        } else {
            f = 0.0f;
        }

        this.walkAnimation.update(f * 1.5f, 0.3F, 1.0F);
    }

    private void pickRandomIdleAnim(int i) {
        switch (i) {
            case 1 -> this.flapAnimationStateTwo.startIfStopped(this.tickCount);
            case 2 -> this.preenAnimationState.startIfStopped(this.tickCount);
            default -> this.flapAnimationStateOne.startIfStopped(this.tickCount);
        }
    }

    private void setupAnimationStates() {
        if (this.randomAnimationTimeout <= 0 && random.nextInt(400) == 0 && this.getNavigation().isDone()) {
            this.randomAnimationTimeout = 400;
            this.pickRandomIdleAnim(random.nextInt(3));
        } else {
            --this.randomAnimationTimeout;
        }
    }
}
