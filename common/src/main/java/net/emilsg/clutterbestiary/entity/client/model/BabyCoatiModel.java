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

public class BabyCoatiModel<T extends CoatiEntity> extends ParentTameableModel<T> {
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
    private final ModelPart backLeftLeg;
    private final ModelPart backRightLeg;
    private final ModelPart front;
    private final ModelPart torso;
    private final ModelPart main;
    private final ModelPart chest;
    private final ModelPart leftChest;
    private final ModelPart rightChest;
    private final ModelPart tail;
    private final ModelPart frontLeftLeg;
    private final ModelPart frontRightLeg;
    private final ModelPart neck;
    private final ModelPart head;
    private final ModelPart leftEar;
    private final ModelPart rightEar;

    public BabyCoatiModel(ModelPart root) {
        super(root);
        this.all = root.getChild("all");
        this.body = this.all.getChild("body");
        this.backLeftLeg = this.body.getChild("backLeftLeg");
        this.backRightLeg = this.body.getChild("backRightLeg");
        this.front = this.body.getChild("front");
        this.torso = this.front.getChild("torso");
        this.main = this.torso.getChild("main");
        this.chest = this.torso.getChild("chest");
        this.leftChest = this.chest.getChild("leftChest");
        this.rightChest = this.chest.getChild("rightChest");
        this.tail = this.torso.getChild("tail");
        this.frontLeftLeg = this.front.getChild("frontLeftLeg");
        this.frontRightLeg = this.front.getChild("frontRightLeg");
        this.neck = this.front.getChild("neck");
        this.head = this.neck.getChild("head");
        this.leftEar = this.head.getChild("leftEar");
        this.rightEar = this.head.getChild("rightEar");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition all = partdefinition.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 20.0F, 1.0F));

        PartDefinition body = all.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition backLeftLeg = body.addOrReplaceChild("backLeftLeg", CubeListBuilder.create().texOffs(21, 8).addBox(-1.0F, 0.0F, -0.5F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 1.0F, 0.5F));

        PartDefinition backRightLeg = body.addOrReplaceChild("backRightLeg", CubeListBuilder.create().texOffs(18, 28).addBox(0.0F, 0.0F, -0.5F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, 1.0F, 0.5F));

        PartDefinition front = body.addOrReplaceChild("front", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 4.5F));

        PartDefinition torso = front.addOrReplaceChild("torso", CubeListBuilder.create(), PartPose.offset(0.0F, -2.0F, -3.5F));

        PartDefinition main = torso.addOrReplaceChild("main", CubeListBuilder.create().texOffs(23, 52).addBox(-1.5F, -0.5F, -5.5F, 3.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, -0.5F));

        PartDefinition chest = torso.addOrReplaceChild("chest", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 0.0F));

        PartDefinition leftChest = chest.addOrReplaceChild("leftChest", CubeListBuilder.create(), PartPose.offset(2.875F, -2.75F, 0.5F));

        PartDefinition rightChest = chest.addOrReplaceChild("rightChest", CubeListBuilder.create(), PartPose.offset(-2.875F, -2.75F, 0.5F));

        PartDefinition tail = torso.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(3, 18).addBox(-1.0F, 0.5F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.5F, 2.0071F, 0.0F, 0.0F));

        PartDefinition frontLeftLeg = front.addOrReplaceChild("frontLeftLeg", CubeListBuilder.create().texOffs(27, 28).addBox(-1.0F, 0.0F, -1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 0.0F, -7.5F));

        PartDefinition frontRightLeg = front.addOrReplaceChild("frontRightLeg", CubeListBuilder.create().texOffs(32, 8).addBox(0.0F, 0.0F, -1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, 0.0F, -7.5F));

        PartDefinition neck = front.addOrReplaceChild("neck", CubeListBuilder.create(), PartPose.offset(0.0F, -4.0F, -9.5F));

        PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create().texOffs(17, 17).addBox(-2.5F, -2.5F, -5.0F, 4.0F, 4.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(21, 1).addBox(-1.5F, -0.5F, -8.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 1.0F, 1.0F));

        PartDefinition leftEar = head.addOrReplaceChild("leftEar", CubeListBuilder.create().texOffs(30, 13).addBox(-0.5F, -2.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, -2.5F, -1.5F));

        PartDefinition rightEar = head.addOrReplaceChild("rightEar", CubeListBuilder.create().texOffs(0, 33).addBox(-1.5F, -2.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, -2.5F, -1.5F));

        return LayerDefinition.create(meshdefinition, 64, 64);
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
