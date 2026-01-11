package net.emilsg.clutterbestiary.animation_handling;

import net.minecraft.entity.Entity;

public interface HandledEntityAnimations<E extends Entity, S extends Enum<S> & IndexedAnimationState> {
    EntityAnimationController<E, S> getAnimationController();

    default void startState(S state) {
        this.getAnimationController().requestState(state);
    }

    default void replayState(S state) {
        this.getAnimationController().replayState(state);
    }
}
