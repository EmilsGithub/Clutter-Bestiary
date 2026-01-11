package net.emilsg.clutterbestiary.animation_handling.animation_states;

import net.emilsg.clutterbestiary.animation_handling.IndexedAnimationState;

public enum RiverTurtleAnimationState implements IndexedAnimationState {
    IDLING(0),
    HIDING(1),
    UNHIDING(2),
    SIT_START(3),
    SIT_END(4);

    private final int index;

    RiverTurtleAnimationState(final int index) {
        this.index = index;
    }

    public int getIndex() {
        return this.index;
    }
}
