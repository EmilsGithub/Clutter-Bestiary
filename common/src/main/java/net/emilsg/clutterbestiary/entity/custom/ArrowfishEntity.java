package net.emilsg.clutterbestiary.entity.custom;

import net.emilsg.clutterbestiary.animation_handling.AnimationPlayback;
import net.emilsg.clutterbestiary.entity.custom.parent.ParentFishEntity;
import net.emilsg.clutterbestiary.item.ModItems;
import net.emilsg.clutterbestiary.util.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.animal.fish.AbstractFish;
import net.minecraft.world.entity.animal.fish.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Nullable;

public class ArrowfishEntity extends ParentFishEntity {
    public final AnimationState swimmingAnimationState = new AnimationState();

    public ArrowfishEntity(EntityType<? extends AbstractFish> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(ModItems.ARROWFISH_BUCKET.get());
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Player.class, 8.0F, 1.2, 1.0, EntitySelector.NO_SPECTATORS::test));
    }

    public static AttributeSupplier.Builder setAttributes() {
        return ParentFishEntity.createMobAttributes().add(Attributes.MAX_HEALTH, 6D);
    }

    public static boolean isValidNaturalSpawn(EntityType<? extends WaterAnimal> type, LevelAccessor world, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
        return world.getFluidState(pos).is(FluidTags.WATER)
                && world.getBlockState(pos.above()).is(ModBlockTags.ARROWFISH_SPAWN_ON);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) AnimationPlayback.updateLoop(this, this.swimmingAnimationState, this.isAlive());
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SALMON_DEATH;
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.TROPICAL_FISH_FLOP;
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.SALMON_HURT;
    }

    @Override
    public float getVoicePitch() {
        return super.getVoicePitch() * 1.15f;
    }

}
