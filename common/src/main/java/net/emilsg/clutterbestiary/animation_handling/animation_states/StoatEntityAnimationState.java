package net.emilsg.clutterbestiary.animation_handling.animation_states;

import net.emilsg.clutterbestiary.animation_handling.IndexedAnimationState;

public enum StoatEntityAnimationState implements IndexedAnimationState {
    IDLING(0),
    LAYING_DOWN(1),
    SLEEPING(2),
    STANDING_UP(3),
    SIT_START(4),
    SIT_END(5),
    SITTING(6),
    SITTING_UP(7);

    private final int index;

    StoatEntityAnimationState(final int index) {
        this.index = index;
    }

    public int getIndex() {
        return this.index;
    }
}
