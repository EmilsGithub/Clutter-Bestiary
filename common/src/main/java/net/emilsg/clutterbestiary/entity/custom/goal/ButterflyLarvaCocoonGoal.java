package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.block.ModBlocks;
import net.emilsg.clutterbestiary.block.custom.ButterflyCocoonBlock;
import net.emilsg.clutterbestiary.block.entity.ButterflyCocoonBlockEntity;
import net.emilsg.clutterbestiary.entity.custom.ButterflyLarvaEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class ButterflyLarvaCocoonGoal extends Goal {
    private static final int COCOON_SITE_SEARCH_ATTEMPTS = 64;
    private static final int COCOON_SEARCH_COOLDOWN_TICKS = 100;
    private static final int MAX_CLIMB_HEIGHT = 10;
    private static final int MAX_CLIMB_DURATION_TICKS = MAX_CLIMB_HEIGHT * 40;
    private static final double CLIMB_SPEED = 0.05;
    private static final double CLIMB_ALIGNMENT_SPEED = 0.04;
    private static final double CLIMB_ALIGNMENT_DISTANCE = 0.05;
    private static final double TRUNK_ALIGNMENT_OFFSET = 0.32;

    private final ButterflyLarvaEntity larva;
    @Nullable private BlockPos climbStartPos;
    @Nullable private BlockPos cocoonPlacementPos;
    @Nullable private Path pathToClimbStart;
    @Nullable private Direction directionToTrunk;
    private int searchCooldownTicks;
    private int climbingTimeTicks;
    private boolean hasStartedClimbing;

    public ButterflyLarvaCocoonGoal(ButterflyLarvaEntity larva) {
        this.larva = larva;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.larva.getLifeTicks() < ButterflyLarvaEntity.WANDER_TICKS || this.larva.isInLiquid()) return false;
        if (this.searchCooldownTicks > 0) {
            this.searchCooldownTicks--;
            return false;
        }

        this.searchCooldownTicks = COCOON_SEARCH_COOLDOWN_TICKS;
        return this.findCocoonSite();
    }

    @Override
    public boolean canContinueToUse() {
        if (this.climbStartPos == null || this.cocoonPlacementPos == null || this.larva.isInLiquid()) return false;
        if (!this.isCocoonSiteValid(this.cocoonPlacementPos, this.climbStartPos)) return false;
        return this.hasStartedClimbing || !this.larva.getNavigation().isDone()
                || this.larva.distanceToSqr(Vec3.atBottomCenterOf(this.climbStartPos)) <= 1.0;
    }

    @Override
    public void start() {
        this.climbingTimeTicks = 0;
        this.hasStartedClimbing = false;
        this.larva.setClimbing(false);
        if (this.pathToClimbStart != null) this.larva.getNavigation().moveTo(this.pathToClimbStart, 0.8);
    }

    @Override
    public void stop() {
        this.larva.getNavigation().stop();
        this.climbStartPos = null;
        this.cocoonPlacementPos = null;
        this.pathToClimbStart = null;
        this.directionToTrunk = null;
        this.hasStartedClimbing = false;
        this.larva.setClimbing(false);
        this.searchCooldownTicks = COCOON_SEARCH_COOLDOWN_TICKS;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (this.climbStartPos == null || this.cocoonPlacementPos == null || this.directionToTrunk == null) return;

        Vec3 climbStartCenter = Vec3.atBottomCenterOf(this.climbStartPos);
        if (!this.hasStartedClimbing && this.larva.distanceToSqr(climbStartCenter) > 1.0) return;

        if (++this.climbingTimeTicks > MAX_CLIMB_DURATION_TICKS) {
            this.stop();
            return;
        }

        if (!this.hasStartedClimbing && !this.prepareToClimb()) return;

        if (this.larva.getY() >= this.cocoonPlacementPos.getY() - 0.15) {
            this.placeCocoon();
            return;
        }

        Vec3 climbingPosition = this.getClimbingPos(this.cocoonPlacementPos);
        double movementX = this.getAlignmentSpeed(climbingPosition.x - this.larva.getX());
        double movementZ = this.getAlignmentSpeed(climbingPosition.z - this.larva.getZ());
        this.faceTrunk();
        this.larva.setDeltaMovement(movementX, CLIMB_SPEED, movementZ);
        this.larva.fallDistance = 0.0F;
    }

    private boolean prepareToClimb() {
        this.larva.getNavigation().stop();
        this.faceTrunk();

        Vec3 climbingPosition = this.getClimbingPos(this.climbStartPos);
        double distanceX = climbingPosition.x - this.larva.getX();
        double distanceZ = climbingPosition.z - this.larva.getZ();
        double horizontalDistanceSquared = distanceX * distanceX + distanceZ * distanceZ;
        if (horizontalDistanceSquared > CLIMB_ALIGNMENT_DISTANCE * CLIMB_ALIGNMENT_DISTANCE || !this.larva.onGround()) {
            this.larva.setDeltaMovement(
                    this.getAlignmentSpeed(distanceX),
                    this.larva.getDeltaMovement().y,
                    this.getAlignmentSpeed(distanceZ)
            );
            return false;
        }

        this.hasStartedClimbing = true;
        this.larva.setClimbing(true);
        return true;
    }

    private Vec3 getClimbingPos(BlockPos pos) {
        return Vec3.atBottomCenterOf(pos).add(
                this.directionToTrunk.getStepX() * TRUNK_ALIGNMENT_OFFSET,
                0.0,
                this.directionToTrunk.getStepZ() * TRUNK_ALIGNMENT_OFFSET
        );
    }

    private double getAlignmentSpeed(double distance) {
        return Math.clamp(distance * 0.25, -CLIMB_ALIGNMENT_SPEED, CLIMB_ALIGNMENT_SPEED);
    }

    private void faceTrunk() {
        float trunkYaw = this.directionToTrunk.toYRot();
        this.larva.setYRot(trunkYaw);
        this.larva.yBodyRot = trunkYaw;
        this.larva.yHeadRot = trunkYaw;
    }

    private boolean findCocoonSite() {
        if (!(this.larva.level() instanceof ServerLevel serverWorld)) return false;

        BlockPos homePos = this.larva.getHomePos();
        BlockState cocoonState = this.getCocoonState();
        for (int attempt = 0; attempt < COCOON_SITE_SEARCH_ATTEMPTS; attempt++) {
            int searchX = homePos.getX() + this.larva.getRandom().nextInt(ButterflyLarvaEntity.HOME_RADIUS * 2 + 1) - ButterflyLarvaEntity.HOME_RADIUS;
            int searchZ = homePos.getZ() + this.larva.getRandom().nextInt(ButterflyLarvaEntity.HOME_RADIUS * 2 + 1) - ButterflyLarvaEntity.HOME_RADIUS;
            BlockPos searchColumn = new BlockPos(searchX, homePos.getY(), searchZ);
            if (homePos.distSqr(searchColumn) > ButterflyLarvaEntity.HOME_RADIUS * ButterflyLarvaEntity.HOME_RADIUS) continue;

            for (int searchY = homePos.getY() - 4; searchY <= homePos.getY() + MAX_CLIMB_HEIGHT; searchY++) {
                BlockPos trunkPos = new BlockPos(searchX, searchY, searchZ);
                if (!serverWorld.areEntitiesLoaded(ChunkPos.pack(trunkPos))
                        || !this.isTrunk(serverWorld.getBlockState(trunkPos))) continue;

                for (Direction sideOfTrunk : Direction.Plane.HORIZONTAL) {
                    BlockPos possibleCocoonPos = trunkPos.relative(sideOfTrunk);
                    if (!this.canPlaceCocoonAt(serverWorld, possibleCocoonPos, cocoonState)) continue;

                    BlockPos possibleClimbStart = this.findClimbStart(serverWorld, trunkPos, sideOfTrunk, possibleCocoonPos);
                    if (possibleClimbStart == null) continue;

                    Path possiblePath = this.larva.getNavigation().createPath(possibleClimbStart, 0);
                    if (possiblePath == null || !possiblePath.canReach()) continue;

                    this.climbStartPos = possibleClimbStart;
                    this.cocoonPlacementPos = possibleCocoonPos;
                    this.pathToClimbStart = possiblePath;
                    this.directionToTrunk = sideOfTrunk.getOpposite();
                    return true;
                }
            }
        }
        return false;
    }

    @Nullable
    private BlockPos findClimbStart(ServerLevel serverWorld, BlockPos trunkPos, Direction sideOfTrunk, BlockPos cocoonPos) {
        BlockPos trunkBase = trunkPos;
        for (int depth = 0; depth < MAX_CLIMB_HEIGHT && this.isTrunk(serverWorld.getBlockState(trunkBase.below())); depth++) {
            trunkBase = trunkBase.below();
        }

        BlockPos possibleClimbStart = trunkBase.relative(sideOfTrunk);
        if (!serverWorld.areEntitiesLoaded(ChunkPos.pack(possibleClimbStart))) return null;
        if (!serverWorld.getBlockState(possibleClimbStart.below()).isRedstoneConductor(serverWorld, possibleClimbStart.below())) return null;
        if (cocoonPos.getY() - possibleClimbStart.getY() < 2) return null;

        for (int climbY = possibleClimbStart.getY(); climbY <= cocoonPos.getY(); climbY++) {
            BlockPos climbingSpace = new BlockPos(possibleClimbStart.getX(), climbY, possibleClimbStart.getZ());
            BlockPos supportingTrunk = climbingSpace.relative(sideOfTrunk.getOpposite());
            if (!serverWorld.getBlockState(climbingSpace).isAir()
                    || !this.isTrunk(serverWorld.getBlockState(supportingTrunk))) return null;
        }
        return possibleClimbStart;
    }

    private boolean isCocoonSiteValid(BlockPos cocoonPos, BlockPos climbStartPos) {
        if (!(this.larva.level() instanceof ServerLevel serverWorld) || this.directionToTrunk == null) return false;
        if (!this.canPlaceCocoonAt(serverWorld, cocoonPos, this.getCocoonState())) return false;

        for (int climbY = climbStartPos.getY(); climbY <= cocoonPos.getY(); climbY++) {
            BlockPos climbingSpace = new BlockPos(climbStartPos.getX(), climbY, climbStartPos.getZ());
            BlockPos supportingTrunk = climbingSpace.relative(this.directionToTrunk);
            if (!serverWorld.getBlockState(climbingSpace).isAir()
                    || !this.isTrunk(serverWorld.getBlockState(supportingTrunk))) return false;
        }
        return true;
    }

    private boolean canPlaceCocoonAt(ServerLevel serverWorld, BlockPos pos, BlockState cocoonState) {
        return serverWorld.areEntitiesLoaded(ChunkPos.pack(pos))
                && serverWorld.getBlockState(pos).canBeReplaced()
                && serverWorld.getFluidState(pos).isEmpty()
                && cocoonState.canSurvive(serverWorld, pos);
    }

    private void placeCocoon() {
        if (this.cocoonPlacementPos == null || this.climbStartPos == null
                || !(this.larva.level() instanceof ServerLevel serverWorld)) return;
        if (!this.isCocoonSiteValid(this.cocoonPlacementPos, this.climbStartPos)) return;

        BlockState cocoonState = this.getCocoonState();

        if (serverWorld.setBlock(this.cocoonPlacementPos, cocoonState, Block.UPDATE_ALL)) {
            if (serverWorld.getBlockEntity(this.cocoonPlacementPos) instanceof ButterflyCocoonBlockEntity cocoonBlockEntity) {
                cocoonBlockEntity.setParentVariant(this.larva.getVariant());
            }
            serverWorld.gameEvent(
                    GameEvent.BLOCK_PLACE,
                    this.cocoonPlacementPos,
                    GameEvent.Context.of(this.larva, cocoonState)
            );
            this.larva.discard();
        }
    }

    private boolean isTrunk(BlockState state) {
        return state.is(BlockTags.LOGS) || state.is(Blocks.CRIMSON_STEM) || state.is(Blocks.WARPED_STEM)
                || state.is(Blocks.STRIPPED_CRIMSON_STEM) || state.is(Blocks.STRIPPED_WARPED_STEM);
    }

    private BlockState getCocoonState() {
        return ModBlocks.BUTTERFLY_COCOON.get().defaultBlockState().setValue(ButterflyCocoonBlock.CAN_HATCH, true);
    }
}
