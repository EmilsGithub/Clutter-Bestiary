package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.util.ModBlockTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.entity.ai.goal.MoveToTargetPosGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class BeaverStripBottomLogGoal extends MoveToTargetPosGoal {
    private Block targetBlock;
    private Block strippedTargetBlock;
    private BlockState strippedTargetState;

    public BeaverStripBottomLogGoal(PathAwareEntity mob, double speed) {
        super(mob, speed, 8);
    }

    @Override
    public boolean canStart() {
        return super.canStart() && this.mob.getRandom().nextInt(100) == 0;
    }

    @Override
    public double getDesiredDistanceToTarget() {
        return 2.0f;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.hasReached()) {
            World world = this.mob.getWorld();


            if (targetBlock != null && strippedTargetBlock != null && strippedTargetState != null) {
                this.mob.playSound(SoundEvents.BLOCK_WOOD_BREAK, 1.0f, this.mob.getSoundPitch());
                if (world instanceof ServerWorld) world.setBlockState(targetPos, strippedTargetState, Block.NOTIFY_ALL);
                this.stop();
            }
        }
    }

    @Override
    protected boolean isTargetPos(WorldView world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);

        boolean isStrippableBlock = !(String.valueOf(state.getBlock()).contains("stripped") || state.isIn(ModBlockTags.STRIPPED_LOGS) || state.isIn(ModBlockTags.STRIPPED_WOODS)) && (state.isIn(BlockTags.LOGS) || state.isIn(ModBlockTags.WOODS));

        if(world.getBlockState(pos.down()).isIn(BlockTags.LOGS) || !isStrippableBlock) return false;

        Block block = state.getBlock();
        if (block == null) return false;

        targetBlock = state.getBlock();
        String raw = String.valueOf(block)
                .replace("Block{", "")
                .replace("}", "");

        Identifier blockID = Identifier.tryParse(raw);
        if (blockID == null) return false;

        Identifier strippedID = new Identifier(blockID.toString().replace(":", ":stripped_"));
        if(!Registries.BLOCK.containsId(strippedID)) return false;

        targetBlock = state.getBlock();
        strippedTargetBlock = Registries.BLOCK.get(strippedID);

        strippedTargetState = strippedTargetBlock.getDefaultState();
        if (targetBlock instanceof PillarBlock) {
            strippedTargetState = strippedTargetState.with(PillarBlock.AXIS, state.get(PillarBlock.AXIS));
        }

        return world.getBlockState(pos).isIn(BlockTags.LOGS);
    }
}
