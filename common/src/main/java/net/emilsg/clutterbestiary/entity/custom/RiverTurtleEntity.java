package net.emilsg.clutterbestiary.entity.custom;

import io.netty.buffer.ByteBuf;
import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.ModTrackedDataHandler;
import net.emilsg.clutterbestiary.entity.custom.goal.RiverTurtleHideGoal;
import net.emilsg.clutterbestiary.entity.custom.goal.WanderAroundFarOftenGoal;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentAnimalEntity;
import net.emilsg.clutterbestiary.entity.variants.RiverTurtleVariant;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.function.ValueLists;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.function.IntFunction;

public class RiverTurtleEntity extends ParentAnimalEntity {
    private static final TrackedData<String> VARIANT = DataTracker.registerData(RiverTurtleEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<RiverTurtleAnimationState> ANIMATION_STATE = DataTracker.registerData(RiverTurtleEntity.class, ModTrackedDataHandler.RIVER_TURTLE_ANIMATION_STATE);
    private static final TrackedData<Boolean> HIDING = DataTracker.registerData(RiverTurtleEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public final AnimationState hidingAnimationState = new AnimationState();
    public final AnimationState unhidingAnimationState = new AnimationState();

    private int healthTicker = 0;
    private boolean canHealPassively = false;

    public RiverTurtleEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new RiverTurtleHideGoal(this));
        this.goalSelector.add(2, new WanderAroundFarOftenGoal(this, 1.0f));
        this.goalSelector.add(3, new LookAroundGoal(this));
        this.goalSelector.add(4, new LookAtEntityGoal(this, PlayerEntity.class, 6f));
    }

    @Override
    public void tick() {
        if (this.getHealth() < this.getMaxHealth()) {
            if (this.getHealth() <= (this.getMaxHealth() / 2)) {
                this.setIsHiding(true);
                canHealPassively = true;
            }

            if (canHealPassively) {
                healthTicker++;

                if (healthTicker >= 150 + random.nextInt(100)) {
                    this.heal(1);
                    healthTicker = 0;
                }
            }
        } else {
            canHealPassively = false;
            this.setIsHiding(false);
        }

        super.tick();
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        this.setVariant(RiverTurtleVariant.getRandom());
        return super.initialize(world, difficulty, spawnReason, entityData);
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 12D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.175F);
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        RiverTurtleEntity child = ModEntityTypes.RIVER_TURTLE.get().create(world);
        if (child != null) child.setVariant(RiverTurtleVariant.getRandom());
        return child;
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isOf(Items.APPLE);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(ANIMATION_STATE, RiverTurtleAnimationState.IDLING);
        builder.add(HIDING, false);
        builder.add(VARIANT, RiverTurtleVariant.SANDY.getId());
    }

    public String getTypeVariant() {
        return this.dataTracker.get(VARIANT);
    }

    public RiverTurtleVariant getVariant() {
        return RiverTurtleVariant.fromId(this.getTypeVariant());
    }

    public void setVariant(RiverTurtleVariant variant) {
        this.dataTracker.set(VARIANT, variant.getId());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dataTracker.set(VARIANT, nbt.getString("Variant"));
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putString("Variant", this.getTypeVariant());
    }

    protected void updateLimbs(float v) {
        float f;
        if (this.getPose() == EntityPose.STANDING) {
            f = Math.min(v * 6.0F, 1.0F);
        } else {
            f = 0.0F;
        }

        this.limbAnimator.updateLimbs(f * 1.15f, 0.7F);
    }

    public boolean isHiding() {
        return this.dataTracker.get(HIDING);
    }

    public void setIsHiding(boolean isHiding) {
        this.dataTracker.set(HIDING, isHiding);
    }

    //Animation Handling

    private RiverTurtleAnimationState getState() {
        return this.dataTracker.get(ANIMATION_STATE);
    }

    private void setState(RiverTurtleAnimationState state) {
        this.dataTracker.set(ANIMATION_STATE, state);
    }

    public void onTrackedDataSet(TrackedData<?> data) {
        if (ANIMATION_STATE.equals(data)) {
            RiverTurtleAnimationState state = this.getState();
            this.stopAnimations();
            switch (state.ordinal()) {
                case 1:
                    this.hidingAnimationState.startIfNotRunning(this.age);
                    break;
                case 2:
                    this.unhidingAnimationState.startIfNotRunning(this.age);
                    break;
                default:
                    break;
            }

            this.calculateDimensions();
        }

        super.onTrackedDataSet(data);
    }

    private void stopAnimations() {
        this.hidingAnimationState.stop();
        this.unhidingAnimationState.stop();
    }

    public void startState(RiverTurtleAnimationState state) {
        switch (state.ordinal()) {
            case 0:
                this.setState(RiverTurtleAnimationState.IDLING);
                break;
            case 1:
                this.setState(RiverTurtleAnimationState.HIDING);
                break;
            case 2:
                this.setState(RiverTurtleAnimationState.UNHIDING);
                break;
        }
    }

    public static enum RiverTurtleAnimationState {
        IDLING(0),
        HIDING(1),
        UNHIDING(2);

        public static final IntFunction<RiverTurtleAnimationState> INDEX_TO_VALUE = ValueLists.createIdToValueFunction(RiverTurtleAnimationState::getIndex, values(), ValueLists.OutOfBoundsHandling.ZERO);
        public static final PacketCodec<ByteBuf, RiverTurtleAnimationState> PACKET_CODEC = PacketCodecs.indexed(INDEX_TO_VALUE, RiverTurtleAnimationState::getIndex);
        private final int index;

        private RiverTurtleAnimationState(final int index) {
            this.index = index;
        }

        public int getIndex() {
            return this.index;
        }
    }

    public void onDeath(DamageSource damageSource) {
        this.startState(RiverTurtleAnimationState.IDLING);
        super.onDeath(damageSource);
    }
}
