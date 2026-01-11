package net.emilsg.clutterbestiary.mixin;

import net.emilsg.clutterbestiary.entity.custom.BoopletEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ZombieEntity.class)
public abstract class ZombieEntityMixin extends HostileEntity {

    protected ZombieEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initGoals", at = @At("TAIL"))
    private void clutterbestiary$addBoopletFleeGoal(CallbackInfo callbackInfo) {
        this.goalSelector.add(1, new FleeEntityGoal<>(this, BoopletEntity.class, 6.0F, 1.0, 1.2));
    }
}
