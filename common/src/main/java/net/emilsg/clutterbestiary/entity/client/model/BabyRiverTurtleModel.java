package net.emilsg.clutterbestiary.entity.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.emilsg.clutterbestiary.animation_handling.animation_states.RiverTurtleAnimationState;
import net.emilsg.clutterbestiary.entity.client.animation.AnimationBindings;
import net.emilsg.clutterbestiary.entity.client.animation.RiverTurtleAnimations;
import net.emilsg.clutterbestiary.entity.client.model.parent.BestiaryModel;
import net.emilsg.clutterbestiary.entity.custom.RiverTurtleEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class BabyRiverTurtleModel<T extends RiverTurtleEntity> extends BestiaryModel<T> {
    private static final AnimationBindings<RiverTurtleEntity, RiverTurtleAnimationState> ANIMATIONS = new AnimationBindings<RiverTurtleEntity, RiverTurtleAnimationState>()
            .bind(RiverTurtleAnimationState.HIDING, RiverTurtleAnimations.RIVER_TURTLE_HIDE)
            .bind(RiverTurtleAnimationState.UNHIDING, RiverTurtleAnimations.RIVER_TURTLE_UNHIDE)
            .bind(RiverTurtleAnimationState.SIT_START, RiverTurtleAnimations.RIVER_TURTLE_SIT_START)
            .bind(RiverTurtleAnimationState.SIT_END, RiverTurtleAnimations.RIVER_TURTLE_SIT_END);
    private final ModelPart all;
    private final ModelPart body;
    private final ModelPart neck;
    private final ModelPart head;
    private final ModelPart tail;

    public BabyRiverTurtleModel(ModelPart root) {
        super(root);
        this.all = root.getChild("all");
        this.body = this.all.getChild("body");
        this.neck = this.body.getChild("neck");
        this.head = this.neck.getChild("head");
        this.tail = this.body.getChild("tail");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition all = modelPartData.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(2.0F, 24.0F, 3.0F));

        PartDefinition body = all.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(-2.0F, -2.0F, 0.0F));

        PartDefinition frontLegs = body.addOrReplaceChild("frontLegs", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, -5.25F));

        PartDefinition frontRightLeg = frontLegs.addOrReplaceChild("frontRightLeg", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.01F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, -1.0F, 0.0F));

        PartDefinition frontRightLegToes = frontRightLeg.addOrReplaceChild("frontRightLegToes", CubeListBuilder.create().texOffs(1, 13).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.001F)), PartPose.offset(0.0F, 1.99F, -1.0F));

        PartDefinition frontLeftLeg = frontLegs.addOrReplaceChild("frontLeftLeg", CubeListBuilder.create().texOffs(0, 4).addBox(-1.0F, 0.0F, -1.01F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -1.0F, 0.0F));

        PartDefinition frontLeftLegToes = frontLeftLeg.addOrReplaceChild("frontLeftLegToes", CubeListBuilder.create().texOffs(-1, 35).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.99F, -1.0F));

        PartDefinition backLegs = body.addOrReplaceChild("backLegs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.25F));

        PartDefinition backRightLeg = backLegs.addOrReplaceChild("backRightLeg", CubeListBuilder.create().texOffs(8, 21).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.001F)), PartPose.offset(-3.0F, 0.0F, 0.0F));

        PartDefinition backRightLegToes = backRightLeg.addOrReplaceChild("backRightLegToes", CubeListBuilder.create().texOffs(1, 15).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.99F, -1.0F));

        PartDefinition backLeftLeg = backLegs.addOrReplaceChild("backLeftLeg", CubeListBuilder.create().texOffs(16, 21).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.001F)), PartPose.offset(3.0F, 0.0F, 0.0F));

        PartDefinition backLeftLegToes = backLeftLeg.addOrReplaceChild("backLeftLegToes", CubeListBuilder.create().texOffs(1, 17).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.99F, -1.0F));

        PartDefinition stomach = body.addOrReplaceChild("stomach", CubeListBuilder.create().texOffs(0, 12).addBox(-5.0F, -3.0F, -6.0F, 6.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 2.0F, 0.0F));

        PartDefinition shell = body.addOrReplaceChild("shell", CubeListBuilder.create().texOffs(0, 0).addBox(-4.5F, -2.5F, -4.5F, 9.0F, 3.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, -2.5F));

        PartDefinition neck = body.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(19, 12).addBox(-1.0F, -1.0F, -4.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -6.0F));

        PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 21).addBox(-1.0F, -1.0F, -3.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.125F)), PartPose.offset(0.0F, -1.0F, -3.0F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(19, 27).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.5F, 1.0F));
        return LayerDefinition.create(modelData, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.setHeadAngles(entity, headYaw, headPitch, animationProgress);

        if (!entity.isHiding() && !entity.isInWater()) {
            this.animateWalk(RiverTurtleAnimations.RIVER_TURTLE_WALK, limbAngle, limbDistance, 3f, 2f);
        } else if (entity.isInWater()) {
            this.animateWalk(RiverTurtleAnimations.RIVER_TURTLE_SWIM, limbAngle, limbDistance, 3f, 2f);
        }

        ANIMATIONS.apply(entity, entity.getAnimationController(), animationProgress, this::animate);
    }

    @Override
    protected ModelPart getHeadPart() {
        return neck;
    }
}
