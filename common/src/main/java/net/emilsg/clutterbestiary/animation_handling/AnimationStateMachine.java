package net.emilsg.clutterbestiary.animation_handling;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class AnimationStateMachine<E, S extends Enum<S> & IndexedAnimationState> {

    private final E entity;
    private final Map<S, List<Transition<E, S>>> transitions;
    private S current;
    private int ticksInState = 0;

    public AnimationStateMachine(E entity, S initialState, Class<S> enumClass) {
        this.entity = entity;
        this.current = initialState;

        this.transitions = new EnumMap<>(enumClass);
        for (S s : enumClass.getEnumConstants()) {
            this.transitions.put(s, new ArrayList<>());
        }
    }

    public void addTransition(S from, S to, Condition<E, S> condition) {
        List<Transition<E, S>> list = transitions.computeIfAbsent(from, k -> new ArrayList<>());
        list.add(new Transition<>(to, condition));
    }

    public void forceState(S newState) {
        this.current = newState;
        this.ticksInState = 0;
    }

    public S getCurrent() {
        return current;
    }

    public int getTicksInState() {
        return ticksInState;
    }

    public boolean tick() {
        ticksInState++;

        List<Transition<E, S>> list = transitions.get(current);
        if (list == null || list.isEmpty()) return false;

        for (Transition<E, S> t : list) {
            if (t.condition.test(entity, current, ticksInState)) {
                current = t.target;
                ticksInState = 0;
                return true;
            }
        }
        return false;
    }

    @FunctionalInterface
    public interface Condition<E, S> {
        boolean test(E entity, S state, int ticksInState);
    }

    public static final class Transition<E, S> {
        final S target;
        final Condition<E, S> condition;

        Transition(S target, Condition<E, S> condition) {
            this.target = target;
            this.condition = condition;
        }
    }
}
