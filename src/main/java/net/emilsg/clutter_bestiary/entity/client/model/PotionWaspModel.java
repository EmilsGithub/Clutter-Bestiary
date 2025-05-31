package net.emilsg.clutter_bestiary.entity.client.model;

import net.emilsg.clutter_bestiary.entity.client.animation.PotionWaspEntityAnimations;
import net.emilsg.clutter_bestiary.entity.client.model.parent.BestiaryModel;
import net.emilsg.clutter_bestiary.entity.custom.PotionWaspEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;


public class PotionWaspModel<T extends PotionWaspEntity> extends BestiaryModel<T> {
    private final ModelPart root;
    private final ModelPart all;
    private final ModelPart body;
    private final ModelPart potionSac;
    private final ModelPart head;

    public PotionWaspModel(ModelPart root) {
        this.root = root;
        this.all = root.getChild("all");
        this.body = this.all.getChild("body");
        this.potionSac = this.body.getChild("potionSac");
        this.head = this.body.getChild("head");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData all = modelPartData.addChild("all", ModelPartBuilder.create(), ModelTransform.pivot(2.5F, 15.0F, 0.0F));

        ModelPartData body = all.addChild("body", ModelPartBuilder.create().uv(24, 15).cuboid(-2.0F, -1.5F, -3.0F, 4.0F, 3.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.5F, -0.5F, -0.5F));

        ModelPartData potionSac = body.addChild("potionSac", ModelPartBuilder.create().uv(0, 0).cuboid(-3.5F, 0.0F, -3.5F, 7.0F, 8.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 1.5F, 0.5F));

        ModelPartData tail = body.addChild("tail", ModelPartBuilder.create().uv(24, 24).cuboid(-1.5F, -1.0F, -1.0F, 3.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 3.0F, -0.5236F, 0.0F, 0.0F));

        ModelPartData leftWing = body.addChild("leftWing", ModelPartBuilder.create().uv(-1, 15).cuboid(-1.0F, 0.0F, 0.0F, 4.0F, 0.0F, 9.0F, new Dilation(0.0F)), ModelTransform.pivot(1.0F, -1.5F, -2.0F));

        ModelPartData rightWing = body.addChild("rightWing", ModelPartBuilder.create().uv(0, 24).cuboid(-3.0F, 0.0F, 0.0F, 4.0F, 0.0F, 9.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.0F, -1.5F, -2.0F));

        ModelPartData head = body.addChild("head", ModelPartBuilder.create().uv(28, 0).cuboid(-1.0F, -1.0F, -2.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.5F, -3.0F));

        ModelPartData leftInnerMandible = head.addChild("leftInnerMandible", ModelPartBuilder.create().uv(24, 30).cuboid(0.0F, -0.5F, -1.0F, 0.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, 0.5F, -1.5F, 0.0F, -0.1309F, 0.0F));

        ModelPartData leftOuterMandible = leftInnerMandible.addChild("leftOuterMandible", ModelPartBuilder.create().uv(26, 30).cuboid(0.0F, -0.5F, -1.0F, 0.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -1.0F, 0.0F, 0.3927F, 0.0F));

        ModelPartData rightInnerMandible = head.addChild("rightInnerMandible", ModelPartBuilder.create().uv(28, 30).cuboid(0.0F, -0.5F, -1.0F, 0.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-1.0F, 0.5F, -1.5F, 0.0F, 0.1309F, 0.0F));

        ModelPartData rightOuterMandible = rightInnerMandible.addChild("rightOuterMandible", ModelPartBuilder.create().uv(30, 29).cuboid(0.0F, -0.5F, -1.0F, 0.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -1.0F, 0.0F, -0.4363F, 0.0F));

        ModelPartData innerRightFrontLeg = body.addChild("innerRightFrontLeg", ModelPartBuilder.create().uv(28, 10).cuboid(-2.0F, 0.0F, -0.5F, 2.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-2.0F, 0.5F, -1.5F, 0.0F, 0.0F, -0.3491F));

        ModelPartData outerRightFrontLeg = innerRightFrontLeg.addChild("outerRightFrontLeg", ModelPartBuilder.create().uv(28, 11).cuboid(-2.0F, 0.0F, -0.5F, 2.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-2.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.3963F));

        ModelPartData innerRightMiddleLeg = body.addChild("innerRightMiddleLeg", ModelPartBuilder.create().uv(28, 12).cuboid(-2.0F, 0.0F, -0.5F, 2.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-2.0F, 0.5F, 0.5F, 0.0F, 0.0F, -0.2618F));

        ModelPartData outerRightMiddleLeg = innerRightMiddleLeg.addChild("outerRightMiddleLeg", ModelPartBuilder.create().uv(28, 13).cuboid(-2.0F, 0.0F, -0.5F, 2.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-2.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5272F));

        ModelPartData innerRightBackLeg = body.addChild("innerRightBackLeg", ModelPartBuilder.create().uv(28, 14).cuboid(-2.0F, 0.0F, -0.5F, 2.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-2.0F, 0.5F, 2.5F, 0.0F, 0.0F, -0.3491F));

        ModelPartData outerRightBackLeg = innerRightBackLeg.addChild("outerRightBackLeg", ModelPartBuilder.create().uv(24, 29).cuboid(-2.0F, 0.0F, -0.5F, 2.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-2.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.3963F));

        ModelPartData innerLeftFrontLeg = body.addChild("innerLeftFrontLeg", ModelPartBuilder.create().uv(28, 4).cuboid(0.0F, 0.0F, -0.5F, 2.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(2.0F, 0.5F, -1.5F, 0.0F, 0.0F, 0.3491F));

        ModelPartData outerLeftFrontLeg = innerLeftFrontLeg.addChild("outerLeftFrontLeg", ModelPartBuilder.create().uv(28, 5).cuboid(0.0F, 0.0F, -0.5F, 2.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(2.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.3963F));

        ModelPartData innerLeftMiddleLeg = body.addChild("innerLeftMiddleLeg", ModelPartBuilder.create().uv(28, 6).cuboid(0.0F, 0.0F, -0.5F, 2.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(2.0F, 0.5F, 0.5F, 0.0F, 0.0F, 0.2618F));

        ModelPartData outerLeftMiddleLeg = innerLeftMiddleLeg.addChild("outerLeftMiddleLeg", ModelPartBuilder.create().uv(28, 7).cuboid(0.0F, 0.0F, -0.5F, 2.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(2.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5272F));

        ModelPartData innerLeftBackLeg = body.addChild("innerLeftBackLeg", ModelPartBuilder.create().uv(28, 8).cuboid(0.0F, 0.0F, -0.5F, 2.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(2.0F, 0.5F, 2.5F, 0.0F, 0.0F, 0.3491F));

        ModelPartData outerLeftBackLeg = innerLeftBackLeg.addChild("outerLeftBackLeg", ModelPartBuilder.create().uv(28, 9).cuboid(0.0F, 0.0F, -0.5F, 2.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(2.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.3963F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public ModelPart getPart() {
        return root;
    }

    public ModelPart getPotionSacPart() {
        return potionSac;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        root.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    @Override
    public void setAngles(PotionWaspEntity potionWasp, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        this.setHeadAngles(potionWasp, netHeadYaw, headPitch, ageInTicks);

        this.getPotionSacPart().hidden = !potionWasp.hasPotionSac();

        this.updateAnimation(potionWasp.flyingAnimState, potionWasp.hasPotionSac() ? PotionWaspEntityAnimations.POTIONWASP_FLY : PotionWaspEntityAnimations.POTIONWASP_FLY_NO_SAC, ageInTicks, 1f);
    }

    @Override
    protected ModelPart getHeadPart() {
        return head;
    }
}