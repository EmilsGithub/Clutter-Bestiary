package net.emilsg.clutterbestiary.animation_handling.animation_states;

import net.emilsg.clutterbestiary.animation_handling.IndexedAnimationState;
import net.emilsg.clutterbestiary.entity.custom.CapybaraEntity;

public enum CapybaraEntityAnimationState implements IndexedAnimationState {
    IDLING(0),
    LAYING_DOWN(1),
    SLEEPING(2),
    STANDING_UP(3);

    private final int index;

    CapybaraEntityAnimationState(final int index) {
        this.index = index;
    }

    public static CapybaraEntityAnimationState fromIndex(int idx) {
        for (var s : values()) if (s.index == idx) return s;
        return IDLING;
    }

    public int getIndex() {
        return this.index;
    }
}
