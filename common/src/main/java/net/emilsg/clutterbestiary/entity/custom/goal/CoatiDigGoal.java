package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.ModEntityTypes;
import net.emilsg.clutterbestiary.entity.custom.CoatiEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.EnumSet;

public class CoatiDigGoal extends Goal {
    private final CoatiEntity coati;
    private int diggingTimer;

    public CoatiDigGoal(CoatiEntity coati) {
        this.coati = coati;
        this.setControls(EnumSet.of(Control.JUMP, Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        return !this.coati.isBaby() && this.coati.isDigging() && this.coati.isOnGround() && !this.coati.isInsideWaterOrBubbleColumn() && this.coati.getBurrowPos().isWithinDistance(this.coati.getPos(), 2.0);
    }

    @Override
    public boolean shouldContinue() {
        return canStart();
    }

    @Override
    public void start() {
        this.coati.getNavigation().stop();
        this.coati.startState(CoatiEntity.CoatiEntityAnimationState.DIGGING);
        diggingTimer = 160;
    }

    @Override
    public void tick() {
        if (--diggingTimer > 0) return;

        this.coati.setDigging(false);
        this.coati.startState(CoatiEntity.CoatiEntityAnimationState.IDLING);

        if (this.coati.getWorld() instanceof ServerWorld serverWorld) {
            CoatiEntity child = ModEntityTypes.COATI.get().create(serverWorld);
            if (child != null) {
                child.setPosition(this.coati.getPos());
                child.setBaby(true);
                child.setUnBurrowing(true);
                serverWorld.spawnEntity(child);
            }
            for (int i = 0; i < this.coati.getWildInventory().size(); i++) {
                ItemStack s = this.coati.getWildInventory().getStack(i);
                if (!s.isEmpty()) this.coati.dropStack(s);
            }
            this.coati.clearInventory(this.coati.getWildInventory());
            this.coati.setForageGrace(1200);
        }
        this.coati.setDigSessionActive(false);
    }
}

