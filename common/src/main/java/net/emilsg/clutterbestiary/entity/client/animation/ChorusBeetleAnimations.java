package net.emilsg.clutterbestiary.entity.client.animation;

import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;

public class ChorusBeetleAnimations {
	public static final Animation CHORUS_BEETLE_WALK = Animation.Builder.create(1.0F).looping()
			.addBoneAnimation("head", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -2.5F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 2.5F), Transformation.Interpolations.CUBIC),
					new Keyframe(1.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -2.5F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("body", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 5.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -5.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(1.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 5.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("tail", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 2.5F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -2.5F), Transformation.Interpolations.CUBIC),
					new Keyframe(1.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 2.5F), Transformation.Interpolations.CUBIC)
			))
			.build();

	public static final Animation CHORUS_BEETLE_FLY = Animation.Builder.create(0.5F).looping()
			.addBoneAnimation("leftElytra", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(-5.0F, 55.0F, 5.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.25F, AnimationHelper.createRotationalVector(-5.5F, 56.0F, 5.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.5F, AnimationHelper.createRotationalVector(-5.0F, 55.0F, 5.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("rightElytra", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(-5.0F, -55.0F, -5.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.25F, AnimationHelper.createRotationalVector(-7.0F, -56.5F, -5.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.5F, AnimationHelper.createRotationalVector(-5.0F, -55.0F, -5.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("all", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 1.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.25F, AnimationHelper.createRotationalVector(5.0F, 0.0F, -1.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.5F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 1.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("tail", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(-25.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.25F, AnimationHelper.createRotationalVector(-22.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.5F, AnimationHelper.createRotationalVector(-25.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("frontLeftLeg", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(23.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.25F, AnimationHelper.createRotationalVector(26.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.5F, AnimationHelper.createRotationalVector(23.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("middleLeftLeg", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(25.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.25F, AnimationHelper.createRotationalVector(27.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.5F, AnimationHelper.createRotationalVector(25.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("backLeftLeg", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(24.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.25F, AnimationHelper.createRotationalVector(26.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.5F, AnimationHelper.createRotationalVector(24.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("leftWing", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(15.0F, 60.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.125F, AnimationHelper.createRotationalVector(50.0F, 60.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.25F, AnimationHelper.createRotationalVector(15.0F, 60.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.375F, AnimationHelper.createRotationalVector(50.0F, 60.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.5F, AnimationHelper.createRotationalVector(15.0F, 60.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("rightWing", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(15.0F, -60.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.125F, AnimationHelper.createRotationalVector(50.0F, -60.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.25F, AnimationHelper.createRotationalVector(15.0F, -60.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.375F, AnimationHelper.createRotationalVector(50.0F, -60.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.5F, AnimationHelper.createRotationalVector(15.0F, -60.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("frontRightLeg", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(23.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.25F, AnimationHelper.createRotationalVector(26.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.5F, AnimationHelper.createRotationalVector(23.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("middleRightLeg", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(25.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.25F, AnimationHelper.createRotationalVector(27.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.5F, AnimationHelper.createRotationalVector(25.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("backRightLeg", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(24.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.25F, AnimationHelper.createRotationalVector(26.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.5F, AnimationHelper.createRotationalVector(24.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.build();

	public static final Animation CHORUS_BEETLE_HOVER = Animation.Builder.create(0.5F).looping()
			.addBoneAnimation("leftElytra", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(-5.0F, 55.0F, 5.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.25F, AnimationHelper.createRotationalVector(-5.5F, 56.0F, 5.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.5F, AnimationHelper.createRotationalVector(-5.0F, 55.0F, 5.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("rightElytra", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(-5.0F, -55.0F, -5.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.25F, AnimationHelper.createRotationalVector(-7.0F, -56.5F, -5.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.5F, AnimationHelper.createRotationalVector(-5.0F, -55.0F, -5.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("tail", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(-25.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.25F, AnimationHelper.createRotationalVector(-22.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.5F, AnimationHelper.createRotationalVector(-25.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("frontLeftLeg", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.25F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 5.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("middleLeftLeg", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.25F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 5.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("backLeftLeg", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.25F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 5.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("leftWing", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(15.0F, 75.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.125F, AnimationHelper.createRotationalVector(50.0F, 75.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.25F, AnimationHelper.createRotationalVector(15.0F, 75.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.375F, AnimationHelper.createRotationalVector(50.0F, 75.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.5F, AnimationHelper.createRotationalVector(15.0F, 75.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("rightWing", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(15.0F, -75.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.125F, AnimationHelper.createRotationalVector(50.0F, -75.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.25F, AnimationHelper.createRotationalVector(15.0F, -75.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.375F, AnimationHelper.createRotationalVector(50.0F, -75.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.5F, AnimationHelper.createRotationalVector(15.0F, -75.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("frontRightLeg", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.25F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -5.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("middleRightLeg", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.25F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -5.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("backRightLeg", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.25F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -5.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.build();

	public static final Animation CHORUS_BEETLE_LAND = Animation.Builder.create(0.5F).looping()
			.addBoneAnimation("leftElytra", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(-5.0F, 55.0F, 5.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.25F, AnimationHelper.createRotationalVector(-5.5F, 56.0F, 5.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.5F, AnimationHelper.createRotationalVector(-5.0F, 55.0F, 5.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("rightElytra", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(-5.0F, -55.0F, -5.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.25F, AnimationHelper.createRotationalVector(-7.0F, -56.5F, -5.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.5F, AnimationHelper.createRotationalVector(-5.0F, -55.0F, -5.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("tail", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(-25.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.25F, AnimationHelper.createRotationalVector(-22.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.5F, AnimationHelper.createRotationalVector(-25.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("frontLeftLeg", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.25F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 5.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("middleLeftLeg", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.25F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 5.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("backLeftLeg", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.25F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 5.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("leftWing", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(15.0F, 75.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.125F, AnimationHelper.createRotationalVector(50.0F, 75.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.25F, AnimationHelper.createRotationalVector(15.0F, 75.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.375F, AnimationHelper.createRotationalVector(50.0F, 75.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.5F, AnimationHelper.createRotationalVector(15.0F, 75.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("rightWing", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(15.0F, -75.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.125F, AnimationHelper.createRotationalVector(50.0F, -75.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.25F, AnimationHelper.createRotationalVector(15.0F, -75.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.375F, AnimationHelper.createRotationalVector(50.0F, -75.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.5F, AnimationHelper.createRotationalVector(15.0F, -75.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("frontRightLeg", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.25F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -5.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("middleRightLeg", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.25F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -5.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("backRightLeg", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.25F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -5.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.build();

	public static final Animation CHORUS_BEETLE_WING_FLICK = Animation.Builder.create(0.75F)
			.addBoneAnimation("leftElytra", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
					new Keyframe(0.125F, AnimationHelper.createRotationalVector(0.0F, 35.0F, 7.5F), Transformation.Interpolations.LINEAR),
					new Keyframe(0.1875F, AnimationHelper.createRotationalVector(25.0F, 35.0F, 7.5F), Transformation.Interpolations.LINEAR),
					new Keyframe(0.25F, AnimationHelper.createRotationalVector(0.0F, 35.0F, 7.5F), Transformation.Interpolations.LINEAR),
					new Keyframe(0.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation("rightElytra", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.25F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
					new Keyframe(0.375F, AnimationHelper.createRotationalVector(0.0F, -35.0F, -7.5F), Transformation.Interpolations.LINEAR),
					new Keyframe(0.4375F, AnimationHelper.createRotationalVector(25.0F, -35.0F, -7.5F), Transformation.Interpolations.LINEAR),
					new Keyframe(0.5F, AnimationHelper.createRotationalVector(0.0F, -35.0F, -7.5F), Transformation.Interpolations.LINEAR),
					new Keyframe(0.75F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation("leftWing", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
					new Keyframe(0.125F, AnimationHelper.createRotationalVector(0.0F, 20.0F, 0.0F), Transformation.Interpolations.LINEAR),
					new Keyframe(0.1875F, AnimationHelper.createRotationalVector(12.5F, 35.0F, 10.0F), Transformation.Interpolations.LINEAR),
					new Keyframe(0.25F, AnimationHelper.createRotationalVector(0.0F, 20.0F, 0.0F), Transformation.Interpolations.LINEAR),
					new Keyframe(0.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation("rightWing", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.25F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
					new Keyframe(0.375F, AnimationHelper.createRotationalVector(0.0F, -20.0F, 0.0F), Transformation.Interpolations.LINEAR),
					new Keyframe(0.4375F, AnimationHelper.createRotationalVector(12.5F, -35.0F, -10.0F), Transformation.Interpolations.LINEAR),
					new Keyframe(0.5F, AnimationHelper.createRotationalVector(0.0F, -20.0F, 0.0F), Transformation.Interpolations.LINEAR),
					new Keyframe(0.75F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.build();

	public static final Animation CHORUS_BEETLE_MANDIBLE_NIBBLE = Animation.Builder.create(0.5F)
			.addBoneAnimation("rightAntenna", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.125F, AnimationHelper.createRotationalVector(0.0F, -25.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.25F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.375F, AnimationHelper.createRotationalVector(0.0F, -25.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("leftAntenna", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.125F, AnimationHelper.createRotationalVector(0.0F, 25.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.25F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.375F, AnimationHelper.createRotationalVector(0.0F, 25.0F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(0.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.build();
}
