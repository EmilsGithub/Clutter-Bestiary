package net.emilsg.clutterbestiary.mixin;

import net.emilsg.clutterbestiary.entity.custom.CrocodileEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(BoatEntity.class)
public abstract class BoatEntityMixin {
    @Unique
    private static final float CROCODILE_SPEED_MULTIPLIER = 1.25f;
    @Unique
    private static final double CROCODILE_RANGE = 32.0;
    @Unique
    private static final int CROCODILE_CHECK_INTERVAL_TICKS = 10;
    @Unique
    private LivingEntity clutterbestiary$cachedOwner;
    @Unique
    private boolean clutterbestiary$hasFollowingCrocodile;
    @Unique
    private int clutterbestiary$crocodileCheckCooldown;

    @ModifyConstant(method = "updatePaddles", constant = @Constant(floatValue = 0.04f))
    private float clutterbestiary$boostBoatWithFollowingCrocodile(float originalSpeed) {
        BoatEntity boat = (BoatEntity) (Object) this;
        LivingEntity owner = boat.getControllingPassenger();
        if (owner == null) {
            this.clutterbestiary$cachedOwner = null;
            this.clutterbestiary$hasFollowingCrocodile = false;
            this.clutterbestiary$crocodileCheckCooldown = 0;
            return originalSpeed;
        }

        if (owner != this.clutterbestiary$cachedOwner || --this.clutterbestiary$crocodileCheckCooldown <= 0) {
            this.clutterbestiary$cachedOwner = owner;
            this.clutterbestiary$crocodileCheckCooldown = CROCODILE_CHECK_INTERVAL_TICKS;
            this.clutterbestiary$hasFollowingCrocodile = !boat.getWorld().getEntitiesByClass(CrocodileEntity.class,
                    boat.getBoundingBox().expand(CROCODILE_RANGE),
                    crocodile -> crocodile.isFollowingOwner() && crocodile.isOwner(owner)).isEmpty();
        }

        return this.clutterbestiary$hasFollowingCrocodile ? originalSpeed * CROCODILE_SPEED_MULTIPLIER : originalSpeed;
    }
}
