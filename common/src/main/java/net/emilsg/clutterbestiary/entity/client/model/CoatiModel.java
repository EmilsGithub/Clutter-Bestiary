package net.emilsg.clutterbestiary.entity.client.model;

import net.emilsg.clutterbestiary.entity.client.animation.CoatiAnimations;
import net.emilsg.clutterbestiary.entity.client.animation.RiverTurtleAnimations;
import net.emilsg.clutterbestiary.entity.client.model.parent.BestiaryModel;
import net.emilsg.clutterbestiary.entity.client.model.parent.ParentTameableModel;
import net.emilsg.clutterbestiary.entity.custom.CoatiEntity;
import net.emilsg.clutterbestiary.entity.custom.RiverTurtleEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class CoatiModel<T extends CoatiEntity> extends ParentTameableModel<T> {
    private final ModelPart root;
    private final ModelPart all;
    private final ModelPart body;
    private final ModelPart front;
    private final ModelPart torso;
    private final ModelPart chest;
    private final ModelPart neck;
    private final ModelPart head;

    public CoatiModel(ModelPart root) {
        this.root = root;
        this.all = root.getChild("all");
        this.body = this.all.getChild("body");
        this.front = this.body.getChild("front");
        this.torso = this.front.getChild("torso");
        this.chest = this.torso.getChild("chest");
        this.neck = this.front.getChild("neck");
        this.head = this.neck.getChild("head");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData all = modelPartData.addChild("all", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 18.0F, -1.0F));

        ModelPartData body = all.addChild("body", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData backLeftLeg = body.addChild("backLeftLeg", ModelPartBuilder.create().uv(20, 6).cuboid(-1.0F, 0.0F, -1.5F, 2.0F, 5.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(1.5F, 1.0F, 4.5F));

        ModelPartData backRightLeg = body.addChild("backRightLeg", ModelPartBuilder.create().uv(16, 26).cuboid(-1.0F, 0.0F, -1.5F, 2.0F, 5.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.5F, 1.0F, 4.5F));

        ModelPartData front = body.addChild("front", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 1.0F, 4.5F));

        ModelPartData torso = front.addChild("torso", ModelPartBuilder.create().uv(18, 48).cuboid(-2.5F, -3.0F, -6.0F, 5.0F, 5.0F, 11.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -2.0F, -3.5F));

        ModelPartData chest = torso.addChild("chest", ModelPartBuilder.create().uv(38, 12).cuboid(-2.5F, -4.0F, -0.5F, 5.0F, 5.0F, 2.0F, new Dilation(0.125F)), ModelTransform.pivot(0.0F, 1.0F, 0.0F));

        ModelPartData leftChest = chest.addChild("leftChest", ModelPartBuilder.create().uv(38, 4).cuboid(-0.375F, -0.75F, -2.0F, 1.0F, 4.0F, 4.0F, new Dilation(0.0F))
                .uv(46, 1).cuboid(-0.125F, -0.25F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(2.875F, -2.75F, 0.5F));

        ModelPartData rightChest = chest.addChild("rightChest", ModelPartBuilder.create().uv(48, 4).cuboid(-0.625F, -0.75F, -2.0F, 1.0F, 4.0F, 4.0F, new Dilation(0.0F))
                .uv(46, 1).cuboid(-0.875F, -0.25F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.875F, -2.75F, 0.5F));

        ModelPartData tail = torso.addChild("tail", ModelPartBuilder.create().uv(0, 16).cuboid(-2.0F, 0.5F, -2.0F, 4.0F, 13.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -1.0F, 4.5F, 2.0071F, 0.0F, 0.0F));

        ModelPartData frontLeftLeg = front.addChild("frontLeftLeg", ModelPartBuilder.create().uv(26, 26).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(1.5F, 0.0F, -7.5F));

        ModelPartData frontRightLeg = front.addChild("frontRightLeg", ModelPartBuilder.create().uv(30, 6).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.5F, 0.0F, -7.5F));

        ModelPartData neck = front.addChild("neck", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -4.0F, -9.5F));

        ModelPartData head = neck.addChild("head", ModelPartBuilder.create().uv(16, 16).cuboid(-2.5F, -2.5F, -6.0F, 5.0F, 4.0F, 6.0F, new Dilation(0.0F))
                .uv(20, 0).cuboid(-1.5F, -0.5F, -10.0F, 3.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData leftEar = head.addChild("leftEar", ModelPartBuilder.create().uv(30, 13).cuboid(-0.5F, -2.0F, -0.5F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(1.5F, -2.5F, -1.5F));

        ModelPartData rightEar = head.addChild("rightEar", ModelPartBuilder.create().uv(0, 33).cuboid(-1.5F, -2.0F, -0.5F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.5F, -2.5F, -1.5F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        this.setHeadAngles(entity, headYaw, headPitch, animationProgress);
        this.setChestVisibility(entity.hasChest());

        if (entity.getDaysFedHoney() >= 3) {
            this.animateMovement(CoatiAnimations.COATI_FIND_BURROW, limbAngle, limbDistance, 3f, 2f);
        } else {
            this.animateMovement(CoatiAnimations.COATI_WALK, limbAngle, limbDistance, 3f, 2f);
        }

        this.updateAnimation(entity.diggingAnimationState, CoatiAnimations.COATI_DIG, animationProgress, 1.0f);
        this.updateAnimation(entity.sniffingAnimationState, CoatiAnimations.COATI_SNIFF, animationProgress, 1.0f);
        this.updateAnimation(entity.sittingAnimationState, CoatiAnimations.COATI_SIT_START, animationProgress, 1.0f);
        this.updateAnimation(entity.standingUpAnimationState, CoatiAnimations.COATI_SIT_STOP, animationProgress, 1.0f);
        this.updateAnimation(entity.unBurrowingAnimationState, CoatiAnimations.COATI_UNBURROW, animationProgress, 1.0f);
        this.updateAnimation(entity.pickUpItemAnimationState, CoatiAnimations.COATI_PICK_UP_ITEM, animationProgress, 1.0f);

    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
        matrices.push();

        if (this.child) {
            float babyScale = 0.5f;
            matrices.scale(babyScale, babyScale, babyScale);
            matrices.translate(0.0D, 1.5D, 0D);
            this.head.scale(createVec3f(0.6f));
        }

        this.getPart().render(matrices, vertices, light, overlay, color);
        matrices.pop();
    }

    private void setChestVisibility(boolean chestVisibility) {
        chest.visible = chestVisibility;
    }

    @Override
    public ModelPart getPart() {
        return root;
    }

    @Override
    protected ModelPart getHeadPart() {
        return head;
    }
}
