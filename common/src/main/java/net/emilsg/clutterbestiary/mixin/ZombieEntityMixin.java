package net.emilsg.clutterbestiary.mixin;

import net.emilsg.clutterbestiary.entity.custom.BoopletEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Zombie.class)
public abstract class ZombieEntityMixin extends Monster {

    protected ZombieEntityMixin(EntityType<? extends Monster> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void clutterbestiary$addBoopletFleeGoal(CallbackInfo callbackInfo) {
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, BoopletEntity.class, 6.0F, 1.0, 1.2));
    }
}
