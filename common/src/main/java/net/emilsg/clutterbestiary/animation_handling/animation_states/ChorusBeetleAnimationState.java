package net.emilsg.clutterbestiary.animation_handling.animation_states;

import net.emilsg.clutterbestiary.animation_handling.IndexedAnimationState;

public enum ChorusBeetleAnimationState implements IndexedAnimationState {
    IDLING(0),
    WALKING(1),
    FLYING(2),
    HOVERING(3),
    LANDING(4);

    private final int index;

    ChorusBeetleAnimationState(int index) {
        this.index = index;
    }

    @Override
    public int getIndex() {
        return this.index;
    }
}
