package net.emilsg.clutterbestiary.animation_handling;

import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;

public class AnimationPlayback {

    public static void updateLoop(Entity entity, AnimationState state, boolean shouldPlay) {
        if (!entity.level().isClientSide()) return;
        if (shouldPlay) {
            state.startIfStopped(entity.tickCount);
        } else {
            state.stop();
        }
    }

}
