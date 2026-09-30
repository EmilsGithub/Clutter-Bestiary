package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.BeaverEntity;
import net.emilsg.clutterbestiary.util.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;

public class BeaverStripBottomLogGoal extends MoveToBlockGoal {
    private final BeaverEntity beaverEntity;
    private Block targetBlock;
    private Block strippedTargetBlock;
    private BlockState strippedTargetState;

    public BeaverStripBottomLogGoal(BeaverEntity beaverEntity, double speed) {
        super(beaverEntity, speed, 8);
        this.beaverEntity = beaverEntity;
    }

    @Override
    public boolean canUse() {
        return this.mob.getRandom().nextInt(100) == 0 && super.canUse();
    }

    @Override
    public double acceptedDistance() {
        return 2.0f;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isReachedTarget()) {
            Level world = this.mob.level();


            if (targetBlock != null && strippedTargetBlock != null && strippedTargetState != null) {
                this.mob.playSound(SoundEvents.WOOD_BREAK, 1.0f, this.mob.getVoicePitch());
                if (world instanceof ServerLevel serverWorld && serverWorld.setBlock(blockPos, strippedTargetState, Block.UPDATE_ALL)) {
                    this.beaverEntity.onWorldLogStripped();
                }
                this.stop();
            }
        }
    }

    @Override
    protected boolean isValidTarget(LevelReader world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);

        boolean isStrippableBlock = !(String.valueOf(state.getBlock()).contains("stripped") || state.is(ModBlockTags.STRIPPED_LOGS) || state.is(ModBlockTags.STRIPPED_WOODS)) && (state.is(BlockTags.LOGS) || state.is(ModBlockTags.WOODS));

        if (world.getBlockState(pos.below()).is(BlockTags.LOGS) || !isStrippableBlock) return false;

        Block block = state.getBlock();
        if (block == null) return false;

        targetBlock = state.getBlock();
        Identifier blockID = BuiltInRegistries.BLOCK.getKey(block);
        Identifier strippedID = Identifier.fromNamespaceAndPath(blockID.getNamespace(), "stripped_" + blockID.getPath());
        if (!BuiltInRegistries.BLOCK.containsKey(strippedID)) return false;

        targetBlock = state.getBlock();
        strippedTargetBlock = BuiltInRegistries.BLOCK.getValue(strippedID);

        strippedTargetState = strippedTargetBlock.defaultBlockState();
        if (targetBlock instanceof RotatedPillarBlock) {
            strippedTargetState = strippedTargetState.setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS));
        }

        return world.getBlockState(pos).is(BlockTags.LOGS);
    }
}
