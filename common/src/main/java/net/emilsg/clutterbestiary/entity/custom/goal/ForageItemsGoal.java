package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.animation_handling.animation_states.CoatiEntityAnimationState;
import net.emilsg.clutterbestiary.entity.custom.CoatiEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

public class ForageItemsGoal extends Goal {
    private final CoatiEntity coati;
    private final double speed;
    private final double searchRadius;
    private ItemEntity target;
    private int retargetCooldown = 0;

    public ForageItemsGoal(CoatiEntity coati, double speed, double searchRadius) {
        this.coati = coati;
        this.speed = speed;
        this.searchRadius = searchRadius;
        this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
    }

    @Override
    public boolean canStart() {
        if (this.coati.isTamed() || this.coati.isBaby() || this.coati.isSitting() || this.coati.isDigging() || !this.coati.canFitInWild(new ItemStack(Items.BEDROCK)) || this.coati.getForageGrace() > 0)
            return false;

        if (retargetCooldown-- > 0) return target != null && target.isAlive();

        retargetCooldown = 20 + this.coati.getRandom().nextInt(20);

        List<ItemEntity> items = coati.getWorld().getEntitiesByClass(ItemEntity.class, coati.getBoundingBox().expand(searchRadius, 2.0, searchRadius), ie -> ie.isAlive() && !ie.cannotPickup() && !ie.getStack().isEmpty() && coati.canFitInWild(ie.getStack()));

        items.sort(Comparator.comparingDouble(i -> i.squaredDistanceTo(this.coati)));
        target = items.isEmpty() ? null : items.getFirst();
        return target != null;
    }

    @Override
    public boolean shouldContinue() {
        return target != null && target.isAlive() && !coati.isTamed() && !coati.isSitting() && !coati.isDigging() && coati.canFitInWild(target.getStack());
    }

    @Override
    public void stop() {
        target = null;
        this.coati.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (target == null) return;

        this.coati.getLookControl().lookAt(target, 30.0f, 30.0f);
        this.coati.getNavigation().startMovingTo(target, speed);

        double reach = this.coati.getWidth() + 1.25f;
        if (this.coati.squaredDistanceTo(target) <= reach * reach) {
            ItemStack stack = target.getStack();
            if (!stack.isEmpty()) {
                ItemStack remainder = this.coati.insertInto(this.coati.getWildInventory(), stack.copy());
                this.coati.startState(CoatiEntityAnimationState.IDLING);
                this.coati.startState(CoatiEntityAnimationState.PICKING_UP_ITEM);
                this.coati.playSound(SoundEvents.ENTITY_ITEM_PICKUP);
                if (remainder.isEmpty()) {
                    target.discard();
                    target = null;
                } else if (remainder.getCount() != stack.getCount()) {
                    target.setStack(remainder);
                }
            }
        }
    }
}