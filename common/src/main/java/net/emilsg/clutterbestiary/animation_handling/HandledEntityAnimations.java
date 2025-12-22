package net.emilsg.clutterbestiary.animation_handling;

public interface HandledEntityAnimations {
    int getState();

    void setState(int state);

    void setupAnimationStateMachine();

    void stopAnimations();
}
