package net.emilsg.clutterbestiary.animation_handling.animation_states;

import net.emilsg.clutterbestiary.animation_handling.IndexedAnimationState;

public enum CrocodileEntityAnimationState implements IndexedAnimationState {
    IDLING(0),
    OPENING_MOUTH(1),
    MOUTH_WAITING(2),
    SNAPPING_MOUTH_SHUT(3),
    STAYING(4),
    ATTACKING_OPEN_MOUTH(5),
    ATTACKING_SNAP_SHUT(6);

    private final int index;

    CrocodileEntityAnimationState(final int index) {
        this.index = index;
    }

    public int getIndex() {
        return this.index;
    }
}
