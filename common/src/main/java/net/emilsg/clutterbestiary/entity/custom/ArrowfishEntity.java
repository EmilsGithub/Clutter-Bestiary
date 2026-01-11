package net.emilsg.clutterbestiary.entity.custom;

import net.emilsg.clutterbestiary.animation_handling.AnimationPlayback;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentFishEntity;
import net.emilsg.clutterbestiary.item.ModItems;
import net.emilsg.clutterbestiary.util.ModBlockTags;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class ArrowfishEntity extends ParentFishEntity {
    public final AnimationState swimmingAnimationState = new AnimationState();

    public ArrowfishEntity(EntityType<? extends FishEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public ItemStack getBucketItem() {
        return new ItemStack(ModItems.ARROWFISH_BUCKET.get());
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(2, new FleeEntityGoal<>(this, PlayerEntity.class, 8.0F, 1.2, 1.0, EntityPredicates.EXCEPT_SPECTATOR::test));
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return ParentFishEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 6D);
    }

    public static boolean isValidNaturalSpawn(EntityType<? extends WaterCreatureEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getFluidState(pos).isIn(FluidTags.WATER)
                && world.getBlockState(pos.up()).isIn(ModBlockTags.ARROWFISH_SPAWN_ON);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld().isClient) AnimationPlayback.updateLoop(this, this.swimmingAnimationState, this.isAlive());
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SALMON_DEATH;
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.ENTITY_TROPICAL_FISH_FLOP;
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_SALMON_HURT;
    }

    @Override
    public float getSoundPitch() {
        return super.getSoundPitch() * 1.15f;
    }

}
