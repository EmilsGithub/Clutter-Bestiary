package net.emilsg.clutterbestiary.animation_handling;

import java.util.EnumMap;
import net.minecraft.world.entity.AnimationState;

public class AnimationStateCollection<StateKey extends Enum<StateKey>> {
    // Each enum value owns one reusable AnimationState instead of creating new states during rendering.
    private final EnumMap<StateKey, AnimationState> animationStates;
    private StateKey activeState;
    private int activeRevision;

    public AnimationStateCollection(Class<StateKey> stateType) {
        this.animationStates = new EnumMap<>(stateType);
        for (StateKey state : stateType.getEnumConstants()) {
            this.animationStates.put(state, new AnimationState());
        }
    }

    public AnimationState get(StateKey state) {
        return this.animationStates.get(state);
    }

    public void sync(StateKey requestedState, int requestedRevision, long stateStartedAtWorldTime, long currentWorldTime, int entityAge) {
        // A new revision deliberately restarts playback, even when the requested state has not changed.
        if (requestedState == this.activeState && requestedRevision == this.activeRevision) return;

        if (this.activeState != null) {
            this.animationStates.get(this.activeState).stop();
        }

        this.activeState = requestedState;
        this.activeRevision = requestedRevision;

        // Backdate the local start age so clients joining late see the correct point in the animation.
        long elapsedTicks = stateStartedAtWorldTime < 0 ? 0 : Math.max(0, currentWorldTime - stateStartedAtWorldTime);
        int animationStartAge = entityAge - (int) Math.min(elapsedTicks, Integer.MAX_VALUE);

        this.animationStates.get(requestedState).start(animationStartAge);
    }
}
