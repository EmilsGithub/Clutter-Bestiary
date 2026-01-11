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
    private final ButterflyEntity butterflyEntity;
    private final int dupeCooldown;

    public ButterflyDupeSporeBlossomGoal(ButterflyEntity butterflyEntity, double speed, int dupeCooldown) {
        super(butterflyEntity, speed, 12);
        this.butterflyEntity = butterflyEntity;
        this.dupeCooldown = dupeCooldown;
    }

    @Override
    public boolean canStart() {
        return this.butterflyEntity.getDupeTimer() >= this.dupeCooldown && super.canStart();
    }

    @Override
    public double getDesiredDistanceToTarget() {
        return 1.5F;
    }

    @Override
    public boolean shouldContinue() {
        return super.shouldContinue() && this.butterflyEntity.getDupeTimer() >= this.dupeCooldown;
    }

    @Override
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

    @Override
    protected boolean isTargetPos(WorldView world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return state.isOf(Blocks.SPORE_BLOSSOM);
    }
}
