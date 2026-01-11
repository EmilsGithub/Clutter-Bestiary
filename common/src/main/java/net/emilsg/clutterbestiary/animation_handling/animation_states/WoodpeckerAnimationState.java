package net.emilsg.clutterbestiary.animation_handling.animation_states;

import net.emilsg.clutterbestiary.animation_handling.IndexedAnimationState;

public enum WoodpeckerAnimationState implements IndexedAnimationState {
    GROUND_IDLE(0),
    FLYING(1),
    HOVERING(2),
    ATTACHED(3),
    PECKING(4);

    private final int index;

    WoodpeckerAnimationState(int index) {
        this.index = index;
    }

    @Override
    public int getIndex() {
        return this.index;
    }
}
