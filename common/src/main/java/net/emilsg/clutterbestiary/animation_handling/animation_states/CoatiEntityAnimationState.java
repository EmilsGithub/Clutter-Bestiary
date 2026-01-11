package net.emilsg.clutterbestiary.animation_handling.animation_states;

import net.emilsg.clutterbestiary.animation_handling.IndexedAnimationState;

public enum CoatiEntityAnimationState implements IndexedAnimationState {
    IDLING(0),
    SITTING(1),
    STANDING_UP(2),
    SNIFFING(3),
    DIGGING(4),
    UNBURROWING(5),
    PICKING_UP_ITEM(6);

    private final int index;

    CoatiEntityAnimationState(int index) {
        this.index = index;
    }

    @Override
    public int getIndex() {
        return index;
    }
}
