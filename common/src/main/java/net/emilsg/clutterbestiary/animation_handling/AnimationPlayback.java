package net.emilsg.clutterbestiary.animation_handling;

import net.minecraft.entity.AnimationState;
import net.minecraft.entity.Entity;

public class AnimationPlayback {

    public static void updateLoop(Entity entity, AnimationState state, boolean shouldPlay) {
        if (!entity.getWorld().isClient) return;
        if (shouldPlay) {
            state.startIfNotRunning(entity.age);
        } else {
            state.stop();
        }
    }

}
