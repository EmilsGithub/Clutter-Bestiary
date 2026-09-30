package net.emilsg.clutterbestiary.entity.client.model.parent;

import net.emilsg.clutterbestiary.entity.client.render.state.BestiaryRenderState;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Base for the Bestiary entity models.
 * <p>
 * Keeps the pre-render-state animation entry point ({@link #setupAnim(LivingEntity, float, float, float, float, float)})
 * so the authored keyframe logic stays unchanged, and bakes {@link AnimationDefinition}s against this model's root on
 * first use. Baby transforms that used to live in {@code renderToBuffer} are described here and applied by the
 * renderer ({@link #getBabyScale()}, {@link #getBabyYOffset()}) and in {@link #setupAnim(BestiaryRenderState)} for
 * the enlarged baby head.
 */
public abstract class BestiaryEntityModel<E extends LivingEntity> extends EntityModel<BestiaryRenderState<E>> {
    private static final Vector3f BABY_HEAD_SCALE = new Vector3f(0.6F, 0.6F, 0.6F);
    private final Map<AnimationDefinition, KeyframeAnimation> bakedAnimations = new IdentityHashMap<>();
    protected boolean young;

    protected BestiaryEntityModel(ModelPart root) {
        super(root);
    }

    protected BestiaryEntityModel(ModelPart root, Function<Identifier, RenderType> renderType) {
        super(root, renderType);
    }

    @Override
    public void setupAnim(BestiaryRenderState<E> state) {
        super.setupAnim(state);
        this.young = state.isBaby;
        if (state.entity != null) {
            this.setupAnim(state.entity, state.walkAnimationPos, state.walkAnimationSpeed, state.ageInTicks, state.yRot, state.xRot);
        }

        ModelPart babyHead = this.getBabyHead();
        if (this.young && babyHead != null) {
            babyHead.offsetScale(BABY_HEAD_SCALE);
        }
    }

    public abstract void setupAnim(E entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch);

    public Vector3f createVec3f(float scale) {
        return new Vector3f(scale, scale, scale);
    }

    /**
     * Uniform scale applied to baby entities, or {@code 1} when the model has no baby transform.
     */
    public float getBabyScale() {
        return 1.0F;
    }

    /**
     * Vertical translation (in model units, applied after {@link #getBabyScale()}) for baby entities.
     */
    public float getBabyYOffset() {
        return 0.0F;
    }

    /**
     * Part that is enlarged on baby entities, if any.
     */
    @Nullable
    protected ModelPart getBabyHead() {
        return null;
    }

    /**
     * Bakes an animation against this model, skipping bones the model doesn't have (as 1.21.1 did), so that e.g. baby
     * models can share the adult animations.
     */
    protected KeyframeAnimation bake(AnimationDefinition definition) {
        return this.bakedAnimations.computeIfAbsent(definition, def -> {
            Function<String, ModelPart> partLookup = this.root.createPartLookup();
            Map<String, List<AnimationChannel>> presentBones = new HashMap<>();
            def.boneAnimations().forEach((bone, channels) -> {
                if (partLookup.apply(bone) != null) presentBones.put(bone, channels);
            });
            return new AnimationDefinition(def.lengthInSeconds(), def.looping(), presentBones).bake(this.root);
        });
    }

    protected void animate(AnimationState animationState, AnimationDefinition definition, float ageInTicks) {
        this.animate(animationState, definition, ageInTicks, 1.0F);
    }

    protected void animate(AnimationState animationState, AnimationDefinition definition, float ageInTicks, float speed) {
        this.bake(definition).apply(animationState, ageInTicks, speed);
    }

    protected void animateWalk(AnimationDefinition definition, float limbSwing, float limbSwingAmount, float maxAnimationSpeed, float animationScaleFactor) {
        this.bake(definition).applyWalk(limbSwing, limbSwingAmount, maxAnimationSpeed, animationScaleFactor);
    }

    protected void applyStatic(AnimationDefinition definition) {
        this.bake(definition).applyStatic();
    }
}
