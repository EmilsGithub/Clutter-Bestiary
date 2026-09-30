package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.ButterflyEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class ButterflyDupeSporeBlossomGoal extends MoveToBlockGoal {
    private final ButterflyEntity butterflyEntity;
    private final int dupeCooldown;

    public ButterflyDupeSporeBlossomGoal(ButterflyEntity butterflyEntity, double speed, int dupeCooldown) {
        super(butterflyEntity, speed, 12);
        this.butterflyEntity = butterflyEntity;
        this.dupeCooldown = dupeCooldown;
    }

    @Override
    public boolean canUse() {
        return this.butterflyEntity.getDupeTimer() >= this.dupeCooldown && super.canUse();
    }

    @Override
    public double acceptedDistance() {
        return 1.5F;
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && this.butterflyEntity.getDupeTimer() >= this.dupeCooldown;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isReachedTarget()) {
            if (!this.butterflyEntity.level().isClientSide()) {
                this.butterflyEntity.setDupeTimer(0);
                this.butterflyEntity.spawnAtLocation(getServerLevel(this.butterflyEntity), new ItemStack(Items.SPORE_BLOSSOM));
            }
            this.stop();
        }
    }

    @Override
    protected boolean isValidTarget(LevelReader world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return state.is(Blocks.SPORE_BLOSSOM);
    }
}
