package net.emilsg.clutterbestiary.entity.custom.parent;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.animal.fish.AbstractFish;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public abstract class ParentFishEntity extends AbstractFish {
    private static final EntityDataAccessor<Boolean> MOVING = SynchedEntityData.defineId(ParentFishEntity.class, EntityDataSerializers.BOOLEAN);

    protected ParentFishEntity(EntityType<? extends AbstractFish> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(MOVING, false);
    }

    @Override
    public abstract ItemStack getBucketItemStack();

    public boolean getHasSelfControl() {
        return canRandomSwim();
    }

    public boolean isMoving() {
        return this.entityData.get(MOVING);
    }

    public void setMoving(boolean moving) {
        this.entityData.set(MOVING, moving);
    }

    @Override
    public void aiStep() {
        if (!this.level().isClientSide()) {
            Vec3 velocity = this.getDeltaMovement();
            boolean isMoving = velocity.lengthSqr() > 0.0005f;
            this.setMoving(isMoving);
        }
        super.aiStep();
    }

    @Override
    public void travel(Vec3 movementInput) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(0.01F, movementInput);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.7));
            if (this.getTarget() == null) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.005, 0.0));
            }
        } else {
            super.travel(movementInput);
        }

    }

    @Override
    protected abstract SoundEvent getFlopSound();

}
