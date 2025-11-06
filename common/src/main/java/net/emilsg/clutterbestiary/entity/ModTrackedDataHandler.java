package net.emilsg.clutterbestiary.entity;

import net.emilsg.clutterbestiary.entity.custom.CoatiEntity;
import net.emilsg.clutterbestiary.entity.custom.RiverTurtleEntity;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;

public class ModTrackedDataHandler {

    public static final TrackedDataHandler<RiverTurtleEntity.RiverTurtleAnimationState> RIVER_TURTLE_ANIMATION_STATE = TrackedDataHandler.create(RiverTurtleEntity.RiverTurtleAnimationState.PACKET_CODEC);
    public static final TrackedDataHandler<CoatiEntity.CoatiEntityAnimationState> COATI_ANIMATION_STATE = TrackedDataHandler.create(CoatiEntity.CoatiEntityAnimationState.PACKET_CODEC);

    public static void registerAnimationStates() {
        TrackedDataHandlerRegistry.register(RIVER_TURTLE_ANIMATION_STATE);
        TrackedDataHandlerRegistry.register(COATI_ANIMATION_STATE);

    }
}
