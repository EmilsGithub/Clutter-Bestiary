package net.emilsg.clutterbestiary.entity.custom.goal;
import net.minecraft.world.entity.EntitySpawnReason;

import net.emilsg.clutterbestiary.animation_handling.animation_states.CoatiEntityAnimationState;
import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.custom.CoatiEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import java.util.EnumSet;

public class CoatiDigGoal extends Goal {
    private final CoatiEntity coati;
    private int diggingTimer;

    public CoatiDigGoal(CoatiEntity coati) {
        this.coati = coati;
        this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return !this.coati.isBaby() && this.coati.isDigging() && this.coati.onGround() && !this.coati.isInWater() && this.coati.getBurrowPos().closerToCenterThan(this.coati.position(), 2.0);
    }

    @Override
    public boolean canContinueToUse() {
        return canUse();
    }

    @Override
    public void start() {
        this.coati.getNavigation().stop();
        this.coati.startState(CoatiEntityAnimationState.DIGGING);
        diggingTimer = this.adjustedTickDelay(CoatiEntity.DIG_DURATION_TICKS);
    }

    @Override
    public void stop() {
        this.coati.setDigging(false);
        this.coati.setDigSessionActive(false);
        if (this.coati.getAnimationController().getState() == CoatiEntityAnimationState.DIGGING) {
            this.coati.startState(CoatiEntityAnimationState.IDLING);
        }
    }

    @Override
    public void tick() {
        if (--diggingTimer > 0) return;

        this.coati.setDigging(false);
        this.coati.startState(CoatiEntityAnimationState.IDLING);

        if (this.coati.level() instanceof ServerLevel serverWorld) {
            CoatiEntity child = ModEntityTypes.COATI.get().create(serverWorld, EntitySpawnReason.BREEDING);
            if (child != null) {
                child.setPos(this.coati.position());
                child.setBaby(true);
                child.setUnBurrowing(true);
                child.startState(CoatiEntityAnimationState.UNBURROWING);
                serverWorld.addFreshEntity(child);
            }
            for (int i = 0; i < this.coati.getWildInventory().getContainerSize(); i++) {
                ItemStack s = this.coati.getWildInventory().getItem(i);
                if (!s.isEmpty()) this.coati.spawnAtLocation(getServerLevel(this.coati), s);
            }
            this.coati.clearInventory(this.coati.getWildInventory());
            this.coati.setForageGrace(1200);
        }
        this.coati.setDigSessionActive(false);
    }
}

