package net.emilsg.clutterbestiary.entity;

import net.emilsg.clutterbestiary.entity.custom.BeaverEntity;
import net.emilsg.clutterbestiary.entity.custom.CapybaraEntity;
import net.emilsg.clutterbestiary.entity.custom.CoatiEntity;
import net.emilsg.clutterbestiary.entity.custom.RiverTurtleEntity;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;

public class ModTrackedDataHandler {

    public static final TrackedDataHandler<RiverTurtleEntity.RiverTurtleAnimationState> RIVER_TURTLE_ANIMATION_STATE = TrackedDataHandler.create(RiverTurtleEntity.RiverTurtleAnimationState.PACKET_CODEC);
    public static final TrackedDataHandler<CoatiEntity.CoatiEntityAnimationState> COATI_ANIMATION_STATE = TrackedDataHandler.create(CoatiEntity.CoatiEntityAnimationState.PACKET_CODEC);
    public static final TrackedDataHandler<CapybaraEntity.CapybaraEntityAnimationState> CAPYBARA_ANIMATION_STATE = TrackedDataHandler.create(CapybaraEntity.CapybaraEntityAnimationState.PACKET_CODEC);
    public static final TrackedDataHandler<BeaverEntity.BeaverEntityAnimationState> BEAVER_ANIMATION_STATE = TrackedDataHandler.create(BeaverEntity.BeaverEntityAnimationState.PACKET_CODEC);

    public static void registerAnimationStates() {
        TrackedDataHandlerRegistry.register(RIVER_TURTLE_ANIMATION_STATE);
        TrackedDataHandlerRegistry.register(COATI_ANIMATION_STATE);
        TrackedDataHandlerRegistry.register(CAPYBARA_ANIMATION_STATE);
        TrackedDataHandlerRegistry.register(BEAVER_ANIMATION_STATE);
    }
}
