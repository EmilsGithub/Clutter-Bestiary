package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.ButterflyEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.goal.MoveToTargetPosGoal;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

public class ButterflyDupeSporeBlossomGoal extends MoveToTargetPosGoal {
    ButterflyEntity butterflyEntity;
    int dupeCooldown;

    public ButterflyDupeSporeBlossomGoal(ButterflyEntity butterflyEntity, double speed, int dupeCooldown) {
        super(butterflyEntity, speed, 12);
        this.butterflyEntity = butterflyEntity;
        this.dupeCooldown = dupeCooldown;
    }

    public boolean canStart() {
        return this.butterflyEntity.getDupeTimer() >= dupeCooldown && super.canStart();
    }

    @Override
    public double getDesiredDistanceToTarget() {
        return 1.5F;
    }

    public boolean shouldContinue() {
        return super.shouldContinue() && this.butterflyEntity.getDupeTimer() >= dupeCooldown;
    }

    public void tick() {
        super.tick();
        if (this.hasReached()) {
            if (!this.butterflyEntity.getWorld().isClient) {
                this.butterflyEntity.setDupeTimer(0);
                this.butterflyEntity.dropStack(new ItemStack(Items.SPORE_BLOSSOM));
            }
            this.stop();
        }
    }

    protected boolean isTargetPos(WorldView world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return state.isOf(Blocks.SPORE_BLOSSOM);
    }
}
