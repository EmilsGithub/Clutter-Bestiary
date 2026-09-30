package net.emilsg.clutterbestiary.entity.custom.goal;

import net.emilsg.clutterbestiary.entity.custom.KiwiBirdEntity;
import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.level.gamerules.GameRules;

public class KiwiBirdMateGoal extends BreedGoal {
    KiwiBirdEntity kiwiBird;

    public KiwiBirdMateGoal(KiwiBirdEntity kiwiBird, double speed) {
        super(kiwiBird, speed);
        this.kiwiBird = kiwiBird;
    }

    public boolean canUse() {
        return super.canUse() && !this.kiwiBird.hasEgg();
    }

    protected void breed() {
        ServerPlayer serverPlayerEntity = this.animal.getLoveCause();
        if (this.partner == null) return;

        if (serverPlayerEntity == null && this.partner.getLoveCause() != null) {
            serverPlayerEntity = this.partner.getLoveCause();
        }

        if (serverPlayerEntity != null) {
            serverPlayerEntity.awardStat(Stats.ANIMALS_BRED);
            CriteriaTriggers.BRED_ANIMALS.trigger(serverPlayerEntity, this.animal, this.partner, null);
        }

        this.kiwiBird.beginCarryingEgg();
        this.animal.setAge(6000);
        this.partner.setAge(6000);
        this.animal.resetLove();
        this.partner.resetLove();
        RandomSource random = this.animal.getRandom();
        if (this.level.getGameRules().get(GameRules.MOB_DROPS)) {
            this.level.addFreshEntity(new ExperienceOrb(this.level, this.animal.getX(), this.animal.getY(), this.animal.getZ(), random.nextInt(7) + 1));
        }

    }
}
