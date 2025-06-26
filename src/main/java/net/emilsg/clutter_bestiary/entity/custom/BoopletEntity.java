package net.emilsg.clutter_bestiary.entity.custom;

import net.emilsg.clutter_bestiary.entity.custom.goal.BoopletWanderGoal;
import net.emilsg.clutter_bestiary.entity.custom.goal.TrackedFleeGoal;
import net.emilsg.clutter_bestiary.entity.custom.parent.ParentAnimalEntity;
import net.emilsg.clutter_bestiary.sound.ModSoundEvents;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class BoopletEntity extends ParentAnimalEntity implements Shearable {
    private static final TrackedData<Boolean> IS_FLUFFY = DataTracker.registerData(BoopletEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> FLUFF_TIMER = DataTracker.registerData(BoopletEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public final AnimationState happyAnimationState = new AnimationState();
    public final AnimationState boopAnimationState = new AnimationState();
    public final AnimationState swimAnimationState = new AnimationState();
    private boolean isBooped = false;
    private int timeSinceBoop = 0;
    private int happyDanceTimer = 0;
    private int happyAnimationTimer = 0;
    private int swimAnimationTimeout = 0;
    private boolean isHappy = false;
    private PlayerEntity lastPetPlayer = null;

    public BoopletEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER, -2.0F);
        this.setPathfindingPenalty(PathNodeType.WATER_BORDER, 16.0F);
        this.setPathfindingPenalty(PathNodeType.COCOA, -1.0F);
        this.setPathfindingPenalty(PathNodeType.FENCE, -1.0F);
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return ParentAnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 8D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.18f)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0f);
    }

    public boolean canBeHappy() {
        return this.hurtTime <= 0 && this.random.nextInt(20) == 0 && this.happyDanceTimer <= 0 && !this.isTouchingWaterOrRain();
    }

    public int getFluffTimer() {
        return this.dataTracker.get(FLUFF_TIMER);
    }

    public void setFluffTimer(int fluffTimer) {
        this.dataTracker.set(FLUFF_TIMER, fluffTimer);
    }

    public int getTimeSinceBoop() {
        return this.timeSinceBoop;
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.isOf(Items.SHEARS)) {
            if (!this.getWorld().isClient && this.isFluffy()) {
                this.sheared(SoundCategory.PLAYERS);
                this.emitGameEvent(GameEvent.SHEAR, player);
                itemStack.damage(1, player, (playerEntity) -> playerEntity.sendToolBreakStatus(hand));
                return ActionResult.SUCCESS;
            } else {
                return ActionResult.CONSUME;
            }
        } else if (player.getStackInHand(Hand.MAIN_HAND).isEmpty()) {
            player.swingHand(Hand.MAIN_HAND);

            if(!player.getWorld().isClient) {
                this.playSound(ModSoundEvents.ENTITY_BOOPLET_SQUEAK, this.getSoundVolume(), this.getSoundPitch());
            }

            if (this.canBeHappy()) {
                this.lastPetPlayer = player;
                this.isHappy = true;
                this.getLookControl().lookAt(player);
            } else {
                this.isBooped = true;
                this.timeSinceBoop = 0;
            }
            this.getNavigation().stop();
            return ActionResult.SUCCESS;
        }
        return super.interactMob(player, hand);
    }

    public boolean isFluffy() {
        return this.dataTracker.get(IS_FLUFFY);
    }

    @Override
    public boolean isShearable() {
        return isFluffy();
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.setIsFluffy(nbt.getBoolean("IsFluffy"));
        this.setFluffTimer(nbt.getInt("FluffTimer"));
    }

    public void setIsFluffy(boolean fluffy) {
        this.dataTracker.set(IS_FLUFFY, fluffy);
    }

    public void sheared(SoundCategory shearedSoundCategory) {
        this.getWorld().playSoundFromEntity(null, this, SoundEvents.ENTITY_SHEEP_SHEAR, shearedSoundCategory, 1.0F, 1.25F);
        this.setIsFluffy(false);
        this.setFluffTimer(0);
        int droppedAmount = 1 + this.random.nextInt(3);

        for (int j = 0; j < droppedAmount; ++j) {
            ItemEntity itemEntity = this.dropItem(Items.STRING, 0);
            if (itemEntity != null) {
                itemEntity.setVelocity(itemEntity.getVelocity().add(((this.random.nextFloat() - this.random.nextFloat()) * 0.1F), (this.random.nextFloat() * 0.05F), ((this.random.nextFloat() - this.random.nextFloat()) * 0.1F)));
            }
        }

    }

    @Override
    public void tick() {
        super.tick();
        World world = this.getWorld();
        if (timeSinceBoop <= 10) timeSinceBoop++;
        if (happyDanceTimer > 0) happyDanceTimer--;

        if (this.happyAnimationTimer > 0) this.getNavigation().stop();

        if (world.isClient) {
            this.setupAnimationStates();
            if (this.isBooped && !this.isHappy) {
                this.stopAndOrRestartBoopAnimation(true);
            } else if (this.isHappy) {
                this.stopAndOrRestartBoopAnimation(false);
                this.happyDanceTimer = 600;
                this.happyAnimationState.start(this.age);
                this.happyAnimationTimer = 80;
                this.isHappy = false;
            }
        } else if (!this.isFluffy()) {
            int fluffTimer = this.getFluffTimer();
            if (fluffTimer < 5000) this.setFluffTimer(fluffTimer++);
            this.setIsFluffy(fluffTimer >= 5000);
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("IsFluffy", this.isFluffy());
        nbt.putInt("FluffTimer", this.getFluffTimer());
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(IS_FLUFFY, true);
        this.dataTracker.startTracking(FLUFF_TIMER, 0);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new TrackedFleeGoal(this, 1.5f));
        this.goalSelector.add(2, new LookAroundGoal(this));
        this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 4));
        this.goalSelector.add(4, new BoopletWanderGoal(this, 1.0f));
    }

    protected void updateLimbs(float v) {
        float f;
        if (this.getPose() == EntityPose.STANDING) {
            f = Math.min(v * 6.0F, 1.0F);
        } else {
            f = 0.0F;
        }

        this.limbAnimator.updateLimbs(f * 2.5f, 0.2F);
    }

    private void setupAnimationStates() {
        if (happyAnimationTimer > 0) happyAnimationTimer--;
        if (happyAnimationTimer <= 0 || this.hurtTime > 0) this.happyAnimationState.stop();

        if(swimAnimationTimeout <= 0 && this.isTouchingWater()) {
            this.swimAnimationTimeout = 20;
            this.swimAnimationState.start(this.age);
        } else {
            --this.swimAnimationTimeout;
        }
    }

    private void stopAndOrRestartBoopAnimation(boolean restart) {
        this.boopAnimationState.stop();
        if (restart) {
            this.boopAnimationState.start(this.age);
            this.isBooped = false;
        }
    }
}