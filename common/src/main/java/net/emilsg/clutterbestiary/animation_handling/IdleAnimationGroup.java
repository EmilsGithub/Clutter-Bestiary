package net.emilsg.clutterbestiary.animation_handling;

import net.minecraft.entity.AnimationState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.random.Random;

import java.util.ArrayList;
import java.util.List;

public class IdleAnimationGroup {
    private final int minDelay;
    private final int maxDelay;
    private final int chance;
    private final List<Entry> entries = new ArrayList<>();
    private int totalWeight;
    private int cooldown;

    public IdleAnimationGroup(int minDelay, int maxDelay, int chance) {
        if (minDelay < 0 || maxDelay < minDelay || chance <= 0) throw new IllegalArgumentException("Invalid random animation timing");
        this.minDelay = minDelay;
        this.maxDelay = maxDelay;
        this.chance = chance;
    }

    public IdleAnimationGroup add(int weight, AnimationState... states) {
        if (weight <= 0 || states.length == 0) throw new IllegalArgumentException("Animation entries need a positive weight and at least one state");
        totalWeight = Math.addExact(totalWeight, weight);
        entries.add(new Entry(weight, states.clone()));
        return this;
    }

    public void tick(Entity entity, boolean isValid) {
        if (!entity.getWorld().isClient) return;
        if (cooldown > 0) {
            cooldown--;
            return;
        }
        if (!isValid || entries.isEmpty()) return;

        Random random = entity.getRandom();
        if (random.nextInt(chance) != 0) return;
        cooldown = minDelay + random.nextInt(maxDelay - minDelay + 1);
        int choice = random.nextInt(totalWeight);

        for (Entry entry : entries) for (AnimationState state : entry.states) state.stop();

        for (Entry entry : entries) {
            choice -= entry.weight;
            if (choice < 0) {
                for (AnimationState state : entry.states) state.start(entity.age);
                return;
            }
        }
    }

    private record Entry(int weight, AnimationState[] states) {}

}
