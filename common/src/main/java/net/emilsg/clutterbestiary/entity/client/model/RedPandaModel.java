package net.emilsg.clutterbestiary.entity.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.emilsg.clutterbestiary.animation_handling.animation_states.RedPandaEntityAnimationState;
import net.emilsg.clutterbestiary.entity.client.animation.AnimationBindings;
import net.emilsg.clutterbestiary.entity.client.animation.RedPandaAnimations;
import net.emilsg.clutterbestiary.entity.client.model.parent.ParentTameableModel;
import net.emilsg.clutterbestiary.entity.custom.RedPandaEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class RedPandaModel<T extends RedPandaEntity> extends ParentTameableModel<T> {
    private static final AnimationBindings<RedPandaEntity, RedPandaEntityAnimationState> ANIMATIONS = new AnimationBindings<RedPandaEntity, RedPandaEntityAnimationState>()
            .bind(RedPandaEntityAnimationState.LAYING_DOWN, RedPandaAnimations.RED_PANDA_LAY_DOWN)
            .bind(RedPandaEntityAnimationState.SLEEPING, RedPandaAnimations.RED_PANDA_SLEEP)
            .bind(RedPandaEntityAnimationState.STANDING_UP, RedPandaAnimations.RED_PANDA_STAND_UP)
            .bind(RedPandaEntityAnimationState.STARTING_Y_POSE, RedPandaAnimations.RED_PANDA_Y_POSE_START)
            .bind(RedPandaEntityAnimationState.Y_POSING, RedPandaAnimations.RED_PANDA_Y_POSING)
            .bind(RedPandaEntityAnimationState.ENDING_Y_POSE, RedPandaAnimations.RED_PANDA_Y_POSE_END)
            .bind(RedPandaEntityAnimationState.SIT_START, RedPandaAnimations.RED_PANDA_SIT_START)
            .bind(RedPandaEntityAnimationState.SIT_END, RedPandaAnimations.RED_PANDA_SIT_END);
    private final ModelPart all;
    private final ModelPart body;
    private final ModelPart head;
    private float dropPitch;

    public RedPandaModel(ModelPart root) {
        super(root);
        this.all = root.getChild("all");
        this.body = this.all.getChild("body");
        this.head = this.body.getChild("head");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition all = modelPartData.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 19.0F, 2.5F));

        PartDefinition body = all.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -3.0F));

        PartDefinition torso = body.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0F, -4.5F, 4.0F, 4.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition tailOne = body.addOrReplaceChild("tailOne", CubeListBuilder.create().texOffs(19, 14).addBox(-1.5F, -1.5F, -0.15F, 3.0F, 3.0F, 5.0F, new CubeDeformation(-0.001F)), PartPose.offsetAndRotation(0.0F, -0.5F, 4.15F, 0.1745F, 0.0F, 0.0F));

        PartDefinition tailTwo = tailOne.addOrReplaceChild("tailTwo", CubeListBuilder.create().texOffs(0, 23).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 4.8F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 14).addBox(-2.5F, -2.0F, -4.0F, 5.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, -4.5F));

        PartDefinition snout = head.addOrReplaceChild("snout", CubeListBuilder.create().texOffs(17, 30).addBox(-1.5F, -1.0F, -0.5F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, -4.5F));

        PartDefinition rightEar = head.addOrReplaceChild("rightEar", CubeListBuilder.create().texOffs(0, 32).addBox(-1.5F, -1.5F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -1.5F, -2.0F));

        PartDefinition leftEar = head.addOrReplaceChild("leftEar", CubeListBuilder.create().texOffs(26, 30).addBox(-0.5F, -1.5F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -1.5F, -2.0F));

        PartDefinition frontLeftLeg = all.addOrReplaceChild("frontLeftLeg", CubeListBuilder.create().texOffs(17, 23).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 1.0F, -6.0F));

        PartDefinition frontRightLeg = all.addOrReplaceChild("frontRightLeg", CubeListBuilder.create().texOffs(26, 23).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, 1.0F, -6.0F));

        PartDefinition backLeftLeg = all.addOrReplaceChild("backLeftLeg", CubeListBuilder.create().texOffs(27, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 1.0F, 0.0F));

        PartDefinition backRightLeg = all.addOrReplaceChild("backRightLeg", CubeListBuilder.create().texOffs(27, 7).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, 1.0F, 0.0F));
        return LayerDefinition.create(modelData, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.setHeadAngles(entity, headYaw, headPitch, animationProgress);

        this.animateWalk(RedPandaAnimations.RED_PANDA_WALK, limbAngle, limbDistance, 3f, 2f);

        ANIMATIONS.apply(entity, entity.getAnimationController(), animationProgress, this::animate);
        this.animate(entity.rightEarTwitchAnimationState, RedPandaAnimations.RED_PANDA_RIGHT_EAR_TWITCH, animationProgress, 1f);
        this.animate(entity.leftEarTwitchAnimationState, RedPandaAnimations.RED_PANDA_LEFT_EAR_TWITCH, animationProgress, 1f);
        this.animate(entity.sniffAnimationState, RedPandaAnimations.RED_PANDA_SNIFF, animationProgress, 1f);
    }

    @Override
    protected ModelPart getHeadPart() {
        return this.head;
    }
}
