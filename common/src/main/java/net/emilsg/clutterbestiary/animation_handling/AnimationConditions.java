package net.emilsg.clutterbestiary.animation_handling;

import net.emilsg.clutterbestiary.animation_handling.AnimationStateMachine.Condition;

public class AnimationConditions {

    public static <E, S> Condition<E, S> timeAtLeast(int ticks) {
        return (e, s, age) -> age >= ticks;
    }

    public static <E, S> Condition<E, S> and(Condition<E, S> a, Condition<E, S> b) {
        return (e, s, age) -> a.test(e, s, age) && b.test(e, s, age);
    }

    @SafeVarargs
    public static <E, S> Condition<E, S> multiple(Condition<E, S>... conditions) {
        return (e, s, age) -> {
            for (var c : conditions) {
                if (!c.test(e, s, age)) return false;
            }
            return true;
        };
    }

    public static <E, S> Condition<E, S> or(Condition<E, S> a, Condition<E, S> b) {
        return (e, s, age) -> a.test(e, s, age) || b.test(e, s, age);
    }

    public static <E, S> Condition<E, S> not(Condition<E, S> a) {
        return (e, s, age) -> !a.test(e, s, age);
    }
}
