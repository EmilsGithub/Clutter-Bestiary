package net.emilsg.clutterbestiary.entity.client.model;

import net.emilsg.clutterbestiary.entity.client.animation.NetherNewtEntityAnimations;
import net.emilsg.clutterbestiary.entity.client.model.parent.ParentTameableModel;
import net.emilsg.clutterbestiary.entity.custom.AbstractNetherNewtEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;


public class NetherNewtModel<T extends AbstractNetherNewtEntity> extends ParentTameableModel<T> {

    private final ModelPart root;
    private final ModelPart all;
    private final ModelPart body;
    private final ModelPart head;

    private final ModelPart frontMushroom;
    private final ModelPart middleRightMushroom;
    private final ModelPart middleLeftMushroom;
    private final ModelPart backLeftMushroom;
    private final ModelPart backRightMushroom;

    public NetherNewtModel(ModelPart root) {
        this.root = root;
        this.all = root.getChild("all");
        this.body = this.all.getChild("body");
        this.head = this.body.getChild("head");

        this.frontMushroom = this.body.getChild("frontMushroom");
        this.middleRightMushroom = this.body.getChild("middleRightMushroom");
        this.middleLeftMushroom = this.body.getChild("middleLeftMushroom");
        this.backLeftMushroom = this.body.getChild("backLeftMushroom");
        this.backRightMushroom = this.body.getChild("backRightMushroom");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData all = modelPartData.addChild("all", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, -2.0F));

        ModelPartData body = all.addChild("body", ModelPartBuilder.create().uv(13, 4).cuboid(0.0F, -3.25F, -4.75F, 0.0F, 2.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -3.75F, 2.75F));

        ModelPartData torso = body.addChild("torso", ModelPartBuilder.create().uv(0, 0).cuboid(-1.5F, -1.5F, -3.5F, 3.0F, 3.0F, 7.0F, new Dilation(0.125F))
                .uv(0, 10).cuboid(-1.5F, -1.5F, -3.5F, 3.0F, 3.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.25F, -2.25F));

        ModelPartData head = body.addChild("head", ModelPartBuilder.create().uv(3, 15).cuboid(-2.3F, -2.5F, -4.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.25F))
                .uv(13, 5).cuboid(1.3F, -2.5F, -4.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.25F))
                .uv(13, 0).cuboid(-1.5F, -2.0F, -3.5F, 3.0F, 1.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-1.15F, -1.0F, -3.3F, 2.3F, 0.25F, 2.55F, new Dilation(0.0F))
                .uv(18, 18).cuboid(-2.5F, -2.975F, -4.5F, 5.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(13, 12).cuboid(-2.5F, -2.975F, -4.5F, 5.0F, 2.0F, 2.0F, new Dilation(0.125F)), ModelTransform.pivot(0.0F, -0.25F, -5.75F));

        ModelPartData Neck_r1 = head.addChild("Neck_r1", ModelPartBuilder.create().uv(0, 25).cuboid(-1.0F, -0.5F, -0.25F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -1.0F, 1.0908F, 0.0F, 0.0F));

        ModelPartData jaw = head.addChild("jaw", ModelPartBuilder.create().uv(0, 31).cuboid(-1.25F, 0.0F, -2.5F, 2.5F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(0, 2).cuboid(-1.0F, -0.25F, -2.25F, 2.0F, 0.25F, 1.75F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -1.0F, -1.0F));

        ModelPartData beard = jaw.addChild("beard", ModelPartBuilder.create().uv(0, 0).cuboid(0.0F, 0.0F, -1.5F, 0.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 1.0F, -1.0F));

        ModelPartData frontMushroom = body.addChild("frontMushroom", ModelPartBuilder.create().uv(12, 28).cuboid(-0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(8, 25).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(27, 25).cuboid(-0.5F, -3.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -1.0F, -4.25F));

        ModelPartData middleRightMushroom = body.addChild("middleRightMushroom", ModelPartBuilder.create().uv(27, 6).cuboid(-0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(24, 22).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(6, 25).cuboid(-0.5F, -3.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-0.9F, -1.0F, -2.25F));

        ModelPartData middleLeftMushroom = body.addChild("middleLeftMushroom", ModelPartBuilder.create().uv(29, 0).cuboid(-0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(25, 10).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(27, 28).cuboid(-0.5F, -3.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.9F, -1.0F, -2.25F));

        ModelPartData backLeftMushroom = body.addChild("backLeftMushroom", ModelPartBuilder.create().uv(20, 16).cuboid(-0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(23, 0).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(17, 5).cuboid(-0.5F, -3.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.9F, -1.0F, -0.25F));

        ModelPartData backRightMushroom = body.addChild("backRightMushroom", ModelPartBuilder.create().uv(19, 29).cuboid(-0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(25, 14).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(15, 29).cuboid(-0.5F, -3.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-0.9F, -1.0F, 0.75F));

        ModelPartData upperLeftFrontLeg = body.addChild("upperLeftFrontLeg", ModelPartBuilder.create().uv(8, 28).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.125F)), ModelTransform.of(1.5F, -0.25F, -3.75F, 0.0F, 0.0F, -0.2618F));

        ModelPartData lowerLeftFrontLeg = upperLeftFrontLeg.addChild("lowerLeftFrontLeg", ModelPartBuilder.create().uv(27, 3).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 2.0F, 0.0F));

        ModelPartData upperRightFrontLeg = body.addChild("upperRightFrontLeg", ModelPartBuilder.create().uv(24, 26).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.125F)), ModelTransform.of(-1.5F, -0.25F, -3.75F, 0.0F, 0.0F, 0.2618F));

        ModelPartData lowerRightFrontLeg = upperRightFrontLeg.addChild("lowerRightFrontLeg", ModelPartBuilder.create().uv(20, 26).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 2.0F, 0.0F));

        ModelPartData upperLeftBackLeg = body.addChild("upperLeftBackLeg", ModelPartBuilder.create().uv(16, 26).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.125F)), ModelTransform.of(1.5F, -0.25F, 0.25F, 0.0F, 0.0F, -0.2618F));

        ModelPartData lowerLeftBackLeg = upperLeftBackLeg.addChild("lowerLeftBackLeg", ModelPartBuilder.create().uv(7, 20).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 2.0F, 0.0F));

        ModelPartData upperRightBackLeg = body.addChild("upperRightBackLeg", ModelPartBuilder.create().uv(13, 0).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.125F)), ModelTransform.of(-1.5F, -0.25F, 0.25F, 0.0F, 0.0F, 0.2618F));

        ModelPartData lowerRightBackLeg = upperRightBackLeg.addChild("lowerRightBackLeg", ModelPartBuilder.create().uv(0, 13).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 2.0F, 0.0F));

        ModelPartData tailOne = body.addChild("tailOne", ModelPartBuilder.create().uv(10, 20).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 3.0F, new Dilation(0.25F))
                .uv(0, 2).cuboid(0.0F, -2.25F, 0.175F, 0.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.45F, 1.25F));

        ModelPartData tailTwo = tailOne.addChild("tailTwo", ModelPartBuilder.create().uv(20, 5).cuboid(-1.0F, -1.0F, -0.35F, 2.0F, 2.0F, 3.0F, new Dilation(0.0F))
                .uv(0, 1).cuboid(0.0F, -1.9F, 0.675F, 0.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.15F, 3.25F));

        ModelPartData tailThree = tailTwo.addChild("tailThree", ModelPartBuilder.create().uv(0, 20).cuboid(-1.0F, -1.0F, -0.3F, 2.0F, 2.0F, 3.0F, new Dilation(-0.25F))
                .uv(2, 1).cuboid(0.0F, -1.7F, 0.675F, 0.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.15F, 2.6F));

        ModelPartData tailFour = tailThree.addChild("tailFour", ModelPartBuilder.create().uv(0, 10).cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.15F, 2.45F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public ModelPart getPart() {
        return root;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
        if (this.child) {
            float babyScale = 0.5f;
            this.head.scale(createVec3f(babyScale));
            matrices.push();
            matrices.scale(babyScale, babyScale, babyScale);
            matrices.translate(0.0D, 1.5f, 0D);
            this.getPart().render(matrices, vertices, light, overlay, color);
            matrices.pop();
            this.head.scale(createVec3f(0.9f));
        } else {
            matrices.push();
            this.getPart().render(matrices, vertices, light, overlay, color);
            matrices.pop();
        }    }

    @Override
    public void setAngles(AbstractNetherNewtEntity newt, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        this.setHeadAngles(newt, netHeadYaw, headPitch, ageInTicks);

        if (!newt.isSitting()) {
            this.animateMovement(NetherNewtEntityAnimations.NETHER_NEWT_WALK, limbSwing, limbSwingAmount, 1.5f, 2f);
            this.updateAnimation(newt.idleAnimationState, NetherNewtEntityAnimations.NETHER_NEWT_IDLE, ageInTicks, 1f);

        } else {
            this.updateAnimation(newt.sittingAnimationState, NetherNewtEntityAnimations.NETHER_NEWT_SIT, ageInTicks, 1.0f);
        }

        this.updateVisibleParts(newt);
    }

    @Override
    protected ModelPart getHeadPart() {
        return head;
    }

    private void updateVisibleParts(AbstractNetherNewtEntity netherNewtEntity) {
        int fungiCount = netherNewtEntity.getFungiCount();

        frontMushroom.visible = fungiCount >= 1;
        middleRightMushroom.visible = fungiCount >= 4;
        middleLeftMushroom.visible = fungiCount >= 2;
        backLeftMushroom.visible = fungiCount >= 5;
        backRightMushroom.visible = fungiCount >= 3;
    }
}