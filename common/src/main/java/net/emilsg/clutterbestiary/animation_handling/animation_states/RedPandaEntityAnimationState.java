package net.emilsg.clutterbestiary.animation_handling.animation_states;

import net.emilsg.clutterbestiary.animation_handling.IndexedAnimationState;
import net.emilsg.clutterbestiary.entity.custom.RedPandaEntity;

public enum RedPandaEntityAnimationState implements IndexedAnimationState {
    IDLING(0),
    LAYING_DOWN(1),
    SLEEPING(2),
    STANDING_UP(3),
    STARTING_Y_POSE(4),
    Y_POSING(5),
    ENDING_Y_POSE(6),
    SIT_START(7),
    SIT_END(8);

    private final int index;

    RedPandaEntityAnimationState(final int index) {
        this.index = index;
    }

    public static RedPandaEntityAnimationState fromIndex(int idx) {
        for (var s : values()) if (s.index == idx) return s;
        return IDLING;
    }

    public int getIndex() {
        return this.index;
    }
}
