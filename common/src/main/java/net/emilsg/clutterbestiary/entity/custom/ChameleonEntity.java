package net.emilsg.clutterbestiary.entity.custom;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.ValueInput;

import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.custom.goal.TamedEscapeDangerGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.WanderAroundFarOftenGoal;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentTameableEntity;
import net.emilsg.clutterbestiary.item.ModItems;
import net.emilsg.clutterbestiary.item.custom.ButterflyBottleItem;
import net.emilsg.clutterbestiary.util.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ChameleonEntity extends ParentTameableEntity {

    private static final Ingredient BREEDING_INGREDIENT;
    private static final Item TAMING_ITEM = Items.APPLE;
    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(ChameleonEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> ATTACKING = SynchedEntityData.defineId(ChameleonEntity.class, EntityDataSerializers.BOOLEAN);

    static {
        BREEDING_INGREDIENT = Ingredient.of(ModItems.BUTTERFLY_IN_A_BOTTLE.get());
    }

    public final AnimationState tailIdleAnimationState = new AnimationState();
    public final AnimationState toungeIdleAnimationState = new AnimationState();
    public final AnimationState sittingAnimationState = new AnimationState();
    public int tailIdleAnimationTimeout = 0;
    public int toungeIdleAnimationTimeout = 0;
    private int currentColor = 0x90C47C;
    private int targetColor = 0x90C47C;
    private int retainColorChangeTimer = 0;
    private int colorTicker = 0;
    private boolean hasNearbyEntity;

    public ChameleonEntity(EntityType<? extends ParentTameableEntity> entityType, Level world) {
        super(entityType, world);
        this.setPathfindingMalus(PathType.FIRE_IN_NEIGHBOR, -1.0F);
        this.setPathfindingMalus(PathType.WATER, -1.0F);
        this.setPathfindingMalus(PathType.WATER_BORDER, 16.0F);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SITTING, false);
        builder.define(ATTACKING, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(3, new TamedEscapeDangerGoal(this, 1.5));
        this.goalSelector.addGoal(4, new FollowOwnerGoal(this, 1.2, 10.0F, 2.0F));
        this.goalSelector.addGoal(5, new BreedGoal(this, 1));
        this.goalSelector.addGoal(6, new TemptGoal(this, 1.2, BREEDING_INGREDIENT, false));
        this.goalSelector.addGoal(7, new FollowParentGoal(this, 1.2));
        this.goalSelector.addGoal(8, new LeapAtTargetGoal(this, 0.5f));
        this.goalSelector.addGoal(9, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(10, new WanderAroundFarOftenGoal(this, 1.0f));
        this.goalSelector.addGoal(11, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(12, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, ButterflyEntity.class, true));
    }

    @Override
    public void readAdditionalSaveData(ValueInput nbt) {
        super.readAdditionalSaveData(nbt);
        this.entityData.set(SITTING, nbt.getBooleanOr("isSitting", false));
    }

    @Override
    public void addAdditionalSaveData(ValueOutput nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("isSitting", this.entityData.get(SITTING));
    }

    public static boolean checkAnimalSpawnRules(EntityType<? extends Animal> type, LevelAccessor world, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
        return world.getBlockState(pos.below()).is(ModBlockTags.CHAMELEONS_SPAWN_ON);
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Animal.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 6.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.18f)
                .add(Attributes.ATTACK_SPEED, 1.0f)
                .add(Attributes.ATTACK_KNOCKBACK, 0.1f)
                .add(Attributes.ATTACK_DAMAGE, 3.0f)
                .add(Attributes.FOLLOW_RANGE, 16.0f);
    }

    @Override
    public void spawnChildFromBreeding(ServerLevel world, Animal other) {
        super.spawnChildFromBreeding(world, other);
    }

    @Override
    public boolean canMate(Animal other) {
        if (other == this) {
            return false;
        } else if (!this.isTame()) {
            return false;
        } else if (!(other instanceof ChameleonEntity chameleonEntity)) {
            return false;
        } else {
            if (!chameleonEntity.isTame()) {
                return false;
            } else if (chameleonEntity.isInSittingPose()) {
                return false;
            } else {
                return this.isInLove() && chameleonEntity.isInLove();
            }
        }
    }

    @Nullable
    public ChameleonEntity getBreedOffspring(ServerLevel serverWorld, AgeableMob passiveEntity) {
        ChameleonEntity chameleonEntity = ModEntityTypes.CHAMELEON.get().create(serverWorld, EntitySpawnReason.BREEDING);
        if (chameleonEntity != null) {
            EntityReference<LivingEntity> owner = this.getOwnerReference();
            if (owner != null) {
                chameleonEntity.setOwnerReference(owner);
                chameleonEntity.setTame(true, true);
            }
        }

        return chameleonEntity;
    }

    public int getCurrentColor() {
        return currentColor;
    }

    @Override
    public Item getTamingItem() {
        return TAMING_ITEM;
    }

    public int getTargetColor() {
        return targetColor;
    }

    public void setTargetColor(int color) {
        this.targetColor = color;
    }

    @Override
    public boolean causeFallDamage(double fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stackInHand = player.getItemInHand(hand);
        Item item = stackInHand.getItem();

        Item itemForTaming = this.getTamingItem();

        if (this.isFood(stackInHand) && this.getHealth() < this.getMaxHealth()) {
            if (this.level().isClientSide()) return InteractionResult.CONSUME;
            if (!player.getAbilities().instabuild) {
                stackInHand.shrink(1);
            }

            FoodProperties foodComponent = stackInHand.get(DataComponents.FOOD);
            float nutrition = foodComponent != null ? (float) foodComponent.nutrition() : 1.0F;
            this.heal(2.0F * nutrition);
            return InteractionResult.SUCCESS;
        }

        if (item == itemForTaming && !isTame()) {
            this.playSound(SoundEvents.FROG_EAT, 1.0F, 1.25F);
            if (this.level().isClientSide()) {
                return InteractionResult.CONSUME;
            } else {
                if (!player.getAbilities().instabuild) {
                    stackInHand.shrink(1);
                }

                if (this.random.nextInt(3) == 0 && !this.level().isClientSide()) {
                    super.tame(player);
                    this.navigation.recomputePath();
                    this.setHealth(this.getMaxHealth());
                    this.setTarget(null);
                    this.level().broadcastEntityEvent(this, (byte) 7);
                    setSit(true);
                } else {
                    this.level().broadcastEntityEvent(this, (byte) 6);
                }

                return InteractionResult.SUCCESS;
            }
        }

        if (isTame() && !this.level().isClientSide() && hand == InteractionHand.MAIN_HAND && !(stackInHand.getItem() instanceof ButterflyBottleItem) && isOwnedBy(player)) {
            setSit(!isOrderedToSit());
            return InteractionResult.SUCCESS;
        }

        if (stackInHand.getItem() == itemForTaming) {
            return InteractionResult.PASS;
        }

        return super.mobInteract(player, hand);
    }

    public boolean isAggressive() {
        return this.entityData.get(ATTACKING);
    }

    public void setAggressive(boolean attacking) {
        this.entityData.set(ATTACKING, attacking);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return BREEDING_INGREDIENT.test(stack);
    }

    public boolean isOrderedToSit() {
        return this.entityData.get(SITTING);
    }

    public void setSit(boolean sitting) {
        this.entityData.set(SITTING, sitting);
        super.setOrderedToSit(sitting);
    }

    @Override
    public void tick() {
        super.tick();
        Level world = this.level();

        if (world.isClientSide()) {
            this.setupAnimationStates();

            if (this.tickCount % 10 == 0) {
                this.hasNearbyEntity = !world.getEntitiesOfClass(LivingEntity.class,
                        this.getBoundingBox().inflate(6.0),
                        entity -> entity != this && entity != this.getOwner()
                                && !(entity instanceof ChameleonEntity) && !entity.isShiftKeyDown()).isEmpty();
                this.setAggressive(this.hasNearbyEntity);
            }

            this.updateColorTransition(this.hasNearbyEntity);
        }
    }

    protected void checkFallDamage(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {
    }

    @Override
    protected void applyTamingSideEffects() {
        getAttribute(Attributes.MAX_HEALTH).setBaseValue(18.0D);
        getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(6.0f);
    }

    protected void updateWalkAnimation(float v) {
        float f;
        if (this.getPose() == Pose.STANDING) {
            f = Math.min(v * 6.0F, 1.0F);
        } else {
            f = 0.0F;
        }

        this.walkAnimation.update(f * 2.75f, 0.2F, 1.0F);
    }

    private void setupAnimationStates() {
        if (this.tailIdleAnimationTimeout <= 0 && !this.isMoving()) {
            this.tailIdleAnimationTimeout = 40;
            this.tailIdleAnimationState.start(this.tickCount);
        } else {
            --this.tailIdleAnimationTimeout;
        }

        if (this.toungeIdleAnimationTimeout <= 0) {
            this.toungeIdleAnimationTimeout = 20 + ((random.nextInt(5) + 3) * 100);
            this.toungeIdleAnimationState.start(this.tickCount);
        } else {
            --this.toungeIdleAnimationTimeout;
        }

        if (this.isOrderedToSit() && !this.sittingAnimationState.isStarted()) {
            this.sittingAnimationState.start(this.tickCount);
        } else if (!this.isOrderedToSit()) {
            this.sittingAnimationState.stop();
        }
    }

    private void updateColorTransition(boolean shouldChangeColor) {
        colorTicker++;
        if (colorTicker % 2 == 0) {
            if (shouldChangeColor) {
                retainColorChangeTimer = 200;
            }

            int r1 = (currentColor >> 16) & 0xFF;
            int g1 = (currentColor >> 8) & 0xFF;
            int b1 = currentColor & 0xFF;

            int r2 = (targetColor >> 16) & 0xFF;
            int g2 = (targetColor >> 8) & 0xFF;
            int b2 = targetColor & 0xFF;

            float lerpSpeed = 0.1f; // slower & smoother

            int r = (int) (r1 + (r2 - r1) * lerpSpeed);
            int g = (int) (g1 + (g2 - g1) * lerpSpeed);
            int b = (int) (b1 + (b2 - b1) * lerpSpeed);

            currentColor = (r << 16) | (g << 8) | b;

            // Optional: revert to default after timer runs out
            if (retainColorChangeTimer > 0) {
                retainColorChangeTimer--;
            } else {
                targetColor = 0x90C47C;
            }

            colorTicker = 0;
        }
    }


}
