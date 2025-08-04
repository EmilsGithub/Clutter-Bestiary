package net.emilsg.clutterbestiary.entity.custom;

import net.emilsg.clutterbestiary.block.ModBlocks;
import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.custom.goal.KiwiBirdLayEggGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.KiwiBirdMateGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.KiwiBirdWanderAroundFarGoal;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentAnimalEntity;
import net.emilsg.clutterbestiary.sound.ModSoundEvents;
import net.emilsg.clutterbestiary.util.ModItemTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class KiwiBirdEntity extends ParentAnimalEntity {
    private static final Ingredient BREEDING_INGREDIENT = Ingredient.fromTag(ModItemTags.C_SEEDS);
    private static final TrackedData<Boolean> HAS_EGG = DataTracker.registerData(KiwiBirdEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> EGG_TIMER = DataTracker.registerData(KiwiBirdEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> IS_DANCING = DataTracker.registerData(KiwiBirdEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState dancingAnimationState = new AnimationState();
    public int idleAnimationTimeout = 0;
    public int dancingAnimationTimeout = 0;
    private boolean songPlaying;
    @Nullable
    private BlockPos songSource;

    public KiwiBirdEntity(EntityType<? extends ParentAnimalEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.DAMAGE_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.COCOA, -1.0F);
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return ParentAnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 6.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.225f)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 0.5f)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 0.1f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0f)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0f);
    }

    public boolean canEat() {
        return super.canEat() && !this.hasEgg() && !this.isDancing();
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return ModEntityTypes.KIWI_BIRD.create(world);
    }

    public int getEggTimer() {
        return this.dataTracker.get(EGG_TIMER);
    }

    public void setEggTimer(int time) {
        this.dataTracker.set(EGG_TIMER, time);
    }

    public boolean hasEgg() {
        return this.dataTracker.get(HAS_EGG);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isIn(ModItemTags.C_SEEDS);
    }

    public boolean isDancing() {
        return this.dataTracker.get(IS_DANCING);
    }

    public void setDancing(boolean dancing) {
        this.dataTracker.set(IS_DANCING, dancing);
    }

    public boolean isSongPlaying() {
        return this.songPlaying;
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setHasEgg(nbt.getBoolean("HasEgg"));
        this.setEggTimer(nbt.getInt("EggTimer"));
        this.setDancing(nbt.getBoolean("IsDancing"));
    }

    public void setHasEgg(boolean hasEgg) {
        this.dataTracker.set(HAS_EGG, hasEgg);
    }

    public void setNearbySongPlaying(BlockPos songPosition, boolean playing) {
        this.songSource = songPosition;
        this.songPlaying = playing;
    }

    @Override
    public void tick() {
        super.tick();
        World world = this.getWorld();

        if (world.isClient) {
            this.setupAnimationStates();
        }

        if (this.songSource == null || !this.songSource.isWithinDistance(this.getPos(), 3.46) || !this.getWorld().getBlockState(this.songSource).isOf(Blocks.JUKEBOX)) {
            this.songPlaying = false;
            this.songSource = null;
        }

        this.setDancing(this.isSongPlaying());

    }

    public void tickMovement() {


        if (this.hasEgg()) {
            this.setEggTimer(this.getEggTimer() + 1);
        }

        super.tickMovement();
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("HasEgg", this.hasEgg());
        nbt.putInt("EggTimer", this.getEggTimer());
        nbt.putBoolean("IsDancing", this.isDancing());
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return ModSoundEvents.ENTITY_KIWI_CALL;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_PARROT_DEATH;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_PARROT_HURT;
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(HAS_EGG, false);
        this.dataTracker.startTracking(EGG_TIMER, 0);
        this.dataTracker.startTracking(IS_DANCING, false);

    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.25));
        this.goalSelector.add(2, new KiwiBirdMateGoal(this, 1));
        this.goalSelector.add(3, new KiwiBirdLayEggGoal(this, 1, ModBlocks.KIWI_BIRD_EGG.getDefaultState()));
        this.goalSelector.add(4, new TemptGoal(this, 1.1, BREEDING_INGREDIENT, false));
        this.goalSelector.add(5, new FollowParentGoal(this, 1));
        this.goalSelector.add(6, new KiwiBirdWanderAroundFarGoal(this, this, 1.0, 1));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ENTITY_PARROT_STEP, 0.15F, 0.75F);
    }

    private void setupAnimationStates() {
        if (this.idleAnimationTimeout <= 0 && !this.isMoving() && !this.isSongPlaying()) {
            this.idleAnimationTimeout = 30;
            this.idleAnimationState.start(this.age);
        } else {
            --this.idleAnimationTimeout;
        }

        if (dancingAnimationTimeout <= 0 && this.isSongPlaying()) {
            this.dancingAnimationTimeout = 20;
            this.dancingAnimationState.start(this.age);
        } else {
            --this.dancingAnimationTimeout;
        }
    }

}
