package net.emilsg.clutterbestiary.animation_handling.animation_states;

import net.emilsg.clutterbestiary.animation_handling.IndexedAnimationState;

public enum MossbloomAnimationState implements IndexedAnimationState {
    IDLING(0),
    SHAKING(1);

    private final int index;

    MossbloomAnimationState(int index) {
        this.index = index;
    }

    @Override
    public int getIndex() {
        return this.index;
    }
}
