package net.emilsg.clutterbestiary.entity.client.animation;

import net.emilsg.clutterbestiary.animation_handling.EntityAnimationController;
import net.emilsg.clutterbestiary.animation_handling.IndexedAnimationState;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;

public class AnimationBindings<E extends Entity, S extends Enum<S> & IndexedAnimationState> {
    private final List<Binding<E, S>> bindings = new ArrayList<>();
    private final Set<S> boundStates = new HashSet<>();

    public AnimationBindings<E, S> bind(S state, AnimationDefinition animation) {
        return this.bind(state, entity -> animation, 1.0f);
    }

    public AnimationBindings<E, S> bind(S state, AnimationDefinition animation, float speed) {
        return this.bind(state, entity -> animation, speed);
    }

    public AnimationBindings<E, S> bind(S state, AnimationDefinition animation, ToDoubleFunction<E> speed) {
        return this.bind(state, entity -> animation, speed);
    }

    public AnimationBindings<E, S> bind(S state, Function<E, AnimationDefinition> animation, float speed) {
        if (speed <= 0 || !Float.isFinite(speed)) throw new IllegalArgumentException("Animation speed must be positive and finite");
        return this.bind(state, animation, entity -> speed);
    }

    private AnimationBindings<E, S> bind(S state, Function<E, AnimationDefinition> animation, ToDoubleFunction<E> speed) {
        if (!boundStates.add(state)) throw new IllegalArgumentException("Duplicate animation binding for " + state);
        bindings.add(new Binding<>(state, animation, speed));
        return this;
    }

    public void apply(E entity, EntityAnimationController<E, S> controller, float age, AnimationUpdater updater) {
        for (Binding<E, S> binding : bindings) {
            AnimationState state = controller.getAnimationState(binding.state);
            if (state.isStarted()) {
                float speed = (float) binding.speed.applyAsDouble(entity);
                if (speed <= 0 || !Float.isFinite(speed)) throw new IllegalStateException("Animation speed must be positive and finite");
                updater.update(state, binding.animation.apply(entity), age, speed);
            }
        }
    }

    @FunctionalInterface
    public interface AnimationUpdater {
        void update(AnimationState state, AnimationDefinition animation, float age, float speed);
    }

    private record Binding<E, S>(S state, Function<E, AnimationDefinition> animation, ToDoubleFunction<E> speed) {
    }
}
