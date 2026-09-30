package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.MantaRayEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.JumpGoal;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;

public class MantaRayJumpGoal extends JumpGoal {
    private static final int[] OFFSET_MULTIPLIERS = new int[]{0, 1, 4, 5, 6, 7};
    private final MantaRayEntity mantaRay;
    private final int chance;
    private boolean inWater;

    public MantaRayJumpGoal(MantaRayEntity mantaRay, int chance) {
        this.mantaRay = mantaRay;
        this.chance = reducedTickDelay(chance);
    }

    public boolean canUse() {
        if (this.mantaRay.getRandom().nextInt(this.chance) != 0) {
            return false;
        } else {
            Direction direction = this.mantaRay.getMotionDirection();
            int i = direction.getStepX();
            int j = direction.getStepZ();
            BlockPos blockPos = this.mantaRay.blockPosition();
            int[] var5 = OFFSET_MULTIPLIERS;
            int var6 = var5.length;

            for (int var7 = 0; var7 < var6; ++var7) {
                int k = var5[var7];
                if (!this.isWater(blockPos, i, j, k) || !this.isAirAbove(blockPos, i, j, k)) {
                    return false;
                }
            }

            return true;
        }
    }

    public boolean isInterruptable() {
        return false;
    }

    public boolean canContinueToUse() {
        double d = this.mantaRay.getDeltaMovement().y;
        return (!(d * d < 0.029999999329447746) || this.mantaRay.getXRot() == 0.0F || !(Math.abs(this.mantaRay.getXRot()) < 10.0F) || !this.mantaRay.isInWater()) && !this.mantaRay.onGround();
    }

    public void start() {
        this.inWater = false;
        Direction direction = this.mantaRay.getMotionDirection();
        this.mantaRay.setDeltaMovement(this.mantaRay.getDeltaMovement().add((double) direction.getStepX() * 0.6, 0.7, (double) direction.getStepZ() * 0.6));
        this.mantaRay.getNavigation().stop();
    }

    public void stop() {
        this.mantaRay.setXRot(0.0F);
    }

    public void tick() {
        boolean bl = this.inWater;
        if (!bl) {
            FluidState fluidState = this.mantaRay.level().getFluidState(this.mantaRay.blockPosition());
            this.inWater = fluidState.is(FluidTags.WATER);
        }

        if (this.inWater && !bl) {
            this.mantaRay.playSound(SoundEvents.DOLPHIN_JUMP, 1.0F, 1.0F);
        }

        Vec3 vec3d = this.mantaRay.getDeltaMovement();
        if (vec3d.y * vec3d.y < 0.029999999329447746 && this.mantaRay.getXRot() != 0.0F) {
            this.mantaRay.setXRot(Mth.rotLerp(0.2F, this.mantaRay.getXRot(), 0.0F));
        } else if (vec3d.length() > 9.999999747378752E-6) {
            double d = vec3d.horizontalDistance();
            double e = Math.atan2(-vec3d.y, d) * 57.2957763671875;
            this.mantaRay.setXRot((float) e);
        }

    }

    private boolean isAirAbove(BlockPos pos, int offsetX, int offsetZ, int multiplier) {
        return this.mantaRay.level().getBlockState(pos.offset(offsetX * multiplier, 1, offsetZ * multiplier)).isAir() && this.mantaRay.level().getBlockState(pos.offset(offsetX * multiplier, 2, offsetZ * multiplier)).isAir();
    }

    private boolean isWater(BlockPos pos, int offsetX, int offsetZ, int multiplier) {
        BlockPos blockPos = pos.offset(offsetX * multiplier, 0, offsetZ * multiplier);
        return this.mantaRay.level().getFluidState(blockPos).is(FluidTags.WATER) && !this.mantaRay.level().getBlockState(blockPos).isSolid();
    }
}
