package net.emilsg.clutterbestiary.entity.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.emilsg.clutterbestiary.animation_handling.animation_states.CoatiEntityAnimationState;
import net.emilsg.clutterbestiary.entity.client.animation.AnimationBindings;
import net.emilsg.clutterbestiary.entity.client.animation.CoatiAnimations;
import net.emilsg.clutterbestiary.entity.client.model.parent.ParentTameableModel;
import net.emilsg.clutterbestiary.entity.custom.CoatiEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class CoatiModel<T extends CoatiEntity> extends ParentTameableModel<T> {
    private static final AnimationBindings<CoatiEntity, CoatiEntityAnimationState> ANIMATIONS = new AnimationBindings<CoatiEntity, CoatiEntityAnimationState>()
            .bind(CoatiEntityAnimationState.IDLING, CoatiAnimations.COATI_IDLE)
            .bind(CoatiEntityAnimationState.SITTING, CoatiAnimations.COATI_SIT_START)
            .bind(CoatiEntityAnimationState.STANDING_UP, CoatiAnimations.COATI_SIT_STOP)
            .bind(CoatiEntityAnimationState.SNIFFING, CoatiAnimations.COATI_SNIFF)
            .bind(CoatiEntityAnimationState.DIGGING, CoatiAnimations.COATI_DIG, CoatiEntity.DIG_ANIMATION_SPEED)
            .bind(CoatiEntityAnimationState.UNBURROWING, CoatiAnimations.COATI_UNBURROW)
            .bind(CoatiEntityAnimationState.PICKING_UP_ITEM, CoatiAnimations.COATI_PICK_UP_ITEM);
    private final ModelPart all;
    private final ModelPart body;
    private final ModelPart front;
    private final ModelPart torso;
    private final ModelPart chest;
    private final ModelPart neck;
    private final ModelPart head;

    public CoatiModel(ModelPart root) {
        super(root);
        this.all = root.getChild("all");
        this.body = this.all.getChild("body");
        this.front = this.body.getChild("front");
        this.torso = this.front.getChild("torso");
        this.chest = this.torso.getChild("chest");
        this.neck = this.front.getChild("neck");
        this.head = this.neck.getChild("head");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition all = modelPartData.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 18.0F, -1.0F));

        PartDefinition body = all.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition backLeftLeg = body.addOrReplaceChild("backLeftLeg", CubeListBuilder.create().texOffs(20, 6).addBox(-1.0F, 0.0F, -1.5F, 2.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 1.0F, 4.5F));

        PartDefinition backRightLeg = body.addOrReplaceChild("backRightLeg", CubeListBuilder.create().texOffs(16, 26).addBox(-1.0F, 0.0F, -1.5F, 2.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, 1.0F, 4.5F));

        PartDefinition front = body.addOrReplaceChild("front", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 4.5F));

        PartDefinition torso = front.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(18, 48).addBox(-2.5F, -3.0F, -6.0F, 5.0F, 5.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, -3.5F));

        PartDefinition chest = torso.addOrReplaceChild("chest", CubeListBuilder.create().texOffs(38, 12).addBox(-2.5F, -4.0F, -0.5F, 5.0F, 5.0F, 2.0F, new CubeDeformation(0.125F)), PartPose.offset(0.0F, 1.0F, 0.0F));

        PartDefinition leftChest = chest.addOrReplaceChild("leftChest", CubeListBuilder.create().texOffs(38, 4).addBox(-0.375F, -0.75F, -2.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(46, 1).addBox(-0.125F, -0.25F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.875F, -2.75F, 0.5F));

        PartDefinition rightChest = chest.addOrReplaceChild("rightChest", CubeListBuilder.create().texOffs(48, 4).addBox(-0.625F, -0.75F, -2.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(46, 1).addBox(-0.875F, -0.25F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.875F, -2.75F, 0.5F));

        PartDefinition tail = torso.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.5F, -2.0F, 4.0F, 13.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 4.5F, 2.0071F, 0.0F, 0.0F));

        PartDefinition frontLeftLeg = front.addOrReplaceChild("frontLeftLeg", CubeListBuilder.create().texOffs(26, 26).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 0.0F, -7.5F));

        PartDefinition frontRightLeg = front.addOrReplaceChild("frontRightLeg", CubeListBuilder.create().texOffs(30, 6).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, 0.0F, -7.5F));

        PartDefinition neck = front.addOrReplaceChild("neck", CubeListBuilder.create(), PartPose.offset(0.0F, -4.0F, -9.5F));

        PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create().texOffs(16, 16).addBox(-2.5F, -2.5F, -6.0F, 5.0F, 4.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(20, 0).addBox(-1.5F, -0.5F, -10.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition leftEar = head.addOrReplaceChild("leftEar", CubeListBuilder.create().texOffs(30, 13).addBox(-0.5F, -2.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, -2.5F, -1.5F));

        PartDefinition rightEar = head.addOrReplaceChild("rightEar", CubeListBuilder.create().texOffs(0, 33).addBox(-1.5F, -2.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, -2.5F, -1.5F));
        return LayerDefinition.create(modelData, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.setHeadAngles(entity, headYaw, headPitch, animationProgress);
        this.setChestVisibility(entity.hasChest());

        if (entity.getDaysFedHoney() >= entity.getDaysFedHoneyNeeded()) {
            this.animateWalk(CoatiAnimations.COATI_FIND_BURROW, limbAngle, limbDistance, 3f, 2f);
        } else {
            this.animateWalk(CoatiAnimations.COATI_WALK, limbAngle, limbDistance, 3f, 2f);
        }

        ANIMATIONS.apply(entity, entity.getAnimationController(), animationProgress, this::animate);
        this.animate(entity.leftEarTwitchAnimationState, CoatiAnimations.COATI_LEFT_EAR_TWITCH, animationProgress, 1.0f);
        this.animate(entity.rightEarTwitchAnimationState, CoatiAnimations.COATI_RIGHT_EAR_TWITCH, animationProgress, 1.0f);

    }

    @Override
    protected ModelPart getHeadPart() {
        return head;
    }

    private void setChestVisibility(boolean chestVisibility) {
        chest.visible = chestVisibility;
    }
}
