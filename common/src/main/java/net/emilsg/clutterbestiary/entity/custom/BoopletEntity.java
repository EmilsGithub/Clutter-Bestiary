package net.emilsg.clutterbestiary.entity.custom;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.item.component.SwingAnimation;
import net.minecraft.world.level.storage.ValueOutput;

import net.emilsg.clutterbestiary.entity.custom.goal.BoopletWanderGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.TrackedFleeGoal;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentAnimalEntity;
import net.emilsg.clutterbestiary.sound.ModSoundEvents;
import net.emilsg.clutterbestiary.util.ModBlockTags;
import net.emilsg.clutterbestiary.util.ModUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathType;
import org.jetbrains.annotations.Nullable;

public class BoopletEntity extends ParentAnimalEntity implements Shearable {
    private static final EntityDataAccessor<Boolean> IS_FLUFFY = SynchedEntityData.defineId(BoopletEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> BOOP_ACTION = SynchedEntityData.defineId(BoopletEntity.class, EntityDataSerializers.INT);

    public final AnimationState happyAnimationState = new AnimationState();
    public final AnimationState boopAnimationState = new AnimationState();
    public final AnimationState swimAnimationState = new AnimationState();
    private boolean isBooped = false;
    private int timeSinceBoop = 0;
    private int happyDanceTimer = 0;
    private int happyAnimationTimer = 0;
    private int swimAnimationTimeout = 0;
    private boolean isHappy = false;
    private int fluffTimer;

    public BoopletEntity(EntityType<? extends Animal> entityType, Level world) {
        super(entityType, world);
        this.setPathfindingMalus(PathType.FIRE_IN_NEIGHBOR, -1.0F);
        this.setPathfindingMalus(PathType.WATER, -2.0F);
        this.setPathfindingMalus(PathType.WATER_BORDER, 16.0F);
        this.setPathfindingMalus(PathType.COCOA, -1.0F);
        this.setPathfindingMalus(PathType.FENCE, -1.0F);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_FLUFFY, true);
        builder.define(BOOP_ACTION, 0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new TrackedFleeGoal(this, 1.5f));
        this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 4));
        this.goalSelector.addGoal(4, new BoopletWanderGoal(this, 1.0f));
    }

    @Override
    protected void readAdditionalSaveData(ValueInput nbt) {
        super.readAdditionalSaveData(nbt);
        this.setIsFluffy(nbt.getBooleanOr("IsFluffy", false));
        this.setFluffTimer(nbt.getIntOr("FluffTimer", 0));
    }

    @Override
    public void addAdditionalSaveData(ValueOutput nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("IsFluffy", this.isFluffy());
        nbt.putInt("FluffTimer", this.getFluffTimer());
    }

    public static AttributeSupplier.Builder setAttributes() {
        return ParentAnimalEntity.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 8D)
                .add(Attributes.MOVEMENT_SPEED, 0.18f)
                .add(Attributes.FOLLOW_RANGE, 16.0f);
    }

    public static boolean checkAnimalSpawnRules(EntityType<? extends Animal> type, LevelAccessor world, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
        return world.getBlockState(pos.below()).is(ModBlockTags.BOOPLETS_SPAWN_ON);
    }

    public boolean canBeHappy() {
        return this.hurtTime <= 0 && this.random.nextInt(20) == 0 && this.happyDanceTimer <= 0 && !this.isInWaterOrRain();
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
        return null;
    }

    public int getFluffTimer() {
        return this.fluffTimer;
    }

    public void setFluffTimer(int fluffTimer) {
        this.fluffTimer = fluffTimer;
    }

    public int getTimeSinceBoop() {
        return this.timeSinceBoop;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.is(Items.SHEARS)) {
            if (this.level() instanceof ServerLevel serverLevel && this.isFluffy()) {
                this.shear(serverLevel, SoundSource.PLAYERS, itemStack);
                this.gameEvent(GameEvent.SHEAR, player);
                itemStack.hurtAndBreak(1, player, hand.asEquipmentSlot());
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.CONSUME;
            }
        } else if (player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
            player.swing(InteractionHand.MAIN_HAND, SwingAnimation.DEFAULT, false);

            if (!player.level().isClientSide()) {
                this.playSound(ModSoundEvents.ENTITY_BOOPLET_SQUEAK.get(), this.getSoundVolume() / 2, this.getVoicePitch());

                if (this.level() instanceof ServerLevel) {
                    ModUtil.grantImpossibleAdvancement("bestiary/boop", player);
                }
                boolean happy = this.canBeHappy();
                if (happy) {
                    this.happyDanceTimer = 600;
                    this.getLookControl().setLookAt(player);
                }
                int nextAction = (this.entityData.get(BOOP_ACTION) / 2 + 1) * 2;
                this.entityData.set(BOOP_ACTION, nextAction + (happy ? 1 : 0));
                this.timeSinceBoop = 0;
                this.getNavigation().stop();
            }
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    public boolean isFluffy() {
        return this.entityData.get(IS_FLUFFY);
    }

    @Override
    public boolean readyForShearing() {
        return isFluffy();
    }

    public void setIsFluffy(boolean fluffy) {
        this.entityData.set(IS_FLUFFY, fluffy);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> data) {
        super.onSyncedDataUpdated(data);
        if (BOOP_ACTION.equals(data) && this.level().isClientSide()) {
            this.isHappy = (this.entityData.get(BOOP_ACTION) & 1) != 0;
            this.isBooped = !this.isHappy;
            this.timeSinceBoop = 0;
        }
    }

    @Override
    public void shear(ServerLevel serverLevel, SoundSource shearedSoundCategory, ItemStack tool) {
        serverLevel.playSound(null, this, SoundEvents.SHEEP_SHEAR, shearedSoundCategory, 1.0F, 1.25F);
        this.setIsFluffy(false);
        this.setFluffTimer(0);
        int droppedAmount = 1 + this.random.nextInt(3);

        for (int j = 0; j < droppedAmount; ++j) {
            ItemEntity itemEntity = this.spawnAtLocation(serverLevel, new ItemStack(Items.STRING), 0);
            if (itemEntity != null) {
                itemEntity.setDeltaMovement(itemEntity.getDeltaMovement().add(((this.random.nextFloat() - this.random.nextFloat()) * 0.1F), (this.random.nextFloat() * 0.05F), ((this.random.nextFloat() - this.random.nextFloat()) * 0.1F)));
            }
        }

    }

    @Override
    public void tick() {
        super.tick();
        Level world = this.level();
        if (timeSinceBoop <= 10) timeSinceBoop++;
        if (happyDanceTimer > 0) happyDanceTimer--;

        if (this.happyAnimationTimer > 0) this.getNavigation().stop();

        if (world.isClientSide()) {
            this.setupAnimationStates();
            if (this.isBooped && !this.isHappy) {
                this.stopAndOrRestartBoopAnimation(true);
            } else if (this.isHappy) {
                this.stopAndOrRestartBoopAnimation(false);
                this.happyDanceTimer = 600;
                this.happyAnimationState.start(this.tickCount);
                this.happyAnimationTimer = 80;
                this.isHappy = false;
            }
        } else if (!this.isFluffy()) {
            int fluffTimer = Math.min(5000, this.getFluffTimer() + 1);
            this.setFluffTimer(fluffTimer);
            this.setIsFluffy(fluffTimer >= 5000);
        }
    }

    protected void updateWalkAnimation(float v) {
        float f;
        if (this.getPose() == Pose.STANDING) {
            f = Math.min(v * 6.0F, 1.0F);
        } else {
            f = 0.0F;
        }

        this.walkAnimation.update(f * 2.5f, 0.2F, 1.0F);
    }

    private void setupAnimationStates() {
        if (happyAnimationTimer > 0) happyAnimationTimer--;
        if (happyAnimationTimer <= 0 || this.hurtTime > 0) this.happyAnimationState.stop();

        if (swimAnimationTimeout <= 0 && this.isInWater()) {
            this.swimAnimationTimeout = 20;
            this.swimAnimationState.start(this.tickCount);
        } else {
            --this.swimAnimationTimeout;
        }
    }

    private void stopAndOrRestartBoopAnimation(boolean restart) {
        this.boopAnimationState.stop();
        if (restart) {
            this.boopAnimationState.start(this.tickCount);
            this.isBooped = false;
        }
    }
}
