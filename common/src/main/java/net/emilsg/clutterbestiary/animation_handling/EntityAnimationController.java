package net.emilsg.clutterbestiary.animation_handling;

import net.emilsg.clutterbestiary.animation_handling.AnimationStateMachine.Condition;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;

import java.util.HashMap;
import java.util.Map;

public class EntityAnimationController<E extends Entity, S extends Enum<S> & IndexedAnimationState> {
    private final E entity;
    private final AnimationStateMachine<E, S> machine;
    private final TrackedData<Integer> trackedState;
    private final TrackedData<Integer> trackedRevision;
    private final TrackedData<Long> trackedStart;
    private final S initialState;
    private final Map<Integer, S> statesByIndex = new HashMap<>();
    private final AnimationStateCollection<S> animations;
    private long lastServerTick = Long.MIN_VALUE;

    public EntityAnimationController(E entity, S initialState, Class<S> enumClass, TrackedData<Integer> trackedState, TrackedData<Integer> trackedRevision, TrackedData<Long> trackedStart) {
        this.entity = entity;
        this.initialState = initialState;
        this.machine = new AnimationStateMachine<>(entity, initialState, enumClass);
        this.trackedState = trackedState;
        this.trackedRevision = trackedRevision;
        this.trackedStart = trackedStart;
        this.animations = new AnimationStateCollection<>(enumClass);

        for (S state : enumClass.getEnumConstants()) {
            if (statesByIndex.put(state.getIndex(), state) != null) {
                throw new IllegalArgumentException("Duplicate animation index: " + state.getIndex());
            }
        }
    }

    public void addTransition(S from, S to, Condition<E, S> condition) {
        machine.addTransition(from, to, condition);
    }

    public void addCompletion(S from, S to, int ticks) {
        machine.addCompletion(from, to, ticks);
    }

    public S getState() {
        return entity.getWorld().isClient ? statesByIndex.getOrDefault(entity.getDataTracker().get(trackedState), initialState) : machine.getCurrent();
    }

    public AnimationState getAnimationState(S state) {
        this.syncPlayback();
        return animations.get(state);
    }

    public void requestState(S state) {
        if (!entity.getWorld().isClient && machine.requestState(state)) {
            this.publishState();
        }
    }

    public void replayState(S state) {
        if (!entity.getWorld().isClient) {
            machine.forceState(state);
            this.publishState();
        }
    }

    public void tick() {
        if (entity.getWorld().isClient) {
            this.syncPlayback();
        } else if (entity.isAlive() && lastServerTick != entity.getWorld().getTime()) {
            lastServerTick = entity.getWorld().getTime();
            if (machine.tick()) this.publishState();
        }
    }

    private void publishState() {
        // A goal can request a state before the controller ticks in the same entity tick.
        lastServerTick = entity.getWorld().getTime();
        DataTracker tracker = entity.getDataTracker();
        tracker.set(trackedState, machine.getCurrent().getIndex());
        tracker.set(trackedStart, entity.getWorld().getTime());
        tracker.set(trackedRevision, tracker.get(trackedRevision) + 1);
    }

    private void syncPlayback() {
        if (!entity.getWorld().isClient) return;

        DataTracker tracker = entity.getDataTracker();
        // Read the complete tracked snapshot during ticking/rendering, after metadata updates have been applied.
        animations.sync(this.getState(), tracker.get(trackedRevision), tracker.get(trackedStart), entity.getWorld().getTime(), entity.age);
    }
}
