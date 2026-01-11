package net.emilsg.clutterbestiary.animation_handling.animation_states;

import net.emilsg.clutterbestiary.animation_handling.IndexedAnimationState;

public enum CapybaraEntityAnimationState implements IndexedAnimationState {
    IDLING(0),
    LAYING_DOWN(1),
    SLEEPING(2),
    STANDING_UP(3);

    private final int index;

    CapybaraEntityAnimationState(final int index) {
        this.index = index;
    }

    public int getIndex() {
        return this.index;
    }
}
