package net.emilsg.clutterbestiary.animation_handling;

import net.emilsg.clutterbestiary.animation_handling.AnimationStateMachine.Condition;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import java.util.HashMap;
import java.util.Map;

public class EntityAnimationController<E extends Entity, S extends Enum<S> & IndexedAnimationState> {
    private final E entity;
    private final AnimationStateMachine<E, S> machine;
    private final EntityDataAccessor<Integer> trackedState;
    private final EntityDataAccessor<Integer> trackedRevision;
    private final EntityDataAccessor<Long> trackedStart;
    private final S initialState;
    private final Map<Integer, S> statesByIndex = new HashMap<>();
    private final AnimationStateCollection<S> animations;
    private long lastServerTick = Long.MIN_VALUE;

    public EntityAnimationController(E entity, S initialState, Class<S> enumClass, EntityDataAccessor<Integer> trackedState, EntityDataAccessor<Integer> trackedRevision, EntityDataAccessor<Long> trackedStart) {
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
        return entity.level().isClientSide() ? statesByIndex.getOrDefault(entity.getEntityData().get(trackedState), initialState) : machine.getCurrent();
    }

    public AnimationState getAnimationState(S state) {
        this.syncPlayback();
        return animations.get(state);
    }

    public void requestState(S state) {
        if (!entity.level().isClientSide() && machine.requestState(state)) {
            this.publishState();
        }
    }

    public void replayState(S state) {
        if (!entity.level().isClientSide()) {
            machine.forceState(state);
            this.publishState();
        }
    }

    public void tick() {
        if (entity.level().isClientSide()) {
            this.syncPlayback();
        } else if (entity.isAlive() && lastServerTick != entity.level().getGameTime()) {
            lastServerTick = entity.level().getGameTime();
            if (machine.tick()) this.publishState();
        }
    }

    private void publishState() {
        // A goal can request a state before the controller ticks in the same entity tick.
        lastServerTick = entity.level().getGameTime();
        SynchedEntityData tracker = entity.getEntityData();
        tracker.set(trackedState, machine.getCurrent().getIndex());
        tracker.set(trackedStart, entity.level().getGameTime());
        tracker.set(trackedRevision, tracker.get(trackedRevision) + 1);
    }

    private void syncPlayback() {
        if (!entity.level().isClientSide()) return;

        SynchedEntityData tracker = entity.getEntityData();
        // Read the complete tracked snapshot during ticking/rendering, after metadata updates have been applied.
        animations.sync(this.getState(), tracker.get(trackedRevision), tracker.get(trackedStart), entity.level().getGameTime(), entity.tickCount);
    }
}
