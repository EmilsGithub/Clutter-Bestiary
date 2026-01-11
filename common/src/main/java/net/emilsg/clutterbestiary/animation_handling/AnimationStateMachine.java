package net.emilsg.clutterbestiary.animation_handling;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AnimationStateMachine<E, S extends Enum<S>> {

    private final E entity;
    private final Map<S, List<Transition<E, S>>> transitions;
    private final Map<S, Completion<S>> completions;
    private S current;
    private int ticksInState = 0;

    public AnimationStateMachine(E entity, S initialState, Class<S> enumClass) {
        this.entity = entity;
        this.current = Objects.requireNonNull(initialState);

        this.transitions = new EnumMap<>(enumClass);
        this.completions = new EnumMap<>(enumClass);
        for (S s : enumClass.getEnumConstants()) {
            this.transitions.put(s, new ArrayList<>());
        }
    }

    public void addTransition(S from, S to, Condition<E, S> condition) {
        List<Transition<E, S>> list = transitions.get(Objects.requireNonNull(from));
        list.add(new Transition<>(Objects.requireNonNull(to), Objects.requireNonNull(condition)));
    }

    public void addCompletion(S from, S to, int ticks) {
        if (ticks <= 0) throw new IllegalArgumentException("Completion duration must be positive");
        if (completions.containsKey(from)) throw new IllegalArgumentException("Duplicate completion for " + from);
        completions.put(Objects.requireNonNull(from), new Completion<>(Objects.requireNonNull(to), ticks));
    }

    public boolean requestState(S newState) {
        if (current == Objects.requireNonNull(newState)) return false;
        this.forceState(newState);
        return true;
    }

    public void forceState(S newState) {
        this.current = Objects.requireNonNull(newState);
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
        // Registered transitions take priority over timed completion, in registration order.
        for (Transition<E, S> t : list) {
            if (t.condition.test(entity, current, ticksInState)) {
                this.forceState(t.target);
                return true;
            }
        }
        Completion<S> completion = completions.get(current);
        if (completion != null && ticksInState >= completion.ticks) {
            this.forceState(completion.target);
            return true;
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

    private record Completion<S>(S target, int ticks) {
    }
}
