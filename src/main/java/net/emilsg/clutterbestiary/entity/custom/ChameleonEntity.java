package net.emilsg.clutterbestiary.entity.custom;

import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.custom.goal.TamedEscapeDangerGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.WanderAroundFarOftenGoal;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentTameableEntity;
import net.emilsg.clutterbestiary.item.ModItems;
import net.emilsg.clutterbestiary.item.custom.ButterflyBottleItem;
import net.minecraft.block.BlockState;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public class ChameleonEntity extends ParentTameableEntity {

    private static final Ingredient BREEDING_INGREDIENT;
    private static final TrackedData<Boolean> SITTING = DataTracker.registerData(ChameleonEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> ATTACKING = DataTracker.registerData(ChameleonEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    private int currentColor = 0x90C47C;
    private int targetColor = 0x90C47C;
    private int retainColorChangeTimer = 0;
    private int colorTicker = 0;

    public final AnimationState tailIdleAnimationState = new AnimationState();
    public final AnimationState toungeIdleAnimationState = new AnimationState();

    public int tailIdleAnimationTimeout = 0;
    public int toungeIdleAnimationTimeout = 0;


    private void setupAnimationStates() {
        if (this.tailIdleAnimationTimeout <= 0 && !this.isMoving()) {
            this.tailIdleAnimationTimeout = 40;
            this.tailIdleAnimationState.start(this.age);
        } else {
            --this.tailIdleAnimationTimeout;
        }

        if (this.toungeIdleAnimationTimeout <= 0) {
            this.toungeIdleAnimationTimeout = 20 + ((random.nextInt(5) + 3) * 100);
            this.toungeIdleAnimationState.start(this.age);
        } else {
            --this.toungeIdleAnimationTimeout;
        }

        if (this.isSitting() && !this.sittingAnimationState.isRunning()) {
            this.sittingAnimationState.start(this.age);
        } else if (!this.isSitting()) {
            this.sittingAnimationState.stop();
        }
    }

    public boolean isAttacking() {
        return this.dataTracker.get(ATTACKING);
    }

    public void setAttacking(boolean attacking) {
        this.dataTracker.set(ATTACKING, attacking);
    }

    static {
        BREEDING_INGREDIENT = Ingredient.ofItems(ModItems.BUTTERFLY_IN_A_BOTTLE);
    }

    public final AnimationState sittingAnimationState = new AnimationState();

    public int getCurrentColor() {
        return currentColor;
    }

    public int getTargetColor() {
        return targetColor;
    }

    public void setTargetColor(int color) {
        this.targetColor = color;
    }

    public ChameleonEntity(EntityType<? extends ParentTameableEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER_BORDER, 16.0F);
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 6.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.18f)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 1.0f)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 0.1f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0f)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0f);
    }

    protected void updateLimbs(float v) {
        float f;
        if (this.getPose() == EntityPose.STANDING) {
            f = Math.min(v * 6.0F, 1.0F);
        } else {
            f = 0.0F;
        }

        this.limbAnimator.updateLimbs(f * 2.75f, 0.2F);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stackInHand = player.getStackInHand(hand);
        Item item = stackInHand.getItem();

        Item itemForTaming = Items.APPLE;

        if (this.isBreedingItem(stackInHand) && this.getHealth() < this.getMaxHealth()) {
            if (!player.getAbilities().creativeMode) {
                stackInHand.decrement(1);
            }

            this.heal((float) Objects.requireNonNull(item.getFoodComponent()).getHunger());
            return ActionResult.SUCCESS;
        }

        if (item == itemForTaming && !isTamed()) {
            this.playSound(SoundEvents.ENTITY_FROG_EAT, 1.0F, 1.25F);
            if (this.getWorld().isClient()) {
                return ActionResult.CONSUME;
            } else {
                if (!player.getAbilities().creativeMode) {
                    stackInHand.decrement(1);
                }

                if (this.random.nextInt(3) == 0 && !this.getWorld().isClient()) {
                    super.setOwner(player);
                    this.navigation.recalculatePath();
                    this.setHealth(this.getMaxHealth());
                    this.setTarget(null);
                    this.getWorld().sendEntityStatus(this, (byte) 7);
                    setSit(true);
                } else {
                    this.getWorld().sendEntityStatus(this, (byte) 6);
                }

                return ActionResult.SUCCESS;
            }
        }

        if (isTamed() && !this.getWorld().isClient() && hand == Hand.MAIN_HAND && !(stackInHand.getItem() instanceof ButterflyBottleItem) && isOwner(player)) {
            setSit(!isSitting());
            return ActionResult.SUCCESS;
        }

        if (stackInHand.getItem() == itemForTaming) {
            return ActionResult.PASS;
        }

        return super.interactMob(player, hand);
    }

    @Override
    public void breed(ServerWorld world, AnimalEntity other) {
        super.breed(world, other);
    }

    public boolean canBeLeashedBy(PlayerEntity player) {
        return !this.isSitting() && this.isOwner(player);
    }

    @Override
    public boolean canBreedWith(AnimalEntity other) {
        if (other == this) {
            return false;
        } else if (!this.isTamed()) {
            return false;
        } else if (!(other instanceof ChameleonEntity chameleonEntity)) {
            return false;
        } else {
            if (!chameleonEntity.isTamed()) {
                return false;
            } else if (chameleonEntity.isInSittingPose()) {
                return false;
            } else {
                return this.isInLove() && chameleonEntity.isInLove();
            }
        }
    }

    @Nullable
    public ChameleonEntity createChild(ServerWorld serverWorld, PassiveEntity passiveEntity) {
        ChameleonEntity chameleonEntity = ModEntityTypes.CHAMELEON.create(serverWorld);
        if (chameleonEntity != null) {
            UUID uUID = this.getOwnerUuid();
            if (uUID != null) {
                chameleonEntity.setOwnerUuid(uUID);
                chameleonEntity.setTamed(true);
            }
        }

        return chameleonEntity;
    }

    @Override
    public double getMountedHeightOffset() {
        return 0.15D;
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        World world = this.getWorld();

        if (world.isClient) {
            this.setupAnimationStates();

            boolean hasNearbyEntity = !world.getOtherEntities(this, this.getBoundingBox().expand(6.0), entity -> entity != null && entity != this && this.getOwner() != entity && !(entity instanceof ChameleonEntity) && !entity.isSneaking()).isEmpty();
            this.setAttacking(hasNearbyEntity);

            this.updateColorTransition(hasNearbyEntity);
        }
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.getItem() instanceof ButterflyBottleItem;
    }

    public boolean isSitting() {
        return this.dataTracker.get(SITTING);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dataTracker.set(SITTING, nbt.getBoolean("isSitting"));
    }

    public void setSit(boolean sitting) {
        this.dataTracker.set(SITTING, sitting);
        super.setSitting(sitting);
    }

    @Override
    public void setTamed(boolean tamed) {
        super.setTamed(tamed);
        if (tamed) {
            getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(18.0D);
            getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(6.0f);
        } else {
            getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(6.0D);
            getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(3.0f);
        }
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new SitGoal(this));
        this.goalSelector.add(3, new TamedEscapeDangerGoal(this, 1.5));
        this.goalSelector.add(4, new FollowOwnerGoal(this, 1.2, 10.0F, 2.0F, false));
        this.goalSelector.add(5, new AnimalMateGoal(this, 1));
        this.goalSelector.add(6, new TemptGoal(this, 1.2, BREEDING_INGREDIENT, false));
        this.goalSelector.add(7, new FollowParentGoal(this, 1.2));
        this.goalSelector.add(8, new PounceAtTargetGoal(this,0.5f));
        this.goalSelector.add(9, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.add(10, new WanderAroundFarOftenGoal(this, 1.0f));
        this.goalSelector.add(11, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(12, new LookAroundGoal(this));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, ButterflyEntity.class, true));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("isSitting", this.dataTracker.get(SITTING));
    }

    protected void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(SITTING, false);
        this.dataTracker.startTracking(ATTACKING, false);

    }

    private void updateColorTransition(boolean shouldChangeColor) {
        colorTicker++;
        if(colorTicker % 2 == 0) {
            if (shouldChangeColor) {
                retainColorChangeTimer = 200;
            }

            if (retainColorChangeTimer > 0) {
                retainColorChangeTimer--;
                int r1 = (currentColor >> 16) & 0xFF;
                int g1 = (currentColor >> 8) & 0xFF;
                int b1 = currentColor & 0xFF;

                int r2 = (targetColor >> 16) & 0xFF;
                int g2 = (targetColor >> 8) & 0xFF;
                int b2 = targetColor & 0xFF;

                float lerpSpeed = 0.5f;

                int r = (int) (r1 + (r2 - r1) * lerpSpeed);
                int g = (int) (g1 + (g2 - g1) * lerpSpeed);
                int b = (int) (b1 + (b2 - b1) * lerpSpeed);

                currentColor = (r << 16) | (g << 8) | b;
            } else {
                targetColor = 0x90C47C;
                currentColor = 0x90C47C;
            }

            colorTicker = 0;
        }
    }

}
