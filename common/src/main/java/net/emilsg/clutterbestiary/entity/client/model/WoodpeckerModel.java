package net.emilsg.clutterbestiary.entity.client.model;

import net.emilsg.clutterbestiary.animation_handling.animation_states.WoodpeckerAnimationState;
import net.emilsg.clutterbestiary.entity.client.animation.AnimationBindings;
import net.emilsg.clutterbestiary.entity.client.animation.WoodpeckerAnimations;
import net.emilsg.clutterbestiary.entity.custom.WoodpeckerEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector3f;

public class WoodpeckerModel extends SinglePartEntityModel<WoodpeckerEntity> {
    private static final AnimationBindings<WoodpeckerEntity, WoodpeckerAnimationState> ANIMATIONS = new AnimationBindings<WoodpeckerEntity, WoodpeckerAnimationState>()
            .bind(WoodpeckerAnimationState.FLYING, WoodpeckerAnimations.WOODPECKER_FLY)
            .bind(WoodpeckerAnimationState.HOVERING, WoodpeckerAnimations.WOODPECKER_HOVERING)
            .bind(WoodpeckerAnimationState.ATTACHED, WoodpeckerAnimations.WOODPECKER_ATTACHED)
            .bind(WoodpeckerAnimationState.PECKING, WoodpeckerAnimations.WOODPECKER_PECKING);

    private final ModelPart root;
    private final ModelPart all;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart leftWing;
    private final ModelPart leftWingBottom;
    private final ModelPart rightWing;
    private final ModelPart rightWingBottom;
    private final ModelPart tail;
    private final ModelPart leftLeg;
    private final ModelPart rightLeg;

    public WoodpeckerModel(ModelPart root) {
        this.root = root;
        this.all = root.getChild("all");
        this.head = this.all.getChild("head");
        this.body = this.all.getChild("body");
        this.leftWing = this.body.getChild("leftWing");
        this.leftWingBottom = this.leftWing.getChild("leftWingBottom");
        this.rightWing = this.body.getChild("rightWing");
        this.rightWingBottom = this.rightWing.getChild("rightWingBottom");
        this.tail = this.body.getChild("tail");
        this.leftLeg = this.body.getChild("leftLeg");
        this.rightLeg = this.body.getChild("rightLeg");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData all = modelPartData.addChild("all", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 16.1F, -1.9F));

        ModelPartData head = all.addChild("head", ModelPartBuilder.create().uv(0, 19).cuboid(-1.0F, -1.5F, -1.75F, 2.0F, 2.0F, 3.0F, new Dilation(0.0F))
                .uv(11, 10).cuboid(-1.0F, -2.5F, -2.75F, 2.0F, 1.0F, 4.0F, new Dilation(0.0F))
                .uv(22, 0).cuboid(-0.5F, -1.5F, -4.65F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(5, 25).cuboid(-0.5F, -0.5F, -2.65F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 10).cuboid(0.0F, -3.8F, -1.85F, 0.0F, 3.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.6F, -0.15F));

        ModelPartData body = all.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-1.5F, 0.0F, -1.5F, 3.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.4F, -0.1F, 0.4363F, 0.0F, 0.0F));

        ModelPartData leftWing = body.addChild("leftWing", ModelPartBuilder.create().uv(13, 0).cuboid(-0.5F, 0.0F, -1.5F, 1.0F, 5.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(1.5F, 0.4F, 0.2F, 0.1745F, 0.0F, 0.0F));

        ModelPartData leftWingBottom = leftWing.addChild("leftWingBottom", ModelPartBuilder.create().uv(22, 5).cuboid(-0.5F, 0.0F, -1.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 5.0F, 0.5F));

        ModelPartData rightWing = body.addChild("rightWing", ModelPartBuilder.create().uv(11, 16).cuboid(-0.5F, 0.0F, -1.5F, 1.0F, 5.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-1.5F, 0.4F, 0.2F, 0.1745F, 0.0F, 0.0F));

        ModelPartData rightWingBottom = rightWing.addChild("rightWingBottom", ModelPartBuilder.create().uv(20, 23).cuboid(-0.5F, 0.0F, -1.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 5.0F, 0.5F));

        ModelPartData tail = body.addChild("tail", ModelPartBuilder.create().uv(20, 16).cuboid(-1.5F, -0.25F, -0.5F, 3.0F, 5.0F, 1.0F, new Dilation(-0.01F)), ModelTransform.of(0.0F, 5.5F, 1.45F, 0.8727F, 0.0F, 0.0F));

        ModelPartData leftLeg = body.addChild("leftLeg", ModelPartBuilder.create().uv(24, 10).cuboid(-1.0F, -0.25F, -0.75F, 1.0F, 2.0F, 1.0F, new Dilation(-0.01F))
                .uv(24, 14).cuboid(-1.0F, 1.75F, -1.75F, 1.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(1.5F, 6.0F, -1.0F, -0.4363F, 0.0F, 0.0F));

        ModelPartData rightLeg = body.addChild("rightLeg", ModelPartBuilder.create().uv(0, 25).cuboid(0.0F, -0.25F, -0.75F, 1.0F, 2.0F, 1.0F, new Dilation(-0.01F))
                .uv(10, 25).cuboid(0.0F, 1.75F, -1.75F, 1.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-1.5F, 6.0F, -1.0F, -0.4363F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 32, 32);
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
        matrices.push();

        if (this.child) {
            float babyScale = 0.5f;
            matrices.scale(babyScale, babyScale, babyScale);
            matrices.translate(0.0D, 1.5D, 0D);
            this.head.scale(new Vector3f(0.6f, 0.6f, 0.6f));
        }

        this.getPart().render(matrices, vertices, light, overlay, color);
        matrices.pop();
    }

    @Override
    public void setAngles(WoodpeckerEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.root.traverse().forEach(ModelPart::resetTransform);
        ANIMATIONS.apply(entity, entity.getAnimationController(), animationProgress, this::updateAnimation);

        if (entity.getAnimationController().getState() == WoodpeckerAnimationState.GROUND_IDLE) {
            this.head.yaw = MathHelper.clamp(headYaw, -30.0f, 30.0f) * MathHelper.RADIANS_PER_DEGREE;
            this.head.pitch = MathHelper.clamp(headPitch, -25.0f, 45.0f) * MathHelper.RADIANS_PER_DEGREE;
        }
    }
}
