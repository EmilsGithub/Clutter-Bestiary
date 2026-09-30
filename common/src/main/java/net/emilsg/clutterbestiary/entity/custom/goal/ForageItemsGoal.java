package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.animation_handling.animation_states.CoatiEntityAnimationState;
import net.emilsg.clutterbestiary.entity.custom.CoatiEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.coati.isTame() || this.coati.isBaby() || this.coati.isOrderedToSit() || this.coati.isDigging() || !this.coati.canFitInWild(new ItemStack(Items.BEDROCK)) || this.coati.getForageGrace() > 0)
            return false;

        if (retargetCooldown-- > 0) return target != null && target.isAlive();

        retargetCooldown = 20 + this.coati.getRandom().nextInt(20);

        List<ItemEntity> items = coati.level().getEntitiesOfClass(ItemEntity.class, coati.getBoundingBox().inflate(searchRadius, 2.0, searchRadius), ie -> ie.isAlive() && !ie.hasPickUpDelay() && !ie.getItem().isEmpty() && coati.canFitInWild(ie.getItem()));

        items.sort(Comparator.comparingDouble(i -> i.distanceToSqr(this.coati)));
        target = items.isEmpty() ? null : items.getFirst();
        return target != null;
    }

    @Override
    public boolean canContinueToUse() {
        return target != null && target.isAlive() && !coati.isTame() && !coati.isOrderedToSit() && !coati.isDigging() && coati.canFitInWild(target.getItem());
    }

    @Override
    public void stop() {
        target = null;
        this.coati.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (target == null) return;

        this.coati.getLookControl().setLookAt(target, 30.0f, 30.0f);
        this.coati.getNavigation().moveTo(target, speed);

        double reach = this.coati.getBbWidth() + 1.25f;
        if (this.coati.distanceToSqr(target) <= reach * reach) {
            ItemStack stack = target.getItem();
            if (!stack.isEmpty()) {
                ItemStack remainder = this.coati.insertInto(this.coati.getWildInventory(), stack.copy());
                this.coati.replayState(CoatiEntityAnimationState.PICKING_UP_ITEM);
                this.coati.makeSound(SoundEvents.ITEM_PICKUP);
                if (remainder.isEmpty()) {
                    target.discard();
                    target = null;
                } else if (remainder.getCount() != stack.getCount()) {
                    target.setItem(remainder);
                }
            }
        }
    }
}