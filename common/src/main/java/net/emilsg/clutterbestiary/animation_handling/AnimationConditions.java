package net.emilsg.clutterbestiary.animation_handling;

public class AnimationConditions {

    public static <E, S> AnimationStateMachine.Condition<E, S> timeAtLeast(int ticks) {
        return (e, s, age) -> age >= ticks;
    }

    public static <E, S> AnimationStateMachine.Condition<E, S> and(AnimationStateMachine.Condition<E, S> a, AnimationStateMachine.Condition<E, S> b) {
        return (e, s, age) -> a.test(e, s, age) && b.test(e, s, age);
    }

    @SafeVarargs
    public static <E, S> AnimationStateMachine.Condition<E, S> multiple(AnimationStateMachine.Condition<E, S>... conditions) {
        return (e, s, age) -> {
            for (var c : conditions) {
                if (!c.test(e, s, age)) return false;
            }
            return true;
        };
    }

    public static <E, S> AnimationStateMachine.Condition<E, S> or(AnimationStateMachine.Condition<E, S> a, AnimationStateMachine.Condition<E, S> b) {
        return (e, s, age) -> a.test(e, s, age) || b.test(e, s, age);
    }

    public static <E, S> AnimationStateMachine.Condition<E, S> not(AnimationStateMachine.Condition<E, S> a) {
        return (e, s, age) -> !a.test(e, s, age);
    }
}
